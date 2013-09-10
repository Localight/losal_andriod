package co.localism.losal.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import co.localism.losal.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class OnBoardSequenceActivity extends FragmentActivity {

	private MyPageAdapter pageAdapter;
	private static Context ctx;
	private String tag = "OnBoardSequence";
	private String db_phone = "-1";
	private static OnClickListener verify_onclick, close_page_onclick,
			email_onclick;
	private static ProgressBar progress;
	private static EditText et_phone = null;
	private static Button btn_verify;
	private static TextView tv_ob_verify_full_experience;
	private static TextView tv_title;
	private static TextView tv_subtitle_1;
	private static TextView tv_subtitle_2;
	private static ImageView iv_ob_screenshot;
	private static TextView tv_message;
	private static TextView tv_lower_message;
	private static ImageButton iv_ob_verify_close;
	
	private static Typeface slab_font;
	
	private String address = "";
	private String subject = "";
	private String EMAIL_NO_TEXT = "losalsmserror@localism.zendesk.com";
	private String EMAIL_NOT_FOUND = "losalaccess@localism.zendesk.com";

	/*** Circles ***/
	private static ImageView iv_circle1, iv_circle2, iv_circle3;
	private static GradientDrawable bgShape1, bgShape2, bgShape3;

	// private View screen1, screen2, screen3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences user_info = getBaseContext().getSharedPreferences(
				"UserInfo", MODE_PRIVATE);
		if (!user_info.getBoolean("isFirstVisit", true)) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		} else {

			// getActionBar().hide();
			setContentView(R.layout.onboardsequence);

			slab_font = Typeface.createFromAsset(this.getAssets(),
					"robotoslab_regular.ttf");

			ctx = this;
			List<Fragment> fragments = getFragments();
			pageAdapter = new MyPageAdapter(getSupportFragmentManager(),
					fragments);
			final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
			pager.setAdapter(pageAdapter);
			Parse.initialize(this,
					getResources().getString(R.string.parse_app_id),
					getResources().getString(R.string.parse_client_key));
			//

			// SharedPreferences.Editor prefEditor = user_info.edit();
			// prefEditor.putBoolean("isFirstVisit", false);
			// prefEditor.commit();
			//

			// RelativeLayout rl = (RelativeLayout)
			// findViewById(R.id.rl_onboard_all);
			// rl.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// if(pager.getCurrentItem() == pageAdapter.getItem(1))
			// pager.setCurrentItem(pageAdapter.getItem(2));
			// }
			// });

			verify_onclick = new OnClickListener() {

				@Override
				public void onClick(View v) {
					final EditText et_phone = (EditText) findViewById(R.id.et_phone);
					if (et_phone.getText().toString().length() > 9) {
						if (hasNetworkConnection()) {
							Button btn_verify = (Button) findViewById(R.id.btn_verify);
							btn_verify.setText(R.string.verify_button_retry);
							btn_verify.setVisibility(View.GONE);
							progress.setVisibility(View.VISIBLE);

							new VerifyUser().execute(et_phone.getText()
									.toString(), "", "");
						} else {
							showNoNetworkConnection();
						}
					}
				}
			};

			close_page_onclick = new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					Intent i = new Intent(ctx, MainActivity.class);
					startActivity(i);
					finish();
				}

			};
			email_onclick = new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
							Uri.fromParts("mailto", address, null));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
					startActivity(Intent.createChooser(emailIntent,
							"Send email..."));
				}
			};

			iv_circle1 = (ImageView) findViewById(R.id.iv_circle1);
			bgShape1 = (GradientDrawable) iv_circle1.getBackground();

			iv_circle2 = (ImageView) findViewById(R.id.iv_circle2);
			bgShape2 = (GradientDrawable) iv_circle2.getBackground();
			iv_circle3 = (ImageView) findViewById(R.id.iv_circle3);
			bgShape3 = (GradientDrawable) iv_circle3.getBackground();

			bgShape1.setColor(getResources().getColor(R.color.activated_circle));
			bgShape2.setColor(getResources().getColor(R.color.inactive_circle));
			bgShape3.setColor(getResources().getColor(R.color.inactive_circle));

			// OnPageListener pageListener = new OnPageChangedListener();
			pager.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageScrollStateChanged(int arg0) {

				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {

				}

				@Override
				public void onPageSelected(int x) {
					Log.d(tag, "current page: " + x);
					switch (x) {
					case 0:
						bgShape1.setColor(getResources().getColor(
								R.color.activated_circle));
						bgShape2.setColor(getResources().getColor(
								R.color.inactive_circle));
						bgShape3.setColor(getResources().getColor(
								R.color.inactive_circle));

						break;
					case 1:
						bgShape2.setColor(getResources().getColor(
								R.color.activated_circle));
						bgShape3.setColor(getResources().getColor(
								R.color.inactive_circle));

						break;
					case 2:
						bgShape3.setColor(getResources().getColor(
								R.color.activated_circle));
						break;

					}

				}

			});
		}
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		fList.add(MyFragment.newInstance("Fragment 1", 1));
		fList.add(MyFragment.newInstance("Fragment 2", 2));
		fList.add(MyFragment.newInstance("Fragment 3", 3));

		return fList;
	}

	class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	public static class MyFragment extends Fragment {
		public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

		public final static MyFragment newInstance(String message,
				int screen_number) {
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(2);
			bdl.putString(EXTRA_MESSAGE, message);
			bdl.putInt("screen", screen_number);

			f.setArguments(bdl);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			String message = getArguments().getString(EXTRA_MESSAGE);
			int screen = getArguments().getInt("screen");
			View v = null;

			switch (screen) {
			case 1:
				v = inflater.inflate(R.layout.onboard_page, container, false);
				iv_ob_screenshot = (ImageView) v
						.findViewById(R.id.iv_ob_screenshot);
				iv_ob_screenshot
						.setBackgroundResource(R.drawable.screens1_android_text);

				break;
			case 2:
				v = inflater.inflate(R.layout.onboard_page, container, false);
				iv_ob_screenshot = (ImageView) v
						.findViewById(R.id.iv_ob_screenshot);
				iv_ob_screenshot
						.setBackgroundResource(R.drawable.screens2_android_text);

				break;

			case 3:
				v = inflater.inflate(R.layout.onboard_verify, container, false);

				tv_message = (TextView) v
						.findViewById(R.id.tv_ob_verify_message);
				tv_message.setTypeface(slab_font);
				tv_message.setText(R.string.verify_start);
				tv_lower_message = (TextView) v
						.findViewById(R.id.tv_ob_verify_lower_message);
				tv_lower_message.setTypeface(slab_font);

				et_phone = (EditText) v.findViewById(R.id.et_phone);
				tv_lower_message.setText(R.string.enter_phone);
				iv_ob_verify_close = (ImageButton) v
						.findViewById(R.id.iv_ob_verify_close);
				btn_verify = (Button) v.findViewById(R.id.btn_verify);
				btn_verify.setText(R.string.verify_button);
				btn_verify.setClickable(false);

				progress = (ProgressBar) v.findViewById(R.id.verify_progress);
				progress.setVisibility(View.GONE);
				progress.setIndeterminate(true);
				tv_ob_verify_full_experience = (TextView) v
						.findViewById(R.id.tv_ob_verify_full_experience);
				tv_ob_verify_full_experience.setTypeface(slab_font);

				iv_ob_verify_close.setOnClickListener(close_page_onclick);

				

				et_phone.addTextChangedListener(new TextWatcher() {

					@Override
					public void afterTextChanged(Editable s) {
						// new PhoneNumberUtils();
						// PhoneNumberUtils.formatNanpNumber(s);
						Log.d("OnBoard", "afterTextChanged called");

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						Log.d("OnBoard", "Count: " + count);

						if (s.length() > 9) {
							// btn_verify.setBackgroundColor(getResources()
							// .getColor(R.color.trans_white));
							et_phone.setTextColor(getResources().getColor(
									R.color.localism_blue));
							btn_verify.setClickable(true);
							btn_verify.setBackgroundColor(getResources()
									.getColor(R.color.localism_blue));
						} else {
							btn_verify.setBackgroundColor(getResources()
									.getColor(R.color.trans_white));
							btn_verify.setClickable(false);
							et_phone.setTextColor(getResources().getColor(
									R.color.white));
						}
					}

				});

				btn_verify.setOnClickListener(verify_onclick);

				break;
			}
			// TextView messageTextView = (TextView)
			// v.findViewById(R.id.textView);
			// messageTextView.setText(message);

			return v;
		}
	}

	private void showErrorScreen() {
		Log.d("OnBoard", "showError called");
		LinearLayout llll = (LinearLayout) findViewById(R.id.verify_views);
		llll.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.onboard_verify_error, null);
		llll.addView(view);
		address = EMAIL_NOT_FOUND;
		subject = "";

		tv_message = (TextView) findViewById(R.id.tv_ob_verify_message);
		String ms = tv_message.getText().toString().toLowerCase(Locale.US);
		int x = tv_message.getText().toString().toLowerCase(Locale.US)
				.indexOf("email us");
		Log.d(tag, "ms: " + ms);

		Log.d(tag, "x: " + x);
		if (x != -1) {
			SpannableString ss = new SpannableString(tv_message.getText()
					.toString());
			ss.setSpan(new ForegroundColorSpan(Color.RED), x, x + 8,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			SpannableStringBuilder strBuilder = new SpannableStringBuilder(ss);

			ClickableSpan myActivityLauncher = new ClickableSpan() {
				public void onClick(View view) {
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
							Uri.fromParts("mailto", address, null));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
					startActivity(Intent.createChooser(emailIntent,
							"Send email..."));
				}
			};

			// ss.setSpan(new URLSpan("tel:8052334924"), 2, 5,
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// strBuilder.
			ss.setSpan(myActivityLauncher, x, x + 8, 0);

			tv_message.setTypeface(slab_font);
			tv_message.setText(ss);
			tv_message.setClickable(true);
			tv_message.setMovementMethod(LinkMovementMethod.getInstance());

		}

		// TextView tv_email = (TextView) findViewById(R.id.tv_verify_email);
		// tv_email.setOnClickListener(email_onclick);
		// try {
		// tv_message.setText(R.string.verify_error);
		// tv_lower_message.setVisibility(View.INVISIBLE);
		// tv_ob_verify_full_experience.setVisibility(View.INVISIBLE);
		// // .setText(R.string.enter_phone);
		btn_verify.setVisibility(View.VISIBLE);
		btn_verify.setText(R.string.verify_button_retry);
		btn_verify.setOnClickListener(verify_onclick);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	private void showSuccessScreen() {
		Log.d("OnBoard", "showSuccess called");
		LinearLayout llll = (LinearLayout) findViewById(R.id.verify_views);
		llll.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.onboard_verify_success, null);
		llll.addView(view);
		// TextView tv_email = (TextView) findViewById(R.id.tv_verify_email);
		// tv_email.setOnClickListener(email_onclick);

		address = EMAIL_NO_TEXT;
		subject = "";

		tv_message = (TextView) findViewById(R.id.tv_ob_verify_lower_message);
		String ms = tv_message.getText().toString().toLowerCase(Locale.US);
		int x = tv_message.getText().toString().toLowerCase(Locale.US)
				.indexOf("email us");
		Log.d(tag, "ms: " + ms);

		Log.d(tag, "x: " + x);
		if (x != -1) {
			SpannableString ss = new SpannableString(tv_message.getText()
					.toString());
			ss.setSpan(new ForegroundColorSpan(Color.RED), x, x + 8,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			SpannableStringBuilder strBuilder = new SpannableStringBuilder(ss);

			ClickableSpan myActivityLauncher = new ClickableSpan() {
				public void onClick(View view) {
					Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
							Uri.fromParts("mailto", address, null));
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
					startActivity(Intent.createChooser(emailIntent,
							"Send email..."));
				}
			};

			// ss.setSpan(new URLSpan("tel:8052334924"), 2, 5,
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			// strBuilder.
			ss.setSpan(myActivityLauncher, x, x + 8, 0);

			tv_message.setTypeface(slab_font);
			tv_message.setText(ss);
			tv_message.setClickable(true);
			tv_message.setMovementMethod(LinkMovementMethod.getInstance());
		}

		// tv_message.setText(R.string.verify_success);
		// tv_ob_verify_full_experience.setVisibility(View.INVISIBLE);
		// et_phone.setVisibility(View.GONE);
		// tv_lower_message.setText(R.string.verify_no_text);
		btn_verify.setText(R.string.verify_button_retry);
		btn_verify.setOnClickListener(verify_onclick);
		btn_verify.setVisibility(View.VISIBLE);
	}

	private class VerifyUser extends AsyncTask<String, String, String> {
		private String returnedString = "";

		@Override
		protected String doInBackground(String... args) {
			// if (checkPhoneInParse(args[0]))
			// returnedString = "yesphone";
			// else
			// returnedString = "no";

			return checkPhoneInParse(args[0]);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// publishProgress();
			// progress = new ProgressDialog(ctx);
			progress.setIndeterminate(true);
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(String result) {
			progress.setVisibility(View.GONE);
			Log.d("OnBoard", "Result: " + result);
			if (result.equals("yesphone")) {
				Log.d("OnBoard", "success");

				showSuccessScreen();
				new Twilio().execute(db_phone, "", "");
			} else {
				Log.d("OnBoard", "fail");

				showErrorScreen();
			}
		}
	}

	private class Twilio extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... args) {
			pingTwilio(args[0]);
			return null;
		}
	}

	private String checkPhoneInParse(final String phone) {
		Log.d(tag, "checkPhoneInParse called ");
		Log.d(tag, "phone = " + phone);
		db_phone = "-1";
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereMatches("username", phone);
		userQuery.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> List, ParseException e) {
				try {
					if (e == null) {
						Log.d(tag, "Retrieved " + List.size()
								+ " phone numbers");

						if (List.size() > 0) {
							Log.d(tag,
									"username "
											+ List.get(0).getString("username"));
							db_phone = List.get(0).getString("username");
						} else
							db_phone = "error";
					} else {
						db_phone = "error";
						Log.d(tag, "Error: " + e.getMessage());
					}
				} catch (Exception ep) {
					Log.e(tag, ep.toString());
					db_phone = "error";
				}
			}
		});

		while (db_phone.equals("-1")) {
			// if(db_phone.equals("-1"))
			// break;
		}
		if (db_phone.equals(phone)) {
			// pingTwilio(phone);
			// showSuccessScreen();
			return "yesphone";
		} else {
			// showErrorScreen();
		}
		return "no";
	}

	private void pingTwilio(String phone) {
		// String url = "http://losal.parseapp.com/registration.html?phone=";
		// url += phone;
		String url = "https://api.parse.com/1/functions/register";
		Log.i(tag, "twilio URL: " + url);
		savePhoneNumberToPhone(phone);
		try {
			HttpPost httppost = new HttpPost(url);
			HttpClient client = new DefaultHttpClient();
			httppost.setHeader("X-Parse-Application-Id",
					"zFi294oXTVT6vj6Tfed5heeF6XPmutl0y1Rf7syg");
			httppost.setHeader("X-Parse-REST-API-Key",
					"gyMlPJBhaRG0SV083c3n7ApzsjLnvvbLvXKW0jJm");
			httppost.setHeader("Content-Type",
					"application/x-www-form-urlencoded");

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("phone", phone));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			// is = entity.getContent();
		} catch (ClientProtocolException e) {
			Log.i(tag, "twilio error" + e.toString());

			e.printStackTrace();
		} catch (IOException e) {
			Log.i(tag, "twilio error" + e.toString());

			e.printStackTrace();
		}

	}

	private void savePhoneNumberToPhone(String phone) {
		SharedPreferences user_info = getBaseContext().getSharedPreferences(
				"UserInfo", MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = user_info.edit();
		prefEditor.putString("phone_number", phone);
		prefEditor.commit();
	}

	private void showNoNetworkConnection() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setMessage(
				"Something is wrong with your network connection. \n\nCheck your network connection and try again!")
				.setTitle("Oops ={");

		builder.setPositiveButton("Okay",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private boolean hasNetworkConnection() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();// ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}