package niemisami.badger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;


public class MainActivity extends FragmentActivity {

    //    private ArchiveFragment archiveFragment;
//    private NewBadgeFragment badgeFragment;
//    private BadgeListFragment badgeListFragment;
//    private Fragment currentFragment;
    private final String TAG = "BadgerMainActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);


        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_fade_out);


        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }


    private Fragment createFragment() {
        return new BadgeListFragment();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "on back pressed");
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }}
