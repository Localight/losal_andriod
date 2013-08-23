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
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import co.localism.losal.FetchFeed;
import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.R.layout;
import co.localism.losal.R.menu;
import co.localism.losal.adapters.PostAdapter;

import co.localism.losal.listens.PersonalOptionsOnClickListeners;
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

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements Observer {

	public Context ctx = this;
	public static ListAdapter listadapter;
	public static ArrayList<Post> posts = new ArrayList<Post>();
	private static final String tag = "MainActivity";
	public static final String KEY_UPDATE = "co.localism.losal.MainActivity.updateView";
	public BroadcastReceiver receiver = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		SlidingMenu sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_CONTENT);
		new PersonalOptionsOnClickListeners(
				(LinearLayout) findViewById(R.id.po), this);
		ActionBar a = getActionBar();
//		a.setIcon(new SVGHandler().svg_to_drawable(ctx, R.raw.left_chevron));
		a.setDisplayHomeAsUpEnabled(true);

		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
		ParseTwitterUtils.initialize(
				getResources().getString(R.string.tw_consumer_key),
				getResources().getString(R.string.tw_consumer_secret));
		ParseAnalytics.trackAppOpened(getIntent());
		loginParseUser();

		// ParseFacebookUtils.initialize(getResources().getString(R.string.fb_app_id));
		getPosts();
		// createTestparseUser();
		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		// SharedPreferences.Editor prefEditor = user_info.edit();
		// prefEditor.putBoolean("registered", true);
		// prefEditor.putString("user_type", "student");
		// prefEditor.commit();

		// dummy data
		// posts.add(new Post("Mary H", 1));
		// posts.add(new Post("Joe C", 2));
		// posts.add(new Post("Josh A", 4));

		ListView lv = getListView();
		boolean pauseOnScroll = true; // or true
		boolean pauseOnFling = true; // or false
		PauseOnScrollListener listener = new PauseOnScrollListener(
				PostAdapter.mImageLoader, pauseOnScroll, pauseOnFling);
		lv.setOnScrollListener(listener);
	}

	/**
	 * This checks whether we need to pull data from parse or if we can just
	 * create our posts object from our serialized file.
	 */
	private void getPosts() {
		// check current time against the time that we serialized last.
		SharedPreferences dm = getSharedPreferences("DataManager", MODE_PRIVATE);
		// TODO: Time isn't the best option for this. While the app is running
		// we should check parse every 5 min
		// The school can manually push new data whenever they want.
		// Also on start up we should check parse but maybe load serialized data
		// first and then replace that or
		// add onto it when a new payload is ready
		final Calendar c = Calendar.getInstance();
		if (c.get(Calendar.MONTH) == dm.getInt("LastUpdateMonth", -1)) {
			if (c.get(Calendar.DAY_OF_MONTH) == dm.getInt("LastUpdateDay", -1))
				if ((c.get(Calendar.HOUR_OF_DAY) < 7 && dm.getInt(
						"LastUpdateDay", -1) < 7)
						|| (c.get(Calendar.HOUR_OF_DAY) < 19 && dm.getInt(
								"LastUpdateDay", -1) < 19)) {
					Log.d(tag, "chose to deserialize the data");
					posts = (ArrayList<Post>) fromFile("posts.ser");
				}
		} else {
			Log.d(tag, "chose to fetch new data");

			try {
				FetchFeed ff = new FetchFeed();
				ff.addObserver(this);
				posts = ff.fetch();
			} catch (Exception e) {
				Log.d(tag, e.toString());
			}
		}
		listadapter = new PostAdapter(ctx, R.layout.post, posts, 1);
		updateView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.findItem(R.id.notices).setIcon(
				new SVGHandler().svg_to_drawable(ctx, R.raw.lightning));
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.notices) {
			
			return true;
		}
		return false;
	}

	public void updateView() {
		Log.d(tag, "updateView called");
		setListAdapter(listadapter);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Log.d(tag, "update called");
//		toFile("posts.ser", posts);
		updateView();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}

	private void loginParseUser() {
		ParseUser.logInInBackground("joe", "1234", new LogInCallback() {
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					// Hooray! The user is logged in.
				} else {
					// Signup failed. Look at the ParseException to see what
					// happened.
				}
			}
		});
	}

	private void createTestparseUser() {
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
	
	
	

}
