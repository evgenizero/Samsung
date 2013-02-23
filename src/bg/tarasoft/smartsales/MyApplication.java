package bg.tarasoft.smartsales;

import java.util.ArrayList;

import bg.tarasoft.smartsales.bean.ProductsGroup;
import android.app.Application;
import android.os.PowerManager.WakeLock;

public class MyApplication extends Application {
	private WakeLock mWakeLock;
	private ArrayList<ProductsGroup> categoriesForBar = new ArrayList<ProductsGroup>();

	
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