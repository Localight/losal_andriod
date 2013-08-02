package co.localism.losal.listens;


import co.localism.losal.R;
import co.localism.losal.activities.ScheduleActivity;
import co.localism.losal.activities.WebViewActivity;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class PersonalOptionsOnClickListeners implements View.OnClickListener{
	public static Context ctx;
	private static final String tag = "PersonalOptionsOnClickListeners";
	public PersonalOptionsOnClickListeners() {
	}
	public PersonalOptionsOnClickListeners(LinearLayout l, Context ctx) {
		this.ctx = ctx;
    	Log.d(tag, "ctx: "+ctx);
    	
		l.findViewById(R.id.po_social_feed).setOnClickListener(new PersonalOptionsOnClickListeners());
		l.findViewById(R.id.po_events).setOnClickListener(new PersonalOptionsOnClickListeners());
		l.findViewById(R.id.po_grades).setOnClickListener(new PersonalOptionsOnClickListeners());
		l.findViewById(R.id.po_schedule).setOnClickListener(new PersonalOptionsOnClickListeners());
		l.findViewById(R.id.po_socrative).setOnClickListener(new PersonalOptionsOnClickListeners());
		l.findViewById(R.id.po_edmodo).setOnClickListener(new PersonalOptionsOnClickListeners());


//		for(int i = 0; i < l.getChildCount(); i++){
//			ll2 = (LinearLayout) l.getChildAt(i);
//			for(int j = 0; j < ll2.getChildCount(); i++){
//				ll2.getChildAt(j).setOnClickListener(new PersonalOptionsOnClickListeners());
//			}
//		}
	}
	 
	 @Override 
	 public void onClick(View v) {
		 Log.d(tag, "my onClick was called" + v.toString());
		 Intent intent;
		 switch(v.getId()) {
		 	case R.id.po_social_feed:
		    	Log.d(tag, "Social Feed");
//		    	TODO: check if social feed is already running. if it is, just close the sliding menu
		    	break;
		    case R.id.po_events:
		    	Log.d(tag, "Events");	
			    intent = new Intent(ctx, WebViewActivity.class).putExtra("which", WebViewActivity.EVENTS);
			    ctx.startActivity(intent);
		    	break;
		    case R.id.po_grades:
		    	Log.d(tag, "Grades");
			    intent = new Intent(ctx, WebViewActivity.class);
			    intent.putExtra("which", WebViewActivity.GRADES);
			    ctx.startActivity(intent);
		    	break;
		    case R.id.po_schedule:
		    	Log.d(tag, "Schedule");	
			    intent = new Intent(ctx, ScheduleActivity.class);
			    ctx.startActivity(intent);
		    	break;
		    case R.id.po_socrative:
		    	Log.d(tag, "Socrative");	
			    intent = new Intent(ctx, WebViewActivity.class).putExtra("which", WebViewActivity.SOCRATIVE);
			    ctx.startActivity(intent);
		    	break;
		    case R.id.po_edmodo:
		    	Log.d(tag, "Edmodo");	
			    intent = new Intent(ctx, WebViewActivity.class).putExtra("which", WebViewActivity.EDMODO);
			    ctx.startActivity(intent);
		    	break;
//		 Log.d("PersonalOptionsOnClickListeners", "Other");
		 }

		 
		 
	 }
}
