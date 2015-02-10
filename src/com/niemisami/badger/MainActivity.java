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


	public void changeFragment(){


		ArchiveFragment newArchiveFragment = new ArchiveFragment();
		Bundle args = new Bundle();
		args.putInt(ArchiveFragment.ARG_POSITION, -1);
		newArchiveFragment.setArguments(args);
		FragmentTransaction transcation = getSupportFragmentManager().beginTransaction();
		
		transcation.replace(R.id.fragment_main, newArchiveFragment);
		transcation.addToBackStack(null);
		transcation.commit();
		
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
