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





	@Override
	protected String doInBackground(String... params) {
		favoriteTweet(params[0]);
		return null;
	}

	
	
	
	
	private String favoriteTweet(String id) {
		Log.d(tag, "favoriteTweet called");

		InputStream is = null;
		String result = "";
		String returnString = "";
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost favpost = new HttpPost("https://api.twitter.com/1.1/favorites/create.json");
		
	
		String sss = "https://api.twitter.com/1.1/favorites/create.json";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet verifyGet = new HttpGet(
//				sss);
		        "https://api.twitter.com/1.1/account/verify_credentials.json");
//		https://api.twitter.com/1.1/favorites/create.json
		
		
		ParseTwitterUtils.getTwitter().signRequest(verifyGet);
		try {
			HttpResponse response = client.execute(verifyGet);
			HttpEntity entity = response.getEntity();
            is = entity.getContent();
		Log.d(tag, "tw resp: "+responseToString(is));
		
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
