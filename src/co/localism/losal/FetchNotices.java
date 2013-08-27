package co.localism.losal;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.content.Intent;
import android.util.Log;

import co.localism.losal.activities.MainActivity;
import co.localism.losal.objects.Notice;
import co.localism.losal.objects.Post;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;




public class FetchNotices extends Observable  {





	private static final String tag = "FetchNotices";

	public FetchNotices() {
		// fetch();
	}

	/**
	 * This fetches the feed data from Parse. Sends it to {@link createnotices()}
	 * to parse the data. Then triggers observer from MainActivity to update the
	 * listadapter.
	 * 
	 * @return ArrayList<Post>
	 */
	public ArrayList<Notice> fetch() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Notifications");
		final ArrayList<Notice> notices;
		notices = new ArrayList<Notice>();
		
		
//		query.whereEqualTo("status", "1");
//		query.addDescendingOrder("postTime");
//		query.include("user");

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> noticeList, ParseException e) {
				if (e == null) {
					Log.d(tag, "Retrieved " + noticeList.size() + " notices");
					notices.addAll(createnotices(noticeList));
					triggerObservers();
				} else {
					Log.d(tag, "Error: " + e.getMessage());
				}
			}
		});
		return notices;
	}

	/**
	 * Should only be called from within fetch(). Parses out the ParseObjects
	 * into Post objects.
	 * 
	 * @param noticesList
	 * @return ArrayList<Post>
	 */
	private ArrayList<Notice> createnotices(List<ParseObject> noticesList) {
		ArrayList<Notice> notices = new ArrayList<Notice>();
		Log.d(tag, "empty: " + noticesList.isEmpty());

		if (noticesList != null) {

			for (int i = 0; i < noticesList.size(); i++) {
				Notice n = new Notice();
				Log.d(tag, "notice title: "
						+ noticesList.get(i).getString("title"));
				n.setTitle(noticesList.get(i).getString("title"));
				/*n.setPostTime(noticesList.get(i).getDate("postTime"));
				// (noticesList.get(i).getString("featured"));
				n.setSocialNetworkPostId(noticesList.get(i).getString(
						"socialNetworkPostID"));
				// (noticesList.get(i).getString("createdAt"));
				p.setParseObjectId(noticesList.get(i).getString("objectId"));

				// if(noticesList.get(i).getParseObject("user") != null);
				try {
					// noticesList.get(i).get("user").toString();
					Log.d(tag, ""
							+ noticesList.get(i).getParseObject("user")
									.toString());
					// Log.d(tag, ""+
					// noticesList.get(i).getParseObject("user").toString());

					p.setName(noticesList.get(i).getParseObject("user")
							.getString("firstName"));
					p.setClassYear(noticesList.get(i).getParseObject("user")
							.getString("year"));
					p.setUserIcon(noticesList.get(i).getParseObject("user")
							.getString("icon"));
					p.setFaveColor(noticesList.get(i).getParseObject("user")
							.getString("faveColor"));
				} catch (Exception e) {
					Log.e(tag, e.toString());
					p.setName("");// placeholder data
				}
				// Log.d(tag, ""+
				// noticesList.get(i).getParseObject("user").get("firstName"));

				p.setText(noticesList.get(i).getString("text"));
				Log.d(tag, noticesList.get(i).getString("text"));
				p.setSocialNetworkName(noticesList.get(i).getString(
						"socialNetworkName"));
				try {
					p.setUrl(noticesList.get(i).getString("url"));
				} catch (NullPointerException npe) {
					p.setUrl("");
				}
				*/
//				p.setClassYear(3);// placeholder data
				notices.add(n);
			}
		}
		return notices;
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

