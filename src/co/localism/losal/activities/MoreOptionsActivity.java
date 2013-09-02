package co.localism.losal.activities;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

public class MoreOptionsActivity extends Activity implements OnClickListener {
	private SlidingMenu sm;
	private static final String SUGGEST_FEATURE_EMAIL = "losalsuggestion@localism.zendesk.com";
	private static final String HELP_EMAIL = "losalhelp@localism.zendesk.com";
	private String tag = "MoreOptionsActivity";
	private ViewSwitcher viewSwitcher;
	private LinearLayout myFirstView;
	private LinearLayout mySecondView;
	
	
	
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
		a.setTitle("");
		a.setCustomView(R.layout.actionbar_custome_view);
		TextView title = (TextView) a.getCustomView().findViewById(
				R.id.ab_title);
		title.setText("More Options");
		setContentView(R.layout.activity_more_options);
		sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_WINDOW, true);// .SLIDING_CONTENT);
		new PersonalOptionsOnClickListeners(
				(LinearLayout) findViewById(R.id.po), this);
		TextView tv_suggest = (TextView) findViewById(R.id.tv_mo_suggest);
		tv_suggest.setOnClickListener(this);
		TextView tv_help = (TextView) findViewById(R.id.tv_mo_help);
		tv_suggest.setOnClickListener(this);
		TextView tv_about = (TextView) findViewById(R.id.tv_mo_about);
		tv_suggest.setOnClickListener(this);
		TextView tv_reset = (TextView) findViewById(R.id.tv_mo_reset);
		tv_suggest.setOnClickListener(this);

		try {
			// LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
			LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_mo);

			Bitmap bmImg = (BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory() + "/losal_bg.jpg"));
			Drawable d = new BitmapDrawable(getResources(), bmImg);
			Log.d(tag, "used image as background sucessfully!");
			ll_main.setBackgroundDrawable(d);// .setBackground(d);
		} catch (Exception e) {
			Log.e(tag, "failed to use image as background. e: " + e.toString());
		}

		
//		viewSwitcher = (ViewSwitcher) findViewById(R.id.vs);
//		myFirstView = (LinearLayout) findViewById(R.id.view_app);
//		mySecondView = (LinearLayout) findViewById(R.id.view_controls);
//	
//		
		
		
		
	}

	
	
	
	
	
	@Override
	public void onBackPressed(){
//		if(viewSwitcher.getCurrentView() != myFirstView){
//			viewSwitcher.showPrevious();
//		}else
			super.onBackPressed();
		
		
	}
	
	
	
	
	private void email(String address, String subject) {

		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				"mailto", address, null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		startActivity(Intent.createChooser(emailIntent, "Send email..."));

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

	@Override
	public void onClick(View v) {
		Log.d(tag, "click");
		switch (v.getId()) {
		case R.id.tv_mo_suggest:
			email(MoreOptionsActivity.SUGGEST_FEATURE_EMAIL, "My Suggestion");
			break;

		case R.id.tv_mo_help:
			email(MoreOptionsActivity.HELP_EMAIL, "Help Me");
			break;

		case R.id.tv_mo_about:
//			if (viewSwitcher.getCurrentView() != myFirstView) {
//				viewSwitcher.showPrevious();
//			} else if (viewSwitcher.getCurrentView() != mySecondView) {
//				viewSwitcher.showNext();
//			}
			break;
		case R.id.tv_mo_reset:
			resetApp();

			break;

		}
	}

	private void resetApp() {
		SharedPreferences sh;
		sh = this.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		sh.edit().clear().commit();
		sh = this.getSharedPreferences("UserLikes", Context.MODE_PRIVATE);
		sh.edit().clear().commit();
		sh = this.getSharedPreferences("InstagramInfo", Context.MODE_PRIVATE);
		sh.edit().clear().commit();
		sh = this.getSharedPreferences("TwitterInfo", Context.MODE_PRIVATE);
		sh.edit().clear().commit();
		unlinkUser();
	}

	public void unlinkUser() {

		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
		ParseTwitterUtils.initialize(
				getResources().getString(R.string.tw_consumer_key),
				getResources().getString(R.string.tw_consumer_secret));
		ParseUser currentUser = ParseUser.getCurrentUser();

		try {
			ParseTwitterUtils.unlink(currentUser);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ParseUser.logOut();

	}

}