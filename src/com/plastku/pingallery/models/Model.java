package com.plastku.pingallery.models;

import java.util.Observable;

import android.content.Context;

import com.androidquery.AQuery;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.plastku.pingallery.events.EventDispatcher;

public class Model extends EventDispatcher {

	protected Context mContext;
	protected AQuery aq;

	public Model(Context context)
	{
		mContext = context;
		aq = new AQuery(mContext);
	}
}
