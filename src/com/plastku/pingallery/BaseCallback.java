package com.plastku.pingallery;

import android.app.Activity;
import android.widget.Toast;

import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.model.GCHttpRequestParameters;

public class BaseCallback<T> implements GCHttpCallback<T> {

	private Activity mActivity;
	
	public BaseCallback(Activity activity)
	{
		mActivity = activity;
	}
	
	@Override
	public void onSuccess(T responseData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHttpException(GCHttpRequestParameters params,
			Throwable exception) {
		Toast.makeText(
				mActivity,
				mActivity.getResources().getString(
						R.string.http_exception), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onHttpError(int responseCode, String statusMessage) {
		Toast.makeText(
				mActivity,
				mActivity.getResources().getString(
						R.string.http_error), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onParserException(int responseCode, Throwable exception) {
		Toast.makeText(
				mActivity,
				mActivity.getResources().getString(
						R.string.parsing_exception), Toast.LENGTH_SHORT)
				.show();
	}

}
