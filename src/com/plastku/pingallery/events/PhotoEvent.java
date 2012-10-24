package com.plastku.pingallery.events;

public class PhotoEvent extends SimpleEvent {
	
	public static final String REQUEST_PHOTOS = "requestPhotos";
	public static final String REQUEST_PHOTO_BY_ID = "requestPhotoById";
	
	public int photoId;

	public PhotoEvent(String type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public PhotoEvent(String type, int pid) {
		super(type);
		photoId = pid;
	}
}
