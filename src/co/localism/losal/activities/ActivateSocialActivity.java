package co.localism.losal.activities;

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
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class ActivateSocialActivity extends Activity{

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
        
//        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.fb);
//        Picture picture = svg.getPicture();
//        Drawable drawable = svg.createPictureDrawable();
//        
     // Create a new ImageView
        ImageView imageView = new ImageView(this);
        // Set the background color to white
        imageView.setBackgroundColor(Color.WHITE);
        // Parse the SVG file from the resource
        SVG fb = SVGParser.getSVGFromResource(getResources(), R.raw.fb);//, getResources().getColor(R.color.black), getResources().getColor(R.color.white));
        // Get a drawable from the parsed SVG and set it as the drawable for the ImageView
        
        imageView.setImageDrawable(fb.createPictureDrawable());
//        imageView.setBackgroundColor(Color.BLUE);
        LinearLayout ll_fb = (LinearLayout) findViewById(R.id.ll_activate_fb);
//        ll_fb.setBackgroundDrawable(fb.createPictureDrawable());
        
        
        
        ll_fb.addView(imageView, 0);
        
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
