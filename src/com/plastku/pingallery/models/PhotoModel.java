package com.plastku.pingallery.models;

import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.plastku.pingallery.events.SimpleEvent;
import com.plastku.pingallery.models.UserModel.ChangeEvent;
import com.plastku.pingallery.Constants;
import com.plastku.pingallery.util.GsonTransformer;
import com.plastku.pingallery.vo.PhotoVO;

@Singleton
public class PhotoModel extends Model {
	
	public static class ChangeEvent extends SimpleEvent {
		public static final String PHOTOS_CHANGED = "photosChanged";
		public static final String PHOTO_CHANGED = "photoChanged";
		
		public ChangeEvent(String type) {
			super(type);
		}
	}
	
	private List<PhotoVO> mPhotos;
	private PhotoVO mCurrentPhoto;
	
	@Inject
	public PhotoModel(Context context) {
		super(context);
	}
	
	public PhotoVO getCurrentPhoto()
	{
		return mCurrentPhoto;
	}
	
	public void setCurrentPhoto(PhotoVO photo)
	{
		mCurrentPhoto = photo;
		this.dispatchEvent(new ChangeEvent(ChangeEvent.PHOTO_CHANGED));
	}

	public List<PhotoVO> getPhotos() {
		return mPhotos;
	}

	public void setPhotos(List<PhotoVO> mPhotos) {
		this.mPhotos = mPhotos;
		this.dispatchEvent(new ChangeEvent(ChangeEvent.PHOTOS_CHANGED));
	}
	
	public void requestAllPhotos()
	{
		String url = Constants.SITE_URL + "api/photos/all";

		GsonTransformer t = new GsonTransformer(){
			@Override
			public <T> T transform(String url, Class<T> type, String encoding, byte[] data, AjaxStatus status) {
				
				Gson g = new Gson();
				T result = g.fromJson(new String(data), new TypeToken<List<PhotoVO>>(){}.getType());				
				
				return result;
			}
		};
		aq.transformer(t).ajax(url, List.class, new AjaxCallback<List>() {

			public void callback(String url, List photos, AjaxStatus status) {
				setPhotos(photos);
			}
		});
	}

	public void requestPhotoById(int id) {
		String url = Constants.SITE_URL + "api/photos/photo/id/"+id;

		GsonTransformer t = new GsonTransformer();
			
		aq.transformer(t).ajax(url, PhotoVO.class, new AjaxCallback<PhotoVO>() {

			public void callback(String url, PhotoVO photo, AjaxStatus status) {				
				setCurrentPhoto(photo);
			}
		});
	}
	
}
