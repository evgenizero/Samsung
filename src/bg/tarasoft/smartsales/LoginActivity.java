package bg.tarasoft.smartsales;

import bg.tarasoft.smartsales.bean.LoginData;
import bg.tarasoft.smartsales.requests.LoginRequest;
import bg.tarasoft.smartsales.samsung.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText email, password;
	private CheckBox checkBoxLogin;
	
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		checkBoxLogin = (CheckBox) findViewById(R.id.checkBoxLogin);
		
		preferences = getSharedPreferences("settings", 0);
		
		if(preferences.getBoolean("remember", false)) {
			processRequest();
		} 
	}

	public void onLoginClick(View v) {
		
		LoginData data = new LoginData();
		data.setEmail(email.getText().toString());
		data.setPassword(password.getText().toString());
		email.setText("");
		password.setText("");
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean("guest", false);
		edit.apply();
		new LoginRequest(this, data);
	}

	public void onGuestClick(View v) {
		checkBoxLogin.setChecked(false);
		SharedPreferences.Editor edit = preferences.edit();
		edit.putBoolean("guest", true);
		edit.apply();
		processRequest();
	}
	
	public void onCancelClick(View v) {
		finish();
	}

	public void processRequest() {
		SharedPreferences.Editor edit = preferences.edit();
		if(checkBoxLogin.isChecked()) {
			edit.putBoolean("remember", true);
		} 
		edit.apply();
		
		Intent intent = new Intent(this, SubCategoriesActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		
	}
}
