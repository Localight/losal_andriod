package co.localism.losal.activities;

import co.localism.losal.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SyncCalendarActivity extends Activity {

	private String LOSAL_CAL_URL = "http://los.al/lahs/t/t.htm?=acal";
	private Context ctx = this;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sync_calendar);
		
		Button btn_sync= (Button) findViewById(R.id.btn_sync_calendar);
		btn_sync.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				try {
					Intent openURL = new Intent(Intent.ACTION_VIEW, Uri.parse(LOSAL_CAL_URL));
					ctx.startActivity(openURL);
				} catch (Exception e) {

				}
			}
		});
		
		ImageView btn_close = (ImageView) findViewById(R.id.btn_sync_cal_close);
		btn_close.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Typeface tf = (Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf"));
		TextView tv = (TextView) findViewById(R.id.tv_sync_cal_title);
		tv.setTypeface(tf);
		tv = (TextView) findViewById(R.id.tv_sync_calendar_message);
		tv.setTypeface(tf);
		tv = (TextView) findViewById(R.id.tv_sync_calendar_footer);
		tv.setTypeface(tf);		
	}	
}
