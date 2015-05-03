package niemisami.badger;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class BadgeFragment extends Fragment {

    //    Variables for the Badge view components
    private static final String TAG = "BadgeFragment";
    private EditText mNameField;
    private Button mDateButton;
    private EditText mExtraInfoField;
    private CheckBox mAttachedCheckBox;
    private ImageButton mAddBadgeButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private Badge mBadge;


    public static final String EXTRA_BADGE_ID = "badger.badgeid";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_IMAGE = "image";

    public BadgeFragment() {
    }

    public static BadgeFragment newInstance(UUID badgeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BADGE_ID, badgeId);
        BadgeFragment fragment = new BadgeFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID badgeId = (UUID) getArguments().getSerializable(EXTRA_BADGE_ID);
        mBadge = BadgeManager.get(getActivity()).getBadge(badgeId);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_badge, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null)
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }


//       Badge's name
        mNameField = (EditText) view.findViewById(R.id.badgeName_editText);
        try {
            mNameField.setText(mBadge.getName());

        } catch (NullPointerException e) {
            Log.e(TAG, "No name", e);
            mNameField.setText("");
        }
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBadge.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


//        Button to start CameraActivity/Fragment
        mPhotoButton = (ImageButton) view.findViewById(R.id.badge_PhotoButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
        if (!hasCamera) {
            mPhotoButton.setEnabled(false);
        }


//        ImageView for showing photo of the badge
        mPhotoView = (ImageView) view.findViewById(R.id.badge_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String photo = mBadge.getPhoto();
                if (photo == null) {
                    return;
                }
                FragmentManager manager = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo).getAbsolutePath();
                ImageFragment.newInstance(path).show(manager, DIALOG_IMAGE);
            }
        });
//        Button to show calendar for the user
        mDateButton = (Button) view.findViewById(R.id.badgeDate_button);
        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mBadge.getDate());
                dialog.setTargetFragment(BadgeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });


//        EditText field where user can add additional info about the badge
        mExtraInfoField = (EditText) view.findViewById(R.id.badge_extraInfo);
        if (mBadge.getExtraInfo() != null)
            mExtraInfoField.setText(mBadge.getExtraInfo());
        mExtraInfoField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBadge.setExtraInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


//        Checkbox for user to select if the badge is attached to the overalls
        mAttachedCheckBox = (CheckBox) view.findViewById(R.id.badge_attached);
        mAttachedCheckBox.setChecked(mBadge.getIsAttached());
        mAttachedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBadge.setIsAttached(isChecked);
            }
        });


        mAddBadgeButton = (ImageButton) view.findViewById(R.id.newBardgeButton);

//        TODO clean this part and last method onPause
        mAddBadgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWorkDoneAndExit();
            }
        });

        return view;
    }


    private void updateDate() {
        String format = "dd.MM.yyyy";
        SimpleDateFormat dateFormating = new SimpleDateFormat(format, Locale.US);
        String parsedDate = dateFormating.format(mBadge.getDate());
        mDateButton.setText(parsedDate);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BadgeManager.get(getActivity()).saveBadges();
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
//
//                removeNewBadgeIfNotWanted();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mBadge.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            Log.d(TAG, "Ensimmäinen else");
            if (mBadge.getPhoto() != null) {
                deletePhoto();

            }
            String filename = data.getStringExtra(CameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Log.d(TAG, filename);
                mBadge.setPhoto(filename);
                showPhoto();

            }
        }
    }


    public void deletePhoto() {
        String photo = mBadge.getPhoto();
        PictureManager.cleanImageView(mPhotoView);
        getActivity().getFileStreamPath(photo).delete();
        mBadge.setPhoto(null);
        Log.i(TAG, mBadge.getPhoto() + "'s photo removed");

    }

    //    Photo is just String object that holds the filepath
    public void showPhoto() {
        String photo = mBadge.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo).getAbsolutePath();
            bitmapDrawable = PictureManager.scaleDrawableForDisplay(getActivity(), path);
        }
        mPhotoView.setImageDrawable(bitmapDrawable);
    }


    @Override
    public void onPause() {
        super.onPause();
        BadgeManager.get(getActivity()).saveBadges();

    }

//    Returns false if mNameField is empty

    private boolean isNameFieldFilled() {
        return !TextUtils.isEmpty(mNameField.getText().toString());
    }

    private void removeNewBadgeIfNotWanted() {
//        if (!isNameFieldFilled()) {
//            Toast.makeText(getActivity().getApplicationContext(), "Merkkiä ei tallennettu! Kirjoita nimi", Toast.LENGTH_SHORT).show();
//            for (int i = 0; i < BadgeManager.get(getActivity()).getBadgeArray().size(); i++) {
//                if (BadgeManager.get(getActivity()).getBadgeArray().get(i).getId() == mBadge.getId()) {
//                    BadgeManager.get(getActivity()).getBadgeArray().remove(i);
//                }
//            }
//        } else {

        Toast.makeText(getActivity().getApplicationContext(), "Merkki tallennettu!", Toast.LENGTH_SHORT).show();
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
//        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onDestroy() {
        BadgeManager.get(getActivity()).closeDb();
        super.onDestroyView();
    }


    //  TODO implement ths method to other places, onPause, add buttons onClickListener
    public void setWorkDoneAndExit() {
//        removeNewBadgeIfNotWanted();
        BadgeManager.get(getActivity()).saveBadges();
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            NavUtils.navigateUpFromSameTask(getActivity());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
