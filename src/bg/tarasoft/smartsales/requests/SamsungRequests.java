package bg.tarasoft.smartsales.requests;

import java.net.HttpURLConnection;
import java.net.URL;

import bg.tarasoft.smartsales.utilities.Utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public abstract class SamsungRequests extends AsyncTask<Void, Void, Void> {

	protected boolean error = true;

	private String baseUrl = "http://system.smartsales.bg/product/xml_categories/";
	protected Context context;

	protected String showMessage;
	protected String initialUrl;
	
	protected URL url;

	protected HttpURLConnection http;
	
	protected static RequestExecutor executor;

	public static RequestExecutor getExecutor() {
		return executor;
	}
	
	protected SamsungRequests(Context context, String showMessage, String baseUrl) {
		this(context, showMessage);
		this.baseUrl = baseUrl;
	}
	
	protected SamsungRequests(Context context, String showMessage) {
		this.context = context;
		this.showMessage = showMessage;
		
		if(executor == null) {
			executor = new RequestExecutor();
		}
		executor.addRequest(this);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (!Utilities.isOnline(context)) {
			cancel(true);
		}
	}

	protected void openConnection() throws Exception {
		url = new URL(baseUrl);
		System.setProperty("http.keepAlive", "false");
		http = (HttpURLConnection) url.openConnection();
	}

	@Override
	protected void onPostExecute(Void result) {
		if(!error) {
			System.out.println("DONE: " + this.toString());
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (!Utilities.isOnline(context)) {
			Toast.makeText(context,
					"No internet connection right now. Please try again later",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	protected void executeRequest() {
		execute();
	}
}