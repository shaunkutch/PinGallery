package com.plastku.pingallery.models;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.plastku.pingallery.events.SimpleEvent;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.interfaces.GetPhotosCallback;
import com.plastku.pingallery.models.UserModel.ChangeEvent;
import com.plastku.pingallery.Constants;
import com.plastku.pingallery.R;
import com.plastku.pingallery.util.FileUtils;
import com.plastku.pingallery.util.GsonTransformer;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

@Singleton
public class PhotoModel extends Model {
	
	private List<PhotoVO> mPhotos = new ArrayList<PhotoVO>();
	private PhotoVO mCurrentPhoto;
	
	public static class ChangeEvent extends SimpleEvent {
		public static final String PHOTOS_CHANGED = "photosChanged";
		public static final String PHOTO_CHANGED = "photoChanged";
		
		public ChangeEvent(String type) {
			super(type);
		}
	}
	
	public class PhotoResultVO extends ResultVO
	{
		public List<PhotoVO> photos;
	}
	
	@Inject
	public PhotoModel(Context context) {
		super(context);
	}

	public List<PhotoVO> getPhotos() {
		return mPhotos;
	}
	
	public void setPhotos(List<PhotoVO> photos)
	{
		mPhotos = photos;
		dispatchEvent(new PhotoModel.ChangeEvent(PhotoModel.ChangeEvent.PHOTOS_CHANGED));
	}
	
	public void requestPhotos(int start, int limit, final ApiCallback callback)
	{
		ParseQuery query = new ParseQuery("Photo");
		query.setSkip(start);
		query.setLimit(limit);
		queryPhotos(query, callback);
	}
	
	public void queryPhotos(ParseQuery query, final ApiCallback callback)
	{
		query.findInBackground(new FindCallback() {
	        public void done(List<ParseObject> content, ParseException e) {
	        	if(e == null)
	        	{
	        		ArrayList<PhotoVO> photos = new ArrayList<PhotoVO>();
		        	for(ParseObject o : content)
		        	{
		        		PhotoVO photo = new PhotoVO();
		        		photo.description = o.getString("description");
		        		ParseUser user = o.getParseUser("user");
		        		photo.userId = user.getObjectId();
		        		ParseFile file = (ParseFile) o.get("file");
		        		photo.path = file.getUrl();
		        		//Check if photo already exists in list 
		        		if(!mPhotos.contains(photo))
		        		{
		        			photos.add(photo);
		        		}
		        	}
		        	setPhotos(photos);
		        	PhotoResultVO result = new PhotoResultVO();
		        	result.message = String.valueOf(content.size()) + mContext.getString(R.string.photos_loaded);
		        	result.photos = mPhotos;
		        	if(callback != null)
		        	{
		        		callback.onSuccess(result);
		        	}
	        	}else{
	        		ResultVO result = new ResultVO();
	        		result.message = e.getMessage();
	        		if(callback != null)
		        	{
	        			callback.onError(result);
		        	}
	        	}
	        }
	});
	}
	
	public void addPhoto(PhotoVO photo, final ApiCallback callback)
	{
		byte[] byteArray = FileUtils.getByteArrayImage(mContext, photo.path);	
		ParseFile file = new ParseFile("photo.png", byteArray);
		try {
			file.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ParseObject photoObject = new ParseObject("Photo");
		photoObject.put("file", file);
		ParseUser currentUser = ParseUser.getCurrentUser();
		photoObject.setACL(new ParseACL(currentUser));
		photoObject.put("user", currentUser);
		photoObject.put("description", photo.description);
		photoObject.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				ResultVO result = new ResultVO();
				if(e == null)
				{	
					result.message = "File Uploaded";
					callback.onSuccess(result);
				}else{
					result.message = e.getMessage();
					callback.onError(result);
				}
			}
		});
	}

	public void setCurrentPhoto(PhotoVO photo) {
		this.mCurrentPhoto = photo;
	}
	
	public PhotoVO getCurrentPhoto()
	{
		return mCurrentPhoto;
	}
}
