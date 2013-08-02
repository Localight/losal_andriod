package co.localism.losal.activities;


import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ScheduleActivity extends Activity{

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        SlidingMenu sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_CONTENT);
        new PersonalOptionsOnClickListeners((LinearLayout) findViewById(R.id.po), this);

    }
}
