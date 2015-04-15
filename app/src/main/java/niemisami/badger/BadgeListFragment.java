package niemisami.badger;

/**
 * Created by Sami on 27.2.2015.
 * BadgeListFragment is ListFragment because it is really useful
 * and provides many useful functions
 */

import android.support.v4.app.ListFragment;
import android.widget.Button;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class BadgeListFragment extends ListFragment {

    private String TAG = "BadgeListFragment";
    private Button archiveButton, newBadgeButton;

    private ArrayList<Badge> mBadges;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.badgeListMainTitle);

//        get(..) created the BadgeManager withing it's class if run first time
        mBadges = BadgeManager.get(getActivity()).getBadges();


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
