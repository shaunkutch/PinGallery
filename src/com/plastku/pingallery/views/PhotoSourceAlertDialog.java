package com.plastku.pingallery.views;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import com.google.inject.Inject;
import com.plastku.pingallery.Constants;
import com.plastku.pingallery.R;
import com.plastku.pingallery.util.FileUtils;

public class PhotoSourceAlertDialog extends AlertDialog {

	private Context mContext;
	
	@Inject
    public PhotoSourceAlertDialog(final Context context) {
        super(context);
        this.mContext = context;
        
        setTitle(mContext.getString(R.string.upload));
        View view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.photo_source_view,null);
        setView(view);
        Button photoButton = (Button) view.findViewById(R.id.photoButton);
		photoButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				File imageFile = new File(FileUtils.getTempImageFileName(mContext));
			    Uri outputFileUri = Uri.fromFile(imageFile);
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				((Activity) mContext).startActivityForResult(intent, Constants.PHOTO_CODE);
				dismiss();
			}
			
		});
		
		Button galleryButton = (Button) view.findViewById(R.id.galleryButton);
		galleryButton.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				((Activity) mContext).startActivityForResult(intent, Constants.GALLERY_CODE);
				dismiss();
			}
			
		});
    }
}
