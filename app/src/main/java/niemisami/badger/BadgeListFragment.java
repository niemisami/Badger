package niemisami.badger;

/**
 * Created by Sami on 27.2.2015.
 * BadgeListFragment is ListFragment because it is really useful
 * and provides many useful functions
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BadgeListFragment extends ListFragment {

    private String TAG = "BadgeListFragment";
    private Button mNewBadgeButton;
    private TextView mBadgeListInfo;

    private ArrayList<Badge> mBadges;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.badgeListMainTitle);

//        get(..) created the BadgeManager withing it's class if run first time
        mBadges = receiveBadgesArray();

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

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

//        TextView that tell to the user how many badges is saved and attached
        mBadgeListInfo = (TextView) view.findViewById(R.id.badge_list_info);
        countBadges();

        mNewBadgeButton = (Button) view.findViewById(R.id.newBardgeButton);
//        Press of a button creates new Badge and opens the empty BadgeFragment
        mNewBadgeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Badge badge = new Badge();
                BadgeManager.get((getActivity())).addBadge(badge);

                Intent i = new Intent(getActivity(), BadgeActivity.class);
                i.putExtra(BadgeFragment.EXTRA_BADGE_ID, badge.getId());
                startActivity(i);
            }
        });
        return view;
    }




    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Badge selectedBadge = ((BadgeAdapter) getListAdapter()).getItem(position);

        Intent i = new Intent(getActivity(), BadgeActivity.class);
        i.putExtra(BadgeFragment.EXTRA_BADGE_ID, selectedBadge.getId());
        startActivity(i);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    //    Action when back button is pressed
    @Override
    public void onResume() {
        super.onResume();
        countBadges();
        ((BadgeAdapter) getListAdapter()).notifyDataSetChanged();
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


        //        getView inflates the view by using badge_list_item layout for every item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.badge_list_item, null);
            }
            if (getItem(position) != null) {
                Badge badge = getItem(position);

                TextView nameTextView = (TextView) convertView.findViewById(R.id.badge_list_item_nameText);
                nameTextView.setText(badge.getName());
                TextView dateTextView = (TextView) convertView.findViewById(R.id.badge_list_item_dateTextView);
                //          getDate() will return time in shitty form
                String formattedTime = formatDate(badge);
                dateTextView.setText(formattedTime);

                CheckBox attachedCheckBox = (CheckBox) convertView.findViewById(R.id.badge_list_item_attachedCheckBox);
                attachedCheckBox.setChecked(badge.getIsAttached());
            }

            return convertView;

        }


        public String formatDate(Badge badge) {
            String format = "dd-MM-yyyy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(badge.getDate());
        }
    }

    public ArrayList<Badge> receiveBadgesArray() {
        return BadgeManager.get(getActivity()).getBadges();
    }



    public void countBadges() {
        mBadges = receiveBadgesArray();
        int badgeCount = mBadges.size();
        int attachedCount = 0;
        for (Badge badge : mBadges) {
            if (badge.getIsAttached())
                attachedCount++;
        }

        String infoText = mBadgeListInfo.getText().toString();

//        chars # and % are found in @string resource and method replaces them with integers
        infoText = infoText.replace("#", Integer.toString(badgeCount));
        infoText = infoText.replace("%", Integer.toString(attachedCount));

        mBadgeListInfo.setText(infoText);

    }

}
