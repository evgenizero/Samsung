package bg.tarasoft.smartsales.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;

public abstract class SamsungGetRequest extends SamsungRequests {

	protected static ProgressDialog dialog, progress;

	protected SamsungGetRequest(Context context, String showMessage, String baseUrl) {
		super(context, showMessage, baseUrl);
	}
	
	protected SamsungGetRequest(Context context, String showMessage) {
		super(context, showMessage);
	}

	protected void cancelDialog() {
		if (dialog != null) {
			dialog.cancel();
		}
		if(progress != null) {
			progress.cancel();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

//		if (showMessage != null) {
//			if (dialog != null && !dialog.getContext().equals(context)) {
//				dialog.cancel();
//				dialog = null;
//			}
//			if (dialog == null)
//				dialog = new ProgressDialog(context);
//			dialog.setTitle(showMessage + "...");
//			dialog.setCancelable(true);
//			dialog.show();
//			dialog.setOnCancelListener(new OnCancelListener() {
//				public void onCancel(DialogInterface dialog) {
//					cancel(true);
//				}
//			});
//		}
		
		if(showMessage != null) {
			progress = new ProgressDialog(context);
			progress.setMessage(showMessage);
			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress.setCancelable(false);
			progress.show();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			openConnection();
			http.setRequestProperty("Accept", "application/xml");
			if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
				readXmlStream(http.getInputStream());
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (http != null) {
				http.disconnect();
			}
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		cancelDialog();
		// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!
		// ((BugzillaActivity) context).returnToPreviousActivity();
	}

	private void readXmlStream(InputStream in) throws IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(in);
			processStream(doc);
			
			doc.getDocumentElement ().normalize ();
            System.out.println ("Root element of the doc is " + 
                 doc.getDocumentElement().getNodeName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		error = false;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		cancelDialog();
		if (error) {
			Toast.makeText(context, "Unable to contact server",
					Toast.LENGTH_SHORT).show();
			// TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// ((BugzillaActivity) context).returnToPreviousActivity();
			executor.clear();
		} else {
			executor.execute();
		}
	}

	protected abstract void processStream(Document doc);

}
