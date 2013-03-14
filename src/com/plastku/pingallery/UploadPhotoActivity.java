package com.plastku.pingallery;

import java.io.ByteArrayOutputStream;

import roboguice.activity.RoboActivity;
import roboguice.activity.RoboFragmentActivity;

import com.google.inject.Inject;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.util.FileUtils;
import com.plastku.pingallery.views.AlertDialogFragment;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadPhotoActivity extends RoboFragmentActivity {

	private Button mPhotoButton;
	private ProgressDialog mProgressDialog;
	ImageView mPhotoPreview;
	private Bitmap mBmp;
	private EditText mPhotoMessage;
	@Inject PhotoModel mPhotoModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_photo_activity);
		
		mPhotoPreview = (ImageView)findViewById(R.id.photoPreview);
		mPhotoMessage = (EditText)findViewById(R.id.photoMessage);

		mPhotoButton = (Button) findViewById(R.id.photoButton);
		mPhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent takePictureIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(takePictureIntent, Constants.PHOTO_CODE);
			}
		});
	}
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	getMenuInflater().inflate(R.menu.upload_menu, menu);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	    	 switch (item.getItemId()) {
	         case R.id.uploadPhoto:
	        	 	savePhoto();
		        return true;
	         default:
	             return super.onOptionsItemSelected(item);
	    	 }
	    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			Bundle extras = intent.getExtras();
			mBmp = (Bitmap) extras.get("data");
			
			mPhotoPreview.setImageBitmap(mBmp);
			mPhotoPreview.setVisibility(View.VISIBLE);
		}
	}
	
	private void savePhoto()
	{
		mProgressDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
		String path = FileUtils.storeBitmapInTempFolder(this, mBmp);
		PhotoVO photo = new PhotoVO();
		photo.message = mPhotoMessage.getText().toString();
		photo.path = path;
		mPhotoModel.savePhoto(photo, new ApiCallback(){

			@Override
			public void onSuccess(ResultVO result) {
				mProgressDialog.dismiss();
				AlertDialogFragment newFragment = new AlertDialogFragment();
				newFragment.setMessage("File uploaded");
				newFragment.show(getSupportFragmentManager(), "dialog");
			}

			@Override
			public void onError(ResultVO result) {
				mProgressDialog.dismiss();
				AlertDialogFragment newFragment = new AlertDialogFragment();
				newFragment.setMessage(result.message);
				newFragment.show(getSupportFragmentManager(), "dialog");
			}
		});
	}
}
