package com.plastku.pingallery.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.plastku.pingallery.R;
import com.plastku.pingallery.delegates.SavePhotoDelegate;
import com.plastku.pingallery.events.SimpleEvent;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

@Singleton
public class PhotoModel extends Model {
	
	private List<PhotoVO> mPhotos = new ArrayList<PhotoVO>();
	private PhotoVO mCurrentPhoto;
	@Inject private SavePhotoDelegate mSavePhotoDelegate;
	
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
		        		ParseFile image = (ParseFile) o.get("image");
		        		ParseFile thumb = (ParseFile) o.get("thumb");
		        		photo.image = image.getUrl();
		        		photo.thumb = thumb.getUrl();
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
		mSavePhotoDelegate.save(photo, callback);
	}

	public void setCurrentPhoto(PhotoVO photo) {
		this.mCurrentPhoto = photo;
	}
	
	public PhotoVO getCurrentPhoto()
	{
		return mCurrentPhoto;
	}
}
