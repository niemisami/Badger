package niemisami.badger;

/**
 * Created by Sami on 27.2.2015.
 * BadgeListFragment is ListFragment because it is really useful
 * and provides many useful functions
 */

import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        BadgeAdapter adapter = new BadgeAdapter(mBadges);
        setListAdapter(adapter);


    }
//    @Override
//    public void onListItemClick(ListView listView, View view, int position, long id) {
//        Log.d(TAG, getListAdapter().getItem(position));
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_badge_list, container, false);

        ListView listView = (ListView)view.findViewById(android.R.id.list);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
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


    /*    With ArrayAdapter it is possible to inflate view with badges
          Constructor gets three parameters
              1. apps context 2. layout file id, not necessary 3. objects that will be listed
              
           BadgeAdapter must override getView(). It gets information from badge in current position
           and it's data is set to list item which layout comes from badge_list_item layout file
            */
    private class BadgeAdapter extends ArrayAdapter<Badge> {
        public BadgeAdapter(ArrayList<Badge> badges) {
            super(getActivity(), 0, badges);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.badge_list_item, null);
            }

            Badge badge = getItem(position);

            TextView nameTextView = (TextView) convertView.findViewById(R.id.badge_list_item_nameText);
            nameTextView.setText(badge.getName());
            TextView dateTextView = (TextView) convertView.findViewById(R.id.badge_list_item_dateTextView);
            //          getDate() will return time in shitty form
            dateTextView.setText(badge.getDate().toString());

            CheckBox attachedCheckBox = (CheckBox) convertView.findViewById(R.id.badge_list_item_attachedCheckBox);
            attachedCheckBox.setChecked(badge.getIsAttached());

            return convertView;

        }

    }
}
