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

import co.localism.losal.R;
import co.localism.losal.activities.NoticeDetailsActivity;
import co.localism.losal.objects.Notice;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeAdapter extends ArrayAdapter<Notice> {

	private LayoutInflater mInflater;

	private ArrayList<Notice> mNotices;
	private TypedArray mIcons;
	private int mViewResourceId;
	private Context ctx;

	private SharedPreferences user_info;
	private OnClickListener notice_click_listener;
	private PostViewHolder holder;
	private final String tag = "NoticeAdapter";
	private OnClickListener notice_onClick;
	/***** ImageLoader *******/
	public static ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;

	// public ImageAndTextAdapter(Context ctx, int viewResourceId,
	// String[] strings, TypedArray icons, ArrayList<Integer> openclose,
	// ArrayList<String> companies, ArrayList<String> place_ids) {
	public NoticeAdapter(final Context ctx, int viewResourceId,
			ArrayList<Notice> notices, int UserType) {
		super(ctx, viewResourceId);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mNotices = notices;
		mViewResourceId = viewResourceId;
		this.ctx = ctx;

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
	public void add(Notice n) {
		mNotices.add(n);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mNotices.size();
	}

	@Override
	public Notice getItem(int position) {
		return mNotices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Notice cur = mNotices.get(position);

		if (convertView == null) {
			// Put things in here that only need to be set once and can be
			// reused.
			convertView = mInflater.inflate(mViewResourceId, null);
			holder = new PostViewHolder();
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_notice_title);
			holder.tv_details = (TextView) convertView
					.findViewById(R.id.tv_notice_details);
			holder.iv_image = (ImageView) convertView
					.findViewById(R.id.iv_image);
			holder.iv_chevron = (ImageView) convertView
					.findViewById(R.id.
			iv_chevron);
			holder.tv_notice_date = (TextView) convertView
					.findViewById(R.id.tv_notice_date);
			// Post Image onclick only needs to be set once
			// notice_click_listener = new OnClickListener() {
			//
			// @Override
			// public void onClick(View view) {
			// Log.d(tag, "item pressed");
			// Intent intent = new Intent(ctx, NoticeDetailsActivity.class);
			// // intent.putExtra("imageURL", cur.getUrl());
			// intent.putExtra("title", getItem(position).getTitle());
			// intent.putExtra("details_text", getItem(position)
			// .getDetails());
			// ctx.startActivity(intent);
			// }
			//
			// };
			convertView.setTag(holder);

		} else {
			holder = (PostViewHolder) convertView.getTag();
		}

		holder.tv_title.setText(cur.getTitle());
		holder.tv_details.setText(cur.getTeaser());
		// holder.iv_image.setText(cur.getTitle());
		holder.tv_notice_date.setText("Received:  "+cur.getDateReceived());
		mImageLoader.displayImage(cur.getImageUrl(), holder.iv_image, options,
				animateFirstListener);
		return convertView;
	}

	public static class PostViewHolder {
		TextView tv_title;
		TextView tv_details;
		ImageView iv_image;
		ImageView iv_chevron;

		TextView tv_notice_date;
	}

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
