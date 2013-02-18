package bg.tarasoft.smartsales;

import bg.tarasoft.smartsales.bean.LoginData;
import bg.tarasoft.smartsales.requests.LoginRequest;
import bg.tarasoft.smartsales.samsung.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private EditText email, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);

	}

	public void onLoginClick(View v) {
		
		LoginData data = new LoginData();
		data.setEmail(email.getText().toString());
		data.setPassword(password.getText().toString());
		email.setText("");
		password.setText("");
		new LoginRequest(this, data);
	}

	public void onCancelClick(View v) {
		finish();
	}

	public void processRequest() {
		Intent intent = new Intent(this, SubCategoriesActivity.class);
		startActivity(intent);
	}
}
