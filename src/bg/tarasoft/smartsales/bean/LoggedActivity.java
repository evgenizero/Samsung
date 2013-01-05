package bg.tarasoft.smartsales.bean;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonUseDeserializer;
import org.codehaus.jackson.annotate.JsonUseSerializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

import bg.tarasoft.smartsales.database.LogsDataSource;
import bg.tarasoft.smartsales.utilities.CustomJsonDateDeserializer;
import bg.tarasoft.smartsales.utilities.CustomJsonDateSerializer;
import bg.tarasoft.smartsales.utilities.CustomJsonTypeSerializer;
import bg.tarasoft.smartsales.utilities.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LoggedActivity {
	private static String key = "activity_log";
	private Date date;
	private int activityId;
	private int type;

	public static final int PRODUCT = 0;
	public static final int CATEGORY = 1;
	public static final int SERIE = 2;

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	@JsonUseSerializer(value = CustomJsonDateSerializer.class)
	public Date getDate() {
		return date;
	}

	@JsonUseDeserializer(value = CustomJsonDateDeserializer.class)
	public void setDate(Date date) {
		this.date = date;
	}

	@JsonUseSerializer(value = CustomJsonTypeSerializer.class)
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static void postData(Context context,String data) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://system.smartsales.bg/product/get_android_log/");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			 nameValuePairs.add(new BasicNameValuePair("data", data));
			 nameValuePairs.add(new BasicNameValuePair("uid",
			 Utilities.getDeviceId(context)));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final void sendLog(Context context) {

		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		String json;
		// BufferedOutputStream out = new BufferedOutputStream(System.out);

		try {
			
			OutputStream out = new ByteArrayOutputStream();
			List<LoggedActivity> log = Utilities.getLog(context);
			mapper.writeValue(out, log);
			String data = out.toString();
			Log.d("ASD", data);
			
			postData(context, data);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// mapper.writeValue(new File("user-modified.json"), user);

	}
}
