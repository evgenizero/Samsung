package bg.tarasoft.smartsales;

import java.io.File;
import java.util.ArrayList;

import bg.tarasoft.smartsales.bean.ProductsGroup;
import android.app.Application;
import android.os.PowerManager.WakeLock;

public class MyApplication extends Application {
	private WakeLock mWakeLock;
	private ArrayList<ProductsGroup> categoriesForBar = new ArrayList<ProductsGroup>();

	@Override
	public void onCreate() {
		super.onCreate();
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