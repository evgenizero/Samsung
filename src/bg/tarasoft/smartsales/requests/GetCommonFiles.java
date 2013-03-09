package bg.tarasoft.smartsales.requests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import bg.tarasoft.smartsales.HTMLPlayerActivity;
import bg.tarasoft.smartsales.parser.ParseXml;
import bg.tarasoft.smartsales.utilities.Decompress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class GetCommonFiles extends AsyncTask<String, String, String> {
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog progress;

	// private String filePath = "/mnt/sdcard/file.zip";

	private static final String BASE_FOLDER = "smartsales";
	private static final String PRODUCTS_FOLDER = "products";
	private Context context;
	private String filePath;

	public GetCommonFiles(Context context) {
		progress = new ProgressDialog(context);
		this.context = context;
		filePath = Environment.getExternalStorageDirectory()
				+ "/common_files.zip";
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		createFolder();
		progress.setMessage("Downloading...");
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setCancelable(false);
		progress.show();
	}

	private void createFolder() {
		File folder = new File(Environment.getExternalStorageDirectory() + "/"
				+ BASE_FOLDER);

		System.out.println("DELETED: " + deleteDirectory(folder));
		folder.mkdir();

		File folder2 = new File(Environment.getExternalStorageDirectory() + "/"
				+ BASE_FOLDER + "/" + PRODUCTS_FOLDER);
		System.out.println("DELETED" + 	deleteDirectory(folder2));
		folder2.mkdir();
	}

	@Override
	protected String doInBackground(String... aurl) {
		int count;

		try {

			URL checksumURL = new URL(aurl[0]);
			URLConnection checksumConnection = checksumURL.openConnection();
			checksumConnection.connect();

			int checksumLenghtOfFile = checksumConnection.getContentLength();
			Log.d("ANDRO_ASYNC", "Lenght of file: " + checksumLenghtOfFile);

			InputStream checksumIN = new BufferedInputStream(
					checksumURL.openStream());

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			Document doc = null;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(checksumIN);

				doc.getDocumentElement().normalize();
				System.out.println("Root element of the doc is "
						+ doc.getDocumentElement().getNodeName());

			} catch (Exception e) {
				e.printStackTrace();
			}
			String dlUrl = ParseXml.parseChecksum(doc);

			URL zipURL = new URL(dlUrl);
			URLConnection zipConnection = zipURL.openConnection();
			zipConnection.connect();

			int zipLenghtOfFile = zipConnection.getContentLength();
			Log.d("ANDRO_ASYNC", "Lenght of file: " + zipLenghtOfFile);

			InputStream zipInput = new BufferedInputStream(zipURL.openStream());

			OutputStream output = new FileOutputStream(filePath);
			byte data[] = new byte[1024];

			long total = 0;

			while ((count = zipInput.read(data)) != -1) {
				total += count;
				progress.setProgress((int) ((total * 100) / zipLenghtOfFile));

				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			zipInput.close();
			checksumIN.close();

		} catch (Exception e) {
		} finally {

			String zipFile = filePath;
			String unzipLocation = Environment.getExternalStorageDirectory()
					+ "/" + BASE_FOLDER + "/" + PRODUCTS_FOLDER + "/";
			System.out.println(unzipLocation);
			// String unzipLocation = "/mnt/sdcard/";
			Decompress d = new Decompress(zipFile, unzipLocation);
			d.unzip();
		}
		return null;

	}

	private static boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	@Override
	protected void onPostExecute(String unused) {
		// dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
		progress.dismiss();

	}
}
