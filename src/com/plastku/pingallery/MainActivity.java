package com.plastku.pingallery;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.parse.Parse;
import com.parse.ParseFile;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.util.FileUtils;
import com.plastku.pingallery.util.NetworkUtils;
import com.plastku.pingallery.views.PhotoSourceAlertDialog;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends RoboFragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();
    ViewPager mViewPager;
	private MainFragmentPagerAdapter mFragmentPagerAdapter;
	@Inject private NetworkUtils mNetworkUtils;
	@Inject PhotoSourceAlertDialog mPhotoSourceAlert;
	@InjectView(R.id.networkError) View mNetworkErrorView;
	@InjectView(R.id.pager) View mPagerView;
	public int photoType = PhotoModel.DEFAULT_PHOTO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        Parse.initialize(this, "YGTPfHz9sKYv8WzQvH3823XRRmJJnjYUfU3IYMu3", "XkOhT5kv0MfBJqMOQXgTvGqNszempNWjKbw47bWh");
        
        // set titles and fragments for view pager
        Map<String, Fragment> screens = new LinkedHashMap<String, Fragment>();
        screens.put("Search", new SearchFragment());
        screens.put("Explore", new ExploreFragment());
        screens.put("Profile", new ProfileFragment());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mFragmentPagerAdapter = new MainFragmentPagerAdapter(screens, getSupportFragmentManager());  
        mViewPager.setAdapter(mFragmentPagerAdapter);
        this.setState();
        
        mNetworkUtils.setNetworkCallback(new NetworkUtils.NetworkCallback() {
			
			@Override
			public void onConnectionChange(NetworkInfo networkInfo) {
				setState();
			}
		});
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	mNetworkUtils.onDestroy();
    }
    
    private void setState()
    {
    	if(mNetworkUtils.isNetworkAvailable())
        {
    		mPagerView.setVisibility(View.VISIBLE);
    		mNetworkErrorView.setVisibility(View.GONE);
        }else{
        	mPagerView.setVisibility(View.GONE);
    		mNetworkErrorView.setVisibility(View.VISIBLE);
        }
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
        	this.photoType = PhotoModel.DEFAULT_PHOTO;
     		mPhotoSourceAlert.show();
	        return true;
         default:
             return super.onOptionsItemSelected(item);
    	 }
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			String imagePath = null;
			switch(requestCode)
			{
				case Constants.PHOTO_CODE:
					imagePath = FileUtils.getTempImageFileName(this);
				break;
				case Constants.GALLERY_CODE:
					imagePath = FileUtils.getRealPathFromURI(this, intent.getData());
				break;
			}
			
			Intent i=new Intent(this, UploadPhotoActivity.class);
			i.putExtra("imagePath", imagePath);
			i.putExtra("photoType", photoType);
			this.startActivity(i);
		}
	}
}
