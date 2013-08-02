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
import android.widget.ImageView;
import android.widget.LinearLayout;

import co.localism.losal.R;
import co.localism.losal.SVGHandler;

public class ActivateSocialActivity extends Activity{

	private static final String tag = "ActivateSocialActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_social);
	
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_social_bg);
        ll.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
//				onBackPressed();
			}});
        LinearLayout ll_fb = (LinearLayout) findViewById(R.id.ll_activate_fb);
        ll_fb.addView(new SVGHandler().svg_to_imageview(this, R.raw.fb2, R.color.black, R.color.white, 0.6f), 0);

        ll_fb.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.d("activate", "start login acti");
				Intent i = new Intent(getApplicationContext(), FbLoginActivity.class);
				startActivity(i);
			}});
        
        
        
        
        
        
//        ImageView im = new ImageView(this, null);
        
        
	}
}
