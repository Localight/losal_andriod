package co.localism.losal;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import co.localism.losal.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SetUpSlidingMenu extends SlidingMenu {

	public SetUpSlidingMenu(Activity activity, int slideStyle) {
		super(activity, slideStyle);

		setMode(SlidingMenu.LEFT_RIGHT);
		setMenu(R.layout.personal_options);
		setSecondaryMenu(R.layout.notices);
		setBehindOffsetRes(R.dimen.slidingmenu_offset);
		LinearLayout ll_po_socrative = (LinearLayout) activity
				.findViewById(R.id.po_socrative);
		ll_po_socrative.addView(new SVGHandler().svg_to_imageview(activity.getApplicationContext(), R.raw.socrative, 1f, (int) getResources().getDimension(R.dimen.personal_options_icon_size), (int) getResources().getDimension(R.dimen.personal_options_icon_size)) , 0);
		LinearLayout ll_po_social = (LinearLayout) activity
				.findViewById(R.id.po_social_feed);
		ll_po_social.addView(new SVGHandler().svg_to_imageview(activity.getApplicationContext(), R.raw.social, 1f, (int) getResources().getDimension(R.dimen.personal_options_icon_size), (int) getResources().getDimension(R.dimen.personal_options_icon_size)) , 0);

		LinearLayout ll_po_events= (LinearLayout) activity
				.findViewById(R.id.po_events);
		ll_po_events.addView(new SVGHandler().svg_to_imageview(activity.getApplicationContext(), R.raw.clock, 1f, (int) getResources().getDimension(R.dimen.personal_options_icon_size), (int) getResources().getDimension(R.dimen.personal_options_icon_size)) , 0);

		LinearLayout ll_po_edmodo= (LinearLayout) activity
				.findViewById(R.id.po_edmodo);
		ll_po_edmodo.addView(new SVGHandler().svg_to_imageview(activity.getApplicationContext(), R.raw.edmodo, 1f, (int) getResources().getDimension(R.dimen.personal_options_icon_size), (int) getResources().getDimension(R.dimen.personal_options_icon_size)) , 0);

		
//		TODO: Add grades svg and schedule svg to raw folder. Then comment out the imageview in the linearlayouts that are found below
//		and uncomment the following lines.
		//		LinearLayout ll_po_grades= (LinearLayout) activity
//				.findViewById(R.id.po_grades);
//		ll_po_grades.addView(new SVGHandler().svg_to_imageview(activity.getApplicationContext(), R.raw.grades, 1f, (int) getResources().getDimension(R.dimen.personal_options_icon_size), (int) getResources().getDimension(R.dimen.personal_options_icon_size)) , 0);

		//		LinearLayout ll_po_schedule= (LinearLayout) activity
//				.findViewById(R.id.po_schedule);
//		ll_po_schedule.addView(new SVGHandler().svg_to_imageview(activity.getApplicationContext(), R.raw.schedule, 1f, (int) getResources().getDimension(R.dimen.personal_options_icon_size), (int) getResources().getDimension(R.dimen.personal_options_icon_size)) , 0);

		
		
		
		
		
		// attachToActivity(this, SlidingMenu.SLIDING_CONTENT);//
		// SlidingMenu.SLIDING_WINDOW | SlidingMenu.SLIDING_CONTENT

	}

}
