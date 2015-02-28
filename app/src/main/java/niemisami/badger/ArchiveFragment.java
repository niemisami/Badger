package niemisami.badger;

/**
 * Created by Sami on 27.2.2015.
 */

import android.view.View;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ArchiveFragment extends Fragment {
    private View view;

    final static String ARG_POSITION = "position";
    int fragmentID = 0;
    private Button returnButton;
    private Fragment currentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_archive, container, false);

        currentFragment = this;


        returnButton = (Button)view.findViewById(R.id.returnFromArchiveButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeFragment(currentFragment, 0);
            }
        });
        return view;


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, fragmentID);
    }
}

