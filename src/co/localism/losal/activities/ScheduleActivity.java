package co.localism.losal.activities;


import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleActivity extends Activity{
	private String tag = "Schedule Activity";
	private SlidingMenu sm;
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
//        ImageView imageView = new ImageView(this);
//        // Set the background color to white
//        imageView.setBackgroundColor(Color.WHITE);
//        // Parse the SVG file from the resource
//        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.android);
//        // Get a drawable from the parsed SVG and set it as the drawable for the ImageView
//        imageView.setImageDrawable(svg.createPictureDrawable());
//        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        ActionBar a = getActionBar();
        a.setDisplayHomeAsUpEnabled(true);
		a.setDisplayShowTitleEnabled(true);
		a.setDisplayUseLogoEnabled(false);
		Drawable dd = new ColorDrawable(R.color.transparent);
		a.setIcon(dd);
		a.setDisplayShowCustomEnabled(true);
		a.setTitle("");
		a.setCustomView(R.layout.actionbar_custome_view);
		TextView title = (TextView) a.getCustomView().findViewById(
				R.id.ab_title);
		title.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf"));
		title.setText("Schedule");
        setContentView(R.layout.activity_schedule);
        sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_WINDOW, true);// .SLIDING_CONTENT);
		new PersonalOptionsOnClickListeners(
				(LinearLayout) findViewById(R.id.po), this, PersonalOptionsOnClickListeners.ACTIVITY_SCHEDULE);
		
		try {
			// LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
			LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_schedule);

			Bitmap bmImg = (BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory() + "/losal_bg.jpg"));
			Drawable d = new BitmapDrawable(getResources(), bmImg);
			Log.d(tag, "used image as background sucessfully!");
			ll_main.setBackgroundDrawable(d);// .setBackground(d);
		} catch (Exception e) {
			Log.e(tag, "failed to use image as background. e: " + e.toString());
		}
		
		LinearLayout l = (LinearLayout) findViewById(R.id.po);
		l.findViewById(R.id.po_schedule).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				sm.toggle();
			}
		});
		
    }
    
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.general, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			sm.showMenu();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    
    
}
