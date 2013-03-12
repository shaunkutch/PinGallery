package com.plastku.pingallery;

import java.util.Observable;
import java.util.Observer;

import com.androidquery.AQuery;
import com.google.inject.Inject;
import com.plastku.pingallery.controllers.Controller;
import com.plastku.pingallery.controllers.PhotoController;
import com.plastku.pingallery.controllers.UserController;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.events.UserEvent;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.models.UserModel;
import com.plastku.pingallery.vo.PhotoVO;
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
	@Inject PhotoController mPhotoController;
	@Inject UserController mUserContoller;
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
        updatePreviewPhoto();
        int userId = mPhotoModel.getCurrentPhoto().user_id;
        mUserContoller.dispatchEvent(new UserEvent(UserEvent.REQUEST_USER_BY_ID, userId));
	}

	public void onPhotoClicked(View view)
	{
		Intent intent = new Intent(this, PhotoViewActivity.class);
		intent.putExtra("photo", mPhoto);
    	this.startActivity(intent);
	}
	
	private void updatePreviewPhoto()
	{
		mPhoto = mPhotoModel.getCurrentPhoto();
		String photoUrl = Constants.SITE_URL+mPhoto.path+"/500/fit";      
        aq.id(mImagePreview).image(photoUrl, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE); 
	}
	
	private void updateUserAvatar()
	{
		mUser = mUserModel.getUserById(mPhoto.user_id);
		String avatarUrl = Constants.SITE_URL+mUser.avatar_path+"/100/fit";
		aq.id(mUserAvatar).image(avatarUrl, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
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
