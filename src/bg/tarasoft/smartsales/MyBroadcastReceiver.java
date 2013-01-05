package bg.tarasoft.smartsales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Alarm alarm = new Alarm();
		alarm.SetAlarm(context);
		Intent startServiceIntent = new Intent(context, MainCategories.class);
		startServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startServiceIntent);
		// context.startService(startServiceIntent);
	}
}
