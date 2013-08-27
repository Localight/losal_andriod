package co.localism.losal;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import android.content.Intent;
import android.util.Log;

import co.localism.losal.activities.MainActivity;
import co.localism.losal.adapters.NoticeAdapter;
import co.localism.losal.objects.Notice;
import co.localism.losal.objects.Post;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;




public class FetchNotices extends Observable  {

	private NoticeAdapter na;
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
	public ArrayList<Notice> fetch(NoticeAdapter na) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Notifications");
		final ArrayList<Notice> notices;
		notices = new ArrayList<Notice>();
		this.na = na;
		
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
				try{
				Log.d(tag, "notice title: "
						+ noticesList.get(i).getString("title"));
				n.setTitle(noticesList.get(i).getString("title"));
				n.setDetails(noticesList.get(i).getString("description"));
				}catch(Exception e){
					Log.e(tag, e.toString());
				}
				na.add(n);
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

