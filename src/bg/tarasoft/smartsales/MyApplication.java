package bg.tarasoft.smartsales;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import bg.tarasoft.smartsales.bean.ProductsGroup;
import android.app.Application;
import android.os.PowerManager.WakeLock;

public class MyApplication extends Application {
	private WakeLock mWakeLock;
	private ArrayList<ProductsGroup> categoriesForBar = new ArrayList<ProductsGroup>();
	private ImageLoader loader;
	
	@Override
	public void onCreate() {
		super.onCreate();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).build();
		loader = ImageLoader.getInstance();
		loader.init(config);
	}
	
	public ImageLoader getLoader() {
		return loader;
	}

	public WakeLock getmWakeLock() {
		return mWakeLock;
	}

	public void setmWakeLock(WakeLock mWakeLock) {
		this.mWakeLock = mWakeLock;
	}

	public ArrayList<ProductsGroup> getCategoriesForBar() {
		return categoriesForBar;
	}

}