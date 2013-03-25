package com.plastku.pingallery.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.plastku.pingallery.events.SimpleEvent;
import com.plastku.pingallery.interfaces.ApiCallback;
import com.plastku.pingallery.vo.ResultVO;
import com.plastku.pingallery.vo.UserResultVO;
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

	public void getUserById(String userId, final ApiCallback callback) {
		
		ParseQuery innerQuery = new ParseQuery("UserAvatar");
		innerQuery.whereEqualTo("userId", userId);
		
		ParseQuery query = ParseUser.getQuery();
		//query.whereEqualTo("objectId", userId);
		query.whereDoesNotMatchQuery("objectId", innerQuery);
		query.findInBackground(new FindCallback() {
			public void done(List<ParseObject> objects, ParseException e) {
				UserResultVO result = new UserResultVO();
				if (e == null) {
					for(ParseObject o : objects)
		        	{
						ParseUser pu = o.getParseUser("user");
						UserVO user = new UserVO();
						user.userName = pu.getUsername();
						user.userId = pu.getObjectId();
		        	}
					callback.onSuccess(result);
				} else {
					result.message = e.getMessage();
					callback.onError(result);
				}
			}
		});
	}
	
	public void getUserAvatar(String userId, final ApiCallback callback)
	{
		ParseQuery query = new ParseQuery("UserAvatar");
		query.findInBackground(new FindCallback() {
	        public void done(List<ParseObject> objects, ParseException e) {
	        	if(e == null)
	        	{
	        		for(ParseObject o : objects)
		        	{
	        			ParseUser pu = o.getParseUser("user");
		        	}
	        	}else{
	        		
	        	}
	        }
		});
	}
	
	public void setAvatar(String photoId, final ApiCallback callback)
	{
		ParseObject photo = new ParseObject("Photo");
		photo.setObjectId(photoId);
		
		ParseObject avatarObject = new ParseObject("UserAvatar");
		avatarObject.put("photo", photo);
		ParseUser currentUser = ParseUser.getCurrentUser();
		avatarObject.setACL(new ParseACL(currentUser));
		avatarObject.put("user", currentUser);
		avatarObject.saveInBackground(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				ResultVO result = new ResultVO();
				if(e == null)
				{	
					result.message = "File Uploaded";
					callback.onSuccess(result);
				}else{
					result.message = e.getMessage();
					callback.onError(result);
				}
			}
		});
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
