package co.localism.losal.async;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

public class PushData extends AsyncTask<String, String, String> {
	private String tag = "PushData";

	@Override
	protected String doInBackground(String... args) {
		if (args[0].equalsIgnoreCase("like"))
			pushLike(args[1], args[2]);
		else if (args[0].equalsIgnoreCase("twitterID"))
			pushTwitterId(args[1], args[2]);
		else if (args[0].equalsIgnoreCase("instagramID"))
			pushInstagramId(args[1], args[2]);
		else if(args[0].equalsIgnoreCase("failed_phone"))
			pushFailedLogin(args[1]);
		return null;
	}

	private void pushTwitterId(String username, String id) {
		try {
			Log.i(tag, "username= " + username);
			Log.i(tag, "id= " + id);

			ParseUser user = ParseUser.getCurrentUser();
			user.put("twitterID", username);
			user.put("userTwitterId", id);

			user.saveInBackground(new SaveCallback() {

				@Override
				public void done(com.parse.ParseException e) {
					if (e != null) {
						Log.e(tag, "not null " + e.toString());
					} else {
						Log.e(tag, "null ");
					}
				}
			});
		} catch (Exception e) {
			Log.e(tag, "exception caught: " + e.toString());
		}
	}

	private void pushInstagramId(String username, String id) {
		try {
			Log.i(tag, "username= " + username);
			Log.i(tag, "id= " + id);

			ParseUser user = ParseUser.getCurrentUser();
			user.put("instagramID", username);
			user.put("userInstagramId", id);

			user.saveInBackground(new SaveCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					if (e != null) {
						Log.e(tag, "not null " + e.toString());
					} else {
						Log.e(tag, "null ");
					}
				}
			});
		} catch (Exception e) {
			Log.e(tag, "exception caught: " + e.toString());
		}
	}

	/**
	 * This is called when a user likes a post. This will push the data into the
	 * Likes table in our parse db.
	 * 
	 * @param postId
	 * @param userId
	 */
	private void pushLike(String postId, String userId) {
		Log.d(tag, "pushing like to db");
		try {
			ParseObject like = new ParseObject("Likes");
			ParseUser pu = ParseUser.getCurrentUser();

			ParseObject post = ParseObject.createWithoutData("Posts", postId);
			like.put("postID", post);
			ParseObject user = ParseObject.createWithoutData("User", userId);
			like.put("userID", pu);
//			ParseACL acl = new ParseACL();
//			like.setACL(acl);
			like.saveInBackground(new SaveCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					if (e == null) {
						Log.e("PushData", "pushed like successful");
					} else {
						Log.e("PushData", e.toString());
					}
				}
			});
		} catch (Exception e) {
			Log.e("PushData", e.toString());
		}
	}
	
	/**
	 * This logs the phone number of any failed attempts of logging.
	 * 
	 * @param phone the phonenumber attempted
	 */
	private void pushFailedLogin(String phone) {
		Log.d(tag, "pushing failed login phone num to db");
		try {
			ParseObject fl = new ParseObject("FailedLogin");
			fl.put("phoneNumber", phone);
			fl.saveInBackground(new SaveCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					if (e == null) {
						Log.i("PushData", "pushed FailedLogin successfully");
					} else {
						Log.e("PushData", e.toString());
					}
				}
			});
		} catch (Exception e) {
			Log.e("PushData", e.toString());
		}
	}
	
	
	
	
	
}
