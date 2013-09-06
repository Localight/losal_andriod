package co.localism.losal.adapters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

	private ArrayList<Post> mPosts;
	private ArrayList<Post> copyOfPosts;
	private boolean isFiltered = false;

	private TypedArray mIcons;
	private int mViewResourceId;
	private Context ctx;
	private ImageView TW_IMAGE_VIEW, FB_IMAGE_VIEW, INSTA_IMAGE_VIEW;
	private static final String tag = "PostAdapter", TWITTER = "Twitter",
			FACEBOOK = "Facebook", INSTAGRAM = "Instagram";
	public static Filter filter;
	private static String[] CLASS_YEAR = new String[] { "", "Freshman",
			"Sophomore", "Junior", "Senior" };
	private SharedPreferences user_info, user_likes;
	private OnClickListener activate_onClick, fb_onClick, insta_onClick,
			fullscreen_onClick, tw_onClick;
	public static ImageLoader mImageLoader;
	private PostViewHolder holder;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private TimeHandler TH;
	private Typeface icon_font;
	private DisplayImageOptions options;
	Post cur;
	private Drawable INSTA_ICON, TW_ICON, INSTA_LIKE_ICON, TW_LIKE_ICON,
			INSTA_LIKE_ICON_LIKED, TW_LIKE_ICON_LIKED, ADD_ICON;
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

		INSTA_ICON = new SVGHandler().svg_to_drawable(ctx, R.raw.insta);
		TW_ICON = new SVGHandler().svg_to_drawable(ctx, R.raw.tw);
		INSTA_LIKE_ICON = new SVGHandler().svg_to_drawable(ctx, R.raw.heart);
		TW_LIKE_ICON = new SVGHandler().svg_to_drawable(ctx, R.raw.tw_like);
		TW_LIKE_ICON_LIKED = new SVGHandler().svg_to_drawable(ctx,
				R.raw.tw_like, R.color.white, R.color.localism_blue);
		INSTA_LIKE_ICON_LIKED = new SVGHandler().svg_to_drawable(ctx,
				R.raw.heart, R.color.white, R.color.localism_blue);
		ADD_ICON = new SVGHandler().svg_to_drawable(ctx, R.raw.add);

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

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(ctx));

		options = new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
				// .showStubImage(R.drawable.ic_launcher)
				// .showImageForEmptyUri(R.drawable.ic_launcher)
				// .showImageOnFail(R.drawable.ic_launcher)
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
	public void add(Post p) {
		super.add(p);
		mPosts.add(p);
		notifyDataSetChanged();

	}

	public void add(int index, Post p) {
		mPosts.add(index, p);
		notifyDataSetChanged();

	}

	public void addAll(ArrayList<Post> p) {
		super.addAll(p);
		Log.d(tag, "allAll called!");
		mPosts.addAll(p);
		notifyDataSetChanged();
	}

	public int getSize() {
		if (mPosts == null)
			return mPosts.size();
		else
			return -1;
	}

	public void setStatus(boolean isLoading) {
		this.loading = isLoading;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		cur = mPosts.get(position);
		// if((mPosts.size() == 0 || mPosts.size()-1 == position) && !loading){
		if (mPosts.size() - 4 == position && position > 0 && !loading) {
			Log.d(tag, "FETCHING");
			//
			loading = true;
			FetchFeed fetcher = new FetchFeed();
			fetcher.fetch(this, ctx);
			// // addAll(fetcher.fetch());
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
			holder.time_break_time = (TextView) convertView
					.findViewById(R.id.time_break_time);
			holder.iv_social_like_icon = (ImageView) convertView
					.findViewById(R.id.iv_social_like_icon);
			// holder.iv_user_icon = (ImageView) convertView
			// .findViewById(R.id.iv_user_icon);
			holder.tv_user_icon = (TextView) convertView
					.findViewById(R.id.tv_user_icon);
			holder.post_header = (LinearLayout) convertView
					.findViewById(R.id.post_header);
			holder.iv_post_image = (ImageView) convertView
					.findViewById(R.id.iv_post_image);
			WindowManager wm = (WindowManager) ctx
					.getSystemService(Context.WINDOW_SERVICE);
			int wpx = wm.getDefaultDisplay().getWidth();
			wpx -= (2 * ctx.getResources().getDimension(
					R.dimen.post_side_margin));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, wpx);
			holder.iv_post_image.setLayoutParams(params);
			holder.iv_post_image.setVisibility(View.INVISIBLE);
			holder.ll_social_like_area = (LinearLayout) convertView
					.findViewById(R.id.ll_social_like_area);
			holder.iv_social_site_icon = (ImageView) convertView
					.findViewById(R.id.iv_social_site_icon);
			holder.iv_clock = (ImageView) convertView
					.findViewById(R.id.iv_time);

			holder.tv_post_text = (TextView) convertView
					.findViewById(R.id.tv_post_text);

			holder.ll_social = (LinearLayout) convertView
					.findViewById(R.id.ll_social_like_area);
			holder.time_break = (LinearLayout) convertView
					.findViewById(R.id.time_break);

			// Clock Icon. Only needs to be set once
			holder.iv_clock.setImageDrawable(new SVGHandler().svg_to_drawable(
					ctx, R.raw.clock));
			holder.iv_clock.setAlpha(0.6f);
			holder.iv_clock.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

			convertView.setTag(holder);

		} else {
			holder = (PostViewHolder) convertView.getTag();
		}
		user_info = ctx.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		user_likes = ctx
				.getSharedPreferences("UserLikes", Context.MODE_PRIVATE);

		if (user_info.getBoolean("registered", false)) {
			int userType = 0;

			if (user_info.getString("user_type", "")
					.equalsIgnoreCase("student")) {
				holder.ll_social_like_area.setVisibility(View.VISIBLE);
				holder.tv_post_text.setVisibility(View.VISIBLE);

			} else {
				// parents and adminstrative staff
				// hide social links
				holder.ll_social_like_area.setVisibility(View.INVISIBLE);

			}
			// switch(userType){
			// case 0:
			// break;
			// }

		} else {
			holder.post_header.setVisibility(View.INVISIBLE);
			holder.tv_post_text.setVisibility(View.GONE);
		}

		// TODO: change what is visible based on whether the user is registered
		// or not
		try {
			if (TH.showTimeBreak()) {
				holder.time_break.setVisibility(View.VISIBLE);
				holder.time_break_time.setText(TH.getTimeBreak(cur
						.getPostTime()));
			} else
				holder.time_break.setVisibility(View.GONE);

			holder.tv_name.setText(cur.getName());
			// holder.tv_name.setTextColor(ctx.getResources().getColor(R.color.holo_light_blue));

			holder.tv_class_year.setText(cur.getClassYear());

			holder.tv_time_posted.setText(TH.getTimeAgo(cur.getPostTime()));
			// Log.d(tag, "TimeAgo: " + TH.getTimeAgo(cur.getPostTime()));
			holder.tv_post_text.setText(cur.getText());

			holder.tv_user_icon.setTypeface(icon_font);
			if (cur.getFaveColor().length() > 5)
				holder.tv_user_icon.setTextColor(Color.parseColor(cur
						.getFaveColor()));
			else
				holder.tv_user_icon.setTextColor(Color.WHITE);
			// holder.tv_user_icon.setText(cur.getUserIcon());

			holder.tv_user_icon.setText(cur.getUserIcon().toString());

			/**** Social Site LIKE Icon ****/
			// Log.d(tag, "UL: " +
			// user_likes.getString(cur.getSocialNetworkPostId(), "-1"));
			// Log.d(tag, "POID: " + cur.getParseObjectId());
			if (!cur.getUserLiked())
				if (!user_likes.getString(cur.getSocialNetworkPostId(), "")
						.equalsIgnoreCase(""))
					cur.setUserLiked(true);

			if (cur.getUserLiked()) {
				// holder.iv_social_like_icon.setAlpha(1f);
				if (cur.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM))
					holder.iv_social_like_icon
							.setImageDrawable(INSTA_LIKE_ICON_LIKED);
				else if (cur.getSocialNetworkName().equalsIgnoreCase(TWITTER))
					holder.iv_social_like_icon
							.setImageDrawable(TW_LIKE_ICON_LIKED);
			} else {
				if (cur.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM))
					holder.iv_social_like_icon
							.setImageDrawable(INSTA_LIKE_ICON);
				else if (cur.getSocialNetworkName().equalsIgnoreCase(TWITTER))
					holder.iv_social_like_icon.setImageDrawable(TW_LIKE_ICON);

				// holder.iv_social_like_icon.setAlpha(0.6f);
			}
			// holder.iv_social_like_icon.setAlpha(1f);

			holder.iv_social_like_icon.setLayerType(View.LAYER_TYPE_SOFTWARE,
					null);

			/****** Social Site Icon ******/
			if (cur.getSocialNetworkName().equalsIgnoreCase("instagram")) {
				holder.iv_social_site_icon.setImageDrawable(INSTA_ICON);
				if (user_info.getBoolean("hasInstagram", false)) {
					holder.iv_social_site_icon.setAlpha(1f);
					holder.iv_social_like_icon.setAlpha(1f);

				} else {
					holder.iv_social_site_icon.setAlpha(.6f);
					holder.iv_social_like_icon.setAlpha(.6f);

				}
			} else if (cur.getSocialNetworkName().equalsIgnoreCase("twitter")) {
				holder.iv_social_site_icon.setImageDrawable(TW_ICON);
				if (user_info.getBoolean("hasTwitter", false)) {
					holder.iv_social_site_icon.setAlpha(1f);
					holder.iv_social_like_icon.setAlpha(1f);

				} else {
					holder.iv_social_site_icon.setAlpha(.6f);
					holder.iv_social_like_icon.setAlpha(.6f);

				}
			}
			holder.iv_social_site_icon.setLayerType(View.LAYER_TYPE_SOFTWARE,
					null);

			
			
			
			if (cur.isSystemPost()) {
				holder.tv_name.setText(cur.getText());
				holder.iv_social_like_icon.setImageDrawable(ADD_ICON);
				holder.iv_social_like_icon.setLayerType(
						View.LAYER_TYPE_SOFTWARE, null);
				holder.tv_user_icon.setText("");
				holder.tv_post_text.setText("");

			}

			/****** Post Image ******/
			if (cur.getUrl() != null && cur.getUrl().length() > 3) {
				// this post has an image to display
				holder.tv_post_text
						.setBackgroundResource(R.drawable.gradient_text);
				holder.iv_post_image.setVisibility(View.VISIBLE);
				//
				// Make imageview as tall as it is wide. This will act as a
				// place
				// holder when there is no image loaded yet
				mImageLoader.displayImage(cur.getUrl(), holder.iv_post_image,
						options, animateFirstListener);
				holder.iv_post_image.setTag(cur.getUrl());
				holder.iv_post_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ctx,
								FullScreenImageActivity.class);
						String url = (String) v.getTag();// xx.getUrl();
						intent.putExtra("imageURL", url);// mPosts.get(pos-1).getUrl());
						ctx.startActivity(intent);
					}
				});

			} else {
				// this post does not have an image to display so hide the
				// imageview
				holder.tv_post_text.setBackgroundResource(0);
				holder.iv_post_image.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			Log.e(tag, e.toString());
		}
		holder.ll_social.setTag(position);
		holder.ll_social.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(tag, "social clicked!");
				Log.d(tag, mPosts.get((Integer) v.getTag()).getText());
				int pos = (Integer) v.getTag();
				socialLikeClicked(mPosts.get(pos));
				setSocialIcons(v, pos);
				Log.d(tag, "" + mPosts.get(pos).getUserLiked());
			}
		});

		// if(user_info.getBoolean("hasTwitter", false)){

		// }

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

	/**
	 * This handles changing the appearance of the social icons, inclcuding
	 * social like icon and social site icon. Three States. 1. Inactive social:
	 * alpha 0.6f white. 2. Active social. Post not liked. alpha 1f. white. 3.
	 * Active social. Post liked. alpha 1f blue
	 * 
	 * @param p
	 */
	private void setSocialIcons(View v, int pos) {
		Post p = mPosts.get(pos);
		ImageView iv_like;
		if (v == null)
			iv_like = (ImageView) holder.iv_social_site_icon;
		else
			iv_like = (ImageView) v.findViewById(R.id.iv_social_like_icon);
		if (p.getUserLiked()) {
			// holder.iv_social_like_icon.setAlpha(1f);
			if (p.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM)) {
				iv_like.setImageDrawable(INSTA_LIKE_ICON_LIKED);
				holder.iv_social_like_icon.setAlpha(1f);
				holder.iv_social_site_icon.setAlpha(1f);
			} else if (p.getSocialNetworkName().equalsIgnoreCase(TWITTER)) {
				iv_like.setImageDrawable(TW_LIKE_ICON_LIKED);
				holder.iv_social_like_icon.setAlpha(1f);
				holder.iv_social_site_icon.setAlpha(1f);
			}
		} else {
			if (p.getSocialNetworkName().equalsIgnoreCase(INSTAGRAM)) {
				iv_like.setImageDrawable(INSTA_LIKE_ICON);
				if (!user_info.getBoolean("hasInstagram", false)) {
					holder.iv_social_like_icon.setAlpha(0.6f);
					holder.iv_social_site_icon.setAlpha(0.6f);
				}
			} else if (p.getSocialNetworkName().equalsIgnoreCase(TWITTER)) {
				iv_like.setImageDrawable(TW_LIKE_ICON);
				if (!user_info.getBoolean("hasTwitter", false)) {
					holder.iv_social_like_icon.setAlpha(0.6f);
					holder.iv_social_site_icon.setAlpha(0.6f);
				}

			}
			// holder.iv_social_like_icon.setAlpha(0.6f);
		}
		// holder.iv_social_like_icon.setAlpha(1f);

		holder.iv_social_like_icon.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

	}

	private void socialLikeClicked(Post post) {
		Log.d(tag, post.getText());
		if (hasNetworkConnection()) {
			// TODO: CHECK IF LOGGED IN
			String site = post.getSocialNetworkName();

			if (site.equalsIgnoreCase(ctx.getResources().getString(R.string.tw))) {
				if (user_info.getBoolean("hasTwitter", false)) {
					favTwitter(post.getSocialNetworkPostId(),
							post.getUserLiked(), post.getParseObjectId());
					// new
					// TwitterRequests().execute(post.getSocialNetworkPostId());
					post.setUserLiked(!post.getUserLiked());
				} else
					ctx.startActivity(new Intent(ctx,
							ActivateSocialActivity.class).putExtra("twitter",
							true));
				// MainActivity.favoriteTweet(post.getSocialNetworkPostId());
			} else if (site.equalsIgnoreCase(ctx.getResources().getString(
					R.string.insta)))
				if (user_info.getBoolean("hasInstagram", false)) {
					likeInsta(post.getSocialNetworkPostId(),
							post.getUserLiked(), post.getParseObjectId());
					post.setUserLiked(!post.getUserLiked());

				} else
					ctx.startActivity(new Intent(ctx,
							ActivateSocialActivity.class).putExtra("instagram",
							true));
		} else {
			Toast.makeText(ctx, "No network connection...", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void favTwitter(String id, Boolean already_likes, String objId) {
		// favorites the post on twitter
		SharedPreferences.Editor likesEditor = user_likes.edit();
		if (already_likes) {
			new TwitterRequests().execute(id,
					user_info.getString("user_id", ""), "unfavorite", objId);
			likesEditor.remove(id);
		} else {
			new TwitterRequests().execute(id,
					user_info.getString("user_id", ""), "favorite", objId);
			likesEditor.putString(id, Calendar.getInstance().getTime()
					.toString());
		}
		likesEditor.commit();
		Log.d(tag, "cal " + Calendar.getInstance().getTime().toString());
	}

	public void likeInsta(String id, boolean already_likes, String objId) {
		// like the post on instagram
		SharedPreferences insta_info = ctx.getSharedPreferences(
				"InstagramInfo", Context.MODE_PRIVATE);
		insta_info.getString("access_token", "");
		String request = "like";
		SharedPreferences.Editor likesEditor = user_likes.edit();

		if (already_likes) {
			request = "unlike";
			likesEditor.remove(id);
		} else {
			likesEditor.putString(id, Calendar.getInstance().getTime()
					.toString());
		}
		new InstagramRequests().execute(request, id,
				insta_info.getString("access_token", ""),
				user_info.getString("user_id", ""), objId);
		likesEditor.commit();
	}

	private boolean hasNetworkConnection() {
		ConnectivityManager connMgr = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();// ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		return false;
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
		LinearLayout time_break;
		TextView time_break_time;
		LinearLayout post_header;
		LinearLayout ll_social_like_area;
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

	public void Filter(String filter, HashMap<String, ArrayList<String>> hm) {
		if (isFiltered) {
			removeFilter();
		}
		isFiltered = true;
		copyOfPosts = new ArrayList<Post>();
		copyOfPosts.addAll(mPosts);
		ArrayList<String> al = hm.get(filter);
		Log.i(tag, "filter list: " + al.toString());
		for (int index = 0; index < mPosts.size(); index++) {
			// Log.i(tag, "filter. id: "+mPosts.get(index).getText());
			String id = mPosts.get(index).getParseObjectId();
			Log.i(tag, "filter. id: " + id);

			if (!al.toString().matches(".*" + id + ".*")) {// (id)) {
				Log.i(tag, "filter removing post");
				mPosts.remove(index);
				index--;
			}
		}
		notifyDataSetChanged();

	}

	public void removeFilter() {
		if (copyOfPosts != null) {
			mPosts.removeAll(mPosts);
			this.addAll(copyOfPosts);
			copyOfPosts = null;
		}
		isFiltered = false;
	}

}
