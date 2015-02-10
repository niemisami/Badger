package com.niemisami.badger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(findViewById(R.id.fragment_main)!= null){
			if(savedInstanceState != null)
				return;

			MainFragment mainFragment = new MainFragment();
			mainFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_main,mainFragment).commit();
		}
	}


	public void changeFragment(int fragmentID){

		Bundle args = new Bundle();
		FragmentTransaction transcation = getSupportFragmentManager().beginTransaction();

		if(fragmentID == 1){
			ArchiveFragment archiveFragment = (ArchiveFragment)
					getSupportFragmentManager().findFragmentById(R.layout.fragment_archive);
			if(archiveFragment != null){
				
			}
			else{
				ArchiveFragment newArchiveFragment = new ArchiveFragment();
//				args.putInt(ArchiveFragment.ARG_POSITION, fragmentsPosition);
//				newArchiveFragment.setArguments(args);
				transcation.replace(R.id.fragment_main, newArchiveFragment);
				transcation.addToBackStack(null);
				transcation.commit();
			}
		}
		else if(fragmentID == 2){
			NewBadgeFragment badgeFragment = (NewBadgeFragment)
					getSupportFragmentManager().findFragmentById(R.layout.fragment_badge);
			if(badgeFragment != null){

			}
			else{
				NewBadgeFragment newBadgeFragment = new NewBadgeFragment();
				transcation.replace(R.id.fragment_main, newBadgeFragment);
				transcation.addToBackStack(null);
				transcation.commit();
//				args.putInt(NewBadgeFragment.ARG_POSITION, fragmentsPosition);
			}
			

		}
		else if(fragmentID == 2){

		}
		else if(fragmentID == 3){

		}


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
