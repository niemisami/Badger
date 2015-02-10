package com.niemisami.badger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NewBadgeFragment extends Fragment{

	private Button archiveButton, newBadgeButton;
	private ArchiveFragment archiveFragment;
	final static String ARG_POSITION ="position";
	int fragmentID = 0;
		
		View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_badge, container, false);
		initButtons();
		return view;
		
	}
	
	private void initButtons(){
		
	}

}
