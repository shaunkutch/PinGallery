package com.plastku.pingallery;

import java.util.Observable;
import java.util.Observer;

import com.androidquery.AQuery;
import com.google.inject.Inject;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.events.UserEvent;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.models.UserModel;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;
import com.plastku.pingallery.vo.UserVO;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.photo_info)
public class PhotoInfoActivity extends RoboActivity {
	
	@Inject UserModel mUserModel;
	@Inject PhotoModel mPhotoModel;
	@InjectView(R.id.imagePreview) ImageView mImagePreview;
	@InjectView(R.id.userAvatar) ImageView mUserAvatar;
	private PhotoVO mPhoto;
	private AQuery aq;
	private UserVO mUser;
	
	
	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        aq = new AQuery(this);
        aq.id(mImagePreview).clickable(true);
        aq.id(mImagePreview).clicked(this, "onPhotoClicked");
        mPhotoModel.addListener(PhotoModel.ChangeEvent.PHOTO_CHANGED, photoChangedListener);
        mUserModel.addListener(UserModel.ChangeEvent.USER_ADDED, userChangedListener);
        mPhoto = mPhotoModel.getCurrentPhoto();
        updatePreviewPhoto();
        updateUserAvatar();
	}

	public void onPhotoClicked(View view)
	{
		Intent intent = new Intent(this, PhotoViewActivity.class);
		intent.putExtra("photo", mPhoto);
    	this.startActivity(intent);
	}
	
	private void updatePreviewPhoto()
	{
		String photoUrl = mPhoto.image;      
        aq.id(mImagePreview).image(photoUrl, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE); 
	}
	
	private void updateUserAvatar()
	{
		mUserModel.getUserById(mPhoto.userId, new ApiCallback(){

			@Override
			public void onSuccess(ResultVO result) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onError(ResultVO result) {
				// TODO Auto-generated method stub
				
			}});
		//String avatarUrl = Constants.SITE_URL+mUser.avatar_path+"/100/fit";
		//aq.id(mUserAvatar).image(avatarUrl, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
	}
	
	@Override
    public void onStop()
	{
		super.onStop();
		mPhotoModel.removeListener(PhotoModel.ChangeEvent.PHOTO_CHANGED, photoChangedListener);
        mUserModel.removeListener(UserModel.ChangeEvent.USER_ADDED, userChangedListener);
	}
	
	private EventListener photoChangedListener = new EventListener() {
		@Override
		public void onEvent(Event event) {
			updatePreviewPhoto();
		}
	};
	
	private EventListener userChangedListener = new EventListener() {
		@Override
		public void onEvent(Event event) {
			updateUserAvatar();
		}
	};

	
}
