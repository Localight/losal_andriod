package co.localism.losal.adapters;

import java.util.ArrayList;
import java.util.List;

import co.localism.losal.R;
import co.localism.losal.activities.FullScreenImageActivity;
import co.localism.losal.activities.MainActivity;
import co.localism.losal.objects.Post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PostAdapter extends ArrayAdapter<String> {

	private LayoutInflater mInflater;

	private String[] mStrings;
	private ArrayList<Post> mPosts;
	private TypedArray mIcons;
	private int mViewResourceId;
	private Context ctx;
	private static final String tag = "PostAdapter";
	public static Filter filter;

	// public ImageAndTextAdapter(Context ctx, int viewResourceId,
	// String[] strings, TypedArray icons, ArrayList<Integer> openclose,
	// ArrayList<String> companies, ArrayList<String> place_ids) {
	public PostAdapter(Context ctx, int viewResourceId, ArrayList<Post> posts, int UserType) {
		super(ctx, viewResourceId);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPosts = posts;
		mViewResourceId = viewResourceId;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return mPosts.size();
	}

	@Override
	public String getItem(int position) {
		return mPosts.get(position).Name;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewBinder holder;
		if (convertView == null)
			convertView = mInflater.inflate(mViewResourceId, null);
		
        SharedPreferences user_info = ctx.getSharedPreferences("UserInfo",
				ctx.MODE_PRIVATE);
        user_info.getBoolean("registered", false);
//        TODO: change what is visible based on whether the user is registered or not
        
		Post cur = mPosts.get(position);
		
		TextView name = (TextView) convertView.findViewById(R.id.tv_name);
		name.setText(cur.Name);


		TextView tv = (TextView) convertView.findViewById(R.id.tv_class_year);
		tv.setText(cur.class_year+"");
//		tv.setTypeface(Times.font);

		ImageView iv_user_icon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
		
		ImageView iv_post_image = (ImageView) convertView.findViewById(R.id.iv_post_image);
		final int imageResource = R.drawable.test_bg;
		iv_post_image.setImageResource(imageResource);
		Log.d("",  "");
		iv_post_image.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				Log.d(tag ,"image pressed");
//		 		Long imageID = parent.getAdapter().getItemId(position);
		 		Intent intent = new Intent(ctx, FullScreenImageActivity.class);
		 		intent.putExtra("imageID", imageResource);
		 		ctx.startActivity(intent);				
			}
			
		});
		
		
		return convertView;

		
	}
	
	
	

}
