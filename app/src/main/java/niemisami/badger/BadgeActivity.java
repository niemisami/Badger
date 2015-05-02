package niemisami.badger;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;


public class BadgeActivity extends FragmentActivity {

    private final String TAG = "BadgerActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);


        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);


        if (fragment == null) {
            UUID badgeId = (UUID) getIntent().getSerializableExtra(BadgeFragment.EXTRA_BADGE_ID);
            fragment = BadgeFragment.newInstance(badgeId);
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = getSupportFragmentManager();
        BadgeFragment fragment = (BadgeFragment) manager.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) {
            fragment.setWorkDoneAndExit();
        } else
            super.onBackPressed();


        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_bottom);
    }

    private Fragment createFragment() {
        return new BadgeFragment();
    }
}
