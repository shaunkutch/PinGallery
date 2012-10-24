package com.plastku.pingallery.events;

public interface Event {
	
	public String getType();
	public Object getSource();
	public void setSource(Object source);
}
