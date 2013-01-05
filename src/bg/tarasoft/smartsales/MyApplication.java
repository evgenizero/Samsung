package bg.tarasoft.smartsales;

import android.app.Application;
import android.os.PowerManager.WakeLock;

public class MyApplication extends Application {
	private WakeLock mWakeLock;

	public WakeLock getmWakeLock() {
		return mWakeLock;
	}

	public void setmWakeLock(WakeLock mWakeLock) {
		this.mWakeLock = mWakeLock;
	}
}