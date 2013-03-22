package com.plastku.pingallery;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.androidquery.AQuery;

public class ExploreFragment extends RoboFragment {

	private AQuery aq;
	@InjectView(R.id.photoGrid) GridView mGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.explore, container, false);
		aq = new AQuery(getActivity(), view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
}
