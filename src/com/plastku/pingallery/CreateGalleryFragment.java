package com.plastku.pingallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.chute.ChutesAllGetRequest;
import com.chute.sdk.api.chute.GCChutes;
import com.chute.sdk.api.user.GCUser;
import com.chute.sdk.collections.GCChuteCollection;
import com.chute.sdk.model.GCAccountStore;
import com.chute.sdk.model.GCChuteModel;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.utils.GCConstants;
import com.chute.sdk.utils.GCPreferenceUtil;
import com.plastku.pingallery.dialog.BaseDialog;
import com.plastku.pingallery.dialog.BaseDialog.OnDismissListener;
import com.plastku.pingallery.dialog.EditTextDialog;

public class CreateGalleryFragment extends Fragment implements
		OnDismissListener {

	public static final String TAG = CreateGalleryFragment.class
			.getSimpleName();
	private Button createGalleryBtn;
	private Button btnPasswordChute;
	private Button btnPermissionsChute;
	private Intent intent;
	private Activity mActivity;
	private EditTextDialog mDialog;
	private final GCChuteModel chute = new GCChuteModel();
	private String galleryName;
	private Button pickPhotoBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = this.getActivity();
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.create_gallery, container, false);
		createGalleryBtn = (Button) view.findViewById(R.id.btnChuteBasic);
		pickPhotoBtn = (Button) view.findViewById(R.id.pickPhotoBtn);

		createGalleryBtn.setOnClickListener(new ChuteBasicClickListener());
		pickPhotoBtn.setOnClickListener(new PickPhotoListener());
		return view;
	}

	/** Called when the activity is first created. */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Test token, see GCAuthentication activity on how to authenticate
		GCAccountStore account = GCAccountStore.getInstance(mActivity);
		account.setPassword("4b8c64b3b1e6ba4bf4ad3ce4ec2c6bb3e4dc80d5942b705ef18d8915f7a37921");
		
		GCUser.userChutes(mActivity, GCConstants.CURRENT_USER_ID, new UserChuteCallback()).executeAsync();

		mDialog = new EditTextDialog(mActivity, Constants.DIALOG_EDITTEXT, this);
		mDialog.TitleText = "Test";
		mDialog.DefaultText = "Enter Text here";
	}

	private final class ChuteBasicClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			mDialog.show();
		}

	}
	
	private final class PickPhotoListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(mActivity);
	        wrapper.setMultiPicker(false);
	        wrapper.startActivityForResult(mActivity, PhotoPickerPlusIntentWrapper.REQUEST_CODE);
		}

	}

	public void onDialogDismissed(BaseDialog dialog) {
		if (!dialog.DidAccept) {
			return;
		}
		EditTextDialog editTextDialog = (EditTextDialog) dialog;
		chute.setName(editTextDialog.Text);
		chute.setPermissionView(2); // public chute
		GCChutes.createChute(mActivity, chute, new CreateChuteCallback())
				.executeAsync();
	}
	
	private final class UserChuteCallback implements GCHttpCallback<GCChuteCollection>{

		@Override
		public void onSuccess(GCChuteCollection responseData) {
			Log.i("CHUTES: ", responseData.toString());
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			Log.i("CHUTES: ", "HERE");
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			Log.i("CHUTES: ", "HERE");
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			Log.i("CHUTES: ", "HERE");
		}
		
	}

	private final class CreateChuteCallback implements
			GCHttpCallback<GCChuteModel> {

		@Override
		public void onSuccess(GCChuteModel responseData) {
			Toast.makeText(
					mActivity,
					mActivity.getResources().getString(
							R.string.chute_created), Toast.LENGTH_SHORT).show();
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
}