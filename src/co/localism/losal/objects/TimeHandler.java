package co.localism.losal.objects;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
		final Calendar post_cal = Calendar.getInstance();
		post_cal.setTime(d);
		if (today_cal.YEAR > post_cal.YEAR)
			if (today_cal.YEAR - post_cal.YEAR == 1)
				return "1 year ago";
			else
				return today_cal.YEAR - post_cal.YEAR + " years ago";

		else if (today_cal.MONTH > post_cal.MONTH)
			if (today_cal.MONTH - post_cal.MONTH == 1)
				return "1 month ago";
			else
				return today_cal.MONTH - post_cal.MONTH + " months ago";

		else if (today_cal.DAY_OF_MONTH > post_cal.DAY_OF_MONTH)
			if (today_cal.DAY_OF_MONTH - post_cal.DAY_OF_MONTH == 1)
				return "1 day ago";
			else
				return today_cal.DAY_OF_MONTH - post_cal.DAY_OF_MONTH
						+ " days ago";

		else if (today_cal.HOUR_OF_DAY > post_cal.HOUR_OF_DAY)
			if (today_cal.HOUR_OF_DAY - post_cal.HOUR_OF_DAY == 1)
				return "1 hour ago";
			else
				return today_cal.HOUR_OF_DAY - post_cal.HOUR_OF_DAY
						+ " hours ago";

		else if (today_cal.MINUTE > post_cal.MINUTE)
			if (today_cal.MINUTE - post_cal.MINUTE == 1)
				return "1 minute ago";
			else
				return today_cal.MINUTE - post_cal.MINUTE + " minutes ago";

		else if (today_cal.MILLISECOND > post_cal.MILLISECOND)
			return " just now";

		return "";
	}

}
