package co.localism.losal;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import co.localism.losal.R;
import co.localism.losal.R.color;
import co.localism.losal.adapters.NoticeAdapter;
import co.localism.losal.objects.Notice;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SetUpSlidingMenu extends SlidingMenu {

	private Activity activity;

	public SetUpSlidingMenu(Activity activity, int slideStyle) {
		this(activity, slideStyle, false);
	}

	public SetUpSlidingMenu(Activity activity, int slideStyle,
			boolean disableRight) {
		super(activity, slideStyle);
		this.activity = activity;
		setMenu(R.layout.personal_options);
		if (disableRight) {
			setMode(SlidingMenu.LEFT);

		} else {
			setMode(SlidingMenu.LEFT_RIGHT);
			setSecondaryMenu(R.layout.notices);
		}
		setBehindOffsetRes(R.dimen.slidingmenu_offset);
		LinearLayout ll_po_socrative = (LinearLayout) activity
				.findViewById(R.id.po_socrative);
		ll_po_socrative.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.socrative,
				1f,
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);
		LinearLayout ll_po_social = (LinearLayout) activity
				.findViewById(R.id.po_social_feed);
		ll_po_social.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.social,
				1f,
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);

		LinearLayout ll_po_events = (LinearLayout) activity
				.findViewById(R.id.po_events);
		ll_po_events.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.school_links,
				1f,
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);

		LinearLayout ll_po_edmodo = (LinearLayout) activity
				.findViewById(R.id.po_edmodo);
		ll_po_edmodo.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.edmodo,
				1f,
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);

		// TODO: Add grades svg and schedule svg to raw folder. Then comment out
		// the imageview in the linearlayouts that are found below
		// and uncomment the following lines.
		LinearLayout ll_po_grades = (LinearLayout) activity
				.findViewById(R.id.po_grades);
		ll_po_grades.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.a_plus,
				1f,
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);

		LinearLayout ll_po_schedule = (LinearLayout) activity
				.findViewById(R.id.po_schedule);
		ll_po_schedule.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.schedule,
				1f,
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);

		LinearLayout ll_po_footer = (LinearLayout) activity
				.findViewById(R.id.po_footer);
		ll_po_footer.addView(new SVGHandler().svg_to_imageview(
				activity.getApplicationContext(),
				R.raw.griffin_mascot,
				0.7f,
				(int) getResources().getDimension(
						R.dimen.personal_options_footer_image_size),
				(int) getResources().getDimension(
						R.dimen.personal_options_icon_size)), 0);
		// Set group heading
		LinearLayout ll_po_break = (LinearLayout) activity
				.findViewById(R.id.po_group_header_1);
		TextView tv = (TextView) ll_po_break.findViewById(R.id.po_break_text);
		tv.setText(getResources().getString(R.string.group_heading_1));
		ll_po_break = (LinearLayout) activity
				.findViewById(R.id.po_group_header_2);
		tv = (TextView) ll_po_break.findViewById(R.id.po_break_text);
		tv.setText(getResources().getString(R.string.group_heading_2));
		ll_po_break = (LinearLayout) activity
				.findViewById(R.id.po_group_header_3);
		tv = (TextView) ll_po_break.findViewById(R.id.po_break_text);
		tv.setText(getResources().getString(R.string.group_heading_3));

		/******* ActionBar *******/
		configActionBar();
		/*
		 * ImageView iv_settings = (ImageView)
		 * activity.findViewById(R.id.iv_ab_settings);
		 * iv_settings.setImageDrawable(new SVGHandler().svg_to_drawable(
		 * activity.getApplicationContext(), R.raw.gear));
		 * iv_settings.setAlpha(0.6f);
		 * iv_settings.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		 */

		// ******** NOTICES ********

		/*
		 * notices = new ArrayList<Notice>(); Notice n = new Notice();
		 * n.setTitle("asdf  sfdfd asf"); n.setDetails(""); notices.add(n);
		 * notices.add(n); notices.add(n); notices.add(n); notices.add(n);
		 * notices.add(n); notices.add(n); notices.add(n); notices.add(n);
		 * notices.add(n); notices.add(n);
		 * 
		 * 
		 * 
		 * // TODO: possibly serialize and de when this is created and destroyed
		 * ListView myList = (ListView) findViewById(R.id.notices_list);
		 * 
		 * String[] listContent = { "abc", "def", "ghi", "Jkl" };
		 * 
		 * ListAdapter adapter = new NoticeAdapter(activity,
		 * R.layout.notice_list_item, notices, 1); // ArrayAdapter<String>
		 * adapter = new //
		 * ArrayAdapter<String>(activity,R.layout.notice_list_item,listContent);
		 * 
		 * myList.setAdapter(adapter);
		 */
		// attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//
		// SlidingMenu.SLIDING_WINDOW | SlidingMenu.SLIDING_CONTENT

	}

	public void configActionBar() {
		activity.getApplicationContext();
		SharedPreferences user_info = activity.getApplicationContext()
				.getSharedPreferences("UserInfo",
						Context.MODE_PRIVATE);

		TextView user_icon = (TextView) findViewById(R.id.tv_ab_user_icon);

		user_icon.setTypeface(Typeface.createFromAsset(activity
				.getApplicationContext().getAssets(), "icomoon.ttf"));

		// if(!user_info.getString("user_icon", "").equalsIgnoreCase("")){
		String s = ("\\u" + user_info.getString("user_icon", "e017"));
		Character c = (char) Integer.parseInt(s.substring(2), 16);
		// }else{

		// }
		user_icon.setText(c.toString());
		user_icon.setTextColor(Color.parseColor(user_info.getString(
				"fav_color", "#FFFFFF")));
		TextView user_name = (TextView) findViewById(R.id.tv_ab_user_name);
		user_name.setTypeface(Typeface.createFromAsset(activity.getApplicationContext().getAssets(),
				"robotoslab_regular.ttf"));
		user_name.setText(user_info.getString("user_name", "Unregistered"));
	}

}
