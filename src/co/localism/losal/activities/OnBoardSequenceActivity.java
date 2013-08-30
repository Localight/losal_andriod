package co.localism.losal.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OnBoardSequenceActivity extends FragmentActivity {

	private MyPageAdapter pageAdapter;
	private Context ctx = this;
	private String tag = "OnBoardSequence";
	private String db_phone = "-1";
	private static OnClickListener verify_onclick, close_page_onclick;
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

	/*** Circles ***/
	private static ImageView iv_circle1, iv_circle2, iv_circle3;
	private static GradientDrawable bgShape1, bgShape2, bgShape3;

	// private View screen1, screen2, screen3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onboardsequence);
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);
		getActionBar().hide();
		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
		//
		// SharedPreferences user_info =
		// getBaseContext().getSharedPreferences("UserInfo",
		// MODE_PRIVATE);
		// SharedPreferences.Editor prefEditor = user_info.edit();
		// prefEditor.putBoolean("isFirstVisit", false);
		// prefEditor.commit();
		//
		verify_onclick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btn_verify = (Button) findViewById(R.id.btn_verify);
				btn_verify.setText(R.string.verify_button_retry);
				btn_verify.setVisibility(View.GONE);
				progress.setVisibility(View.VISIBLE);
				final EditText et_phone = (EditText) findViewById(R.id.et_phone);

				new VerifyUser().execute(et_phone.getText().toString(), "", "");
			}

		};

		close_page_onclick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent i = new Intent(ctx, MainActivity.class);
				startActivity(i);
			}

		};

		iv_circle1 = (ImageView) findViewById(R.id.iv_circle1);
		bgShape1 = (GradientDrawable) iv_circle1.getBackground();

		iv_circle2 = (ImageView) findViewById(R.id.iv_circle2);
		bgShape2 = (GradientDrawable) iv_circle2.getBackground();
		iv_circle3 = (ImageView) findViewById(R.id.iv_circle3);
		bgShape3 = (GradientDrawable) iv_circle3.getBackground();

		bgShape1.setColor(getResources().getColor(
				R.color.activated_circle));
		bgShape2.setColor(getResources().getColor(
				R.color.inactive_circle));
		bgShape3.setColor(getResources().getColor(
				R.color.inactive_circle));
		
		// OnPageListener pageListener = new OnPageChangedListener();
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

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
				tv_title = (TextView) v.findViewById(R.id.ob_title);
				tv_subtitle_1 = (TextView) v.findViewById(R.id.ob_subtitle);
				tv_subtitle_2 = (TextView) v.findViewById(R.id.ob_subtitle_2);
				iv_ob_screenshot = (ImageView) v
						.findViewById(R.id.iv_ob_screenshot);

				// tv_title.setText(R.string.ob_1_title_1);
				// tv_subtitle_1.setText(R.string.ob_1_subtitle_1);
				// tv_subtitle_2.setText(R.string.ob_1_subtitle_2);

				tv_title.setText("");
				tv_subtitle_1.setText("");
				tv_subtitle_2.setText("");

				iv_ob_screenshot
						.setBackgroundResource(R.drawable.screens1_android_text);

				break;
			case 2:
				v = inflater.inflate(R.layout.onboard_page, container, false);
				tv_title = (TextView) v.findViewById(R.id.ob_title);
				tv_subtitle_1 = (TextView) v.findViewById(R.id.ob_subtitle);
				tv_subtitle_2 = (TextView) v.findViewById(R.id.ob_subtitle_2);
				iv_ob_screenshot = (ImageView) v
						.findViewById(R.id.iv_ob_screenshot);

				// tv_title.setText(R.string.ob_2_title_1);
				// tv_subtitle_1.setText(R.string.ob_2_subtitle_1);
				// tv_subtitle_2.setText(R.string.ob_2_subtitle_2);

				tv_title.setText("");
				tv_subtitle_1.setText("");
				tv_subtitle_2.setText("");

				iv_ob_screenshot
						.setBackgroundResource(R.drawable.screens2_android_text);

				break;

			case 3:
				v = inflater.inflate(R.layout.onboard_verify, container, false);
				tv_message = (TextView) v
						.findViewById(R.id.tv_ob_verify_message);
				tv_message.setText(R.string.verify_start);
				tv_lower_message = (TextView) v
						.findViewById(R.id.tv_ob_verify_lower_message);
				et_phone = (EditText) v.findViewById(R.id.et_phone);
				tv_lower_message.setText(R.string.enter_phone);
				iv_ob_verify_close = (ImageButton) v
						.findViewById(R.id.iv_ob_verify_close);
				btn_verify = (Button) v.findViewById(R.id.btn_verify);
				btn_verify.setText(R.string.verify_button);

				progress = (ProgressBar) v.findViewById(R.id.verify_progress);
				progress.setVisibility(View.GONE);
				progress.setIndeterminate(true);
				tv_ob_verify_full_experience = (TextView) v
						.findViewById(R.id.tv_ob_verify_full_experience);
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
						} else
							et_phone.setTextColor(getResources().getColor(
									R.color.white));
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

		try {
			tv_message.setText(R.string.verify_error);
			tv_lower_message.setVisibility(View.INVISIBLE);
			tv_ob_verify_full_experience.setVisibility(View.INVISIBLE);
			// .setText(R.string.enter_phone);
			btn_verify.setVisibility(View.VISIBLE);
			btn_verify.setText(R.string.verify_button_retry);
			btn_verify.setOnClickListener(verify_onclick);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showSuccessScreen() {
		Log.d("OnBoard", "showSuccess called");

		// btn_verify = (Button) findViewById(R.id.btn_verify);

		tv_message.setText(R.string.verify_success);
		tv_ob_verify_full_experience.setVisibility(View.INVISIBLE);
		et_phone.setVisibility(View.GONE);
		tv_lower_message.setText(R.string.verify_no_text);
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

		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		userQuery.whereMatches("username", phone);
		userQuery.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> List, ParseException e) {
				try {
					if (e == null) {
						Log.d(tag, "Retrieved " + List.size()
								+ " phone numbers");
						Log.d(tag,
								"username " + List.get(0).getString("username"));

						if (List.size() > 0)
							db_phone = List.get(0).getString("username");
						else
							db_phone = "error";
					} else {
						db_phone = "errpr";
						Log.d(tag, "Error: " + e.getMessage());
					}
				} catch (Exception ep) {
					Log.e(tag, ep.toString());
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
		String url = "http://losal.parseapp.com/registration.html?phone=";
		url += phone;
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			// is = entity.getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}