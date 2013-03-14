package com.plastku.pingallery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.plastku.pingallery.views.AlertDialogFragment;

public class LoginActivity extends FragmentActivity {
	
	private String mUsername;
	private String mPassword;
	private EditText mUsernameEditText;
	private EditText mPasswordEditText;
	private ProgressDialog mProgressDialog;

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
		
		ParseUser.logInInBackground(mUsername, mPassword, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException e) {
				mProgressDialog.dismiss();
				if (user != null) {
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					AlertDialogFragment newFragment = new AlertDialogFragment();
					newFragment.setMessage(e.getMessage());
					newFragment.show(getSupportFragmentManager(), "dialog");
				}
			}
		});
	}
}
