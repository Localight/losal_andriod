package co.localism.losal.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import co.localism.losal.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class SplashScreenActivity extends Activity {

	private String tag = "SplashScreenActivity";
	private int post_days = -1;
	private Context ctx = this;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);
		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
//		ActivateSocialActivity.unlinkTwitterUser();
//		TODO:REMOVE. DUMMY DATA
		SharedPreferences.Editor prefEditor = user_info.edit();
		prefEditor.putBoolean("isFirstVisit", true);

		prefEditor.putBoolean("hasInstagram", true);
		prefEditor.putBoolean("hasTwitter", true);

		prefEditor.commit();
		
			new PrefetchData().execute();
		
	
		
		
	}

	private class PrefetchData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// before making http calls

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			Log.d(tag, "doInBack called ");

			if (getAppSettings())
				return null;

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.d(tag, "onPostExec called ");
			// After completing http call
			// will close this activity and lauch main activity

			// close this activity
			// finish();
		}

	}

	public boolean getAppSettings() {
		Log.d(tag, "getAppSettings called ");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("AppSettings");

		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> List, ParseException e) {
				if (e == null) {
					Log.d(tag, "Retrieved " + List.size() + " app settings");
					// MainActivity.
					post_days = List.get(0).getInt("queryIntervalDays");
					ParseFile f = List.get(0).getParseFile("backgroundImage");
					try {
						saveToPhone(f.getData());
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				} else {
					Log.d(tag, "Error: " + e.getMessage());
				}
				Intent i = new Intent(SplashScreenActivity.this,
						MainActivity.class);
				i.putExtra("POST_DAYS", post_days);
				SharedPreferences user_info = getSharedPreferences("UserInfo",
						MODE_PRIVATE);
				if(user_info.getBoolean("isFirstVisit", true))
					startActivity(new Intent(ctx, OnBoardSequenceActivity.class));
				else
					startActivity(i);
			}
		});
		return true;
	}

	
	
	
	 protected String saveToPhone(byte[]... jpeg) {
	      File photo=new File(Environment.getExternalStorageDirectory(), "losal_bg.jpg");

	      if (photo.exists()) {
	            photo.delete();
	      }

	      try {
	        FileOutputStream fos=new FileOutputStream(photo.getPath());

	        fos.write(jpeg[0]);
	        fos.close();
            Log.d(tag, "Saved Image!");

	      }
	      catch (java.io.IOException e) {
	        Log.e(tag, "Exception in photoCallback", e);
            Log.d(tag, "Failed to Save Image...");

	      }

	      return(null);
	    }
	
	

}
