package com.plastku.pingallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
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
import com.plastku.pingallery.util.FileUtils;
import com.plastku.pingallery.views.AlertDialogFragment;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

public class UploadPhotoActivity extends RoboFragmentActivity {

	private ProgressDialog mProgressDialog;
	private Bitmap mBmp;
	@Inject PhotoModel mPhotoModel;
	@InjectView(R.id.photoPreview) ImageView mPhotoPreview;
	@InjectView(R.id.photoMessage) EditText mPhotoMessage;
	private String mImagePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_photo_activity);
		
		Intent intent = getIntent();
		mImagePath = intent.getStringExtra("imagePath");
		
		mBmp = FileUtils.decodeFile(mImagePath, 500, 500);	
		if(mBmp != null)
		{
			mPhotoPreview.setImageBitmap(mBmp);
		}
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

	private void savePhoto() {
		PhotoVO photo = new PhotoVO();
		photo.description = mPhotoMessage.getText().toString();
		photo.image = mImagePath;
		
		Bitmap thumb = ThumbnailUtils.extractThumbnail(mBmp, 100, 100);
		photo.thumb = FileUtils.storeBitmapInTempFolder(UploadPhotoActivity.this, thumb, "thumb");

		mProgressDialog = ProgressDialog.show(this, "", "Uploading. Please wait...", true);
		mPhotoModel.addPhoto(photo, new ApiCallback(){
			@Override
			public void onSuccess(ResultVO result) {
				mProgressDialog.dismiss();
				finish();
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
