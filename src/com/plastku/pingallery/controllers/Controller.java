package com.plastku.pingallery.controllers;

import java.util.ArrayList;
import java.util.List;

import com.plastku.pingallery.models.Model;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class Controller {

	protected static final String TAG = Controller.class.getSimpleName();

	protected static final int C_QUIT = 0;
	
	protected final HandlerThread inboxHandlerThread;
	protected final Handler inboxHandler;
	protected final List<Handler> outboxHandlers = new ArrayList<Handler>();
	
	public Controller() {
		inboxHandlerThread = new HandlerThread("Controller Inbox"); // note you can also set a priority here
		inboxHandlerThread.start();

		inboxHandler = new Handler(inboxHandlerThread.getLooper()) {
			@Override
			public void handleMessage(Message msg) {
				Controller.this.handleMessage(msg);
			}
		};
	}
	
	public final void dispose() {
		// ask the inbox thread to exit gracefully
		inboxHandlerThread.getLooper().quit();
	}
	
	public final Handler getInboxHandler() {
		return inboxHandler;
	}
	
	public final void addOutboxHandler(Handler handler) {
		outboxHandlers.add(handler);
	}

	public final void removeOutboxHandler(Handler handler) {
		outboxHandlers.remove(handler);
	}
	
	final void notifyOutboxHandlers(int what, int arg1, int arg2, Object obj) {
		if (outboxHandlers.isEmpty()) {
			Log.w(TAG, String.format("No outbox handler to handle outgoing message (%d)", what));
		} else {
			for (Handler handler : outboxHandlers) {
				Message msg = Message.obtain(handler, what, arg1, arg2, obj);
				msg.sendToTarget();
			}
		}
	}

	protected boolean handleMessage(Message msg) {
		Log.d(TAG, "Received message: " + msg);
		return false;
	}

	final void quit() {
		notifyOutboxHandlers(C_QUIT, 0, 0, null);
	}
	
}
