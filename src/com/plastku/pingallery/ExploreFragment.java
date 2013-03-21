package com.plastku.pingallery;

import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.google.inject.Inject;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.views.AlertDialogFragment;
import com.plastku.pingallery.views.EndlessScrollListener;
import com.plastku.pingallery.views.ImageAdapter;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;

public class ExploreFragment extends RoboFragment implements OnScrollListener {

	private AQuery aq;
	private AQuery aq2;
	private ImageAdapter aa;
	@Inject
	PhotoModel mPhotoModel;
	@InjectView(R.id.photoGrid) GridView mGridView;
	private int mThreshold = 20;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.explore, container, false);
		aq = new AQuery(getActivity(), view);
		aq2 = new AQuery(getActivity());
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		aa = new ImageAdapter(getActivity(), mPhotoModel.getPhotos());
		aq.id(mGridView).adapter(aa);
		aq.id(mGridView).itemClicked(itemClickListener);
		
		mGridView.setOnScrollListener(this);
		mPhotoModel.addListener(PhotoModel.ChangeEvent.PHOTOS_CHANGED, photosChangedListener);
		queryPhotos();
	}
	
	public void queryPhotos()
	{
		mPhotoModel.requestPhotos(0, 24, new ApiCallback() {

			@Override
			public void onSuccess(ResultVO result) {
				PhotoModel.PhotoResultVO r = (PhotoModel.PhotoResultVO) result;
				//updateGrid(r.photos);
			}

			@Override
			public void onError(ResultVO result) {
				AlertDialogFragment newFragment = new AlertDialogFragment();
				newFragment.setMessage(result.message);
				newFragment.show(ExploreFragment.this.getFragmentManager(), "dialog");
			}

		});
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.explore_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.reload:
				this.queryPhotos();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			PhotoVO photo = ((PhotoVO) mGridView.getAdapter().getItem(position));
			mPhotoModel.setCurrentPhoto(photo);
			Intent intent = new Intent(getActivity(), PhotoInfoActivity.class);
			getActivity().startActivity(intent);
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
	public void onScroll(AbsListView view, int firstVisibleItem,
	        int visibleItemCount, int totalItemCount) {
		System.out.println("On Refresh invoked..");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	    if (scrollState == SCROLL_STATE_IDLE) {
	        if (mGridView.getLastVisiblePosition() >= mGridView.getCount() - mThreshold) {
	            //currentPage++;
	            //load more list items:
	            //loadElements(currentPage);
	        	System.out.println("On Refresh invoked..");
	        }
	    }
	}
}
