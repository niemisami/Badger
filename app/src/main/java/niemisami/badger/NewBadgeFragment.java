package niemisami.badger;

import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.widget.EditText;

/**
 * Created by Sami on 27.2.2015.
 */
public class NewBadgeFragment extends Fragment {

    private Button archiveButton, newBadgeButton, returnButton;
    private ArchiveFragment archiveFragment;
    final static String ARG_POSITION = "position";
    int fragmentID = 0;
    private Fragment currentFragment;
    private Time badgeTime;
    private String badgeTimeText;
    private EditText timeEditText;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_badge, container, false);
        initButtons();
        currentFragment = this;

       initTimeField();
        return view;

    }

    private void initButtons() {

        returnButton = (Button)view.findViewById(R.id.returnFromBadgeButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(currentFragment, 0);
            }
        });
    }

    private void initTimeField(){
        timeEditText = (EditText)view.findViewById(R.id.badgeTime);
        badgeTime = new Time(Time.getCurrentTimezone());
        badgeTime.setToNow();
        timeEditText.setText(badgeTime.monthDay + "/" + badgeTime.yearDay + "/" + badgeTime.year );


    }

}
