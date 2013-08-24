package co.localism.losal.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONTokener;

import co.localism.losal.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Instagram extends Activity {
	private WebView webView;
	private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
	public static final String APIURL = "https://api.instagram.com/v1/";
	public static String CALLBACKURL = "";
	private final String tag = "Instagram";
	public String request_token = "";
	private String authURLString = "";
	private String tokenURLString = "";
	private String client_id = "";
	private String client_secret = "";
	private Context ctx = this;
	private static final int NOT_STARTED = 0;
	private static final int SUCCESSFULLY_LOGGED_IN = 1;
	private static final int ALREADY_LOGGED_IN = 2;
	private static final int ERROR_LOGGING_IN = 3;
	private String ERROR_MESSAGE= "";

	private int STATUS = NOT_STARTED;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		CALLBACKURL = getResources().getString(
				R.string.XJOEXinstagram_callback_url);

		client_id = getResources().getString(R.string.XJOEXinstagram_client_id);
		client_secret = getResources().getString(
				R.string.XJOEXinstagram_client_secret);
		authURLString = AUTHURL
				+ "?client_id="
				+ client_id
				+ "&redirect_uri="
				+ CALLBACKURL
				+ "&response_type=code&display=touch&scope=likes+comments+relationships";

		// tokenURLString = TOKENURL + "?client_id=" + client_id
		// + "&client_secret=" + client_secret + "&redirect_uri="
		// + CALLBACKURL + "&grant_type=authorization_code";
		tokenURLString = TOKENURL + "?client_id=" + client_id
				+ "&amp;client_secret=" + client_secret + "&amp;redirect_uri="
				+ CALLBACKURL + "&amp;grant_type=authorization_code";

		showWebView();
	}

	private void showWebView() {
		WebView webView;// = new WebView(getBaseContext());
		webView = (WebView) findViewById(R.id.webview);

		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new AuthWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(authURLString);

	}

	private void login() {

	}

	public class AuthWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(tag, "shouldOverrideUrlLoading called");

			if (url.startsWith(CALLBACKURL)) {
				System.out.println(url);
				String parts[] = url.split("=");
				request_token = parts[1]; // This is your request token.
				new InBackground().execute("", "", "");
				return true;
			}
			return false;

		}
	}

	public class InBackground extends AsyncTask<String, String, String> {

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			Toast.makeText(ctx, "Login cancelled.", Toast.LENGTH_SHORT).show();

		}

		@Override
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
			Toast.makeText(ctx, "Login cancelled.", Toast.LENGTH_SHORT).show();

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			switch(STATUS){
			case SUCCESSFULLY_LOGGED_IN:
				Toast.makeText(ctx, "Successfully logged in!", Toast.LENGTH_SHORT).show();
				break;
			case ERROR_LOGGING_IN:
				Toast.makeText(ctx, "Error logging in.", Toast.LENGTH_SHORT).show();
				break;
			}
			finish();

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(String... params) {
			Log.d(tag, "doInBackground called");

			if(getAccessToken())
				STATUS = SUCCESSFULLY_LOGGED_IN;
			else
				STATUS = ERROR_LOGGING_IN;
			return null;
		}

	}

	private void XXgetAccessToken() {
		try {
			Log.d(tag, "get access token called");
			InputStream is = null;
			String result = "";
			String returnString = "";

			HttpClient httpclient = new DefaultHttpClient();
			// HttpPost accesspost = new HttpPost(tokenURLString);
			HttpPost accesspost = new HttpPost(TOKENURL);
			HttpResponse response = httpclient.execute(accesspost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("client_id", client_id));
			nameValuePairs.add(new BasicNameValuePair("client_secret",
					client_secret));
			nameValuePairs.add(new BasicNameValuePair("grant_type",
					"authorization_code"));
			nameValuePairs.add(new BasicNameValuePair("redirect_uri",
					CALLBACKURL));
			nameValuePairs.add(new BasicNameValuePair("code", request_token));

			// "client_id="
			// + getResources().getString(R.string.instagram_client_id)
			// + "client_secret="
			// + getResources()
			// .getString(R.string.instagram_client_secret)
			// + "grant_type=authorization_code" + "redirect_uri="
			// + CALLBACKURL + "code=" + token);

			accesspost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			String response_string = streamToString(is);
			JSONObject jsonObject = (JSONObject) new JSONTokener(
					response_string).nextValue();
			Log.d(tag, "json:  " + jsonObject.toString());

			String accessTokenString = "";
			accessTokenString = jsonObject.getString("access_token");
			Log.d(tag, "access token = " + accessTokenString);
			// Here is your ACCESS TOKEN
			// This is how you can get the user info. You can eplore the JSON
			// sent by Instagram as well to know what info you go tin a
			// response.
			String id = "";
			String username = "";
			id = jsonObject.getJSONObject("user").getString("id");
			username = jsonObject.getJSONObject("user").getString("username");
			is.close();
		} catch (Exception e) {
			Log.e(tag, "getAccessToken error: " + e.toString());

			e.printStackTrace();
		}

	}

	private boolean getAccessToken() {
		try {
			Log.d(tag, "get access token called");

			URL url = new URL(TOKENURL);
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
					.openConnection();
			httpsURLConnection.setRequestMethod("POST");
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setDoOutput(true);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					httpsURLConnection.getOutputStream());
			outputStreamWriter.write("client_id=" + client_id
					+ "&amp;client_secret=" + client_secret
					+ "&amp;grant_type=authorization_code"
					+ "&amp;redirect_uri=" + CALLBACKURL + "&amp;code="
					+ request_token);

			outputStreamWriter.flush();
			String response = streamToString(httpsURLConnection
					.getInputStream());
			JSONObject jsonObject = (JSONObject) new JSONTokener(response)
					.nextValue();
			Log.d(tag, "json:  " + jsonObject.toString());
			String accessTokenString = "";
			accessTokenString = jsonObject.getString("access_token"); // Here is
																		// your
																		// ACCESS
																		// TOKEN
			Log.d(tag, "access token = " + accessTokenString);

			// JSONObject jsonObject = (JSONObject) new JSONTokener(response)
			// .nextValue();
			// accessTokenString = jsonObject.getString("access_token");
			// Here is your ACCESS TOKEN
			// This is how you can get the user info. You can eplore the JSON
			// sent by Instagram as well to know what info you go tin a
			// response.
			String id = "";
			String username = "";
			String full_name = "";
			id = jsonObject.getJSONObject("user").getString("id");
			username = jsonObject.getJSONObject("user").getString("username");
			full_name = jsonObject.getJSONObject("user").getString("full_name");
			saveToPhone(id, username, accessTokenString, full_name);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method that returns String from the InputStream given by p_is
	 * 
	 * @param p_is
	 *            The given InputStream
	 * @return The String from the InputStream
	 */
	public static String streamToString(InputStream p_is) {
		try {
			BufferedReader m_br;
			StringBuffer m_outString = new StringBuffer();
			m_br = new BufferedReader(new InputStreamReader(p_is));
			String m_read = m_br.readLine();
			while (m_read != null) {
				m_outString.append(m_read);
				m_read = m_br.readLine();
			}
			return m_outString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Instagram", "streamToString error: " + e.toString());
			return "";
		}
	}

	private void saveToPhone(String id, String uname, String access_token,
			String full_name) {
		/*****  InstagramInfo  *****/
		SharedPreferences insta_info = getSharedPreferences("InstagramInfo",
				MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = insta_info.edit();
		prefEditor.putBoolean("isLinked", true);
		prefEditor.putString("id", id);
		prefEditor.putString("user_name", uname);
		prefEditor.putString("access_token", access_token);
		prefEditor.putString("request_token", request_token);
		prefEditor.putString("full_name", full_name);
		prefEditor.commit();
	
		/*****  InstagramInfo  *****/
		SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
		prefEditor = user_info.edit();
		prefEditor.putBoolean("hasInstagram", true);
		prefEditor.commit();
	
	}

}
