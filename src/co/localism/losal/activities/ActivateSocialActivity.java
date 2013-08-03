package co.localism.losal.activities;

import java.io.IOException;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParseException;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.localism.losal.R;
import co.localism.losal.SVGHandler;

public class ActivateSocialActivity extends Activity {

	private static final String tag = "ActivateSocialActivity";
	private OnClickListener fb_onclick;
	private static final int icon_width = 60;
	private static final int icon_height = 60;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_activate_social);

		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_social_bg);
		ll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// onBackPressed();
			}
		});
		setup_onclick_listeners();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				icon_width, icon_height);

		LinearLayout ll_footer = (LinearLayout) findViewById(R.id.ll_social_footer);
		ll_footer.addView(
				new SVGHandler().svg_to_imageview(this, R.raw.localism),
				ll_footer.getChildCount());

		// add twitter icon and set onclick
		LinearLayout ll_tw = (LinearLayout) findViewById(R.id.ll_activate_tw);
		ll_tw.addView(new SVGHandler().svg_to_imageview(this, R.raw.tw, 0.6f),
				0, params);
		ll_tw.setOnClickListener(fb_onclick);
		TextView tv_tw = (TextView) findViewById(R.id.tv_activate_tw_text);
		tv_tw.setAlpha(0.6f);
		// add fb icon and set onclick
		LinearLayout ll_fb = (LinearLayout) findViewById(R.id.ll_activate_fb);
		ll_fb.addView(new SVGHandler().svg_to_imageview(this, R.raw.fb2,
				R.color.android_green, R.color.white, 1f), 0, params);
		ll_fb.setOnClickListener(fb_onclick);

		// add instagram icon and set onclick
		LinearLayout ll_insta = (LinearLayout) findViewById(R.id.ll_activate_insta);
		ll_insta.addView(
				new SVGHandler().svg_to_imageview(this, R.raw.insta, 0.6f), 0,
				params);
		TextView tv_insta = (TextView) findViewById(R.id.tv_activate_insta_text);
		tv_insta.setAlpha(0.6f);
		ll_insta.setOnClickListener(fb_onclick);

	}

	private void setup_onclick_listeners() {
		fb_onclick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("activate", "start login acti");
				Intent i = new Intent(getApplicationContext(),
						FbLoginActivity.class);
				startActivity(i);
			}
		};

	}
}
