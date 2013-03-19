package com.plastku.pingallery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import roboguice.activity.RoboFragmentActivity;
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

	private Button mPhotoButton;
	private Button mGalleryButton;
	private ProgressDialog mProgressDialog;
	ImageView mPhotoPreview;
	private Bitmap mBmp;
	private EditText mPhotoMessage;
	@Inject
	PhotoModel mPhotoModel;
	private File mImageFile;
	private String mThumbPath;
	private String mImagePath;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_photo_activity);
		
		mPhotoPreview = (ImageView)findViewById(R.id.photoPreview);
		mPhotoMessage = (EditText)findViewById(R.id.photoMessage);
		
		mGalleryButton = (Button) findViewById(R.id.galleryButton);
		mGalleryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, Constants.GALLERY_CODE);
			}
		});

		mPhotoButton = (Button) findViewById(R.id.photoButton);
		mPhotoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mImageFile = new File(FileUtils.getTempImageFileName(UploadPhotoActivity.this));
			    Uri outputFileUri = Uri.fromFile(mImageFile);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			switch(requestCode)
			{
				case Constants.PHOTO_CODE:
					mBmp = FileUtils.decodeFile(FileUtils.getTempImageFileName(UploadPhotoActivity.this));
				break;
				case Constants.GALLERY_CODE:
					Uri chosenImageUri = intent.getData();
					
					try {
						mBmp = Media.getBitmap(this.getContentResolver(),chosenImageUri);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				break;
			}
			mImagePath = FileUtils.storeBitmapInTempFolder(UploadPhotoActivity.this, mBmp, "image");

			mPhotoPreview.setImageBitmap(mBmp);
			mPhotoPreview.setVisibility(View.VISIBLE);
			
			Bitmap thumb = ThumbnailUtils.extractThumbnail(mBmp, 100, 100);
			mThumbPath = FileUtils.storeBitmapInTempFolder(UploadPhotoActivity.this, thumb, "thumb");
		}
	}

	private void savePhoto() {
		PhotoVO photo = new PhotoVO();
		photo.description = mPhotoMessage.getText().toString();
		photo.image = mImagePath;
		photo.thumb = mThumbPath;

		mProgressDialog = ProgressDialog.show(this, "", "Uploading. Please wait...", true);
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
