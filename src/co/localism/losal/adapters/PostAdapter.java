package co.localism.losal.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import co.localism.losal.FetchFeed;
import co.localism.losal.R;
import co.localism.losal.SVGHandler;
import co.localism.losal.activities.ActivateSocialActivity;
import co.localism.losal.activities.FullScreenImageActivity;
import co.localism.losal.activities.MainActivity;
import co.localism.losal.async.InstagramRequests;
import co.localism.losal.async.TwitterRequests;
import co.localism.losal.objects.Post;
import co.localism.losal.objects.TimeHandler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PostAdapter extends ArrayAdapter<Post> {

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
	private TimeHandler TH;
	private Typeface icon_font;
	private DisplayImageOptions options;
	Post cur;
	private Intent fullscreen_intent;
	private Drawable INSTA_ICON, TW_ICON, INSTA_LIKE_ICON, TW_LIKE_ICON;
	private boolean loading = false;
	
	public PostAdapter(final Context ctx, int viewResourceId,
			ArrayList<Post> posts, int UserType) {
		super(ctx, viewResourceId);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPosts = posts;
		mViewResourceId = viewResourceId;
		this.ctx = ctx;

		TH = new TimeHandler();
		icon_font = Typeface.createFromAsset(ctx.getAssets(), "icomoon.ttf");

//		TW_IMAGE_VIEW = new SVGHandler().svg_to_imageview(ctx, R.raw.tw, 1f);
//		FB_IMAGE_VIEW = new SVGHandler().svg_to_imageview(ctx, R.raw.fb, 1f);
//		INSTA_IMAGE_VIEW = new SVGHandler().svg_to_imageview(ctx, R.raw.insta,
//				1f);
		INSTA_ICON = new SVGHandler()
		.svg_to_drawable(ctx, R.raw.insta);
		TW_ICON = new SVGHandler()
		.svg_to_drawable(ctx, R.raw.tw);
		INSTA_LIKE_ICON = new SVGHandler()
		.svg_to_drawable(ctx, R.raw.heart);
		TW_LIKE_ICON = new SVGHandler()
					.svg_to_drawable(ctx, R.raw.tw_like);
		

		fullscreen_intent = new Intent(ctx, FullScreenImageActivity.class);
		OnClickListener zxa = new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		};

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
				new TwitterRequests().execute("519175652481234876_14802876",
						"", "");
				// favoriteTweet("519175652481234876_14802876");
			}
		};
		// insta_onClick = new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// // like the post on instagram
		// // TODO: check if user liked the post already
		// SharedPreferences insta_info = ctx.getSharedPreferences(
		// "InstagramInfo", ctx.MODE_PRIVATE);
		// insta_info.getString("access_token", "");
		// Log.d(tag, cur.getText());
		// new InstagramRequests().execute("like",
		// cur.getSocialNetworkPostId(),
		// insta_info.getString("access_token", ""));
		//
		// }
		// };

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(ctx));

		options = new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				// .showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.cacheOnDisc(true)
				// .displayer(new RoundedBitmapDisplayer(20))
				.build();

	}

	@Override
	public int getCount() {
		return mPosts.size();
	}

	public Post getPost(int position) {
		return mPosts.get(position);
	}

	@Override
	public Post getItem(int position) {
		return mPosts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	@Override
	public void add(Post p){
		super.add(p);
		mPosts.add(p);
		notifyDataSetChanged();

	}
	
	public void addAll(ArrayList<Post> p){
		super.addAll(p);
		Log.d(tag, "allAll called!");
		mPosts.addAll(p);
		notifyDataSetChanged();
	}
	
	public void setStatus(boolean isLoading){
		this.loading = isLoading;
	}
		
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		cur = mPosts.get(position);
//		if((mPosts.size() == 0 || mPosts.size()-1 == position) && !loading){
		if(mPosts.size()-4 == position && position > 0 && !loading){
			Log.d(tag, "FETCHING");
//
			loading = true;
			FetchFeed fetcher = new FetchFeed();
			fetcher.fetch(this);
////			addAll(fetcher.fetch());
		}
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
			// holder.iv_user_icon = (ImageView) convertView
			// .findViewById(R.id.iv_user_icon);
			holder.tv_user_icon = (TextView) convertView
					.findViewById(R.id.tv_user_icon);

			holder.iv_post_image = (ImageView) convertView
					.findViewById(R.id.iv_post_image);
			WindowManager wm = (WindowManager) ctx
					.getSystemService(Context.WINDOW_SERVICE);
			int wpx = wm.getDefaultDisplay().getWidth();
			wpx -= (2 * ctx.getResources().getDimension(
					R.dimen.post_side_margin));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, wpx);
			holder.iv_post_image.setLayoutParams(params);
			holder.iv_post_image.setVisibility(View.INVISIBLE);

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
					// Intent intent = new Intent(ctx,
					// FullScreenImageActivity.class);
					// intent.putExtra("imageURL",
					// mPosts.get(position).getUrl());
					ctx.startActivity(fullscreen_intent);
					// fullscreen_intent.removeExtra("imageURL");
					// fullscreen_intent.putExtra("imageURL",
					// mPosts.get(position).getUrl());

				}

			};
			convertView.setTag(holder);

		} else {
			holder = (PostViewHolder) convertView.getTag();
		}

		user_info = ctx.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		user_info.getBoolean("registered", false);
		
		try {
			// if (cur.getSocialNetworkName().equalsIgnoreCase(TWITTER))
			// if (user_info.getBoolean("hasTwitter", false))
			// holder.ll_social.setOnClickListener(tw_onClick);
			// else
			// holder.ll_social.setOnClickListener(activate_onClick);
			// else if (cur.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM))
			// if (user_info.getBoolean("hasInstagram", false))
			// holder.ll_social.setOnClickListener(insta_onClick);
			// else
			// holder.ll_social.setOnClickListener(activate_onClick);
			// else if (cur.getSocialNetworkName().equalsIgnoreCase(FACEBOOK))
			// if (user_info.getBoolean("hasFacebook", false))
			// holder.ll_social.setOnClickListener(fb_onClick);
			// else
			// holder.ll_social.setOnClickListener(activate_onClick);
		} catch (Exception e) {
			Log.e(tag, e.toString());
			holder.ll_social
					.setOnClickListener((OnClickListener) itemClickListener);

		}
		// TODO: change what is visible based on whether the user is registered
		// or not
		try{
		holder.tv_name.setText(cur.getName());
		// holder.tv_name.setTextColor(ctx.getResources().getColor(R.color.holo_light_blue));

		// holder.tv_class_year.setText(CLASS_YEAR[cur.getClassYear()]);
		holder.tv_class_year.setText(cur.getClassYear());

		// holder.tv_time_posted.setText("1 hour ago ");
		holder.tv_time_posted.setText(TH.getTimeAgo(cur.getPostTime()));
		Log.d(tag, "TimeAgo: " + TH.getTimeAgo(cur.getPostTime()));
		holder.tv_post_text.setText(cur.getText());

		// TODO: use string UserIcon to get the correct svg from R.raw and use
		// the color pulled down and
		// stored (still need to do this) in the post object
		/*
		 * holder.iv_user_icon.setImageDrawable(new
		 * SVGHandler().svg_to_drawable( ctx, R.raw.edmodo, R.color.white,
		 * R.color.holo_light_blue)); holder.iv_user_icon.setAlpha(1f);
		 * holder.iv_user_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		 */

		// Character ch =new Character('\ue000');
		holder.tv_user_icon.setTypeface(icon_font);
		if (cur.getFaveColor().length() > 5)
			holder.tv_user_icon.setTextColor(Color.parseColor(cur
					.getFaveColor()));
		// holder.tv_user_icon.setText(cur.getUserIcon());
		holder.tv_user_icon.setText(cur.getUserIcon().toString());

		// Social Site LIKE Icon
		if (cur.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM))
			holder.iv_social_like_icon.setImageDrawable(INSTA_LIKE_ICON);
				
		else if (cur.getSocialNetworkName().equalsIgnoreCase(TWITTER))
			holder.iv_social_like_icon.setImageDrawable(TW_LIKE_ICON);
		if(cur.getUserLiked())
			holder.iv_social_like_icon.setAlpha(1f);
		else			
			holder.iv_social_like_icon.setAlpha(0.6f);

			
		holder.iv_social_like_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// Social Site Icon
		if (cur.getSocialNetworkName().equalsIgnoreCase("instagram"))
			holder.iv_social_site_icon.setImageDrawable(INSTA_ICON);
		else if (cur.getSocialNetworkName().equalsIgnoreCase("twitter"))
			holder.iv_social_site_icon.setImageDrawable(TW_ICON);

//			holder.iv_social_site_icon.setImageDrawable(new SVGHandler()
//					.svg_to_drawable(ctx, R.raw.tw));
//		else if (cur.getSocialNetworkName().equalsIgnoreCase("facebook"))
//			holder.iv_social_site_icon.setImageDrawable(new SVGHandler()
//					.svg_to_drawable(ctx, R.raw.fb));

//		holder.iv_social_site_icon.setAlpha(0.6f);
		holder.iv_social_site_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

		// Post Image
		if (cur.getUrl() != null && cur.getUrl().length() > 3) {
			// this post has an image to display

			holder.iv_post_image.setVisibility(View.VISIBLE);
			//
			// Make imageview as tall as it is wide. This will act as a place
			// holder when there is no image loaded yet

			mImageLoader.displayImage(cur.getUrl(), holder.iv_post_image,
					options, animateFirstListener);
			fullscreen_intent.removeExtra("imageURL");
			fullscreen_intent.putExtra("imageURL", mPosts.get(position)
					.getUrl());
		
			holder.iv_post_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(ctx,
							FullScreenImageActivity.class);
					intent.putExtra("imageURL", mPosts.get(position).getUrl());
					ctx.startActivity(fullscreen_intent);
					// fullscreen_intent.removeExtra("imageURL");
					// fullscreen_intent.putExtra("imageURL",
					// mPosts.get(position).getUrl());

				}

			});

		} else {// this post does not have an image to display so hide the
				// imageview
			holder.iv_post_image.setVisibility(View.GONE);
		}
		}catch(Exception e){
			Log.e(tag, e.toString());
		}
		Log.d("", "");
		// holder.iv_post_image.setOnClickListener(fullscreen_onClick);
		holder.ll_social.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// like the post on instagram
				Log.d(tag, "social clicked!");
				Log.d(tag, mPosts.get(position).getText());
				if(!mPosts.get(position).getUserLiked())
					v.findViewById(R.id.iv_social_like_icon).setAlpha(1f);
				else
					v.findViewById(R.id.iv_social_like_icon).setAlpha(0.6f);

				socialLikeClicked(mPosts.get(position));
				Log.d(tag, ""+mPosts.get(position).getUserLiked());
//				View xx = getView(position, v, parent);
//				if (mPosts.get(position).getSocialNetworkName()
//						.equalsIgnoreCase(INSTAGRAM)) {
//					Log.d(tag, INSTAGRAM);
//					
//					// TODO: check if user liked the post already
//					SharedPreferences insta_info = ctx.getSharedPreferences(
//							"InstagramInfo", ctx.MODE_PRIVATE);
//					insta_info.getString("access_token", "");
//					new InstagramRequests().execute("like",
//							cur.getSocialNetworkPostId(),
//							insta_info.getString("access_token", ""));
//				} else if (mPosts.get(position).getSocialNetworkName()
//						.equalsIgnoreCase(TWITTER)) {
//					Log.d(tag, TWITTER);
//				}
//				mPosts.get(position).setUserLiked(!mPosts.get(position).getUserLiked());
			}
		});
		
		return convertView;
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int position,
				long id) {
			Log.d(tag, "item clicked!!!!");

			Log.d(tag, "" + v.getId());
			switch (v.getId()) {
			case R.id.iv_post_image:
				Log.d(tag, "image pressed");
				Intent intent = new Intent(ctx, FullScreenImageActivity.class);
				intent.putExtra("imageURL", mPosts.get(position).getUrl());
				ctx.startActivity(intent);
				break;
			case R.id.ll_social_like_area:
				Log.d(tag, "like area");
				socialLikeClicked(mPosts.get(position));
				break;
			case R.id.iv_social_like_icon:
				Log.d(tag, "like icon");
				mPosts.get(position);
				socialLikeClicked(mPosts.get(position));
				break;
			case R.id.iv_social_site_icon:
				Log.d(tag, "site icon");
				socialLikeClicked(mPosts.get(position));
				break;
			}

		}

	};

	private void itemClickListener(ListView l, View v, int position, long id) {

		Log.d(tag, "" + v.getId());
		switch (v.getId()) {
		case R.id.iv_post_image:
			Log.d(tag, "image pressed");
			Intent intent = new Intent(ctx, FullScreenImageActivity.class);
			intent.putExtra("imageURL", mPosts.get(position).getUrl());
			ctx.startActivity(intent);
			break;
		case R.id.ll_social_like_area:
			Log.d(tag, "like area");
			socialLikeClicked(mPosts.get(position));
			break;
		case R.id.iv_social_like_icon:
			Log.d(tag, "like icon");
			mPosts.get(position);
			socialLikeClicked(mPosts.get(position));
			break;
		case R.id.iv_social_site_icon:
			Log.d(tag, "site icon");
			socialLikeClicked(mPosts.get(position));
			break;
		}

	}

	private void socialLikeClicked(Post post) {
		Log.d(tag, post.getText());
//		TODO: CHECK IF LOGGED IN
		String site = post.getSocialNetworkName();
		post.setUserLiked(!post.getUserLiked());

		if (site.equalsIgnoreCase(ctx.getResources().getString(R.string.tw))){
			new TwitterRequests().execute(post.getSocialNetworkPostId());
//			MainActivity.favoriteTweet(post.getSocialNetworkPostId());
		}else if (site.equalsIgnoreCase(ctx.getResources().getString(
				R.string.insta)))
			likeInsta(post.getSocialNetworkPostId());

	}

	public void likeInsta(String id) {
		// like the post on instagram
		// TODO: check if user liked the post already
		SharedPreferences insta_info = ctx.getSharedPreferences(
				"InstagramInfo", ctx.MODE_PRIVATE);
		insta_info.getString("access_token", "");
		new InstagramRequests().execute("like", id,
				insta_info.getString("access_token", ""));
	}

	public static class PostViewHolder {

		TextView tv_name;
		TextView tv_class_year;
		TextView tv_time_posted;
		TextView tv_post_text;
		TextView tv_user_icon;
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

	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.iv_post_image:
	// Log.d(tag, "image pressed");
	// Intent intent = new Intent(ctx, FullScreenImageActivity.class);
	// intent.putExtra("imageURL", mPosts.get(position).getUrl());
	// ctx.startActivity(intent);
	// break;
	// case R.id.ll_social_like_area:
	// Log.d(tag, "like area");
	// socialLikeClicked(mPosts.get(position));
	// break;
	// case R.id.iv_social_like_icon:
	// Log.d(tag, "like icon");
	// mPosts.get(position);
	// socialLikeClicked(mPosts.get(position));
	// break;
	// case R.id.iv_social_site_icon:
	// Log.d(tag, "site icon");
	// socialLikeClicked(mPosts.get(position));
	// break;
	// }
	// }
}
