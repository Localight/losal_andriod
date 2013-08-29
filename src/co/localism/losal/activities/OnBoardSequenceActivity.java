package co.localism.losal.activities;

import java.util.ArrayList;
import java.util.List;

import co.localism.losal.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class OnBoardSequenceActivity extends FragmentActivity {

	MyPageAdapter pageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onboardsequence);
		List<Fragment> fragments = getFragments();
		pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);
	}

	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		fList.add(MyFragment.newInstance("Fragment 1", 1));
		fList.add(MyFragment.newInstance("Fragment 2", 2));
		fList.add(MyFragment.newInstance("Fragment 3", 3));

		return fList;
	}

	class MyPageAdapter extends FragmentPagerAdapter {
		private List<Fragment> fragments;

		public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		@Override
		public int getCount() {
			return this.fragments.size();
		}
	}

	public static class MyFragment extends Fragment {
		public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

			
		
		public final static MyFragment newInstance(String message, int screen_number) {
			MyFragment f = new MyFragment();
			Bundle bdl = new Bundle(2);
			bdl.putString(EXTRA_MESSAGE, message);
			bdl.putInt("screen", screen_number);

			f.setArguments(bdl);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			String message = getArguments().getString(EXTRA_MESSAGE);
			int screen =  getArguments().getInt("screen");
			View v = null;
			TextView tv_title; 
			TextView tv_subtitle_1;
			TextView tv_subtitle_2;
			switch(screen){
			case 1:
				v = inflater.inflate(R.layout.onboard_page, container, false);
				tv_title = (TextView) v.findViewById(R.id.ob_title);
				tv_subtitle_1 = (TextView) v.findViewById(R.id.ob_subtitle);
				tv_subtitle_2 = (TextView) v.findViewById(R.id.ob_subtitle_2);
				tv_title.setText(R.string.ob_1_title_1);
				tv_subtitle_1.setText(R.string.ob_1_subtitle_1);
				tv_subtitle_2.setText(R.string.ob_1_subtitle_2);

				break;
			case 2:
				v = inflater.inflate(R.layout.onboard_page, container, false);
				tv_title = (TextView) v.findViewById(R.id.ob_title);
				tv_subtitle_1 = (TextView) v.findViewById(R.id.ob_subtitle);
				tv_subtitle_2 = (TextView) v.findViewById(R.id.ob_subtitle_2);
				tv_title.setText(R.string.ob_2_title_1);
				tv_subtitle_1.setText(R.string.ob_2_subtitle_1);
				tv_subtitle_2.setText(R.string.ob_2_subtitle_2);

				break;
				
			case 3:
				v = inflater.inflate(R.layout.onboard_verify, container, false);
				TextView tv_message = (TextView) v.findViewById(R.id.tv_ob_verify_message);
				tv_message.setText(R.string.verify_start);
				TextView tv_lower_message= (TextView) v.findViewById(R.id.tv_ob_verify_lower_message);
				tv_lower_message.setText(R.string.enter_phone);
				Button btn_verify = (Button) v.findViewById(R.id.btn_verify);
				btn_verify.setText(R.string.verify_button);
				break;
			}
//				TextView messageTextView = (TextView) v.findViewById(R.id.textView);
//				messageTextView.setText(message);

			return v;
		}
	}

}