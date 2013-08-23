package co.localism.losal.objects;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class TimeHandler {
	// DEFAULTS
	private static final int DEFAULT_TIME_INTERVAL_HOURS = 6;
	private static final int DEFAULT_TIME_INTERVAL_MINUTES = 0;

	// MODIFIABLE
	private static int TimeIntervalHours = DEFAULT_TIME_INTERVAL_HOURS;
	private static int TimeIntervalMinutes = DEFAULT_TIME_INTERVAL_MINUTES;

	public TimeHandler() {
		this(TimeHandler.DEFAULT_TIME_INTERVAL_HOURS,
				TimeHandler.DEFAULT_TIME_INTERVAL_MINUTES);
	}

	private TimeHandler(int defaultTimeIntervalHours,
			int defaultTimeIntervalMinutes) {

	}

	public void setTimeInterval(int hours, int minutes) {
		TimeHandler.TimeIntervalHours = hours;
		TimeHandler.TimeIntervalMinutes = minutes;
	}

	public boolean showTimeBreak() {
		// TODO: figure out how to decide when to show based on time interval
		// set.
		return true;
	}

	/**
	 * 
	 * @return int[2] index 0 = Hours, index 1 = Minutes
	 */
	public int[] getTimeInterval() {
		int[] i = new int[2];
		i[0] = TimeHandler.TimeIntervalHours;
		i[0] = TimeHandler.TimeIntervalMinutes;
		return i;
	}

	public int getTimeIntervalHours() {
		return TimeHandler.TimeIntervalHours;
	}

	public int getTimeIntervalMinutes() {
		return TimeHandler.TimeIntervalMinutes;
	}

	public String getTimeAgo(Date d) {
		final Calendar today_cal = Calendar.getInstance();
		Calendar post_cal = Calendar.getInstance();
		post_cal.setTime(d);
		Log.d("getTimeAgo", post_cal.toString());
		Log.d("getTimeAgo", today_cal.YEAR +"   "+ post_cal.YEAR);

		if (today_cal.get(Calendar.YEAR) > post_cal.get(Calendar.YEAR))
			if (today_cal.get(Calendar.YEAR) - post_cal.get(Calendar.YEAR) == 1)
				return "1 year ago";
			else
				return today_cal.get(Calendar.YEAR) - post_cal.get(Calendar.YEAR) + " years ago";

		else if (today_cal.get(Calendar.MONTH) > post_cal.get(Calendar.MONTH))
			if (today_cal.MONTH - post_cal.MONTH == 1)
				return "1 month ago";
			else
				return today_cal.get(Calendar.MONTH) - post_cal.get(Calendar.MONTH) + " months ago";

		else if (today_cal.get(Calendar.DAY_OF_MONTH) > post_cal.get(Calendar.DAY_OF_MONTH))
			if (today_cal.get(Calendar.DAY_OF_MONTH) - post_cal.get(Calendar.DAY_OF_MONTH) == 1)
				return "1 day ago";
			else
				return today_cal.get(Calendar.DAY_OF_MONTH) - post_cal.get(Calendar.DAY_OF_MONTH)
						+ " days ago";

		else if (today_cal.get(Calendar.HOUR_OF_DAY) > post_cal.get(Calendar.HOUR_OF_DAY))
			if (today_cal.get(Calendar.HOUR_OF_DAY) - post_cal.get(Calendar.HOUR_OF_DAY) == 1)
				return "1 hour ago";
			else
				return today_cal.get(Calendar.HOUR_OF_DAY) - post_cal.get(Calendar.HOUR_OF_DAY)
						+ " hours ago";

		else if (today_cal.get(Calendar.MINUTE) > post_cal.get(Calendar.MINUTE))
			if (today_cal.MINUTE - post_cal.MINUTE == 1)
				return "1 minute ago";
			else
				return today_cal.get(Calendar.MINUTE) - post_cal.get(Calendar.MINUTE) + " minutes ago";

		else if (today_cal.get(Calendar.MILLISECOND) >= post_cal.get(Calendar.MILLISECOND))
			return " just now";
		else
			return "";
	}

}
