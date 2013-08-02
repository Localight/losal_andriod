package co.localism.losal.activities;


import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ScheduleActivity extends Activity{

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        ImageView imageView = new ImageView(this);
        // Set the background color to white
        imageView.setBackgroundColor(Color.WHITE);
        // Parse the SVG file from the resource
        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.android);
        // Get a drawable from the parsed SVG and set it as the drawable for the ImageView
        imageView.setImageDrawable(svg.createPictureDrawable());
        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setContentView(imageView);
        
//        setContentView(R.layout.activity_schedule);
//        SlidingMenu sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_CONTENT);
//        new PersonalOptionsOnClickListeners((LinearLayout) findViewById(R.id.po), this);

    }
}
