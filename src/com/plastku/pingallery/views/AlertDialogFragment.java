package com.plastku.pingallery.views;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.plastku.pingallery.R;
import com.plastku.pingallery.interfaces.AlertListener;

public class AlertDialogFragment extends DialogFragment {

	private String mTitle;
	private String mMessage;
	private AlertListener mListener;
	private boolean mShowNegativeButton = false;
	private int mIcon = 0;
	
	public static int POSITVIE_RESPONSE = 1;
	public static int NEGATIVE_RESPONCE = 0;

	public AlertDialogFragment() {
		
	}
	
	public void setTitle(String title)
	{
		this.mTitle = title;
	}
	
	public void setMessage(String message)
	{
		this.mMessage = message;
	}
	
	public void setIcon(int icon)
	{
		this.mIcon = icon;
	}

	public void showNegativeButton(boolean show) {
		mShowNegativeButton = show;
	}
	
	public void setListener(AlertListener listener)
	{
		this.mListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Builder dialog = new AlertDialog.Builder(getActivity());
		if(mIcon != 0)
		{
			dialog.setIcon(mIcon);
		}
		if(mTitle != null)
		{
			dialog.setTitle(mTitle);
		}
		if(mMessage != null)
		{
			dialog.setMessage(mMessage);
		}
		dialog.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if(mListener != null)
						{
							mListener.onClick(POSITVIE_RESPONSE);
						}
					}
				});
		if(mShowNegativeButton)
		{
		dialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if(mListener != null)
						{
							mListener.onClick(NEGATIVE_RESPONCE);
						}
					}
				});
		}

		return dialog.create();

	}
}
