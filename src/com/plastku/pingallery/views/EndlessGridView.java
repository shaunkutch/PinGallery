package com.plastku.pingallery.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.google.inject.Inject;
import com.plastku.pingallery.PhotoInfoActivity;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

public class EndlessGridView extends GridView implements OnScrollListener {

	private Activity mContext;
	private AQuery aq;
	private ImageAdapter aa;
	@Inject PhotoModel mPhotoModel;
	private int mThreshold = 20;
	private int mPage;
	private int mPreviousTotal;
	private boolean mIsLoading = false;
	
	public EndlessGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = (Activity)context;
		aq = new AQuery(context);
		aa = new ImageAdapter(context, mPhotoModel.getPhotos());
		aq.id(this).adapter(aa);
		aq.id(this).itemClicked(itemClickListener);		
		aq.id(this).scrolled(this);
		mPhotoModel.addListener(PhotoModel.ChangeEvent.PHOTOS_CHANGED, photosChangedListener);
		this.queryPhotos();
	}
	
	public void queryPhotos()
	{
		if(!mIsLoading)
		{
			System.out.println("LOAD IMAGES");
			int start = mThreshold*mPage;
			mIsLoading = true;
			mPhotoModel.requestPhotos(start, mThreshold, new ApiCallback() {
	
				@Override
				public void onSuccess(ResultVO result) {	
					PhotoModel.PhotoResultVO r = (PhotoModel.PhotoResultVO) result;
					mPage++;
					mIsLoading = false;
				}
	
				@Override
				public void onError(ResultVO result) {
					/*AlertDialogFragment newFragment = new AlertDialogFragment();
					newFragment.setMessage(result.message);
					newFragment.show(mContext.getFragmentManager(), "dialog");*/
				}
	
			});
		}else{
			System.out.println("Already Loading!");
		}
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			PhotoVO photo = ((PhotoVO) EndlessGridView.this.getAdapter().getItem(position));
			mPhotoModel.setCurrentPhoto(photo);
			Intent intent = new Intent(mContext, PhotoInfoActivity.class);
			mContext.startActivity(intent);
		}
	};
	
	EventListener photosChangedListener = new EventListener()
	{
		@Override
		public void onEvent(Event event) {
			aa.notifyDataSetChanged();
		}
		
	};
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if(firstVisibleItem + visibleItemCount >= totalItemCount && totalItemCount > mPreviousTotal)
		{
			System.out.println("On Refresh invoked..");
			mPreviousTotal = totalItemCount;
			queryPhotos();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
