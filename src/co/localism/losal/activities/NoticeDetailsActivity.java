package co.localism.losal.activities;

import co.localism.losal.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeDetailsActivity extends Activity {

	private String URL = "http://www.youtube.com/watch?v=Ojf6sBEd4-A";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_details);
		Bundle extras = getIntent().getExtras();

		ImageView iv = (ImageView) findViewById(R.id.iv_notice_details_image);
		// iv.setImageResource(extras.getInt("imageID"));

		TextView tv_title = (TextView) findViewById(R.id.tv_notice_details_title);
		TextView tv_details = (TextView) findViewById(R.id.tv_notice_details);

//		tv_title.setText(extras.getString("url", ""));
		tv_title.setText(extras.getString("title", ""));
		tv_details.setText(extras.getString("details_text", ""));

		Button btn = (Button) findViewById(R.id.btn_notice_details);
		btn.setText(extras.getString("btn_text", "Learn More!"));
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openURL();
			}

		});
	}

	public void openURL() {
		Intent openURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
		startActivity(openURL);
	}
}
