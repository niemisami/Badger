package com.niemisami.badger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment{
	
	private Button archiveButton, newBadgeButton;
	
	final static String ARG_POSITION ="position";
	int fragmentID = 0;
		
		View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_main, container, false);
		initButtons();
		return view;
	}
	
	private void initButtons(){
		newBadgeButton = (Button)view.findViewById(R.id.newBardgeButton);
		newBadgeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				((MainActivity)getActivity()).changeFragment(2);
			}
		});
		archiveButton = (Button)view.findViewById(R.id.archiveButton);
		archiveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).changeFragment(1);
				}
		});
	}
	
	@Override 
	public void onStart(){
		super.onStart();
//		
//		Bundle args = getArguments();
//		if(args != null){
//			Update
//		}
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}
