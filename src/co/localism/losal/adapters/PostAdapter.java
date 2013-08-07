package co.localism.losal.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
import android.graphics.Bitmap;
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
			fullscreen_onClick, tw_onClick;
	public static ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstDisplayListener;
	private PostViewHolder holder;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	private DisplayImageOptions options;

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

		options = new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				// .showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(false).cacheInMemory(true)
				.cacheOnDisc(true)
				// .displayer(new RoundedBitmapDisplayer(20))
				.build();
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
		final Post cur = mPosts.get(position);

		if (convertView == null) {
			// Put things in here that only need to be set once and can be
			// reused.
			convertView = mInflater.inflate(mViewResourceId, null);
			holder = new PostViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_class_year = (TextView) convertView
					.findViewById(R.id.tv_class_year);
			holder.tv_time_posted = (TextView) convertView
					.findViewById(R.id.tv_time_posted);

			holder.iv_social_like_icon = (ImageView) convertView
					.findViewById(R.id.iv_social_like_icon);
			holder.iv_user_icon = (ImageView) convertView
					.findViewById(R.id.iv_user_icon);

			holder.iv_post_image = (ImageView) convertView
					.findViewById(R.id.iv_post_image);
			holder.iv_social_site_icon = (ImageView) convertView
					.findViewById(R.id.iv_social_site_icon);
			holder.iv_clock = (ImageView) convertView
					.findViewById(R.id.iv_time);

			holder.tv_post_text = (TextView) convertView
					.findViewById(R.id.tv_post_text);

			holder.ll_social = (LinearLayout) convertView
					.findViewById(R.id.ll_social_like_area);

			// Clock Icon. Only needs to be set once
			holder.iv_clock.setImageDrawable(new SVGHandler().svg_to_drawable(
					ctx, R.raw.clock));
			holder.iv_clock.setAlpha(0.6f);
			holder.iv_clock.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

			// Post Image onclick only needs to be set once
			fullscreen_onClick = new OnClickListener() {

				@Override
				public void onClick(View view) {
					Log.d(tag, "image pressed");
					Intent intent = new Intent(ctx,
							FullScreenImageActivity.class);
					intent.putExtra("imageURL", cur.getUrl());
					ctx.startActivity(intent);
				}

			};
			convertView.setTag(holder);

		} else {
			holder = (PostViewHolder) convertView.getTag();
		}

		user_info = ctx.getSharedPreferences("UserInfo", ctx.MODE_PRIVATE);
		user_info.getBoolean("registered", false);

		try {
			if (cur.getSocialNetworkName().equalsIgnoreCase(TWITTER))
				if (user_info.getBoolean("hasTwitter", false))
					holder.ll_social.setOnClickListener(tw_onClick);
				else
					holder.ll_social.setOnClickListener(activate_onClick);
			else if (cur.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM))
				if (user_info.getBoolean("hasInstagram", false))
					holder.ll_social.setOnClickListener(insta_onClick);
				else
					holder.ll_social.setOnClickListener(activate_onClick);
			else if (cur.getSocialNetworkName().equalsIgnoreCase(FACEBOOK))
				if (user_info.getBoolean("hasFacebook", false))
					holder.ll_social.setOnClickListener(fb_onClick);
				else
					holder.ll_social.setOnClickListener(activate_onClick);
		} catch (Exception e) {
			Log.e(tag, e.toString());

		}
		// TODO: change what is visible based on whether the user is registered
		// or not

		holder.tv_name.setText(cur.getName());
		// holder.tv_name.setTextColor(ctx.getResources().getColor(R.color.holo_light_blue));

		holder.tv_class_year.setText(CLASS_YEAR[cur.getClassYear()]);
		// tv.setTypeface(some_font);

		holder.tv_time_posted.setText("1 hour ago ");

		holder.tv_post_text.setText(cur.getText());

		// User Icon
		String UserIcon = cur.getUserIcon();
		// TODO: use string UserIcon to get the correct svg from R.raw and use
		// the color pulled down and
		// stored (still need to do this) in the post object
		holder.iv_user_icon.setImageDrawable(new SVGHandler().svg_to_drawable(
				ctx, R.raw.edmodo, R.color.white, R.color.holo_light_blue));
		holder.iv_user_icon.setAlpha(1f);
		holder.iv_user_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		// Social Site Like Icon

		if (cur.getSocialNetworkName().equalsIgnoreCase("instagram"))
			holder.iv_social_like_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.fb_like));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("twitter"))
			holder.iv_social_like_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.tw_like));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("facebook"))
			holder.iv_social_like_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.fb_like));

		holder.iv_social_like_icon.setAlpha(1f);
		holder.iv_social_like_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// Social Site Icon
		if (cur.getSocialNetworkName().equalsIgnoreCase("instagram"))
			holder.iv_social_site_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.insta));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("twitter"))
			holder.iv_social_site_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.tw));
		else if (cur.getSocialNetworkName().equalsIgnoreCase("facebook"))
			holder.iv_social_site_icon.setImageDrawable(new SVGHandler()
					.svg_to_drawable(ctx, R.raw.fb));

		holder.iv_social_site_icon.setAlpha(0.6f);
		holder.iv_social_site_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// Post Image
		if (cur.getUrl() != null && cur.getUrl().length() > 3) {// this post has
																// an image to
																// display
			holder.iv_post_image.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(cur.getUrl(), holder.iv_post_image,
					options, animateFirstListener);
		} else {// this post does not have an image to display so hide the
				// imageview
			holder.iv_post_image.setVisibility(View.GONE);
		}
		Log.d("", "");
		holder.iv_post_image.setOnClickListener(fullscreen_onClick);

		return convertView;
	}

	public static class PostViewHolder {

		TextView tv_name;
		TextView tv_class_year;
		TextView tv_time_posted;
		TextView tv_post_text;
		ImageView iv_social_like_icon;
		ImageView iv_social_site_icon;
		ImageView iv_user_icon;
		ImageView iv_post_image;
		ImageView iv_clock;
		LinearLayout ll_social;

	}

	/**
	 * 
	 * This is used to animate the images when they first display
	 * 
	 */
	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
