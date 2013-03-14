package com.plastku.pingallery;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_activity);
		Parse.initialize(this, "YGTPfHz9sKYv8WzQvH3823XRRmJJnjYUfU3IYMu3", "XkOhT5kv0MfBJqMOQXgTvGqNszempNWjKbw47bWh");

		ParseUser user = ParseUser.getCurrentUser();
		if (user != null) {
			Log.i("LoginActivity", "We're in!");
			Intent intent = new Intent(IntroActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		// Show the Up button in the action bar.
		Button goToLogin = (Button) findViewById(R.id.loginButton);
		goToLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(IntroActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
		});
		Button goToRegister = (Button) findViewById(R.id.registerButton);
		goToRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(IntroActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});
	}
}
