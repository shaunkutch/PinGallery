package com.plastku.pingallery.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import android.content.Context;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.plastku.pingallery.Constants;
import com.plastku.pingallery.events.SimpleEvent;
import com.plastku.pingallery.util.GsonTransformer;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.UserVO;

@Singleton
public class UserModel extends Model {

	public static class ChangeEvent extends SimpleEvent {
		public static final String USER_ADDED = "userAdded";

		public ChangeEvent(String type) {
			super(type);
		}
	}

	private Context mContext;
	protected Map<Integer, UserVO> mUsers;

	@Inject
	public UserModel(Context context) {
		super(context);
		mUsers = new HashMap();
	}

	public UserVO getUserById(String id) {
		return mUsers.get(id);
	}

	public void addUser(UserVO user) {
		mUsers.put(user.user_id, user);
		this.dispatchEvent(new ChangeEvent(ChangeEvent.USER_ADDED));
	}

	public void requestUserById(int id) {
		String url = Constants.SITE_URL + "api/users/user/id/" + id;

		GsonTransformer t = new GsonTransformer();

		aq.transformer(t).ajax(url, UserVO.class, new AjaxCallback<UserVO>() {

			public void callback(String url, UserVO user, AjaxStatus status) {

				addUser(user);
			}
		});
	}
}
