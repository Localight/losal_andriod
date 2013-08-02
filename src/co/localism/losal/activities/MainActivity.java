package co.localism.losal.activities;

import java.util.ArrayList;


import co.localism.losal.R;
import co.localism.losal.SetUpSlidingMenu;
import co.localism.losal.R.layout;
import co.localism.losal.R.menu;
import co.localism.losal.adapters.PostAdapter;
import co.localism.losal.listens.PersonalOptionsOnClickListeners;
import co.localism.losal.objects.Post;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
public class MainActivity extends ListActivity {

	public Context ctx = this;
	private ListAdapter listadapter;
	public static ArrayList<Post> posts = new ArrayList<Post>();
	private static final String tag = "MainActivity";
	
	
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlidingMenu sm = new SetUpSlidingMenu(this, SlidingMenu.SLIDING_CONTENT);
        new PersonalOptionsOnClickListeners((LinearLayout) findViewById(R.id.po), this);
        getActionBar();
    
        
        
        SharedPreferences user_info = getSharedPreferences("UserInfo",
				MODE_PRIVATE);
//		SharedPreferences.Editor prefEditor = user_info.edit();
//		prefEditor.putBoolean("registered", true);
//		prefEditor.putString("user_type", "student");
//		prefEditor.commit();
        
        
        
        
        
        Button btn = (Button) findViewById(R.id.btn_social);
        btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(ctx, ActivateSocialActivity.class);
				startActivity(i);
			}});
    
        
        
        posts.add(new Post("Mary H", 1));
        posts.add(new Post("Joe C", 2));
        posts.add(new Post("Josh A", 4));
        
        
		listadapter = new PostAdapter(ctx,
				R.layout.post, posts, 1);
		setListAdapter(listadapter);

		ListView lv = getListView();
//		lv.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				 switch(view.getId()) {
//				 	case R.id.iv_post_image:
//				 		Log.d(tag,"image pressed");
//				 		Long imageID = parent.getAdapter().getItemId(position);
//				 		Intent intent = new Intent(ctx, FullScreenImageActivity.class);
//				 		intent.putExtra("imageID", imageID);
//				 		startActivity(intent);
//				 		break;
//				 }
//			}
//		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
