package com.plastku.pingallery.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import android.content.Context;
import android.content.Intent;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.plastku.pingallery.Constants;
import com.plastku.pingallery.LoginActivity;
import com.plastku.pingallery.MainActivity;
import com.plastku.pingallery.RegisterActivity;
import com.plastku.pingallery.events.SimpleEvent;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.util.GsonTransformer;
import com.plastku.pingallery.views.AlertDialogFragment;
import com.plastku.pingallery.vo.PhotoVO;
import com.plastku.pingallery.vo.ResultVO;
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
		
	}
	
	public void login(String username, String password, final ApiCallback callback)
	{
		final ResultVO result = new ResultVO();
		ParseUser.logInInBackground(username, password, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException e) {			
				if (user != null) {
					callback.onSuccess(result);
				} else {
					result.message = e.getMessage();
					callback.onError(result);
				}
			}
		});
	}
	
	public void register(String username, String email, String password, final ApiCallback callback)
	{
		ParseUser user = new ParseUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		
		final ResultVO result = new ResultVO();
		
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		    	callback.onSuccess(result);
		    } else {
		    	result.message = e.getMessage();
				callback.onError(result);
		    }
		  }
		});
	}
}
