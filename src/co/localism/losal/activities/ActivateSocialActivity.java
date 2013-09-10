package co.localism.losal.activities;

import java.io.IOException;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.async.PushData;

public class ActivateSocialActivity extends Activity {

	private static final String tag = "ActivateSocialActivity";
	private OnClickListener fb_onclick, tw_onclick, insta_onclick;

	private static final int icon_width = 120;
	private static final int icon_height = 120;
	private static final int localism_logo_height = 100;
	private static final int localism_logo_width = (int) (3.286 * localism_logo_height);

	private ParseUser currentUser;
	private Context ctx = this;
	private BroadcastReceiver mResponseListener;
	private SharedPreferences user_info;
	private LinearLayout.LayoutParams params;
	private LinearLayout ll_social;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activate_social);
		Boolean show_twitter = false;
		Boolean show_instagram = false;

		Bundle extras = getIntent().getExtras();
		if (extras != null && !extras.isEmpty()) {
			show_twitter = extras.getBoolean("twitter", false);
			show_instagram = extras.getBoolean("instagram", false);
		}
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// do stuff with the user

		} else {
			// show the signup or login screen
		}
		ll_social = (LinearLayout) findViewById(R.id.ll_activate_social);
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_social_bg);
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// onBackPressed();
			}
		});

		setup_onclick_listeners();
		params = new LinearLayout.LayoutParams(icon_width, icon_height);
		LayoutParams logo_params = new LinearLayout.LayoutParams(
				localism_logo_width, localism_logo_height);
		LinearLayout ll_footer = (LinearLayout) findViewById(R.id.ll_social_footer);
		ll_footer.addView(
				new SVGHandler().svg_to_imageview(this, R.raw.localism),
				ll_footer.getChildCount(), logo_params);

		showTwitter(show_twitter);
		showInstagram(show_instagram);

		// add fb icon and set onclick
		/*
		 * LinearLayout ll_fb = (LinearLayout)
		 * findViewById(R.id.ll_activate_fb); ll_fb.addView(new
		 * SVGHandler().svg_to_imageview(this, R.raw.fb2, R.color.android_green,
		 * R.color.white, 1f), 0, params); ll_fb.setOnClickListener(fb_onclick);
		 */
		// add instagram icon and set onclick

	}

	private void showTwitter(Boolean show) {

		if (show) {
			setHeaderTitle(R.string.activate_twitter);

			ll_social.removeAllViews();
			ll_social.addView(
					new SVGHandler().svg_to_imageview(this, R.raw.tw, 1.0f), 0,
					params);
			ll_social.setOnClickListener(tw_onclick);
		}
	}

	private void showInstagram(Boolean show) {
		if (show) {
			setHeaderTitle(R.string.activate_instagram);
			ll_social.removeAllViews();
			ll_social.addView(
					new SVGHandler().svg_to_imageview(this, R.raw.insta, 1.0f), 0,
					params);
			ll_social.setOnClickListener(insta_onclick);
		}
	}

	private void setHeaderTitle(int s) {
		TextView title = (TextView) findViewById(R.id.tv_activate_title);
		title.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf"));
		title.setText(s);
	}

	private void setup_onclick_listeners() {
		fb_onclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("activate", "start fb login acti");
				Intent i = new Intent(getApplicationContext(),
						FbLoginActivity.class);
				startActivity(i);
			}
		};

		tw_onclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("activate", "start twitter login ");
				// twitterLogin();
				linkTwitterUser();
			}
		};

		insta_onclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("activate", "start insta login ");
				Intent i = new Intent(getApplicationContext(), Instagram.class);
				startActivity(i);
			}
		};

	}

	private void twitterLogin() {
		ParseTwitterUtils.initialize(
				getResources().getString(R.string.tw_consumer_key),
				getResources().getString(R.string.tw_consumer_secret));

		ParseTwitterUtils.logIn(this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				if (user == null) {
					Log.d("MyApp",
							"Uh oh. The user cancelled the Twitter login.");
				} else if (user.isNew()) {
					Log.d("MyApp",
							"User signed up and logged in through Twitter!");
					linkTwitterUser();

				} else {
					Log.d("MyApp", "User logged in through Twitter!");
					linkTwitterUser();
				}
			}
		});

	}

	public void unlinkTwitterUser() {
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

	}

	private void linkTwitterUser() {
		try {
			ParseTwitterUtils.initialize(
					getResources().getString(R.string.tw_consumer_key),
					getResources().getString(R.string.tw_consumer_secret));
			if (!ParseTwitterUtils.isLinked(currentUser)) {
				ParseTwitterUtils.link(currentUser, this, new SaveCallback() {
					@Override
					public void done(ParseException ex) {
						if (ParseTwitterUtils.isLinked(currentUser)) {
							Log.d("MyApp",
									"Woohoo, user logged in with Twitter!");
							Toast.makeText(ctx, "Twitter connected!!",
									Toast.LENGTH_SHORT).show();
							// currentUser
							// add this info to user_info
							saveToUserInfo();
						} else
							Log.d("MyApp", "Error linking Twitter.");
					}
				});

			} else {
				// is linked
				Toast.makeText(ctx, "Already Logged In!", Toast.LENGTH_SHORT)
						.show();
				saveToUserInfo();
			}
			try {
				new PushData().execute("twitterID", ParseTwitterUtils
						.getTwitter().getScreenName(), ParseTwitterUtils
						.getTwitter().getUserId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finish();
	}

	private void saveToUserInfo() {

		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = user_info.edit();
		prefEditor.putBoolean("hasTwitter", true);
		prefEditor.commit();

	}

	@Override
	protected void onResume() {
		super.onResume();

		IntentFilter filter = new IntentFilter();
		filter.addAction("co.localism.losal.responselistener");
		filter.addCategory("co.localism.losal.instademo");
		if (mResponseListener != null)
			registerReceiver(mResponseListener, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mResponseListener != null)
			unregisterReceiver(mResponseListener);
	}

}
