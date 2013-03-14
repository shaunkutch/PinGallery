package com.plastku.pingallery;


import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.parse.Parse;
import com.parse.ParseFile;

import roboguice.activity.RoboFragmentActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends RoboFragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();
    ViewPager mViewPager;
	private MainFragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        Parse.initialize(this, "YGTPfHz9sKYv8WzQvH3823XRRmJJnjYUfU3IYMu3", "XkOhT5kv0MfBJqMOQXgTvGqNszempNWjKbw47bWh");
        
        // set titles and fragments for view pager
        Map<String, Fragment> screens = new LinkedHashMap<String, Fragment>();
        screens.put("Search", new SearchFragment());
        screens.put("Explore", new ExploreFragment());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(screens, getSupportFragmentManager());  
        mViewPager.setAdapter(mFragmentPagerAdapter);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	 switch (item.getItemId()) {
         case R.id.addPhoto:
        	 	Intent intent = new Intent(this, UploadPhotoActivity.class);
				startActivity(intent);
	        return true;
         default:
             return super.onOptionsItemSelected(item);
    	 }
    }
    
   
}
