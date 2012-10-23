package com.plastku.pingallery;


import java.util.LinkedHashMap;
import java.util.Map;

import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.darko.imagedownloader.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	 switch (item.getItemId()) {
         case R.id.addPhoto:
	    	PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(MainActivity.this);
	        wrapper.setMultiPicker(false);
	        wrapper.startActivityForResult(MainActivity.this, PhotoPickerPlusIntentWrapper.REQUEST_CODE);
	        return true;
         default:
             return super.onOptionsItemSelected(item);
    	 }
    }
}
