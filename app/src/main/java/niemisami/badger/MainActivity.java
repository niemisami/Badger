package niemisami.badger;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class MainActivity extends FragmentActivity {

//    private ArchiveFragment archiveFragment;
//    private NewBadgeFragment badgeFragment;
//    private BadgeListFragment badgeListFragment;
//    private Fragment currentFragment;
    private final String TAG = "BadgerMainActivity";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private Fragment createFragment() {
        return new BadgeListFragment();
    }
}
