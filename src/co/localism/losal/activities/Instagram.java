package co.localism.losal.activities;

import co.localism.losal.R;
import co.localism.losal.dialogs.InstaLoginDialog;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class Instagram extends Activity {
	private WebView webView;
	private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKENURL ="https://api.instagram.com/oauth/access_token";
	public static final String APIURL = "https://api.instagram.com/v1";
	public static String CALLBACKURL = "Redirect URI";

	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	String client_id = getResources().getString(R.string.instagram_client_id);
	String client_secret = getResources().getString(R.string.instagram_client_secret);
	String authURLString = AUTHURL + "?client_id=" + client_id + "&redirect_uri=" + CALLBACKURL + "&response_type=code&display=touch&scope=likes+comments+relationships";

		String tokenURLString = TOKENURL + "?client_id=" + client_id + "&client_secret=" + client_secret + "&redirect_uri=" + CALLBACKURL + "&grant_type=authorization_code";
	
	

	}
	
	
	
	private void login(){
		
	}
}
