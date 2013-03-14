package com.plastku.pingallery;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.plastku.pingallery.views.AlertDialogFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends FragmentActivity {

	private EditText mUsernameEditText;
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private String mEmail;
	private String mPassword;
	private String mUsername;
	private ProgressDialog mProgressDialog;
	private EditText mPasswordConfirmEditText;
	private String mPasswordConfirm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);

		mUsernameEditText = (EditText) findViewById(R.id.username);
		mEmailEditText = (EditText) findViewById(R.id.email);
		mPasswordEditText = (EditText) findViewById(R.id.password);
		mPasswordConfirmEditText = (EditText) findViewById(R.id.passwordConfirm);

		findViewById(R.id.registerButton).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					doRegister();
				}
			}
		);
	}
	
	private void doRegister()
	{
		mEmail = mEmailEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();
		mPasswordConfirm = mPasswordConfirmEditText.getText().toString();
		mUsername = mUsernameEditText.getText().toString();
		
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordEditText.setError(getString(R.string.error_field_required));
			return;
		} else if (mPassword.length() < 4) {
			mPasswordEditText.setError(getString(R.string.error_invalid_password));
			return;
		}else if (!mPassword.equals(mPasswordConfirm))
		{
			mPasswordEditText.setText("");
			mPasswordConfirmEditText.setText("");
			mPasswordEditText.setError(getString(R.string.error_missmatch_password));
			return;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailEditText.setError(getString(R.string.error_field_required));
			return;
		} else if (!mEmail.contains("@")) {
			mEmailEditText.setError(getString(R.string.error_invalid_email));
			return;
		}
		
		if(TextUtils.isEmpty(mUsername))
		{
			mUsernameEditText.setError(getString(R.string.error_field_required));
			return;
		}else if(mPassword.length() < 3){
			mUsernameEditText.setError(getString(R.string.error_invalid_password));
			return;
		}
		
		mProgressDialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
		
		ParseUser user = new ParseUser();
		user.setUsername(mUsername);
		user.setPassword(mPassword);
		user.setEmail(mEmail);
		 
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
			mProgressDialog.dismiss();
		    if (e == null) {
		    	Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
