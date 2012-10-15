package com.plastku.pingallery.dialog;

import com.plastku.pingallery.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EditTextDialog extends BaseDialog
{
	private EditText mEditText;
	public String DefaultText = "";
	public String Text = "";

	public EditTextDialog(Activity activity, int requestCode, OnDismissListener dismissListener)
	{
		super(activity, requestCode, dismissListener);
	}

	@Override
	protected void onCreate(AlertDialog dialog)
	{
		LayoutInflater inflator = (LayoutInflater)this.m_Context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflator.inflate(R.layout.dialog_edittext, null);

		this.mEditText = (EditText)view.findViewById(R.id.Dialog_EditText_Text);
		this.mEditText.setText(this.DefaultText);
		
		dialog.setView(view, 0, 0, 0, 0);	
	}

	@Override
	public void onClick(DialogInterface dialog, int buttonId)
	{
		this.Text = this.mEditText.getText().toString();
		super.onClick(dialog, buttonId);
	}
}
