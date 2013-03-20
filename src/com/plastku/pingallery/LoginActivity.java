package com.plastku.pingallery;

import roboguice.activity.RoboFragmentActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.models.UserModel;
import com.plastku.pingallery.views.AlertDialogFragment;
import com.plastku.pingallery.vo.ResultVO;

public class LoginActivity extends RoboFragmentActivity {
	
	private String mUsername;
	private String mPassword;
	private EditText mUsernameEditText;
	private EditText mPasswordEditText;
	private ProgressDialog mProgressDialog;
	@Inject private UserModel mUserModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		
		Button loginButton = (Button)findViewById(R.id.loginButton);
		mUsernameEditText = (EditText)findViewById(R.id.username);
		mPasswordEditText = (EditText)findViewById(R.id.password);
		
		loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				doLogin();
			}
		});

	}
	
	private void doLogin()
	{
		mUsername = mUsernameEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();
		
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordEditText.setError(getString(R.string.error_field_required));
			return;
		} else if (mPassword.length() < 4) {
			mPasswordEditText.setError(getString(R.string.error_invalid_password));
			return;
		}

		if (TextUtils.isEmpty(mUsername)) {
			mUsernameEditText.setError(getString(R.string.error_field_required));
			return;
		}
		
		mProgressDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
		mUserModel.login(mUsername, mPassword, new ApiCallback(){

			@Override
			public void onSuccess(ResultVO result) {
				mProgressDialog.dismiss();
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onError(ResultVO result) {
				mProgressDialog.dismiss();
				AlertDialogFragment newFragment = new AlertDialogFragment();
				newFragment.setMessage(result.message);
				newFragment.show(getSupportFragmentManager(), "dialog");
			}});
		
	}
}
