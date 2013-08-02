package co.localism.losal;
import android.app.Activity;

import co.localism.losal.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class SetUpSlidingMenu extends SlidingMenu {

	public SetUpSlidingMenu(Activity activity, int slideStyle) {
		super(activity, slideStyle);

	        setMode(SlidingMenu.LEFT_RIGHT);
	        setMenu(R.layout.personal_options);
	        setSecondaryMenu(R.layout.notices);
	        setBehindOffsetRes(R.dimen.slidingmenu_offset);
	        
//	       attachToActivity(this, SlidingMenu.SLIDING_CONTENT);// SlidingMenu.SLIDING_WINDOW | SlidingMenu.SLIDING_CONTENT

	
	}
	
}
