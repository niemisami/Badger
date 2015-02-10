package com.niemisami.badger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArchiveFragment extends Fragment {
	private View view;

	final static String ARG_POSITION = "position";
	int fragmentID = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		view = inflater.inflate(R.layout.fragment_archive, container, false);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current article selection in case we need to recreate the fragment
		outState.putInt(ARG_POSITION, fragmentID);
	}
}
