package com.plastku.pingallery;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.plastku.pingallery.events.Event;
import com.plastku.pingallery.events.EventListener;
import com.plastku.pingallery.events.PhotoEvent;
import com.plastku.pingallery.models.PhotoModel;
import com.plastku.pingallery.util.GsonTransformer;
import com.plastku.pingallery.vo.PhotoVO;

public class ExploreFragment extends RoboFragment {

	private AQuery aq;
	private AQuery aq2;
	private ArrayAdapter<PhotoVO> aa;
	@Inject PhotoModel mPhotoModel;
	@InjectView(R.id.grid) GridView mGridView;
       
     @Override  
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	 setHasOptionsMenu(true);
         View view = inflater.inflate(R.layout.explore, container, false);
         aq = new AQuery(getActivity(), view);
         aq2 = new AQuery(getActivity());
         return view;  
     }
     
     @Override
     public void onActivityCreated(Bundle savedInstanceState)
     {
    	 super.onActivityCreated(savedInstanceState);
    	 mPhotoModel.addListener(PhotoModel.ChangeEvent.PHOTOS_CHANGED, photosChangedListener);
    	 mPhotoModel.requestAllPhotos();
     }
     
     private void updateGrid(List<PhotoVO> entries){
 		aa = new ArrayAdapter<PhotoVO>(getActivity(), R.layout.griditem, entries){

 			public View getView(int position, View convertView, ViewGroup parent) {

 				if(convertView == null){
 					convertView = aq.inflate(convertView, R.layout.griditem, parent);
 				}

 				PhotoVO photo = getItem(position);
 				AQuery aq = aq2.recycle(convertView);
 				String tbUrl = Constants.SITE_URL+photo.path+"/100/fit";

 				if(aq.shouldDelay(position, convertView, parent, tbUrl)){
 					aq.id(R.id.tb).clear();
 				}else{
 					aq.id(R.id.tb).progress(R.id.progress).image(tbUrl, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
 				}

 				return convertView;
 			}
 		};
 		aq.id(mGridView).adapter(aa);
 		aq.id(mGridView).itemClicked(itemClickListener);
     }

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			PhotoVO photo = ((PhotoVO)mGridView.getAdapter().getItem(position));
	    	mPhotoModel.setCurrentPhoto(photo);
			Intent intent = new Intent(getActivity(), PhotoInfoActivity.class);
	    	getActivity().startActivity(intent);
		}
	};

	
	private EventListener photosChangedListener = new EventListener() {
		@Override
		public void onEvent(Event event) {
			updateGrid(mPhotoModel.getPhotos());
		}
	};
}
