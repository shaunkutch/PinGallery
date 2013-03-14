package com.plastku.pingallery;

import java.io.ByteArrayOutputStream;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

public class UploadPhotoActivity extends Activity {

	private Button mPhotoButton;
	private ProgressDialog mProgressDialog;
	ImageView mPhotoPreview;
	private Bitmap mBmp;
	private EditText mPhotoMessage;

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
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		
		ParseFile file = new ParseFile("photo.png", byteArray);
		try {
			file.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ParseObject photo = new ParseObject("Photo");
		photo.put("file", file);
		ParseUser currentUser = ParseUser.getCurrentUser();
		photo.setACL(new ParseACL(currentUser));
		photo.put("user", currentUser);
		photo.put("message", mPhotoMessage.getText().toString());
		photo.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException arg0) {
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "PHOTO SAVED!", 
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
