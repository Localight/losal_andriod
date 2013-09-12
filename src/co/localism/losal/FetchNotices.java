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
import co.localism.losal.adapters.NoticeAdapter;
import co.localism.losal.objects.Notice;
import co.localism.losal.objects.Post;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FetchNotices extends Observable {

	private NoticeAdapter na;
	private static final String tag = "FetchNotices";
	private Context ctx;

	public FetchNotices(Context ctx) {
		// fetch();
		this.ctx = ctx;
	}

	/**
	 * This fetches the feed data from Parse. Sends it to {@link
	 * createnotices()} to parse the data. Then triggers observer from
	 * MainActivity to update the listadapter.
	 * 
	 * @return ArrayList<Post>
	 */
	public void fetch(NoticeAdapter na) {
		this.na = na;
		new FetchInBackground().execute("", "", "");
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
				Boolean isAd = false;
				try {
					Log.d(tag,
							"notice title: "
									+ noticesList.get(i).getString("title"));
					if (noticesList.get(i).getInt("ad") == 1) {
						isAd = true;
						SharedPreferences user_info = ctx.getSharedPreferences(
								"UserInfo", Context.MODE_PRIVATE);
						if (user_info.getString("user_type", "")
								.equalsIgnoreCase("student")) {
//							they are a student and should only be shown ads targeted to them or no ad at all
							if (noticesList.get(i).getString("audienceTypes")
									.contains("Students")) {
								MainActivity.AD_URL = noticesList.get(i)
										.getString("image");
								MainActivity.AD_CLICK_URL = noticesList.get(i)
										.getString("buttonLink");
							}
						} else {
//							they are not a student and are shown this ad that is targeted at non students
							MainActivity.AD_URL = noticesList.get(i).getString(
									"image");
							MainActivity.AD_CLICK_URL = noticesList.get(i)
									.getString("buttonLink");
						}

					} else {
						n.setTitle(noticesList.get(i).getString("title"));
						n.setDetails(noticesList.get(i)
								.getString("description"));
						n.setTeaser(noticesList.get(i).getString("teaser"));

						n.setImageUrl(noticesList.get(i).getString("image"));
						// n.setLinkUrl(noticesList.get(i).getString("link"));
						n.setButtonLink(noticesList.get(i).getString(
								"buttonLink"));
						n.setButtonText(noticesList.get(i).getString(
								"buttonText"));

						/** Time and Date Received **/
						/*
						 * Date d = noticesList.get(i).getDate("startDate");
						 * Calendar cal = Calendar.getInstance();
						 * cal.setTime(d); String date = "" +
						 * cal.get(Calendar.MONTH) + "-" +
						 * cal.get(Calendar.DAY_OF_MONTH) + "-" +
						 * cal.get(Calendar.YEAR); int hour =
						 * cal.get(Calendar.HOUR_OF_DAY); if (hour == 0) hour +=
						 * 12; String ampm = "PM"; if (cal.get(Calendar.AM_PM)
						 * == Calendar.AM) ampm = "AM"; String time = hour + ":"
						 * + cal.get(Calendar.MINUTE) + " " + ampm;
						 * 
						 * n.setDateReceived(date + "  " + time);
						 */
					}
				} catch (Exception e) {
					Log.e(tag, e.toString());
				}
				if (isAd)
					isAd = false;
				else
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

	private class FetchInBackground extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			final Calendar cal = Calendar.getInstance();
			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("Notifications");
			final ArrayList<Notice> notices;
			notices = new ArrayList<Notice>();

			query.whereGreaterThan("endDate", cal.getTime());
			query.whereLessThan("startDate", cal.getTime());

			query.addDescendingOrder("startDate");

			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> noticeList, ParseException e) {
					if (e == null) {
						Log.d(tag, "Retrieved " + noticeList.size()
								+ " notices");
						notices.addAll(createnotices(noticeList));
						triggerObservers();
					} else {
						Log.d(tag, "Error: " + e.getMessage());
					}
				}
			});

			return null;
		}

	}
}
