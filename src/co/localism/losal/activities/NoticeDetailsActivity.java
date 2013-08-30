package co.localism.losal.activities;

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
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeDetailsActivity extends Activity {

	/***** ImageLoader *******/
	public static ImageLoader mImageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;

	private String URL = ""; //"http://www.youtube.com/watch?v=Ojf6sBEd4-A";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_details);

		Bundle extras = getIntent().getExtras();
		TextView tv_title = (TextView) findViewById(R.id.tv_notice_details_title);
		TextView tv_details = (TextView) findViewById(R.id.tv_notice_details);
		ImageView iv = (ImageView) findViewById(R.id.iv_notice_details_image);
		Button btn = (Button) findViewById(R.id.btn_notice_details);

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(this));

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

		if (extras != null) {
			// iv.setImageResource(extras.getInt("imageID"));
			mImageLoader.displayImage(extras.getString("image_url"), iv,
					options, animateFirstListener);
			URL = extras.getString("link_url", "");
			// tv_title.setText(extras.getString("url", ""));
			tv_title.setText(extras.getString("title", ""));
			tv_details.setText(extras.getString("details_text", ""));

			btn.setText(extras.getString("btn_text", "Learn More!"));
			btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					openURL();
				}

			});
		}
	}

	public void openURL() {
		if (URL.length() > 0) {
			try {
				Intent openURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
				startActivity(openURL);
			} catch (Exception e) {

			}
		}
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
