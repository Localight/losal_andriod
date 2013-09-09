package co.localism.losal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	private Context ctx;
	private SharedPreferences user_info;

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

	public void fetch(PostAdapter pa, Context context) {
		this.pa = pa;
		this.ctx = context;
		user_info = ctx.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

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
			boolean skip = false;
			for (int i = 0; i < postsList.size(); i++) {

				Post p = new Post();

				/****** This is for system posts. It needs this extra logic ******/
				if (postsList.get(i).getInt("system_post") == 1) {
					Log.i(tag, "SYSTEM_POST");
					if (user_info.getString("user_type", "").equalsIgnoreCase(
							"student")) {
						if (postsList.get(i).getString("socialNetworkName")
								.equalsIgnoreCase("twitter")) {
							if (user_info.getBoolean("hasTwitter", false))
								skip = true;
							else
								p.setIsSystemPost(true);
						} else if (postsList.get(i)
								.getString("socialNetworkName")
								.equalsIgnoreCase("instagram")) {
							if (user_info.getBoolean("hasInstagram", false))
								skip = true;
							else
								p.setIsSystemPost(true);
						}
					} else {
						skip = true;
					}
				}

				p.setPostTime(postsList.get(i).getDate("postTime"));
				MainActivity.LAST_POST_DATE = p.getPostTime();
				// (postsList.get(i).getString("featured"));
				p.setSocialNetworkPostId(postsList.get(i).getString(
						"socialNetworkPostID"));
				p.setParseObjectId(postsList.get(i).getObjectId());

				Log.i(tag, "objectID: " + postsList.get(i).getObjectId());
				// if(postsList.get(i).getParseObject("user") != null);
				try {
//					Log.e(tag, "name"							+ postsList.get(i).getParseObject("user").getString("firstName"));
					p.setUserType(postsList.get(i).getParseObject("user").getString("userType"));
					
					if(p.getUserType().equalsIgnoreCase("student")){
//						student users show as first name and first initial of last
						p.setName(postsList.get(i).getParseObject("user")
								.getString("firstName"));
						String lname = postsList.get(i).getParseObject("user")
								.getString("lastName");
						p.setName(p.getName() + " " + lname.substring(0, 1) + ".");
						p.setClassYear(postsList.get(i).getParseObject("user")
								.getString("year"));
					}else{
//						faculty and staff show as prefix and last name
						p.setName(postsList.get(i).getParseObject("user")
								.getString("prefix") + " " + postsList.get(i).getParseObject("user")
								.getString("lastName"));
						p.setClassYear(p.getUserType());
					}
					
				
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
				// }
				if (!skip) {
					if (AddToTop)
						pa.add(0, p);
					else
						pa.add(p);
				} else
					skip = false;
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
