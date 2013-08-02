package co.localism.losal.activities;


import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class WebViewActivity extends Activity{
	   
	private WebView webView;
	public static final int GRADES = 0;
	public static final int SOCRATIVE = 1;
	public static final int EDMODO = 2;
	public static final int EVENTS = 3;
	private final String GRADES_URL = "https://demo.aeries.net/ParentPortal/m/parents?demo=True&user=parent%40aeries.com&pwd=1234"; 
	private final String EVENTS_URL = "http://losal.tandemcal.com/";
	private final String SOCRATIVE_URL ="http://m.socrative.com/"; 	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        SlidingMenu sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_CONTENT);
        new PersonalOptionsOnClickListeners((LinearLayout) findViewById(R.id.po), this);
        getActionBar();
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		
        Bundle extras = getIntent().getExtras();
        switch(extras.getInt("which")){
	        case GRADES:
				webView.loadUrl(GRADES_URL);
	        	break;
	        case SOCRATIVE:
				webView.loadUrl(SOCRATIVE_URL);
	        	break;
	        case EVENTS:
				webView.loadUrl(EVENTS_URL);
	        	break;	
        }

	}
	
	
	
}
