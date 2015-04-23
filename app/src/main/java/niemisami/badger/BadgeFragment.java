package niemisami.badger;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class BadgeFragment extends Fragment {

//    Variables for the Badge view components
    private EditText mNameField;
    private Button mDateButton;
    private EditText mExtraInfoField;
    private ImageButton mAddBadgeButton;
    private ImageButton mTakePhotoButton;
    private ImageView mPhotoView;

    private Badge mBadge;


    public static final String EXTRA_BADGE_ID = "badger.badgeid";
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "date";

    public BadgeFragment() {}

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
        UUID badgeId = (UUID)getArguments().getSerializable(EXTRA_BADGE_ID);
        mBadge = BadgeManager.get(getActivity()).getBadge(badgeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_badge, container, false);

        mNameField = (EditText)view.findViewById(R.id.badgeName_editText);
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

        mDateButton = (Button)view.findViewById(R.id.badgeDate_button);
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


        mExtraInfoField = (EditText)view.findViewById(R.id.badge_extraInfo);
        if(mBadge.getExtraInfo() != null)
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
        return view;
    }


    private void updateDate() {
        String format = "dd.MM.yyyy";
        SimpleDateFormat dateFormating = new SimpleDateFormat(format, Locale.US);
        String parsedDate = dateFormating.format(mBadge.getDate());
        mDateButton.setText(parsedDate);
    }

}
