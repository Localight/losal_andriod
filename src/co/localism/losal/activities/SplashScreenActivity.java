package co.localism.losal.activities;

import java.util.List;

import org.json.JSONObject;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import co.localism.losal.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends Activity {

	private String tag = "SplashScreenActivity";
	private int post_days = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);
		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
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

	        	if(getAppSettings())
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
//	            finish();
	        }
	 
	    }
	
	 
	 public boolean getAppSettings(){
			Log.d(tag, "getAppSettings called ");
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("AppSettings");
			

			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> List, ParseException e) {
					if (e == null) {
						Log.d(tag, "Retrieved " + List.size() + " app settings");
//						MainActivity.
						post_days = List.get(0).getInt("queryIntervalDays");
						List.get(0).getParseFile("backgroundImage");	
					} else {
						Log.d(tag, "Error: " + e.getMessage());
					}
					 Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
			            i.putExtra("POST_DAYS", post_days);
			            i.putExtra("earned", 3);
			            startActivity(i);
				}
			});
			return true;
		}
	 
	
}
