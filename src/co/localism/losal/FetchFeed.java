package co.localism.losal;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.content.Intent;
import android.util.Log;

import co.localism.losal.activities.MainActivity;
import co.localism.losal.objects.Post;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FetchFeed extends Observable{

	private static final String tag = "FetchFeed";

	public FetchFeed() {
//		fetch();
	}

/**
 * This fetches the feed data from Parse. Sends it to {@link createPosts()} to parse the data.
 * Then triggers observer from MainActivity to update the listadapter.
 * 
 * @return ArrayList<Post>
 */
	public ArrayList<Post> fetch() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
		final ArrayList<Post> posts;
		posts = new ArrayList<Post>();
		// query.whereEqualTo("socialNetworkName", "Instagram");
		// query.whereStartsWith("socialNetworkName", "Instagram");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> postList, ParseException e) {
				if (e == null) {
					Log.d(tag, "Retrieved " + postList.size() + " posts");
					posts.addAll(createPosts(postList));
					triggerObservers();
				} else {
					Log.d(tag, "Error: " + e.getMessage());
				}
			}
		});
		return posts;
	}

	/**
	 * Should only be called from within fetch().
	 * Parses out the ParseObjects into Post objects.
	 * @param postsList
	 * @return ArrayList<Post>
	 */
	private ArrayList<Post> createPosts(List<ParseObject> postsList){
		ArrayList<Post> posts = new ArrayList<Post>();
		Log.d(tag, "empty: "+postsList.isEmpty());
		
		if (postsList != null) {

			for (int i = 0; i < postsList.size(); i++) {
				Post p = new Post();

				p.setText(postsList.get(i).getString("text"));
				Log.d(tag, postsList.get(i).getString("text"));
				p.setSocialNetworkName(postsList.get(i).getString("socialNetworkName"));
				p.setUrl(postsList.get(i).getString("url"));

				posts.add(p);
			}
		}
		return posts;
	}
	
	/**
	 * Notifies MainActivity that the data set has changed
	 */
	 private void triggerObservers() {
			Log.d(tag, "triggerObservers called");
	        setChanged();
	        notifyObservers();
	    }
}
