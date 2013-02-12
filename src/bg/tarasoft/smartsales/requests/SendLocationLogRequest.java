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

import bg.tarasoft.smartsales.bean.LocationLog;
import bg.tarasoft.smartsales.utilities.Utilities;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class SendLocationLogRequest extends AsyncTask<LocationLog, Void, Void> {

	private Context context;
	private LocationLog locationLog;

	public SendLocationLogRequest(Context context) {
		this.context = context;

		SharedPreferences preferences = context.getSharedPreferences(
				"settings", 0);

		LocationLog data = new LocationLog();
		data.setCoordinates(Utilities.getCoodinates(context));
		data.setDeviceId(Utilities.getDeviceId(context));
		data.setStoreId(preferences.getInt("store_id", -1));

		execute(data);

	}

	@Override
	protected Void doInBackground(LocationLog... params) {
		sendLocationLog(params[0]);
		return null;
	}

	public void sendLocationLog(LocationLog data) {

		try {
			
			System.out.println("SENDING >>>>>>>>>");
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://system.smartsales.bg/product/get_android_gps_log/");
			
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("device_id", data
					.getDeviceId()));
			nameValuePairs.add(new BasicNameValuePair("latitude", String
					.valueOf(data.getCoordinates().getLatitude())));
			nameValuePairs.add(new BasicNameValuePair("longitude", String
					.valueOf(data.getCoordinates().getLongitude())));
			nameValuePairs.add(new BasicNameValuePair("store_object_id", String
					.valueOf(data.getStoreId())));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			final String responseText =  EntityUtils.toString(response.getEntity());
			
			System.out.println(responseText);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
