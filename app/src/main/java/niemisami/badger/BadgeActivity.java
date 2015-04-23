package niemisami.badger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.UUID;


public class BadgeActivity extends FragmentActivity {

    private final String TAG = "BadgerActivity";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            UUID badgeId = (UUID)getIntent().getSerializableExtra(BadgeFragment.EXTRA_BADGE_ID);
            fragment = BadgeFragment.newInstance(badgeId);
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private Fragment createFragment() {
        return new BadgeFragment();
    }
}
