package com.plastku.pingallery.events;

public class UserEvent extends SimpleEvent {
	
	public static final String REQUEST_USER_BY_ID = "requestUserById";
	
	public int userId;
	
	public UserEvent(String type) {
		super(type);
	}
	
	public UserEvent(String type, int uid) {
		super(type);
		userId = uid;
	}
}
