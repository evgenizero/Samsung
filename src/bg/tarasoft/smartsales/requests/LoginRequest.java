package bg.tarasoft.smartsales.requests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import bg.tarasoft.smartsales.LoginActivity;
import bg.tarasoft.smartsales.bean.LocationLog;
import bg.tarasoft.smartsales.bean.LoginData;
import bg.tarasoft.smartsales.utilities.Utilities;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginRequest extends AsyncTask<LoginData, Void, String> {

	private Context context;
	private LocationLog locationLog;
	private Object showMessage;
	private CharSequence dialogMessage;
	protected static ProgressDialog dialog;

	public LoginRequest(Context context, LoginData data) {
		this.context = context;
		dialogMessage = "Влизане в системата";
		execute(data);

	}

	protected void cancelDialog() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (!Utilities.isOnline(context)) {
			Toast.makeText(context, "Няма връзка с интернет",
					Toast.LENGTH_SHORT).show();
			cancel(true);
		}

		dialog = new ProgressDialog(context);
		dialog.setMessage(dialogMessage);
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.cancel();
			}
		}

	}

	@Override
	protected String doInBackground(LoginData... params) {
		return sendLoginData(params[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		// System.out.println(result);

		if (!"invalid_email".equals(result) && !"invalid_pass".equals(result)) {
			((LoginActivity) context).processRequest();
		} else if("invalid_email".equals(result)) {
			Toast.makeText(context, "Невалиден email", Toast.LENGTH_SHORT).show();
		} else if("invalid_pass".equals(result)) {
			Toast.makeText(context, "Грешна парола", Toast.LENGTH_SHORT).show();
		}
		cancelDialog();
	}

	public String sendLoginData(LoginData data) {

		String responseMessage = null;

		try {

			System.out.println("SENDING >>>>>>>>>");

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://system.smartsales.bg/user/android_login/");

			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

			nameValuePairs
					.add(new BasicNameValuePair("email", data.getEmail()));

			nameValuePairs.add(new BasicNameValuePair("password", data
					.getPassword()));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

			responseMessage = EntityUtils.toString(response.getEntity());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMessage;
	}

}
