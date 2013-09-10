package co.localism.losal.activities;

import java.util.Locale;

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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import co.localism.losal.MyApplication;
import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

public class MoreOptionsActivity extends Activity implements OnClickListener {
	private SlidingMenu sm;
	private static final String SUGGEST_FEATURE_EMAIL = "losalsuggestion@localism.zendesk.com";
	private static final String HELP_EMAIL = "losalhelp@localism.zendesk.com";
	private String tag = "MoreOptionsActivity";
	Drawable down_chevron;
	private String EMAIL_APP_IDEAS = "losalappideas@localism.zendesk.com";

	private LinearLayout ll_mo_suggest_more, ll_mo_about_more;

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
		title.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf"));
		setContentView(R.layout.activity_more_options);
		sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_WINDOW, true);// .SLIDING_CONTENT);
		new PersonalOptionsOnClickListeners(
				(LinearLayout) findViewById(R.id.po), this,
				PersonalOptionsOnClickListeners.ACTIVITY_MORE_OPTIONS);

		
		LinearLayout ll_suggest = (LinearLayout) findViewById(R.id.ll_mo_suggest);
		ll_suggest.setOnClickListener(this);
		// TextView tv_help = (TextView) findViewById(R.id.tv_mo_help);
		// tv_suggest.setOnClickListener(this);
		// TextView tv_about = (TextView) findViewById(R.id.tv_mo_about);
		// tv_suggest.setOnClickListener(this);
		TextView tv_reset = (TextView) findViewById(R.id.tv_mo_reset);
		tv_reset.setOnClickListener(this);

		// try {
		// // LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
		// LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_mo);
		//
		// Bitmap bmImg = (BitmapFactory.decodeFile(Environment
		// .getExternalStorageDirectory() + "/losal_bg.jpg"));
		// Drawable d = new BitmapDrawable(getResources(), bmImg);
		// Log.d(tag, "used image as background sucessfully!");
		// ll_main.setBackgroundDrawable(d);// .setBackground(d);
		// } catch (Exception e) {
		// Log.e(tag, "failed to use image as background. e: " + e.toString());
		// }

		ll_mo_suggest_more = (LinearLayout) findViewById(R.id.ll_mo_suggest_more);
		ll_mo_about_more = (LinearLayout) findViewById(R.id.ll_mo_about_more);

		down_chevron = new SVGHandler().svg_to_drawable(this, R.raw.gear,
				R.color.white, R.color.trans_black);

		Drawable local_logo = new SVGHandler().svg_to_drawable(this,
				R.raw.localism, R.color.white, R.color.black);
		ImageView iv = (ImageView) findViewById(R.id.iv_local_logo);
		iv.setImageDrawable(local_logo);
		// iv.setScaleType(ScaleType.FIT_XY);
		// iv.setScaleX(2);
		// iv.setScaleY(2);
		iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// setAllChevrons();

		setFontOnHeaders();
		setLinks();
		setEasterEgg();
		LinearLayout l = (LinearLayout) findViewById(R.id.po);
		l.findViewById(R.id.po_more_options).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						sm.toggle();
					}
				});
	}

	private void setFontOnHeaders() {
		Typeface slab_font = Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf");
		int[] ids = { R.id.tv_mo_about, R.id.tv_mo_suggest, R.id.tv_mo_faq,
				R.id.tv_mo_help, R.id.tv_mo_safety, R.id.tv_mo_reset };
		for (int i = 0; i < ids.length; i++) {
			TextView tv = (TextView) findViewById(ids[i]);
			tv.setTypeface(slab_font);
		}
	}

	private void setEasterEgg() {
		ImageView iv = (ImageView) findViewById(R.id.iv_arnold);
		iv.setVisibility(View.GONE);
		// taking this view out for the time being because we don't have a link
		// yet.
		// Also in the future we might want to make the image changeable and
		// have that as part of the app settings.
		/*
		 * iv.setOnClickListener(new OnClickListener(){
		 * 
		 * @Override public void onClick(View v) { // openURL(""); } });
		 */
	}

	private void setLinks() {

		ClickableSpan link_schools = new ClickableSpan() {
			public void onClick(View view) {
				openURL("http://www.localism.co/schools");
			}
		};

		ClickableSpan suggest_email = new ClickableSpan() {
			public void onClick(View view) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						Uri.fromParts("mailto", EMAIL_APP_IDEAS, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My idea!)");
				startActivity(Intent
						.createChooser(emailIntent, "Send email..."));
			}
		};
		ClickableSpan parents_suggest_email = new ClickableSpan() {
			public void onClick(View view) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						Uri.fromParts("mailto", EMAIL_APP_IDEAS, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Parents submitting to social feed)");
				startActivity(Intent
						.createChooser(emailIntent, "Send email..."));
			}
		};
		ClickableSpan faculty_suggest_email = new ClickableSpan() {
			public void onClick(View view) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
						Uri.fromParts("mailto", EMAIL_APP_IDEAS, null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Faculty submitting to social feed)");
				startActivity(Intent
						.createChooser(emailIntent, "Send email..."));
			}
		};

		ClickableSpan call_911 = new ClickableSpan() {
			public void onClick(View view) {
				String uri = "tel:" + "911";
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse(uri));
				startActivity(intent);
			}
		};
		ClickableSpan call_non_emergency = new ClickableSpan() {
			public void onClick(View view) {
				String uri = "tel:" + "5623912193";
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse(uri));
				startActivity(intent);
			}
		};
		ClickableSpan link_privacy_en = new ClickableSpan() {
			public void onClick(View view) {
				openURL("http://localism.co/losalapp/privacy/english/");
			}
		};
		ClickableSpan link_privacy_sp = new ClickableSpan() {
			public void onClick(View view) {
				openURL("http://localism.co/losalapp/privacy/espanol/");
			}
		};

		Typeface slab_font = Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf");
		int[] ids = { R.id.tv_mo_about_more, R.id.tv_mo_suggest_more,
				R.id.tv_mo_faq_more, R.id.tv_mo_help_more,
				R.id.tv_mo_faq_more1, R.id.tv_mo_safety_more,
				R.id.tv_mo_safety_more1, R.id.tv_mo_safety_more2,
				R.id.tv_mo_safety_more3 };

		for (int i = 0; i < ids.length; i++) {
			TextView tv = (TextView) findViewById(ids[i]);
			tv.setTypeface(slab_font);
			switch (ids[i]) {
			case R.id.tv_mo_about_more:
				int x = tv.getText().toString().toLowerCase(Locale.US)
						.indexOf("localism.co");
				if (x != -1) {
					SpannableString ss = new SpannableString(tv.getText()
							.toString());
					ss.setSpan(link_schools, x, x + 19, 0);
					tv.setText(ss);
					tv.setClickable(true);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
				}

				break;

			case R.id.tv_mo_suggest_more:

				x = tv.getText().toString().toLowerCase(Locale.US)
						.indexOf("email us");
				if (x != -1) {
					SpannableString ss = new SpannableString(tv.getText()
							.toString());
					ss.setSpan(suggest_email, x, x + 8, 0);
					tv.setText(ss);
					tv.setClickable(true);
					tv.setMovementMethod(LinkMovementMethod.getInstance());
				}

				break;
			case R.id.tv_mo_safety_more:

				x = tv.getText().toString().indexOf("911");
				if (x != -1) {
					SpannableString ss = new SpannableString(tv.getText()
							.toString());
					ss.setSpan(call_911, x, x + 3, 0);
					tv.setText(ss);
				}
				tv.setClickable(true);
				tv.setMovementMethod(LinkMovementMethod.getInstance());

				break;
			case R.id.tv_mo_safety_more1:

				x = tv.getText().toString().indexOf("562-391-2193");
				if (x != -1) {
					SpannableString ss = new SpannableString(tv.getText()
							.toString());
					ss.setSpan(call_non_emergency, x, x + 12, 0);
					tv.setText(ss);
				}
				tv.setClickable(true);
				tv.setMovementMethod(LinkMovementMethod.getInstance());
				break;
			case R.id.tv_mo_safety_more2:

				x = tv.getText().toString().indexOf("Privacy policy (English)");
				if (x != -1) {
					SpannableString ss = new SpannableString(tv.getText()
							.toString());
					ss.setSpan(link_privacy_en, x, x + 14, 0);
					tv.setText(ss);
				}
				tv.setClickable(true);
				tv.setMovementMethod(LinkMovementMethod.getInstance());
				break;
			case R.id.tv_mo_safety_more3:

				x = tv.getText().toString().toLowerCase(Locale.US)
						.indexOf("de privacidad");
				if (x != -1) {
					SpannableString ss = new SpannableString(tv.getText()
							.toString());
					ss.setSpan(link_privacy_sp, x - 9, x + 13, 0);
					tv.setText(ss);
				}

				tv.setClickable(true);
				tv.setMovementMethod(LinkMovementMethod.getInstance());

				break;

			case R.id.tv_mo_faq_more:
				SpannableString ss = new SpannableString(tv.getText()
						.toString());

				x = tv.getText().toString().toLowerCase(Locale.US)
						.indexOf("email us");
				if (x != -1) {
					ss.setSpan(faculty_suggest_email, x, x + 8, 0);
				}

				tv.setText(ss);
				tv.setClickable(true);
				tv.setMovementMethod(LinkMovementMethod.getInstance());
				break;

			case R.id.tv_mo_faq_more1:

				x = tv.getText().toString().toLowerCase(Locale.US)
						.indexOf("emailing us");
				ss = new SpannableString(tv.getText().toString());
				if (x != -1) {
					ss.setSpan(parents_suggest_email, x, x + 11, 0);
				}
				tv.setText(ss);
				tv.setClickable(true);
				tv.setMovementMethod(LinkMovementMethod.getInstance());
				break;
			}

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

	private void setAllChevrons() {
		int[] ids = { R.id.iv_chevron_about, R.id.iv_chevron_suggest,
				R.id.iv_chevron_help, R.id.iv_chevron_safety,
				R.id.iv_chevron_faq };
		for (int i = 0; i < ids.length; i++) {
			ImageView iv = (ImageView) findViewById(ids[i]);
			iv.setImageDrawable(down_chevron);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

	}

	@Override
	public void onBackPressed() {
		// if(viewSwitcher.getCurrentView() != myFirstView){
		// viewSwitcher.showPrevious();
		// }else
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
		case R.id.ll_mo_suggest:
			// if (ll_mo_suggest_more.getVisibility() == View.GONE)
			// ll_mo_suggest_more.startAnimation(new MyScaler(1.0f, 1.0f, 0f,
			// 1.0f, 500, ll_mo_suggest_more, false));
			// else
			// ll_mo_suggest_more.startAnimation(new MyScaler(1.0f, 1.0f,
			// 1.0f, 0.0f, 500, ll_mo_suggest_more, true));

			// new DropDownAnim(v,50, true).setDuration(500)

			// email(MoreOptionsActivity.SUGGEST_FEATURE_EMAIL,
			// "My Suggestion");
			break;

		case R.id.tv_mo_help:
			email(MoreOptionsActivity.HELP_EMAIL, "Help Me");
			break;

		case R.id.tv_mo_about:

			v.startAnimation(new MyScaler(1.0f, 1.0f, 1.0f, 0.0f, 500, v, true));

			// if (viewSwitcher.getCurrentView() != myFirstView) {
			// viewSwitcher.showPrevious();
			// } else if (viewSwitcher.getCurrentView() != mySecondView) {
			// viewSwitcher.showNext();
			// }
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
		Toast.makeText(this, "#LOSAL is now reset.", Toast.LENGTH_LONG).show();
//		startActivity(new Intent(this, OnBoardSequenceActivity.class));
//		finish();
//		RestartApplicationActivity.doRestart((Activity)SplashScreenActivity.class);
		startActivity(new Intent(this, SplashScreenActivity.class));
	}

	public void unlinkUser() {
		try {
			Parse.initialize(this,
					getResources().getString(R.string.parse_app_id),
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class DropDownAnim extends Animation {
		int targetHeight;
		View view;
		boolean down;

		public DropDownAnim(View view, int targetHeight, boolean down) {
			this.view = view;
			this.targetHeight = targetHeight;
			this.down = down;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			int newHeight;
			if (down) {
				newHeight = (int) (targetHeight * interpolatedTime);
			} else {
				newHeight = (int) (targetHeight * (1 - interpolatedTime));
			}
			view.getLayoutParams().height = newHeight;
			view.requestLayout();
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}

	public class MyScaler extends ScaleAnimation {

		private View mView;

		private LayoutParams mLayoutParams;
		private ViewGroup.MarginLayoutParams lp;
		private int mMarginBottomFromY, mMarginBottomToY;

		private boolean mVanishAfter = false;

		public MyScaler(float fromX, float toX, float fromY, float toY,
				int duration, View view, boolean vanishAfter) {
			super(fromX, toX, fromY, toY);
			setDuration(duration);
			mView = view;
			mVanishAfter = vanishAfter;
			mLayoutParams = (LayoutParams) view.getLayoutParams();
			int height = mView.getHeight();
			lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
			mMarginBottomFromY = (int) (height * fromY) + lp.bottomMargin
					- height;
			mMarginBottomToY = (int) (0 - ((height * toY) + lp.bottomMargin))
					- height;

		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			if (interpolatedTime < 1.0f) {
				int newMarginBottom = mMarginBottomFromY
						+ (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
				lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin,
						newMarginBottom);
				mView.getParent().requestLayout();
			} else if (mVanishAfter) {
				mView.setVisibility(View.GONE);
			} else {
				mView.setVisibility(View.VISIBLE);

			}
		}

	}

}