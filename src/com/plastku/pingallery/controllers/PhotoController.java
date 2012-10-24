package com.plastku.pingallery.controllers;

import android.os.Message;

import com.google.inject.Inject;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventDispatcher;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.events.PhotoEvent;
import com.plastku.pingallery.events.UserEvent;
import com.plastku.pingallery.models.PhotoModel;

public class PhotoController extends EventDispatcher {

	private static final String TAG = Controller.class.getSimpleName();

	@Inject
	PhotoModel mPhotoModel;

	public PhotoController() {
		this.addListener(PhotoEvent.REQUEST_PHOTOS, requestPhotos);
	}

	private EventListener requestPhotos = new EventListener() {
		@Override
		public void onEvent(Event event) {
			mPhotoModel.requestAllPhotos();
		}
	};
}
