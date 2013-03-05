package bg.tarasoft.smartsales.listeners;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class OnSettingsButtonClick implements OnTouchListener {
	private Context context;
	private Calendar c;

	private Handler requestsHandler = new Handler();

	private int start, end;
	private boolean zero = false;

	private Runnable requestTask = new Runnable() {

		public void run() {
			c = Calendar.getInstance();
			end = c.get(Calendar.SECOND);

			System.out.println("END: " + String.valueOf(end));

			if(end == 0) {
				zero = true;
			}
			
			if(start > 55) {
				if(zero) {
					end+=60;
				}
			}
			
			
			if ((end - start) > 4) {
				// Toast.makeText(context, "blob", Toast.LENGTH_SHORT).show();

				end = 0;
				zero = false;
				
				Intent intent = new Intent(context,
						bg.tarasoft.smartsales.EnterPassword.class);
				context.startActivity(intent);

			} else {
				requestsHandler.postDelayed(requestTask, 1000);
			}
		}
	};

	public OnSettingsButtonClick(Context context) {
		this.context = context;
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN: {
			c = Calendar.getInstance();
			start = c.get(Calendar.SECOND);

			System.out.println("START: " + String.valueOf(start));

			requestsHandler.post(requestTask);

			return true;
		}

		case MotionEvent.ACTION_UP: {
			requestsHandler.removeCallbacks(requestTask);
			return true;

		}

		default: {
			return false;
		}
		}
	}

}
