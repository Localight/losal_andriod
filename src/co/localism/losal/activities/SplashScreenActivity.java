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
import android.content.Intent;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_screen);
		Parse.initialize(this, getResources().getString(R.string.parse_app_id),
				getResources().getString(R.string.parse_client_key));
		// startActivity(new Intent(this, OnBoardSequenceActivity.class));
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
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					Log.d(tag, "Error: " + e.getMessage());
				}
				Intent i = new Intent(SplashScreenActivity.this,
						MainActivity.class);
				i.putExtra("POST_DAYS", post_days);
				i.putExtra("earned", 3);
				startActivity(i);
			}
		});
		return true;
	}

	private void saveImageToPhone(File file) throws Throwable{
//		 Bitmap bm2 = createBitmap();
//		    OutputStream stream = new FileOutputStream("/sdcard/test.png");
		    /* Write bitmap to file using JPEG and 80% quality hint for JPEG. */
//		    bm2.compress(CompressFormat.JPEG, 80, stream);

		 InputStream in = new FileInputStream(file);
		 try {
		     Bitmap bitmap = BitmapFactory.decodeStream(in);
		     File tmpFile = file;//...;
		     try {
		         OutputStream out = new FileOutputStream(tmpFile);
		         try {
		             if (bitmap.compress(CompressFormat.JPEG, 30, out)) {
		                 { File tmp = file; file = tmpFile; tmpFile = tmp; }
		                 tmpFile.delete();
//		                 Log.d(tag, "Saved Image!");
		             } else {
		                 throw new Exception("Failed to save the image as a JPEG");
		             }
		         } finally {
		             out.close();
		         }
		     } catch (Throwable t) {
		         tmpFile.delete();
		         throw t;
		     }
		 } finally {
		     in.close();
		 }
		 
		 
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
