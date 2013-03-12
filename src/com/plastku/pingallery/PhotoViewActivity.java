package com.plastku.pingallery;

import android.os.Bundle;
import android.webkit.WebView;

import com.androidquery.AQuery;
import com.plastku.pingallery.vo.PhotoVO;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.photo_view)
public class PhotoViewActivity extends RoboActivity {

	private PhotoVO mPhoto;
	@InjectView(R.id.imageViewer) WebView mImageViewer;
	private AQuery aq;

	public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        aq = new AQuery(this);
        
        Bundle extras = getIntent().getExtras();
        mPhoto = (PhotoVO) extras.get("photo");
        String photoUrl = Constants.SITE_URL+mPhoto.path;
        aq.id(mImageViewer).webImage(photoUrl);
	}
}
