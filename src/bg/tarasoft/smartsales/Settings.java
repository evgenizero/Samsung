package bg.tarasoft.smartsales;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends Activity{

	private Button changePassword, chooseCategories;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		context = this;
		
		changePassword = (Button) findViewById(R.id.button1);
		chooseCategories = (Button) findViewById(R.id.button2);
		
		changePassword.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(context, bg.tarasoft.smartsales.PasswordChange.class);
				startActivity(intent);
			}
		});
		
		chooseCategories.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(context, bg.tarasoft.smartsales.ChooseCategories.class);
				startActivity(intent);
			}
		});
		
	}
	
}
