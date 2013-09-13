package co.localism.losal.activities;

import java.io.IOException;

import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WebViewActivity extends Activity {

	private WebView webView;
	public static final int GRADES = 0;
	public static final int SOCRATIVE = 1;
	public static final int EDMODO = 2;
	public static final int EVENTS = 3;
	public static final int LOSAL = 4;

	private final String GRADES_URL = "https://abi.losal.org/abi/loginhome.asp";
	private final String EVENTS_URL = "http://losal.tandemcal.com/";
	private final String SOCRATIVE_URL = "http://m.socrative.com/student/#joinRoom";
	private final String LOSAL_URL = "http://www.losal.org/lahs";
	private final String EDMODO_URL = "https://www.edmodo.com/m";
	private final String GOOGLE_CAL_URL = "http://www.google.com/calendar/render?cid=http%3A%2F%2Flosal.tandemcal.com%2Findex.php%3Ftype%3Dexport%26action%3Dical%26export_type%3Dnow_to_infinity%26schools%3D6%26activities%3D15%26event_status_types%3D1%26limit%3Dnone%26date_start%3D2013-08-28%26page%3D2";
	private SlidingMenu sm;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar a = getActionBar();
		a.setDisplayHomeAsUpEnabled(true);
		a.setDisplayShowTitleEnabled(true);
		a.setDisplayUseLogoEnabled(false);
		Drawable dd = new ColorDrawable(R.color.transparent);
		a.setIcon(dd);
		a.setDisplayShowCustomEnabled(true);
		a.setTitle("");
		a.setCustomView(R.layout.actionbar_custome_view);
		TextView title = (TextView) a.getCustomView().findViewById(
				R.id.ab_title);
		title.setTypeface(Typeface.createFromAsset(this.getAssets(),
				"robotoslab_regular.ttf"));
		title.setText(" ");
		OnClickListener toggle = new OnClickListener(){

			@Override
			public void onClick(View v) {
				sm.toggle();
			}
		};
		setContentView(R.layout.webview);
		sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_WINDOW,
				true);
//		new PersonalOptionsOnClickListeners(
//				(LinearLayout) findViewById(R.id.po), this);
		
		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();

		
		webView = (WebView) findViewById(R.id.webview);
//		CookieManager.getInstance().setAcceptCookie(true);
		
		webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
		LinearLayout l = (LinearLayout) findViewById(R.id.po);

		Bundle extras = getIntent().getExtras();
		switch (extras.getInt("which")) {
		case GRADES:
			title.setText("Grades");
//			new PersonalOptionsOnClickListeners(
//					(LinearLayout) findViewById(R.id.po), this, PersonalOptionsOnClickListeners.ACTIVITY_GRADES);
//			l.findViewById(R.id.po_grades).setOnClickListener(toggle);
			webView.loadUrl(GRADES_URL);
			break;
		case SOCRATIVE:
			title.setText("Socrative");
//			new PersonalOptionsOnClickListeners(
//					(LinearLayout) findViewById(R.id.po), this, PersonalOptionsOnClickListeners.ACTIVITY_SOCRATIVE);
//			l.findViewById(R.id.po_socrative).setOnClickListener(toggle);
			webView.loadUrl(SOCRATIVE_URL);
			break;
		case EVENTS: //no longer used
			title.setText("Events");
			webView.loadUrl(EVENTS_URL);
			break;
		case LOSAL: //no longer used
			title.setText("Losal");
			webView.loadUrl(LOSAL_URL);
			break;
		case EDMODO:
			title.setText("Edmodo");
//			new PersonalOptionsOnClickListeners(
//					(LinearLayout) findViewById(R.id.po), this, PersonalOptionsOnClickListeners.ACTIVITY_EDMODO);
//			l.findViewById(R.id.po_edmodo).setOnClickListener(toggle);
			webView.loadUrl(EDMODO_URL);
			break;
		}
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.general, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
//			sm.showMenu();
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
