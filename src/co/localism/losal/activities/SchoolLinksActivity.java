package co.localism.losal.activities;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class SchoolLinksActivity extends Activity implements OnClickListener{
//	private SlidingMenu sm;
	private String FLICKR = "http://www.flickr.com/photos/97669165@N06/";
	private String INSTA = "http://instagram.com/LosAlamitosHigh";
	private String FB = "http://www.facebook.com/losalamitoshighschool";
	private String TW = "https://twitter.com/LosAlamitosHigh";
	private String GRIFFIN_NEWS = "http://www.youtube.com/user/GriffinNews2013";
	private String ARNOLD_YOUTUBE = "http://www.youtube.com/user/josharnold67"; 
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar a = getActionBar();
		a.setDisplayHomeAsUpEnabled(true);
		a.setDisplayShowTitleEnabled(true);
		a.setDisplayUseLogoEnabled(false);
		Drawable dd = new ColorDrawable(R.color.transparent);
		a.setIcon(dd);
		a.setDisplayShowCustomEnabled(true);
		a.setTitle("   ");
		a.setCustomView(R.layout.actionbar_custome_view);
		TextView title = (TextView) a.getCustomView().findViewById(
				R.id.ab_title);
		title.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf"));
		title.setText("School Links");
		setContentView(R.layout.activity_school_links);
//		sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_WINDOW, true);// .SLIDING_CONTENT);
//		new PersonalOptionsOnClickListeners(
//				(LinearLayout) findViewById(R.id.po), this, PersonalOptionsOnClickListeners.ACTIVITY_SCHOOL_LINKS);

//		LinearLayout l = (LinearLayout) findViewById(R.id.po);
//		l.findViewById(R.id.po_events).setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				sm.toggle();
//			}
//		});
//		
		LayoutParams params = new LinearLayout.LayoutParams(100, 100);

		ImageView iv = new SVGHandler().svg_to_imageview(this, R.raw.social_links_instagram);
		LinearLayout ll = (LinearLayout) findViewById(R.id.sl_insta);
		ll.addView(iv, 0, params);
		ll.setOnClickListener(this);
		
		ImageView iv2 = new SVGHandler().svg_to_imageview(this, R.raw.social_links_facebook);
		LinearLayout ll2 = (LinearLayout) findViewById(R.id.sl_fb);
		ll2.addView(iv2, 0, params);
		ll2.setOnClickListener(this);
		
		ImageView iv3 = new SVGHandler().svg_to_imageview(this, R.raw.social_links_twitter);
		LinearLayout ll3 = (LinearLayout) findViewById(R.id.sl_tw);
		ll3.addView(iv3, 0, params);
		ll3.setOnClickListener(this);
		
		ImageView iv4 = new SVGHandler().svg_to_imageview(this, R.raw.social_links_flicker);
		LinearLayout ll4 = (LinearLayout) findViewById(R.id.sl_flickr);
		ll4.addView(iv4, 0, params);
		ll4.setOnClickListener(this);
		
		ImageView iv5 = new SVGHandler().svg_to_imageview(this, R.raw.social_links_youtube);
		LinearLayout ll5 = (LinearLayout) findViewById(R.id.sl_yt);
		ll5.addView(iv5, 0, params);
		ll5.setOnClickListener(this);
		
		ImageView iv6 = new SVGHandler().svg_to_imageview(this, R.raw.social_links_youtube);
		LinearLayout ll6 = (LinearLayout) findViewById(R.id.sl_yt_2);
		ll6.addView(iv6, 0, params);
		ll6.setOnClickListener(this);
		
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
//			sm.showMenu();
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}




	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.sl_fb:
			openURL(FB);			
			break;
		case R.id.sl_tw:
			openURL(TW);			
			break;
			
		case R.id.sl_insta:
			openURL(INSTA);			
			break;
			
		case R.id.sl_flickr:
			openURL(FLICKR);			
			break;
		case R.id.sl_yt:
			openURL(GRIFFIN_NEWS);			
			break;
		case R.id.sl_yt_2:
			openURL(ARNOLD_YOUTUBE);			
			break;
			
		}
		
		
		
		
	}
	public void openURL(String URL) {
		if (URL.length() > 0) {
			try {
				Intent openURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				this.startActivity(openURL);
			} catch (Exception e) {

			}
		}
	}

    
}
