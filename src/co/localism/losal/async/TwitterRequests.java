package co.localism.losal.async;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;

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

import com.parse.ParseTwitterUtils;

import android.os.AsyncTask;
import android.util.Log;

public class TwitterRequests extends AsyncTask<String, String, String> {



	private final String tag = "TwitterRequests";
	private final String FAV_URL = "https://api.twitter.com/1.1/favorites/create.json";
	private final String UNFAV_URL = "https://api.twitter.com/1.1/favorites/destroy.json";




	@Override
	protected String doInBackground(String... params) {
		if(params[2].equalsIgnoreCase("unfavorite"))
			favoriteTweet(params[0], params[1], false);
		else
			favoriteTweet(params[0], params[1], true);
		return null;
	}

	
	
	
	
	private String favoriteTweet(String id, String user_id, boolean Fav) {
		Log.d(tag, "favoriteTweet called");

		InputStream is = null;
		String result = "";
		String returnString = "";
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost favpost = null;
		if(Fav)
			favpost = new HttpPost(FAV_URL);
		else
			favpost = new HttpPost(UNFAV_URL);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		try {
			favpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		String sss = "https://api.twitter.com/1.1/favorites/create/"+id+".json";
		
		HttpClient client = new DefaultHttpClient();
//		HttpGet verifyGet = new HttpGet(
//				sss);
//		        "https://api.twitter.com/1.1/account/verify_credentials.json");
//		https://api.twitter.com/1.1/favorites/create.json
		
		
//		ParseTwitterUtils.getTwitter().signRequest(verifyGet);
		ParseTwitterUtils.getTwitter().signRequest(favpost);

		try {
//			HttpResponse response = client.execute(verifyGet);
			HttpResponse response = client.execute(favpost);

			HttpEntity entity = response.getEntity();
            is = entity.getContent();
		Log.d(tag, "tw resp: "+responseToString(is));

        new PushData().execute("like",id, user_id);//log the like in our database

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		/*
		

		// http post
		try {
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("id", id));
			favpost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			ParseTwitterUtils.getTwitter().signRequest(favpost);

			
			HttpResponse response = httpclient.execute(favpost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.d(tag, responseToString(is));
			is.close();
			return returnString;
		} catch (SocketException e) {
			
			Log.e(tag, "Socket error in http connection " + e.toString());
		}
	
		catch (Exception e) {
			Log.e(tag, " error in connection" + e.toString());
		}
		
		
		*/
		
		
		
		return "";
	}
	
	
	private String responseToString(InputStream is){
		BufferedReader reader = null;
		StringBuilder sb = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        sb = new StringBuilder();
        String line = null;
			while ((line = reader.readLine()) != null) {
			        sb.append(line + "\n");
			}
			is.close();
        } catch (Exception e) {
        	e.printStackTrace();
		}
		
        return sb.toString();
	}
	
}
