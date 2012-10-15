package com.plastku.pingallery;


import java.util.LinkedHashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class MainActivity extends FragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();
    ViewPager mViewPager;
	private MainFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        // set titles and fragments for view pager
        Map<String, Fragment> screens = new LinkedHashMap<String, Fragment>();
        screens.put(Constants.CREEATE_GALLERY_TITLE, new CreateGalleryFragment());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(screens, getSupportFragmentManager());  
        mViewPager.setAdapter(mFragmentPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
