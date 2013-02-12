package bg.tarasoft.smartsales;

import bg.tarasoft.smartsales.samsung.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class EnterPassword extends Activity {

	private EditText password;
	private Context context;

	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pincode);

		context = this;

		preferences = getSharedPreferences("settings", 0);

		password = (EditText) findViewById(R.id.password);
		password.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					String storedPass = null;

					storedPass = preferences.getString("password", "1234");

					if (storedPass.equals(password.getText().toString())) {
						Intent intent = new Intent(context,
								bg.tarasoft.smartsales.Settings.class);
						password.setText("");
						context.startActivity(intent);
					} else {
						Toast.makeText(context, "Грешна парола.",
								Toast.LENGTH_SHORT).show();
						password.setText("");
					}

					return true;
				}
				return false;
			}
		});

	}

}
