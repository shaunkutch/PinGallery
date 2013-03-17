package com.plastku.pingallery.delegates;

import android.content.Context;

import com.google.inject.Inject;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.util.FileUtils;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

public class SavePhotoDelegate {

	private ParseFile mImageFile;
	private ParseFile mThumbFile;
	private ApiCallback mCallback;
	private PhotoVO mUploadPhoto;
	private Context mContext;
	
	@Inject
	public SavePhotoDelegate(Context context) {
		mContext = context;
	}
	
	public void save(PhotoVO photo, ApiCallback callback)
	{
		mUploadPhoto = photo;
		mCallback = callback;
		saveImage();
	}
	
	private void saveImage()
	{
		byte[] fileByteArray = FileUtils.getByteArrayImage(mContext, mUploadPhoto.image);
		mImageFile = new ParseFile("image.jpg", fileByteArray);
		mImageFile.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				ResultVO result = new ResultVO();
				if(e == null)
				{
					saveThumb();
				}else{
					result.message = e.getMessage();
					mCallback.onError(result);
				}
			}
		});
	}
	
	private void saveThumb()
	{
		byte[] fileByteArray = FileUtils.getByteArrayImage(mContext, mUploadPhoto.thumb);
		mThumbFile = new ParseFile("thumb.jpg", fileByteArray);
		mThumbFile.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				ResultVO result = new ResultVO();
				if(e == null)
				{
					savePhoto();
				}else{
					result.message = e.getMessage();
					mCallback.onError(result);
				}
			}
		});
	}
	
	private void savePhoto()
	{
		ParseObject photoObject = new ParseObject("Photo");
		photoObject.put("image", mImageFile);
		photoObject.put("thumb", mThumbFile);
		ParseUser currentUser = ParseUser.getCurrentUser();
		photoObject.setACL(new ParseACL(currentUser));
		photoObject.put("user", currentUser);
		photoObject.put("description", mUploadPhoto.description);
		photoObject.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				ResultVO result = new ResultVO();
				if(e == null)
				{	
					result.message = "File Uploaded";
					mCallback.onSuccess(result);
				}else{
					result.message = e.getMessage();
					mCallback.onError(result);
				}
			}
		});
	}
}
