package com.plastku.pingallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
	private Spinner mGallerySpinner;
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
		//mGallerySpinner = (Spinner) view.findViewById(R.id.gallerySpinner);
		//pickPhotoBtn = (Button) view.findViewById(R.id.pickPhotoBtn);

		//createGalleryBtn.setOnClickListener(new ChuteBasicClickListener());
		//pickPhotoBtn.setOnClickListener(new PickPhotoListener());
		return view;
	}

	/** Called when the activity is first created. */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Test token, see GCAuthentication activity on how to authenticate
		//GCAccountStore account = GCAccountStore.getInstance(mActivity);
		//account.setPassword("4b8c64b3b1e6ba4bf4ad3ce4ec2c6bb3e4dc80d5942b705ef18d8915f7a37921");
		
		//GCUser.userChutes(mActivity, GCConstants.CURRENT_USER_ID, new UserChuteCallback(mActivity)).executeAsync();
		
		//mDialog = new EditTextDialog(mActivity, Constants.DIALOG_EDITTEXT, this);
		//mDialog.TitleText = "Test";
		//mDialog.DefaultText = "Enter Text here";
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
		GCChutes.createChute(mActivity, chute, new CreateChuteCallback(mActivity)).executeAsync();
	}
		
	private class GallerySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

		private GCChuteCollection mGalleryData;
		
		public GallerySpinnerAdapter(GCChuteCollection data)
		{
			mGalleryData = data;
		}
		
        @Override
        public int getCount() {
            return mGalleryData.size();
        }

        @Override
        public Object getItem(int position) {
            return mGalleryData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            TextView text = new TextView(mActivity);
            //text.setTextSize(20);
            text.setPadding(10, 20, 10, 20);
            text.setText(mGalleryData.get(position).getName());
            return text;
        }

    }
	
	private final class UserChuteCallback extends BaseCallback<GCChuteCollection>{

		public UserChuteCallback(Activity activity) {
			super(activity);
		}

		@Override
		public void onSuccess(GCChuteCollection responseData) {
			super.onSuccess(responseData);
			GallerySpinnerAdapter galleryAdapter = new GallerySpinnerAdapter(responseData);
			mGallerySpinner.setAdapter(galleryAdapter);
		}
		
	}

	private final class CreateChuteCallback extends BaseCallback<GCChuteModel> {

		public CreateChuteCallback(Activity activity) {
			super(activity);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onSuccess(GCChuteModel responseData) {
			Toast.makeText(
					mActivity,
					mActivity.getResources().getString(
							R.string.chute_created), Toast.LENGTH_SHORT).show();
		}

	}
}