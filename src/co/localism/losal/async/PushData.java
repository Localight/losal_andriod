package co.localism.losal.async;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

public class PushData extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... args) {
		if (args[0].equalsIgnoreCase("like"))
			pushLike(args[1], args[2]);

		return null;
	}

	/**
	 * This is called when a user likes a post. This will push the data into the
	 * Likes table in our parse db.
	 * 
	 * @param postId
	 * @param userId
	 */
	private void pushLike(String postId, String userId) {
		Log.d("PushData", "pushing like to db");
		try {
			ParseObject like = new ParseObject("Likes");
			ParseUser pu = ParseUser.getCurrentUser();

			// like.put("postID", postId);
			ParseObject post = ParseObject.createWithoutData("Posts", postId);
			like.put("postID", post);
			ParseObject user = ParseObject.createWithoutData("User", userId);
			like.put("userID", pu);
			like.saveInBackground(new SaveCallback() {
				@Override
				public void done(com.parse.ParseException e) {
					// TODO Auto-generated method stub
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
}
