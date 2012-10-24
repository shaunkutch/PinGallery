package com.plastku.pingallery.controllers;

import android.os.Message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.events.EventDispatcher;
import com.plastku.pingallery.events.UserEvent;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.models.UserModel;

@Singleton
public class UserController extends EventDispatcher {
	

private static final String TAG = Controller.class.getSimpleName();
	
	@Inject UserModel mUserModel;
	
	public UserController() {
		this.addListener(UserEvent.REQUEST_USER_BY_ID, requestUserByIdListener);
	}
	
	private EventListener requestUserByIdListener = new EventListener() {
		@Override
		public void onEvent(Event event) {
			UserEvent e = (UserEvent)event;
			mUserModel.requestUserById(e.userId);
		}
	};
}
