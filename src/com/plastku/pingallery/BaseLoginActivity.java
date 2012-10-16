package com.plastku.pingallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chute.sdk.api.authentication.GCAuthenticationActivity;
import com.chute.sdk.api.authentication.GCAuthenticationFactory.AccountType;
import com.chute.sdk.model.GCAccountStore;
import com.plastku.pingallery.R;

public abstract class BaseLoginActivity extends Activity {

	// Fill with values from your chute developer account

	private static final String CLIENT_SECRET = "fde7352cbcfa10dfc52e21c19fdeac6c5d6ad324421d8f915eb2016f9387f2f8";
	private static final String CLIENT_ID = "50791358cc72f8496f000015";
	private static final String CALLBACK_URL = "http://www.plastku.com/oauth/callback";
	private static final String PERMISSIONS_SCOPE = "all_resources manage_resources profile resources";

	public static final String TAG = BaseLoginActivity.class.getSimpleName();

	private Button facebookLogin;
	private Button twitterLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		facebookLogin = (Button) findViewById(R.id.btnFacebook);
		facebookLogin.setTag(AccountType.FACEBOOK);
		twitterLogin = (Button) findViewById(R.id.btnTwitter);
		twitterLogin.setTag(AccountType.TWITTER);

		OnLoginClickListener loginClickListener = new OnLoginClickListener();
		facebookLogin.setOnClickListener(loginClickListener);
		twitterLogin.setOnClickListener(loginClickListener);

		if (GCAccountStore.getInstance(getApplicationContext()).isTokenValid()) {
			facebookLogin.setVisibility(View.GONE);
			twitterLogin.setVisibility(View.GONE);
			launchMainAppActivity();
		} else {
			facebookLogin.setVisibility(View.VISIBLE);
			twitterLogin.setVisibility(View.VISIBLE);
		}
	}

	private class OnLoginClickListener implements OnClickListener {
		@Override
		public void onClick(final View v) {
			final AccountType accountType = (AccountType) v.getTag();
			launchAuthenticationActivity(accountType);
		}
	}

	public abstract void launchMainAppActivity();

	protected void launchAuthenticationActivity(AccountType accountType) {
		GCAccountStore.getInstance(getApplicationContext())
				.startAuthenticationActivity(BaseLoginActivity.this,
						accountType, PERMISSIONS_SCOPE, CALLBACK_URL,
						CLIENT_ID, CLIENT_SECRET);
	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			NotificationUtil.makeToast(getApplicationContext(),
					R.string.success);
			launchMainAppActivity();
			return;
		}
		if (resultCode == GCAuthenticationActivity.CODE_HTTP_EXCEPTION) {
			NotificationUtil
					.makeConnectionProblemToast(getApplicationContext());
		} else if (resultCode == GCAuthenticationActivity.CODE_HTTP_ERROR) {
			NotificationUtil.makeServerErrorToast(getApplicationContext());
		} else if (resultCode == GCAuthenticationActivity.CODE_PARSER_EXCEPTION) {
			NotificationUtil.makeParserErrorToast(getApplicationContext());
		}
	}
}