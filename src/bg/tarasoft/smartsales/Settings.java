package bg.tarasoft.smartsales;

import bg.tarasoft.smartsales.samsung.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class Settings extends Activity{

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private Button changePassword, chooseCategories, chooseStore;
	private Context context;
	private CheckBox updateAll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		context = this;
		
		changePassword = (Button) findViewById(R.id.button1);
		chooseCategories = (Button) findViewById(R.id.button2);
		chooseStore = (Button) findViewById(R.id.button3);
		updateAll = (CheckBox) findViewById(R.id.checkBox);
		
		preferences = getSharedPreferences("settings", 0);
		editor = preferences.edit();
		
		if(preferences.getInt("updateAll", 0) == 1) {
			updateAll.setChecked(true);
		}
		
		updateAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					System.out.println("checked");
					editor.putInt("updateAll", 1);
				} else {
					System.out.println("not checked");
					editor.putInt("updateAll", 0);
				}
				editor.apply();
			}
		});
		
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
		
		chooseStore.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(context, bg.tarasoft.smartsales.StoresList.class);
				startActivity(intent);
			}
		});
		
	}
	
}
