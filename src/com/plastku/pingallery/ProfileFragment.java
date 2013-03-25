package com.plastku.pingallery;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.google.inject.Inject;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.util.FileUtils;
import com.plastku.pingallery.views.PhotoSourceAlertDialog;

public class ProfileFragment extends RoboFragment {
	
	private AQuery aq;
	@InjectView(R.id.userAvatar) private ImageView mUserAvatarImageView;
	@Inject PhotoSourceAlertDialog mPhotoSourceAlert;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.profile_fragment, container, false);
		aq = new AQuery(getActivity(), view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		aq.id(mUserAvatarImageView).clickable(true);
		aq.id(mUserAvatarImageView).clicked(this, "onAvatarClicled");
	}
	
	public void onAvatarClicled(View view)
	{
		MainActivity c = (MainActivity)this.getActivity();
		c.photoType = PhotoModel.AVATAR_PHOTO;
		mPhotoSourceAlert.show();
	}
}
