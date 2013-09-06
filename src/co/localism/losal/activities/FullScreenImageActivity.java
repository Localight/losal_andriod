package co.localism.losal.activities;

import co.localism.losal.R;
import co.localism.losal.adapters.PostAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
/**
 * This activity displays an image at full screen when it is called from PostAdapter.class
 * 
 * @author Joe
 *
 */
public class FullScreenImageActivity extends Activity {

	
	
	 @Override
		public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fullscreen_image_activity);
	        Bundle extras = getIntent().getExtras();
	        
	        ImageView iv = (ImageView) findViewById(R.id.iv_fullscreen);
//	        iv.setImageResource(extras.getInt("imageID"));
	        PostAdapter.mImageLoader.displayImage(extras.getString("imageURL"), iv);
	        iv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					finish();
					
				}
	        	
	        });
//	        TODO: make sure that it scales the image correctly and leaves black space if needed. 
//	        For Example: A square image should scale so that the image fits to the edge horizontally
//	        but has black space on the top and bottom. Assuming the phone is being held in portrait mode.
	 }
}
