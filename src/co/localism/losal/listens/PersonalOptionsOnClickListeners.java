package co.localism.losal.listens;

import co.localism.losal.R;
import co.localism.losal.activities.MainActivity;
import co.localism.losal.activities.MoreOptionsActivity;
import co.localism.losal.activities.ScheduleActivity;
import co.localism.losal.activities.SchoolLinksActivity;
import co.localism.losal.activities.WebViewActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PersonalOptionsOnClickListeners implements View.OnClickListener {
	public static Context ctx;
	private static final String tag = "PersonalOptionsOnClickListeners";
	private String LOSAL_URL = "http://www.losal.org/lahs";

	public static final int ACTIVITY_MAIN = 0;
	public static final int ACTIVITY_SCHOOL_LINKS = 1;
	public static final int ACTIVITY_GRADES = 2;
	public static final int ACTIVITY_SCHEDULE = 3;
	public static final int ACTIVITY_SOCRATIVE = 4;
	public static final int ACTIVITY_EDMODO = 5;
	public static final int ACTIVITY_MORE_OPTIONS = 6;

	private int which_activity = -1;

	public PersonalOptionsOnClickListeners() {
	}

	public PersonalOptionsOnClickListeners(LinearLayout l, Context ctx) {
		this(l, ctx, -1);
	}

	/**
	 * 
	 * @param l
	 * @param ctx
	 * @param which_activity
	 *            this removes the onclick for the item that would lead to that
	 *            activity. This allows you to set the onclick in the activity
	 *            itself.
	 */
	public PersonalOptionsOnClickListeners(LinearLayout l, Context ctx,
			int which_activity) {
		this.ctx = ctx;
		Log.d(tag, "ctx: " + ctx);
		this.which_activity = which_activity;
		l.findViewById(R.id.po_social_feed).setOnClickListener(this);
		l.findViewById(R.id.po_events).setOnClickListener(this);
		l.findViewById(R.id.po_grades).setOnClickListener(this);
		l.findViewById(R.id.po_schedule).setOnClickListener(this);
		l.findViewById(R.id.po_socrative).setOnClickListener(this);
		l.findViewById(R.id.po_edmodo).setOnClickListener(this);
		l.findViewById(R.id.po_footer).setOnClickListener(this);
		l.findViewById(R.id.po_more_options).setOnClickListener(this);

		// for(int i = 0; i < l.getChildCount(); i++){
		// ll2 = (LinearLayout) l.getChildAt(i);
		// for(int j = 0; j < ll2.getChildCount(); i++){
		// ll2.getChildAt(j).setOnClickListener(new
		// PersonalOptionsOnClickListeners());
		// }
		// }
	}

	@Override
	public void onClick(View v) {
		Log.d(tag, "my onClick was called" + v.toString());
		Intent intent;
		switch (v.getId()) {
		case R.id.po_social_feed:
			Log.d(tag, "Social Feed");

			// TODO: check if social feed is already running. if it is, just
			// close the sliding menu
			// I'm not convinced you need to actually check. I think android
			// checks for you.
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_MAIN) {
				intent = new Intent(ctx, MainActivity.class);
				ctx.startActivity(intent);
			}
			break;
		case R.id.po_events:
			Log.d(tag, "Events");
			// Shared
			// showCalendarDialog();
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_SCHOOL_LINKS) {
				intent = new Intent(ctx, SchoolLinksActivity.class);
				ctx.startActivity(intent);
			}
			break;
		case R.id.po_grades:
			Log.d(tag, "Grades");
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_GRADES) {
				intent = new Intent(ctx, WebViewActivity.class);
				intent.putExtra("which", WebViewActivity.GRADES);
				ctx.startActivity(intent);
			}
			break;
		case R.id.po_schedule:
			Log.d(tag, "Schedule");
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_SCHEDULE) {
				intent = new Intent(ctx, ScheduleActivity.class);
				ctx.startActivity(intent);
			}
			break;
		case R.id.po_socrative:
			Log.d(tag, "Socrative");
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_SOCRATIVE) {
				intent = new Intent(ctx, WebViewActivity.class).putExtra(
						"which", WebViewActivity.SOCRATIVE);
				ctx.startActivity(intent);
			}
			break;
		case R.id.po_edmodo:
			Log.d(tag, "Edmodo");
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_EDMODO) {
				intent = new Intent(ctx, WebViewActivity.class).putExtra(
						"which", WebViewActivity.EDMODO);
				ctx.startActivity(intent);
			}
			break;
		// Log.d("PersonalOptionsOnClickListeners", "Other");
		case R.id.po_footer:
			Log.d(tag, "Footer");
				openURL(LOSAL_URL);
			break;
		case R.id.po_more_options:
			Log.d(tag, "More Options");
			if (which_activity != PersonalOptionsOnClickListeners.ACTIVITY_MORE_OPTIONS) {
				intent = new Intent(ctx, MoreOptionsActivity.class);
				ctx.startActivity(intent);
			}
			break;
		}

	}

	private void showCalendarDialog() {
		// String dialog_title, String dialog_message, String yes_btn, String
		// no_btn){

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setMessage(R.string.calendar_dialog_message).setTitle(
				R.string.calendar_dialog_title);

		builder.setPositiveButton(R.string.calendar_dialog_yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User clicked OK button
						openURL("webcal://losal.tandemcal.com/index.php?type=export&action=ical&export_type=now_to_infinity&limit=none&date_start=2013-08-26&page=2");
						// openURL("http://losal.tandemcal.com/index.php?type=export&action=ical&export_type=now_to_infinity&limit=none&date_start=2013-08-26&page=2");
					}
				});
		builder.setNegativeButton(R.string.calendar_dialog_no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();

	}

	public void openURL(String URL) {
		if (URL.length() > 0) {
			try {
				Intent openURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				ctx.startActivity(openURL);
			} catch (Exception e) {

			}
		}
	}

}
