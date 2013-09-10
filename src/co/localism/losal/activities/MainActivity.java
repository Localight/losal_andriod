package co.localism.losal.activities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import co.localism.losal.FetchFeed;
import co.localism.losal.FetchNotices;
import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.R.layout;
import co.localism.losal.R.menu;
import co.localism.losal.adapters.NoticeAdapter;
import co.localism.losal.adapters.PostAdapter;
import co.localism.losal.async.InstagramRequests;

import co.localism.losal.listens.PersonalOptionsOnClickListeners;
import co.localism.losal.objects.Notice;
import co.localism.losal.objects.Post;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	public Context ctx = this;
	public static PostAdapter listadapter;
	public static ArrayList<Post> posts = new ArrayList<Post>();
	public static ArrayList<Notice> notices = new ArrayList<Notice>();

	private static final String tag = "MainActivity";
	public static final String KEY_UPDATE = "co.localism.losal.MainActivity.updateView";
	public BroadcastReceiver receiver = null;
	public static int POST_DAYS = 7;
	public static Date LAST_POST_DATE = null;
	private boolean loadingMore = false;
	private static FetchFeed ff;
	private SlidingMenu sm;
	private boolean isFiltered = false;
	private boolean isInitialized = false;
	// private HashMap<String, ArrayList<String>> hashtags;
	private HashSet<String> hashtags = new HashSet<String>();
	/**** Ad URL for top of right panel ****/
	public static String AD_URL = "";
	public static ImageView iv_ad;
	public static String AD_CLICK_URL = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar a = getActionBar();
		a.setDisplayHomeAsUpEnabled(true);
		a.setDisplayShowTitleEnabled(true);
		a.setDisplayUseLogoEnabled(false);
		a.setTitle("");
		a.setCustomView(R.layout.actionbar_custome_view);
		TextView title = (TextView) a.getCustomView().findViewById(
				R.id.ab_title);
		title.setText("#LOSAL");
		setContentView(R.layout.activity_main);
		try {
			Parse.initialize(this,
					getResources().getString(R.string.parse_app_id),
					getResources().getString(R.string.parse_client_key));
			ParseTwitterUtils.initialize(
					getResources().getString(R.string.tw_consumer_key),
					getResources().getString(R.string.tw_consumer_secret));
			ParseAnalytics.trackAppOpened(getIntent());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Uri data = getIntent().getData();

		if (data != null) {
			Log.e(tag, "data:" + data.toString());
			pairUserToApp(data.toString());
		}

		/******** dummy data **********/
		// SharedPreferences user_info = getSharedPreferences("UserInfo",
		// MODE_PRIVATE);
		// SharedPreferences.Editor prefEditor = user_info.edit();
		// prefEditor.putBoolean("registered", true);
		// prefEditor.putString("user_type", "student");
		// prefEditor.putString("user_name", "Joe");
		// prefEditor.putString("user_icon", "E07E");
		// prefEditor.putString("fav_color", "#FF3366");
		// prefEditor.commit();
		try {
			// LinearLayout ll_main = (LinearLayout) findViewById(R.id.ll_main);
			FrameLayout ll_main = (FrameLayout) findViewById(R.id.main);
			Bitmap bmImg = (BitmapFactory.decodeFile(Environment
					.getExternalStorageDirectory() + "/losal_bg.jpg"));
			Drawable d = new BitmapDrawable(getResources(), bmImg);
			Log.d(tag, "used image as background sucessfully!");
			ll_main.setBackgroundDrawable(d);// .setBackground(d);
		} catch (Exception e) {
			Log.e(tag, "failed to use image as background. e: " + e.toString());
		}
		sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_WINDOW);// .SLIDING_CONTENT);
		new PersonalOptionsOnClickListeners(
				(LinearLayout) findViewById(R.id.po), this,
				PersonalOptionsOnClickListeners.ACTIVITY_MAIN);
		LinearLayout ll_notices = (LinearLayout) findViewById(R.id.ll_notices);
		iv_ad = (ImageView) ll_notices.findViewById(R.id.iv_notices_ad);
		iv_ad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (AD_CLICK_URL.length() > 0) {
					try {
						Intent openURL = new Intent(Intent.ACTION_VIEW, Uri
								.parse(AD_CLICK_URL));
						startActivity(openURL);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		Log.i(tag, iv_ad.toString());
		LinearLayout l = (LinearLayout) findViewById(R.id.po);
		l.findViewById(R.id.po_social_feed).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						sm.toggle();
						refresh();
					}
				});
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showHashtags();
			}
		});
		Drawable d = new ColorDrawable(R.color.transparent);
		a.setIcon(d);
		a.setDisplayShowCustomEnabled(true);
		ff = new FetchFeed();
		ListView lv = getListView();
		boolean pauseOnScroll = true; // or true
		boolean pauseOnFling = true; // or false
		PauseOnScrollListener listener = new PauseOnScrollListener(
				PostAdapter.mImageLoader, pauseOnScroll, pauseOnFling);
		listadapter = new PostAdapter(ctx, R.layout.post, posts, 1);
		setListAdapter(listadapter);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			POST_DAYS = extras.getInt("POST_DAYS", POST_DAYS);
		}
		if (hasNetworkConnection()) {
			getPosts();
			getNotices();
			new FetchHashtags().execute();
		} else {
			showNoNetworkConnection();
		}
		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = user_info.edit();
		prefEditor.putBoolean("isFirstVisit", false);
		prefEditor.commit();
	}

	private boolean hasNetworkConnection() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();// ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		return false;
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

	private void showPairingError() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

		builder.setMessage(
				"Something is wrong with your network connection.\n\n"
						+ "Unfortunately this is a crutial step and requires an internet connection. "
						+ "Make sure you are connected and reenter your phone number to start the process again."
						+ " \n\nThe link you were sent previously, has expired and will no longer work correctly. Wait for the new link to arrive.")
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

	private void pairUserToApp(String data) {
		if (hasNetworkConnection()) {
			// they should have a connection because they came from the browser.
			// nut just in case they go through a tunnel...we fail safe.
			SharedPreferences user_info = getBaseContext()
					.getSharedPreferences("UserInfo", MODE_PRIVATE);
			String phone = user_info.getString("phone_number", "");
			String user_password = data.substring(data.indexOf('/') + 2);
			loginParseUser(phone, user_password);
			Log.e(tag, "user data= " + phone + " :  " + user_password);
			loginParseUser(phone, user_password);
		} else {
			showPairingError();
			SharedPreferences user_info = getSharedPreferences("UserInfo",
					MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = user_info.edit();
			// so the user can retry when they open the app again.
			prefEditor.putBoolean("isFirstVisit", true);
			prefEditor.commit();
		}

	}

	private void loginParseUser(String username, String pass) {
		// Log.i(tag, ParseUser.getCurrentUser().toString());
		try {
			ParseUser.logInInBackground(username, pass, new LogInCallback() {
				public void done(ParseUser user, ParseException e) {
					if (user != null) {
						Log.i(tag, "Hooray! The user is logged in.");
						// Hooray! The user is logged in.
						Log.d(tag, "");
						String fname = user.getString("firstName");
						String lname = user.getString("lastName");
						String userType = user.getString("userType");
						String year = user.getString("year");
						String faveColor = user.getString("faveColor");
						String icon = user.getString("icon");
						String userID = user.getString("objectId");
						saveUserDataToPhone(userID, userType, fname, lname,
								icon, faveColor, year);
					} else {
						Log.i(tag, "login failed. e: " + e.toString());
						Toast.makeText(ctx,
								"Pairing was unsuccessful. Please try again.",
								Toast.LENGTH_SHORT).show();
						// Signup failed. Look at the ParseException to see what
						// happened.
					}
				}
			});
		} catch (Exception e) {
			Log.e(tag, e.toString());

		}
	}

	private void saveUserDataToPhone(String userID, String userType,
			String fName, String lName, String icon, String favColor,
			String year) {
		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = user_info.edit();
		prefEditor.putString("user_id", userID);
		prefEditor.putBoolean("registered", true);
		prefEditor.putString("user_type", userType);
		prefEditor.putString("first_name", fName);
		prefEditor.putString("last_name", lName);
		prefEditor.putString("user_icon", icon);
		prefEditor.putString("fav_color", favColor);
		prefEditor.putString("year", year);

		String user_name = fName + " " + lName.substring(0, 1) + ".";
		prefEditor.putString("user_name", user_name);
		prefEditor.commit();
		/***** Update Personal Options pane ******/
		((SetUpSlidingMenu) sm).configActionBar();
	}

	private void getNotices() {
		FetchNotices fn = new FetchNotices();
		// fn.addObserver(this);
		if (notices.size() > 0)
			notices.removeAll(notices);
		ListView noticeList = (ListView) findViewById(R.id.notices_list);
		final NoticeAdapter noticeadapter = new NoticeAdapter(this,
				R.layout.notice_list_item, notices, 1);
		fn.fetch(noticeadapter);
		noticeList.setAdapter(noticeadapter);

		noticeList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ctx, NoticeDetailsActivity.class);
				// intent.putExtra("imageURL", cur.getUrl());
				intent.putExtra("title", noticeadapter.getItem(position)
						.getTitle());
				intent.putExtra("details_text", noticeadapter.getItem(position)
						.getDetails());
				intent.putExtra("image_url", noticeadapter.getItem(position)
						.getImageUrl());
				intent.putExtra("link_url", noticeadapter.getItem(position)
						.getButtonLink());
				intent.putExtra("button_text", noticeadapter.getItem(position)
						.getButtonText());
				ctx.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_from_right,
						R.anim.slide_out_to_left);
			}
		});
	}

	private void refresh() {
		if (hasNetworkConnection()) {
			resetFilter();
			AD_URL = "";
			AD_CLICK_URL = "";
			ff.refresh(listadapter);
			getNotices();
			if (hashtags == null || hashtags.size() < 1)
				new FetchHashtags().execute();
			Toast.makeText(this, "Refreshing feed", Toast.LENGTH_SHORT).show();
		} else
			Toast.makeText(this, "No network connection...", Toast.LENGTH_SHORT)
					.show();
		// showNoNetworkConnection();
	}

	private ArrayList<Post> newposts = new ArrayList<Post>();

	/**
	 * This checks whether we need to pull data from parse or if we can just
	 * create our posts object from our serialized file.
	 */
	public void getPosts() {

		// check current time against the time that we serialized last.
		// SharedPreferences dm = getSharedPreferences("DataManager",
		// MODE_PRIVATE);
		// TODO: Time isn't the best option for this. While the app is running
		// we should check parse every 5 min
		// The school can manually push new data whenever they want.
		// Also on start up we should check parse but maybe load serialized data
		// first and then replace that or
		// add onto it when a new payload is ready
		// final Calendar c = Calendar.getInstance();
		// if (c.get(Calendar.MONTH) == dm.getInt("LastUpdateMonth", -1)) {
		// if (c.get(Calendar.DAY_OF_MONTH) == dm.getInt("LastUpdateDay", -1))
		// if ((c.get(Calendar.HOUR_OF_DAY) < 7 && dm.getInt(
		// "LastUpdateDay", -1) < 7)
		// || (c.get(Calendar.HOUR_OF_DAY) < 19 && dm.getInt(
		// "LastUpdateDay", -1) < 19)) {
		// Log.d(tag, "chose to deserialize the data");
		// posts = (ArrayList<Post>) fromFile("posts.ser");
		// }
		// } else {
		Log.d(tag, "chose to fetch new data");

		try {
			ff.fetch(listadapter, this);
			// posts = ff.fetch();
			// posts =
			// return ff.fetch();
			// posts.addAll(ff.fetch());

			// newposts = ff.fetch();
			// posts.addAll(newposts);
		} catch (Exception e) {
			Log.d(tag, e.toString());
		}
		// }
		if (listadapter == null) {
			// posts = ff.fetch();
			// listadapter = new PostAdapter(ctx, R.layout.post, posts, 1);
			// listadapter.add(new Post());
			// Post aa = (Post) listadapter.getItem(2);
			updateView();
		} else {
			updateView();

			// newposts = ff.fetch();
		}
		// return posts;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			sm.showMenu();
			break;
		case R.id.hashtag:
			showHashtags();
			break;
		case R.id.notices:
			sm.showSecondaryMenu();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateView() {
		Log.d(tag, "updateView called");
		loadingMore = false;
	}

	// @Override
	public void update(Observable arg0, Object arg1) {
		Log.d(tag, "update called");
		// toFile("posts.ser", posts);
		updateView();
	}

	@Override
	public void onResume() {
		super.onResume();
		// updateView();
	}

	private void createTestparseUser() {
		try {
			ParseUser user = new ParseUser();
			user.setUsername("joe");
			user.setPassword("1234");
			user.setEmail("joeczubiak@gmail.com");

			// other fields can be set just like with ParseObject
			// user.put("phone", "650-253-0000");

			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						// Hooray! Let them use the app now.
					} else {
						// Sign up didn't succeed. Look at the ParseException
						// to figure out what went wrong
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toFile(String file_name, Object o) {
		try {
			// use buffering
			OutputStream file = openFileOutput(file_name, Context.MODE_PRIVATE);
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			try {
				output.writeObject(serializeObject(o));
			} finally {
				output.close();
				final Calendar c = Calendar.getInstance();
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				int hour = c.get(Calendar.HOUR_OF_DAY);

				SharedPreferences sp = getSharedPreferences("DataManager",
						MODE_PRIVATE);
				// int luMonth = sp.getInt("LastUpdateMonth", -1);
				// int luDay = sp.getInt("LastUpdateDay", -1);
				SharedPreferences.Editor prefEditor = sp.edit();
				prefEditor.putInt("LastUpdateMonth", month);
				prefEditor.putInt("LastUpdateDay", day);
				prefEditor.putInt("LastUpdateHour", hour);
				// prefEditor.putInt("LastUpdateMinute", minute);//do we need
				// minute?

				prefEditor.commit();
			}
		} catch (IOException ex) {
			Log.e("toFile", "Cannot perform output." + ex.toString());
		}
	}

	public Object fromFile(String file_name) {
		try {
			// use buffering
			InputStream file = openFileInput(file_name);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			try {
				// deserialize the List
				return deserializeObject((byte[]) input.readObject());
			} finally {
				input.close();
			}
		} catch (ClassNotFoundException ex) {
			Log.e("", "Cannot perform input. Class not found." + ex.toString());
		} catch (IOException ex) {
			Log.e("", "Cannot perform input" + ex.toString());
		}
		return null;
	}

	// @SuppressLint("ParserError")
	public static byte[] serializeObject(Object o) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(o);
			out.close();

			// Get the bytes of the serialized object
			byte[] buf = bos.toByteArray();

			return buf;
		} catch (IOException ioe) {
			Log.e("serializeObject", "error", ioe);

			return null;
		}
	}

	public static Object deserializeObject(byte[] b) {
		try {
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(b));
			Object object = in.readObject();
			in.close();

			return object;
		} catch (ClassNotFoundException cnfe) {
			Log.e("deserializeObject", "class not found error", cnfe);

			return null;
		} catch (IOException ioe) {
			Log.e("deserializeObject", "io error", ioe);

			return null;
		}
	}

	public void showHashtags() {
		if (hashtags != null && hashtags.size() > 0) {
			final CharSequence[] mHashtags = getHashtagCharSequence();
			if (mHashtags != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setItems(mHashtags,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								Toast.makeText(ctx,
										mHashtags[which].toString(),
										Toast.LENGTH_SHORT).show();
								if (which == 0)// #losal was selected
									resetFilter();
								else {
									setActionBarTitle(mHashtags[which]
											.toString());
									listadapter.removeFilter();
									listadapter.Filter(
											mHashtags[which].toString(),
											hashtags);
									isFiltered = true;
								}
								dialog.dismiss();
							}
						});
				builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK
								&& event.getAction() == KeyEvent.ACTION_UP
								&& !event.isCanceled()) {
							dialog.cancel();
							return true;
						}
						return false;
					}
				});
				AlertDialog dialog = builder.create();

				dialog.setTitle("Sort by... ");
				dialog.show();
			}
		}
	}

	private CharSequence[] getHashtagCharSequence() {
		int count = 0;
		CharSequence[] tags = null;
		if (!hashtags.isEmpty()) {
			Object[] keyset = hashtags.toArray();// .keySet().toArray();
			// if the hashtag hashset does not contain #losal then we need to
			// make the keyset bigger by one
			if (hashtags.contains("#losal"))
				tags = new CharSequence[keyset.length];
			else {
				tags = new CharSequence[keyset.length + 1];
				tags[0] = "#losal";
			}
			// start at 1 because #losal is at index 0
			int next = 1;
			for (int i = 0; i < tags.length; i++) {
				if (keyset[i].toString().equalsIgnoreCase("#losal")) {
					tags[0] = keyset[i].toString();
				} else {
					tags[next] = keyset[i].toString();
					next++;
				}
			}
		}
		return tags;

	}

	private void resetFilter() {
		if (isFiltered) {
			isFiltered = false;
			listadapter.removeFilter();
			setActionBarTitle("#LOSAL");
		}
	}

	@Override
	public void onBackPressed() {
		if (sm.isSecondaryMenuShowing())
			sm.toggle();
		else if (isFiltered) {
			// undo filtering
			resetFilter();
		} else
			super.onBackPressed();

	}

	private void setActionBarTitle(String s) {
		TextView title = (TextView) getActionBar().getCustomView()
				.findViewById(R.id.ab_title);
		// if(s.length() > 14)
		// s=s.substring(0, 14);
		title.setText(s);
	}

	private class FetchHashtags extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// final HashMap<String, ArrayList<ParseObject>> hm = new
			// HashMap<String, ArrayList<ParseObject>>();
			final HashSet<String> hs = new HashSet<String>();

			Log.d(tag, "FetchHashtags called ");

			ParseQuery<ParseObject> query = ParseQuery
					.getQuery("HashTagsIndex");
			// query.include("postId");
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> List, ParseException e) {
					Log.d(tag, "Retrieved " + List.size() + " hashtags");
					for (int i = 0; i < List.size(); i++) {
						if (e == null) {

							try {
								String hashtag = List.get(i).getString(
										"hashTags");
								Log.d(tag, "hashtag " + hashtag);

								// String postID =
								// List.get(i).getParseObject("postId").getObjectId();//.getString("objectId");
								// Log.d(tag, "postID " + postID);
								// ParseObject po =
								// List.get(i).getParseObject("postId");
								if (hs.contains(hashtag)) {

									// hm.get(hashtag).add(po);
									// hm.get(hashtag).add(postID);
								} else {
									hs.add(hashtag);
									// ArrayList<String> value = new
									// ArrayList<String>();
									// value.add(postID);
									// hm.put(hashtag, value);
								}
							} catch (Exception ex) {
								Log.e(tag, ex.toString());
							}
						} else {
							Log.d(tag, "Error: " + e.getMessage());
						}
					}
				}
			});
			hashtags = hs;
			Log.d(tag, "keyset: " + hashtags.toArray().toString());
			// .keySet()
			return null;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		Log.i(tag, "onNewIntent called : ");
	}
}
