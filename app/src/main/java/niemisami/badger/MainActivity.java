package niemisami.badger;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;


public class MainActivity extends FragmentActivity {

    private ArchiveFragment archiveFragment;
    private NewBadgeFragment badgeFragment;
    private MainFragment mainFragment;
    private Fragment currentFragment;
    private final String TAG = "Badger";
    private int fragmentID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_main) != null) {
            if (savedInstanceState != null)
                return;

            mainFragment = new MainFragment();
            mainFragment.setArguments(getIntent().getExtras());
            archiveFragment = new ArchiveFragment();
            badgeFragment = new NewBadgeFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, archiveFragment)
                    .hide(archiveFragment)
                    .add(R.id.fragmentContainer, badgeFragment)
                    .hide(badgeFragment)
                    .add(R.id.fragmentContainer, mainFragment).commit();
            currentFragment = mainFragment;
        }
    }


    public void changeFragment(Fragment fragment, int fragmentID) {

//        if (fragment.isVisible()) {
//            return;
//        }
        this.fragmentID = fragmentID;
        Log.v(TAG,"ennen vaihtoja" + fragmentID);
        if (fragmentID == 0) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);

            fragment.getView().bringToFront();
            currentFragment.getView().bringToFront();

            transaction.hide(currentFragment);
            transaction.show(mainFragment);
            currentFragment = mainFragment;

            transaction.addToBackStack(null);
            transaction.commit();

        }
        if (fragmentID == 1) {



            Log.v(TAG,"Current fragment " + currentFragment.getClass().toString() + " ja lähetetty frag " + fragment.getClass().toString());

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);

            badgeFragment.getView().bringToFront();
            currentFragment.getView().bringToFront();


            transaction.hide(currentFragment);
            transaction.show(badgeFragment);
            currentFragment = badgeFragment;

            transaction.addToBackStack(null);
            transaction.commit();

        }  if (fragmentID == 2) {


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);

            fragment.getView().bringToFront();
            currentFragment.getView().bringToFront();

            transaction.hide(currentFragment);
            transaction.show(archiveFragment);
            currentFragment = archiveFragment;

            transaction.addToBackStack(null);
            transaction.commit();


        }

        //		Bundle args = new Bundle();
        //		FragmentTransaction transcation = getSupportFragmentManager().beginTransaction();
        //
        //

        //		if(fragmentID == 1){
        //			ArchiveFragment archiveFragment = (ArchiveFragment)
        //					getSupportFragmentManager().findFragmentById(R.layout.fragment_archive);
        //			if(archiveFragment != null){
        //
        //			}
        //			else{
        //				ArchiveFragment newArchiveFragment = new ArchiveFragment();
        ////				args.putInt(ArchiveFragment.ARG_POSITION, fragmentsPosition);
        ////				newArchiveFragment.setArguments(args);
        //				transcation.replace(R.id.fragment_main, newArchiveFragment);
        //				transcation.addToBackStack(null);
        //				transcation.commit();
        //			}
        //		}
        //		else if(fragmentID == 2){
        //			NewBadgeFragment badgeFragment = (NewBadgeFragment)
        //					getSupportFragmentManager().findFragmentById(R.layout.fragment_badge);
        //			if(badgeFragment != null){
        //
        //			}
        //			else{
        //				NewBadgeFragment newBadgeFragment = new NewBadgeFragment();
        //				transcation.replace(R.id.fragment_main, newBadgeFragment);
        //				transcation.addToBackStack(null);
        //				transcation.commit();
        ////				args.putInt(NewBadgeFragment.ARG_POSITION, fragmentsPosition);
        //			}
        //
        //
        //		}
        //		else if(fragmentID == 2){
        //
        //		}
        //		else if(fragmentID == 3){
        //
        //		}


    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }


    //	YLÄASETUSNAPPULASETTI
    //	@Override
    //	public boolean onOptionsItemSelected(MenuItem item) {
    //		// Handle action bar item clicks here. The action bar will
    //		// automatically handle clicks on the Home/Up button, so long
    //		// as you specify a parent activity in AndroidManifest.xml.
    //		int id = item.getItemId();
    //		if (id == R.id.action_settings) {
    //			return true;
    //		}
    //		return super.onOptionsItemSelected(item);
    //	}
}