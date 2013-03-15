package com.plastku.pingallery;

import java.io.File;

import roboguice.activity.RoboFragmentActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.inject.Inject;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.views.AlertDialogFragment;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

public class UploadPhotoActivity extends RoboFragmentActivity {

	private Button mPhotoButton;
	private ProgressDialog mProgressDialog;
	ImageView mPhotoPreview;
	private Bitmap mBmp;
	private EditText mPhotoMessage;
	@Inject
	PhotoModel mPhotoModel;
	private Uri mImageUri;

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
				File file = new File(Constants.PHOTO_PATH);
			    Uri outputFileUri = Uri.fromFile(file);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(intent, Constants.PHOTO_CODE);
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
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inSampleSize = 4;
		    	
		    mBmp = BitmapFactory.decodeFile(Constants.PHOTO_PATH, options);

			mPhotoPreview.setImageBitmap(mBmp);
			mPhotoPreview.setVisibility(View.VISIBLE);
		}
	}

	private void savePhoto() {
		mProgressDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
		String path = Constants.PHOTO_PATH;
		PhotoVO photo = new PhotoVO();
		photo.description = mPhotoMessage.getText().toString();
		photo.path = path;

		mPhotoModel.addPhoto(photo, new ApiCallback(){
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
