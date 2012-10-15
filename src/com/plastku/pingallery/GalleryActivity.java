package com.plastku.pingallery;


import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.chute.GCChutes;
import com.chute.sdk.model.GCChuteModel;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.plastku.pingallery.intent.GalleryActivityIntentWrapper;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

public class GalleryActivity extends Activity {

	public static final String TAG = GalleryActivity.class.getSimpleName();
	private final GCChuteModel chute = new GCChuteModel();
	private GalleryActivityIntentWrapper wrapper;
	private TextView chuteName;
	private TextView chutePassword;
	private TextView permissionPhotos;
	private TextView permissionMembers;
	private TextView permissionComments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chute_activity);

		wrapper = new GalleryActivityIntentWrapper(getIntent());

		chuteName = (TextView) findViewById(R.id.txtName);
		chutePassword = (TextView) findViewById(R.id.txtPassword);
		permissionPhotos = (TextView) findViewById(R.id.txtPermissionPhotos);
		permissionMembers = (TextView) findViewById(R.id.txtPermissionMembers);
		permissionComments = (TextView) findViewById(R.id.txtPermissionComments);

		switch (wrapper.getChuteFlag()) {
		case 0:
			displayBasicChute();
			break;
		case 1:
			displayPasswordChute();
			break;
		case 2:
			displayPermissionsChute();
			break;
		}

		GCChutes.createChute(getApplicationContext(), chute,
				new CreateChuteCallback()).executeAsync();

	}

	private final class CreateChuteCallback implements
			GCHttpCallback<GCChuteModel> {

		@Override
		public void onSuccess(GCChuteModel responseData) {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.chute_created), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.http_exception), Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.http_error), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			Toast.makeText(
					getApplicationContext(),
					getApplicationContext().getResources().getString(
							R.string.parsing_exception), Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void displayBasicChute() {
		chuteName.setText(getApplicationContext().getResources().getString(
				R.string.chute_name)
				+ " " + wrapper.getChuteName());

		chute.setName(wrapper.getChuteName());
		chute.setPermissionView(2); // public chute
	}

	public void displayPasswordChute() {
		chuteName.setText(getApplicationContext().getResources().getString(
				R.string.chute_name)
				+ " " + wrapper.getChuteName());
		chutePassword.setText(getApplicationContext().getResources().getString(
				R.string.chute_password)
				+ " " + wrapper.getChutePassword());

		chute.setName(wrapper.getChuteName());
		chute.setPassword(wrapper.getChutePassword());
		chute.setPermissionView(4); // chute with password
	}

	public void displayPermissionsChute() {
		chuteName.setText(getApplicationContext().getResources().getString(
				R.string.chute_name)
				+ " " + wrapper.getChuteName());
		chute.setName(wrapper.getChuteName());

		if (TextUtils.isEmpty(wrapper.getChutePassword())) {
			chute.setPermissionView(2); // public chute
		} else {
			chutePassword.setText(getApplicationContext().getResources()
					.getString(R.string.chute_password)
					+ " "
					+ wrapper.getChutePassword());
			chute.setPassword(wrapper.getChutePassword());
			chute.setPermissionView(4); // chute with password

		}

		chute.setPermissionAddPhotos(2); // public; anyone can add photos
		permissionPhotos.setText(getApplicationContext().getResources()
				.getString(R.string.permission_to_add_photos));

		chute.setPermissionAddMembers(2); // public; anyone can add members
		permissionMembers.setText(getApplicationContext().getResources()
				.getString(R.string.permission_to_add_members));

		chute.setPermissionAddComments(2); // public; anyone can add comments
		permissionComments.setText(getApplicationContext().getResources()
				.getString(R.string.permission_to_wite_comments));

	}

}