package co.localism.losal.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import co.localism.losal.FetchFeed;
import co.localism.losal.R;
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
import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity implements Observer{

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
		getActionBar();

		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
		ParseTwitterUtils.initialize(getResources().getString(R.string.tw_consumer_key), getResources().getString(R.string.tw_consumer_secret));
		ParseAnalytics.trackAppOpened(getIntent());
		loginParseUser();

		// ParseFacebookUtils.initialize(getResources().getString(R.string.fb_app_id));
		try {
			FetchFeed ff = new FetchFeed();
			ff.addObserver(this);
			posts = ff.fetch();
			listadapter = new PostAdapter(ctx, R.layout.post, posts, 1);
			updateView();
		} catch (Exception e) {
			Log.d(tag, e.toString());
		}
//		createTestparseUser();
		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		// SharedPreferences.Editor prefEditor = user_info.edit();
		// prefEditor.putBoolean("registered", true);
		// prefEditor.putString("user_type", "student");
		// prefEditor.commit();

		Button btn = (Button) findViewById(R.id.btn_social);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ctx, ActivateSocialActivity.class);
				startActivity(i);
			}
		});
//			dummy data
		// posts.add(new Post("Mary H", 1));
		// posts.add(new Post("Joe C", 2));
		// posts.add(new Post("Josh A", 4));

		ListView lv = getListView();
		boolean pauseOnScroll = false; // or true
		boolean pauseOnFling = true; // or false
		PauseOnScrollListener listener = new PauseOnScrollListener(PostAdapter.mImageLoader, pauseOnScroll, pauseOnFling);
		lv.setOnScrollListener(listener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void updateView() {
		Log.d(tag, "updateView called");
		setListAdapter(listadapter);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Log.d(tag, "update called");
		updateView();
	}
	@Override
	public void onResume(){
		super.onResume();
		updateView();
	}
	
	
	
	private void loginParseUser(){
	ParseUser.logInInBackground("joe", "1234", new LogInCallback() {
		  public void done(ParseUser user, ParseException e) {
		    if (user != null) {
		      // Hooray! The user is logged in.
		    } else {
		      // Signup failed. Look at the ParseException to see what happened.
		    }
		  }
		});
}
	private void createTestparseUser(){
		ParseUser user = new ParseUser();
		user.setUsername("joe");
		user.setPassword("1234");
		user.setEmail("joeczubiak@gmail.com");
		 
		// other fields can be set just like with ParseObject
//		user.put("phone", "650-253-0000");
		 
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
	
	
	
	
}
