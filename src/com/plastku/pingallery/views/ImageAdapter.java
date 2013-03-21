package com.plastku.pingallery.views;

import java.util.List;

import com.androidquery.AQuery;
import com.plastku.pingallery.R;
import com.plastku.pingallery.vo.PhotoVO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	private List<PhotoVO> mPhotos;
	private AQuery aq;

	public ImageAdapter(Context context, List<PhotoVO> photos) {
		this.mContext = context;
		this.mPhotos = photos;
		aq = new AQuery(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mPhotos.size();
	}

	@Override
	public Object getItem(int position) {
		return mPhotos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


		if (convertView == null) {
			convertView = aq.inflate(convertView, R.layout.photo_grid_item, parent);
		}
		aq.recycle(convertView);
		PhotoVO photo = (PhotoVO) getItem(position);
		String url = photo.thumb;
		
		if (aq.shouldDelay(position, convertView, parent, url)) {
			aq.id(R.id.photoImage).clear();
		} else {
			//aq.id(R.id.photoImage).progress(R.id.progress).image(url, true, true, 0, 0, null, AQuery.FADE_IN, AQuery.RATIO_PRESERVE);
			aq.id(R.id.photoImage).progress(R.id.progress).image(R.drawable.ic_action_picture);
		}

		return convertView;
	}

}
