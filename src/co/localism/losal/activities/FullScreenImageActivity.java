package co.localism.losal.activities;

import co.localism.losal.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class FullScreenImageActivity extends Activity {

	
	
	 @Override
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fullscreen_image_activity);
	        Bundle extras = getIntent().getExtras();
	        
	        ImageView iv = (ImageView) findViewById(R.id.iv_fullscreen);
	        iv.setImageResource(extras.getInt("imageID"));
	 }
}
