package com.plastku.pingallery;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.parse.Parse;
import com.parse.ParseFile;
import com.plastku.pingallery.util.FileUtils;

import roboguice.activity.RoboFragmentActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
        	PhotoSourceAlertDialog dialog = new PhotoSourceAlertDialog(this);
        	dialog.show();
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
			this.startActivity(i);
		}
	}
   
    private static class PhotoSourceAlertDialog extends AlertDialog {

    	private Context mContext;
    	
        protected PhotoSourceAlertDialog(final Context context) {
            super(context);
            this.mContext = context;
            
            setTitle(mContext.getString(R.string.upload));
            View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.photo_source_view,null);
            setView(view);
            Button photoButton = (Button) view.findViewById(R.id.photoButton);
			photoButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					File imageFile = new File(FileUtils.getTempImageFileName(mContext));
				    Uri outputFileUri = Uri.fromFile(imageFile);
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					((Activity) mContext).startActivityForResult(intent, Constants.PHOTO_CODE);
					dismiss();
				}
				
			});
			
			Button galleryButton = (Button) view.findViewById(R.id.galleryButton);
			galleryButton.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					photoPickerIntent.setType("image/*");
					((Activity) mContext).startActivityForResult(photoPickerIntent, Constants.GALLERY_CODE);
					dismiss();
				}
				
			});
        }

    }
}
