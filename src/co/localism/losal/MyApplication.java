package co.localism.losal;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.util.Log;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("MyApplication", "My Application was called");
//		set to allow cache
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();

		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
        .defaultDisplayImageOptions(defaultOptions)
		.build();
		ImageLoader.getInstance().init(config);
	}
}
