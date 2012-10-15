package com.plastku.pingallery;

import java.util.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private Map<String, Fragment> mScreens;
	/*protected static final int[] ICONS = new int[] {
        R.drawable.ic_action_search,
        R.drawable.ic_action_globe,
        R.drawable.ic_action_user,
	};*/

	public MainFragmentPagerAdapter(Map<String, Fragment> screenMap, FragmentManager fm) {
		super(fm);
	    this.mScreens = screenMap;
	}

	@Override
	public Fragment getItem(int position) {

		return mScreens.values().toArray(new Fragment[mScreens.size()])[position];
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mScreens.keySet().toArray(new String[mScreens.size()])[position];
	}

	@Override
	public int getCount() {
		return mScreens.size();
	}

	/*@Override
	public int getIconResId(int index) {
		return ICONS[index % ICONS.length];
	}*/
}
