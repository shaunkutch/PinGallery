package com.plastku.pingallery;


import java.util.LinkedHashMap;
import java.util.Map;

import com.darko.imagedownloader.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
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
        screens.put(Constants.CREATE_GALLERY_TITLE, new CreateGalleryFragment());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(screens, getSupportFragmentManager());  
        mViewPager.setAdapter(mFragmentPagerAdapter);
        
        mImageLoader = createImageLoader(this);
        //PreferenceUtil.init(getApplicationContext());
    }
    
    private static ImageLoader createImageLoader(Context context) {
        ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder);
        imageLoader.setDefaultBitmapSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources()
                        .getDisplayMetrics()));
        return imageLoader;
    }

    private ImageLoader mImageLoader;
    
	@Override
	public Object getSystemService(String name) {
		if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
			return mImageLoader;
		} else {
			return super.getSystemService(name);
		}
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
