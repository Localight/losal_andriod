package co.localism.losal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import co.localism.losal.activities.MainActivity;
import co.localism.losal.adapters.PostAdapter;
import co.localism.losal.objects.Notice;
import co.localism.losal.objects.Post;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FetchFeed {// extends Observable {

	private static final String tag = "FetchFeed";
	private PostAdapter pa;
	private boolean AddToTop = false;

	public FetchFeed() {
		// fetch();
	}

	public void initialFetch() {
		getAppSettings();
	}

	public void getAppSettings() {
		Log.d(tag, "getAppSetting called");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("AppSettings");

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> List, ParseException e) {
				if (e == null) {
					Log.d(tag, "Retrieved " + List.size() + " notices");
					MainActivity.POST_DAYS = List.get(0).getInt(
							"queryIntervalDays");
					// MainActivity.posts = fetch();
				} else {
					Log.d(tag, "Error: " + e.getMessage());
				}
			}
		});

	}

	public void fetch(PostAdapter pa) {
		this.pa = pa;
		Log.d(tag, "fetch called");

		new FetchPosts().execute("", "", "");
	}

	public void refresh(PostAdapter pa) {
		this.pa = pa;
		Log.d(tag, "fetch refresh called");
		if (pa.getSize() > 0)
			new FetchPosts().execute("refresh", "", "");
		else
			new FetchPosts().execute("", "", "");
	}

	/**
	 * Should only be called from within fetch(). Parses out the ParseObjects
	 * into Post objects.
	 * 
	 * @param postsList
	 * @return ArrayList<Post>
	 */
	private ArrayList<Post> createPosts(List<ParseObject> postsList) {
		ArrayList<Post> posts = new ArrayList<Post>();
		Log.d(tag, "empty: " + postsList.isEmpty());

		if (postsList != null) {

			for (int i = 0; i < postsList.size(); i++) {
				Post p = new Post();

				p.setPostTime(postsList.get(i).getDate("postTime"));
				MainActivity.LAST_POST_DATE = p.getPostTime();
				// (postsList.get(i).getString("featured"));
				p.setSocialNetworkPostId(postsList.get(i).getString(
						"socialNetworkPostID"));
				// (postsList.get(i).getString("createdAt"));
				p.setParseObjectId(postsList.get(i).getObjectId());
				// postsList.get(i).getString("objectId"));
				Log.i(tag, "objectID: " + postsList.get(i).getObjectId());
				// if(postsList.get(i).getParseObject("user") != null);
				try {
					// postsList.get(i).get("user").toString();
					// Log.d(tag, ""+
					// postsList.get(i).getParseObject("user").toString());
					// Log.d(tag, ""+
					// postsList.get(i).getParseObject("user").toString());
					Log.e(tag, "before name");
					p.setName(postsList.get(i).getParseObject("user")
							.getString("firstName"));

					Log.e(tag, "name"
							+ postsList.get(i).getParseObject("user")
									.getString("firstName"));
					String lname = postsList.get(i).getParseObject("user")
							.getString("lastName");
					p.setName(p.getName() + " " + lname.substring(0, 1) + ".");
					p.setClassYear(postsList.get(i).getParseObject("user")
							.getString("year"));
					p.setUserIcon(postsList.get(i).getParseObject("user")
							.getString("icon"));
					p.setFaveColor(postsList.get(i).getParseObject("user")
							.getString("faveColor"));
				} catch (Exception e) {
					Log.e(tag, e.toString());
					p.setName("Unknown");// placeholder data
					p.setUserIcon("e00c");
					p.setFaveColor("#FFFFFF");
				}
				// Log.d(tag, ""+
				// postsList.get(i).getParseObject("user").get("firstName"));

				p.setText(postsList.get(i).getString("text"));
				// Log.d(tag, postsList.get(i).getString("text"));
				p.setSocialNetworkName(postsList.get(i).getString(
						"socialNetworkName"));
				try {
					p.setUrl(postsList.get(i).getString("url"));
				} catch (NullPointerException npe) {
					p.setUrl("");
				}
				// p.setClassYear(3);// placeholder data
				// posts.add(p);
				if (AddToTop)
					pa.add(0, p);
				else
					pa.add(p);
			}
		}
		pa.setStatus(false);
		AddToTop = false;
		return posts;
	}

	/**
	 * Notifies MainActivity that the data set has changed
	 */
	private void triggerObservers() {
		Log.d(tag, "triggerObservers called");
		// setChanged();
		// notifyObservers();
	}

	/**
	 * This fetches the feed data from Parse. Sends it to {@link createPosts()}
	 * to parse the data.
	 * 
	 * @return ArrayList<Post>
	 */
	private class FetchPosts extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... args) {
			Log.d(tag, "doing in background");

			int days_back = 7;
			try {
				days_back = MainActivity.POST_DAYS;
			} catch (Exception e) {

			}
			final Calendar cal = Calendar.getInstance();
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
			final ArrayList<Post> posts;
			posts = new ArrayList<Post>();

			query.whereEqualTo("status", "1");
			query.addDescendingOrder("postTime");
			Log.d(tag, "Last Post Date" + MainActivity.LAST_POST_DATE);

			if (args[0].equalsIgnoreCase("refresh")) {
				Log.d(tag, "query setting to refresh");
				AddToTop = true;
				cal.setTime(pa.getPost(0).getPostTime());
				query.whereGreaterThan("postTime", cal.getTime());
			} else if (MainActivity.LAST_POST_DATE == null) {
				Log.d(tag, "last_post_date == null. initial fetch");

				// getAppSettings();
				// then start with less than now
				query.whereLessThan("postTime", cal.getTime());
				cal.add(Calendar.DATE, -days_back);
				query.whereGreaterThan("postTime", cal.getTime());
			} else {
				Log.d(tag, " fetch MORE");

				query.whereLessThan("postTime", MainActivity.LAST_POST_DATE);
				Date d = MainActivity.LAST_POST_DATE;
				cal.setTime(d);
				cal.add(Calendar.DATE, -days_back);
				query.whereGreaterThan("postTime", cal.getTime());
			}

			query.include("user");
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> postList, ParseException e) {
					if (e == null) {
						Log.d(tag, "Retrieved " + postList.size() + " posts");
						createPosts(postList);
					} else {
						Log.d(tag, "Error: " + e.getMessage());
					}
				}
			});
			return null;
		}

	}

}
