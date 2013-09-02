package co.localism.losal.async;

import com.parse.ParseObject;
import com.parse.ParseUser;

import android.os.AsyncTask;
import android.util.Log;

public class PushData extends AsyncTask<String, String, String> {

	
	
	
	@Override
	protected String doInBackground(String... args) {
		if(args[0].equalsIgnoreCase("like"))
			pushLike(args[1], args[2]);
			
		
		return null;
	}

	/**
	 * This is called when a user likes a post. This will push the data into the Likes table in our parse db.
	 * @param postId
	 * @param userId
	 */
	private void pushLike(String postId, String userId){
		Log.d("PushData", "pushing like to db");
		try{
		ParseObject like = new ParseObject("Likes");
		 ParseUser pu = ParseUser.getCurrentUser();

		like.put("postID", postId);
//		like.put("parent", ParseObject.createWithoutData("userID", ));
		like.put("userID", userId);
		like.saveInBackground();
		}catch(Exception e){
			Log.e("PushData", e.toString());
		}
		
	}
	
	
}
