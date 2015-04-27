package niemisami.badger;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;


public class CameraActivity extends FragmentActivity {


    private final String TAG = "CameraActivity";


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


//        Whit these camera view can inflate the whole screen without the action bar or the status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null){
            UUID badgeId = (UUID)getIntent().getSerializableExtra(BadgeFragment.EXTRA_BADGE_ID);
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    private Fragment createFragment() {
        return new CameraFragment();
    }
}
