package co.localism.losal.adapters;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.activities.ActivateSocialActivity;
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
import android.widget.LinearLayout;
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
	private ImageView TW_IMAGE_VIEW, FB_IMAGE_VIEW, INSTA_IMAGE_VIEW;
	private static final String tag = "PostAdapter", TWITTER = "Twitter",
			FACEBOOK = "Facebook", INSTAGRAM = "Instagram";
	public static Filter filter;
	private static String[] CLASS_YEAR = new String[] { "", "Freshman",
			"Sophomore", "Junior", "Senior" };
	private SharedPreferences user_info;
	private OnClickListener activate_onClick, fb_onClick, insta_onClick,
			tw_onClick;
	public static ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstDisplayListener;

	
	
	// public ImageAndTextAdapter(Context ctx, int viewResourceId,
	// String[] strings, TypedArray icons, ArrayList<Integer> openclose,
	// ArrayList<String> companies, ArrayList<String> place_ids) {
	public PostAdapter(final Context ctx, int viewResourceId,
			ArrayList<Post> posts, int UserType) {
		super(ctx, viewResourceId);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPosts = posts;
		mViewResourceId = viewResourceId;
		this.ctx = ctx;

		TW_IMAGE_VIEW = new SVGHandler().svg_to_imageview(ctx, R.raw.tw, 1f);
		FB_IMAGE_VIEW = new SVGHandler().svg_to_imageview(ctx, R.raw.fb, 1f);
		INSTA_IMAGE_VIEW = new SVGHandler().svg_to_imageview(ctx, R.raw.insta,
				1f);

		activate_onClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// check which social site it is and either like, or show
				// activate social activity
				Intent intent = new Intent(ctx, ActivateSocialActivity.class);
				ctx.startActivity(intent);
			}
		};
		fb_onClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// like the post on fb
			}
		};
		tw_onClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// favorite the post on twitter
			}
		};
		insta_onClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// like the post on instagram
			}
		};
	    mImageLoader = ImageLoader.getInstance();
	    mImageLoader.init(ImageLoaderConfiguration.createDefault(ctx));

	}

	@Override
	public int getCount() {
		return mPosts.size();
	}

	@Override
	public String getItem(int position) {
		return mPosts.get(position).getName();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Post cur = mPosts.get(position);
		// ViewBinder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(mViewResourceId, null);
		}
		user_info = ctx.getSharedPreferences("UserInfo", ctx.MODE_PRIVATE);
		user_info.getBoolean("registered", false);
		LinearLayout ll_social = (LinearLayout) convertView
				.findViewById(R.id.ll_social_like_area);

		if (cur.getSocialNetworkName().equalsIgnoreCase(TWITTER))
			if (user_info.getBoolean("hasTwitter", false))
				ll_social.setOnClickListener(tw_onClick);
			else
				ll_social.setOnClickListener(activate_onClick);
		if (cur.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM))
			if (user_info.getBoolean("hasInstagram", false))
				ll_social.setOnClickListener(insta_onClick);
			else
				ll_social.setOnClickListener(activate_onClick);
		if (cur.getSocialNetworkName().equalsIgnoreCase(FACEBOOK))
			if (user_info.getBoolean("hasFacebook", false))
				ll_social.setOnClickListener(fb_onClick);
			else
				ll_social.setOnClickListener(activate_onClick);

		// TODO: change what is visible based on whether the user is registered
		// or not

		TextView name = (TextView) convertView.findViewById(R.id.tv_name);
		name.setText(cur.getName());

		TextView tv_class_year = (TextView) convertView
				.findViewById(R.id.tv_class_year);
		tv_class_year.setText(CLASS_YEAR[cur.class_year]);
		// tv.setTypeface(Times.font);

		TextView tv_time_posted = (TextView) convertView
				.findViewById(R.id.tv_time_posted);
		tv_time_posted.setText("1 hour ago ");

		TextView tv_post_text = (TextView) convertView
				.findViewById(R.id.tv_post_text);
		tv_post_text.setText(cur.getText());

		// Clock Icon
		ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_time);
		iv_clock.setImageDrawable(new SVGHandler().svg_to_drawable(ctx,
				R.raw.clock));
		iv_clock.setAlpha(0.6f);
		iv_clock.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// Social Site Like Icon
		ImageView iv_social_like_icon = (ImageView) convertView
				.findViewById(R.id.iv_social_like_icon);
		if (cur.getSocialNetworkName().equalsIgnoreCase("instagram"))
			iv_social_like_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.fb_like));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("twitter"))
			iv_social_like_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.tw_like));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("facebook"))
			iv_social_like_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.fb_like));

		iv_social_like_icon.setAlpha(1f);
		iv_social_like_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// Social Site Icon
		ImageView iv_social_site_icon = (ImageView) convertView
				.findViewById(R.id.iv_social_site_icon);
		if (cur.getSocialNetworkName().equalsIgnoreCase("instagram"))
			iv_social_site_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.insta));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("twitter"))
			iv_social_site_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.tw));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("facebook"))
			iv_social_site_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.fb));

		iv_social_site_icon.setAlpha(0.6f);
		iv_social_site_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// if(ll_social_site_icon.getChildCount() > 0){
		// ll_social_site_icon.removeViewAt(0);
		// }
		// try{
		// ll_social_site_icon.removeView(TW_IMAGE_VIEW);
		// ll_social_site_icon.addView(TW_IMAGE_VIEW, 0);
		// }catch(Exception e){
		// Log.e(tag, e.toString());
		// }

		// Add image view to layout instead of setting image resource

		// ImageView iv_social_like_icon = (ImageView)
		// convertView.findViewById(R.id.iv_social_like_icon);

		ImageView iv_user_icon = (ImageView) convertView
				.findViewById(R.id.iv_user_icon);

		ImageView iv_post_image = (ImageView) convertView
				.findViewById(R.id.iv_post_image);

		mImageLoader.displayImage(cur.getUrl(), iv_post_image);

		final int imageResource = R.drawable.test_bg;
		iv_post_image.setImageResource(imageResource);
		Log.d("", "");
		iv_post_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.d(tag, "image pressed");
				// Long imageID = parent.getAdapter().getItemId(position);
				Intent intent = new Intent(ctx, FullScreenImageActivity.class);
				intent.putExtra("imageID", imageResource);
				ctx.startActivity(intent);
			}

		});

		return convertView;

	}

}
