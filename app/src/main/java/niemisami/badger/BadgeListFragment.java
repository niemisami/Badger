package niemisami.badger;

/**
 * Created by Sami on 27.2.2015.
 * BadgeListFragment is ListFragment because it is really useful
 * and provides many useful functions
 */

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.LruCache;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;





public class BadgeListFragment extends ListFragment {

    private String TAG = "BadgeListFragment";
    private ArrayList<Badge> mBadges;
    private Button mNewBadgeButton;
    private TextView mBadgeListInfo;
    private ThumbnailCache mThumbnailCache;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setHasOptionsMenu(true);

        //otsikkofonttijuttuja
        SpannableString s = new SpannableString("BADGER");
        Typeface CustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/LAIKA.ttf");
        s.setSpan(CustomFont, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        //SpannableString s = new SpannableString("BADGER");
        //s.setSpan(new niemisami.badger.TypefaceSpan(getActivity(), "fonts/LAIKA.otf"), 0, s.length(),
        //        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(s);

        //getActivity().setTitle(R.string.badgeListMainTitle);


        int memClass = ((ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        final int cacheSize = 1024 * 1024 * memClass / 8;
        mThumbnailCache = new ThumbnailCache(cacheSize);

        mBadges = BadgeManager.get(getActivity()).getBadges();

        BadgeAdapter adapter = new BadgeAdapter(mBadges);
        setListAdapter(adapter);

        setRetainInstance(true);


    }
//    @Override
//    public void onListItemClick(ListView listView, View view, int position, long id) {
//        Log.d(TAG, getListAdapter().getItem(position));
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");


        View view = inflater.inflate(R.layout.fragment_badge_list, container, false);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            Using floating context menus on Froo and Gingerbread
            registerForContextMenu(listView);
        } else {
//            Use contextual menu for Honeycomb and higher
//            Multiple_modal makes it possible to remove many items at once
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_delete_item:
                            BadgeAdapter adapter = (BadgeAdapter) getListAdapter();
                            BadgeManager badgeManager = BadgeManager.get(getActivity());

//                            Delete items from database
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    badgeManager.deleteBadgeFromDB(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            countBadges();
                            ((BadgeAdapter) getListAdapter()).clear();
                            ((BadgeAdapter) getListAdapter()).addAll(
                                    mBadges = BadgeManager.get(getActivity()).getBadges());
                            ((BadgeAdapter) getListAdapter()).notifyDataSetChanged();

                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }
            });


        }

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        BadgeAdapter adapter = (BadgeAdapter) getListAdapter();
        Badge badge = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_delete_item:
                BadgeManager.get(getActivity()).deleteBadgeFromDB(badge);
                countBadges();
                ((BadgeAdapter) getListAdapter()).clear();
                ((BadgeAdapter) getListAdapter()).addAll(
                        mBadges = BadgeManager.get(getActivity()).getBadges());
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
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
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(TAG, "onAttach");

        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");

        BadgeManager.get(getActivity()).closeDb();
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    //    Action when back button is pressed
    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        countBadges();
        ((BadgeAdapter) getListAdapter()).clear();
        ((BadgeAdapter) getListAdapter()).addAll(
                mBadges = BadgeManager.get(getActivity()).getBadges());
        ((BadgeAdapter) getListAdapter()).notifyDataSetChanged();
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

        //        getView inflates the view by using badge_list_item layout for every item
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
            String formattedTime = formatDate(badge);
            dateTextView.setText(formattedTime);

            CheckBox attachedCheckBox = (CheckBox) convertView.findViewById(R.id.badge_list_item_attachedCheckBox);
            attachedCheckBox.setChecked(badge.getIsAttached());

            ImageView badgeThumbnail = (ImageView) convertView.findViewById(R.id.badge_list_item_image);
            String photo = badge.getPhoto();


            if (photo != null) {
                if (mThumbnailCache.get(photo) != null) {
                    badgeThumbnail.setImageBitmap(mThumbnailCache.get(photo));
                } else {
                    Bitmap thumbnail = createThumbnail(photo);
                    badgeThumbnail.setImageBitmap(thumbnail);
                    mThumbnailCache.put(photo, thumbnail);
                }
            }


            return convertView;

        }

        private Bitmap createThumbnail(String photo) {
            String path = getActivity().getFileStreamPath(photo).getAbsolutePath();
//                    new LoadImage(badgeThumbnail, path).execute();
            Bitmap thumbnail = null;
            if (!path.isEmpty()) {
                thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 64, 64);
            }
            return thumbnail;

        }

//
//        //        Takes two objects in constructor ImageView and photo path
//        private class LoadImage extends AsyncTask<Object, Void, Bitmap> {
//
//            private ImageView thumbnailView;
//            private String path;
//
//            public LoadImage(ImageView thumbnailView, String path) {
//                this.thumbnailView = thumbnailView;
//                this.path = path;
//            }
//
//            @Override
//            protected Bitmap doInBackground(Object... params) {
//                Bitmap thumbnail = null;
//                if (!path.isEmpty()) {
//                    thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 64, 64);
//                }
//                return thumbnail;
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                if (bitmap != null && thumbnailView != null) {
//                    thumbnailView.setImageBitmap(bitmap);
//                }
//            }
//        }


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


    //    Cache for saving thumbnail images
    public class ThumbnailCache extends LruCache<String, Bitmap> {

        public ThumbnailCache(int maxSizeBytes) {
            super(maxSizeBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();
        }

    }


}
