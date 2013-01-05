package bg.tarasoft.smartsales;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PasswordChange extends Activity {

	private SharedPreferences preferences;
	private EditText oldPass, newPassOne, newPassTwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);

		preferences = getSharedPreferences("settings", 0);
		oldPass = (EditText) findViewById(R.id.oldPassword);
		newPassOne = (EditText) findViewById(R.id.newPasswordOne);
		newPassTwo = (EditText) findViewById(R.id.newPasswordTwo);
	}

	public void onChangePassword(View v) {

		if (oldPass.getText().toString()
				.equals(preferences.getString("password", "1234"))) {
			if (newPassOne.getText().toString()
					.equals(newPassTwo.getText().toString())) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putString("password", newPassTwo.getText().toString());
				editor.apply();
			}
		}

		finish();
	}
	
	public void onCancelClick(View v) {
		finish();
	}
}
