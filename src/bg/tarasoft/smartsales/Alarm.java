package bg.tarasoft.smartsales;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

public class Alarm extends BroadcastReceiver {
	private Window wind;

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Activity.ACTIVITY_SERVICE);
		int size = am.getRunningTasks(5).size();
		StringBuilder tasks = new StringBuilder();
		for (RunningTaskInfo t : am.getRunningTasks(5)) {
			tasks.append(t.topActivity.getPackageName());
			tasks.append("\n");
		}
//		Toast.makeText(context,
//				"Current running : " + size + "\n" + new String(tasks),
//				Toast.LENGTH_LONG).show(); // For example
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		 boolean isScreenOn = pm.isScreenOn();
		if (!isScreenOn) {
			 WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			//Unlock
			//http://developer.android.com/reference/android/app/Activity.html#getWindow()
	        WakeLock mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YourServie");
	        mWakeLock.acquire();
	        
	        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE); 
            KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
            keyguardLock.disableKeyguard();
   
			
			Intent startServiceIntent = new Intent(context,
					MainCategories.class);
			startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startServiceIntent.putExtra("toReleseLock", true);
			context.startActivity(startServiceIntent);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//	keyguardLock.reenableKeyguard();
			mWakeLock.release();
		}
		// wl.release();
	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 30 * 1, pi); // Millisec * Second * Minute
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}