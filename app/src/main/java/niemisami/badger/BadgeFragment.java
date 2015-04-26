package niemisami.badger;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
//        VAIN TESTIÄ VARTEN
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
        mNameField.setText(mBadge.getName());
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
        mPhotoButton = (ImageButton)view.findViewById(R.id.badge_PhotoButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
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

        return view;
    }


    private void updateDate() {
        String format = "dd.MM.yyyy";
        SimpleDateFormat dateFormating = new SimpleDateFormat(format, Locale.US);
        String parsedDate = dateFormating.format(mBadge.getDate());
        mDateButton.setText(parsedDate);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mBadge.setDate(date);
            updateDate();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }

                BadgeManager.get(getActivity()).saveBadges();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();


//        This is for debugging and will be handled somewhere else
        if (!mNameField.getText().toString().matches("")) {
            Log.v(TAG, "Item saved");
            BadgeManager.get(getActivity()).saveBadges();
            Toast.makeText(getActivity().getApplicationContext(), "Merkki tallennettu!", Toast.LENGTH_SHORT).show();

        } else {
            for (int i = 0; i < BadgeManager.get(getActivity()).getBadges().size(); i++) {
                if (BadgeManager.get(getActivity()).getBadges().get(i).getId() == mBadge.getId()) {
                    BadgeManager.get(getActivity()).getBadges().remove(i);
                }
            }
            Toast.makeText(getActivity().getApplicationContext(), "Merkkiä ei tallennettu!", Toast.LENGTH_SHORT).show();
        }
    }
}
