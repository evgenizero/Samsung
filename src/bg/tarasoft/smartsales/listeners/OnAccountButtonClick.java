package bg.tarasoft.smartsales.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;

public class OnAccountButtonClick implements OnClickListener {

	private Context context;
	private SharedPreferences preferences;

	public OnAccountButtonClick(Context context) {
		this.context = context;
		preferences = context.getSharedPreferences("settings", 0);
	}

	public void onClick(View arg0) {
		Intent intent = new Intent(context,
				bg.tarasoft.smartsales.LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean("remember", false);
		edit.apply();

		context.startActivity(intent);
	}

}
