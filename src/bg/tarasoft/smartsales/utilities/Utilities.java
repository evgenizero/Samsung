package bg.tarasoft.smartsales.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import bg.tarasoft.smartsales.HTMLPlayerActivity;
import bg.tarasoft.smartsales.SubCategoriesActivity;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Coordinate;
import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.bean.ProductsGroup;
import bg.tarasoft.smartsales.database.CategoryDataSource;
import bg.tarasoft.smartsales.database.LogsDataSource;
import bg.tarasoft.smartsales.views.HeaderBar;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Utilities {

	public static Coordinate getCoodinates(Context context) {
		LocationManager locationManager =
		        (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		String mlocProvider;
		Criteria hdCrit = new Criteria();

		hdCrit.setAccuracy(Criteria.ACCURACY_COARSE);

		mlocProvider = locationManager.getBestProvider(hdCrit, true);
		
		//TODO !!!!!!!!!!!!!!!!!!! DA SE OPRAVI !!!!!!!!!!!!
		/*Location currentLocation = locationManager.getLastKnownLocation(mlocProvider);
		
		double currentLatitude = currentLocation.getLatitude();
		double currentLongitude = currentLocation.getLongitude();
		*/
		double currentLatitude = 0;
		double currentLongitude = 0;
	
		
		Coordinate coordinates = new Coordinate();
		coordinates.setLatitude(currentLatitude);
		coordinates.setLongitude(currentLongitude);
		
		return coordinates;
	}
	
	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
	public static String getDeviceId(Context context){
		
		   final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		    final String tmDevice, tmSerial, androidId;
		    tmDevice = "" + tm.getDeviceId();
		    tmSerial = "" + tm.getSimSerialNumber();
		    androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		    String deviceId = deviceUuid.toString();
		    Log.d("ASD","DEVICE ID: " + deviceId);
		    return deviceId;
	}
	public static void addToLog(Context context,int loggedId, int type){
		LoggedActivity loggedActivity = new LoggedActivity();
		loggedActivity.setActivityId(loggedId);
		loggedActivity.setDate(new Date());
		loggedActivity.setType(type);
		LogsDataSource lDS = new LogsDataSource(context);
		lDS.open();
		lDS.insertLog(loggedActivity);
		Log.d("TROLL", "LOG SIZE: " + lDS.getLog().size());
		List<LoggedActivity> log = lDS.getLog();
		String logType = "";
		switch(type){
		case LoggedActivity.SERIE:
			logType= "Serie";
			break;
		case LoggedActivity.CATEGORY:
			logType = "Category";
			break;
		case LoggedActivity.PRODUCT:
			logType = "Product";
			break;
		}
		if(log.size() > 0){
		Log.d("TROLL","LAST LOGGED: " + logType+ " "+ log.get(log.size()-1).getActivityId() + " " + LogsDataSource.formatter.format(log.get(log.size()-1).getDate()));
		} else {
			Log.d("TROLL", "LOG IS EMPTY");
		}
		lDS.close();
	}
	public static List<LoggedActivity> getLog(Context context){
		LogsDataSource ds = new LogsDataSource(context);
		ds.open();
		List<LoggedActivity> log = ds.getLog();
	//	ds.close();
		
		return log;
	}
	public static List<Product> cloneList(List<Product> ProductList) {
	    List<Product> clonedList = new ArrayList<Product>(ProductList.size());
	    for (Product p : ProductList) {
	        clonedList.add(new Product(p));
	    }
	    return clonedList;
	}
	
	public static boolean productExistOnSdCard(int productId) {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/" + HTMLPlayerActivity.BASE_FOLDER + "/"
				+ HTMLPlayerActivity.PRODUCTS_FOLDER + "/" + productId
				+ "/" + productId + ".html");
		return file.exists();
	}
	public static void returnToFirst(Context mContext,TextView headerLabel,boolean shouldFinish) {
		Intent intent = new Intent(mContext,
				SubCategoriesActivity.class);
		CategoryDataSource ds = new CategoryDataSource(mContext);
		ds.open();

		Category category = ds.getCategory(""
				+ headerLabel.getText());
		ds.close();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("categoryName", headerLabel.getText());
		intent.putExtra("parentId", category.getId());
		intent.putExtra("shouldFinish", shouldFinish);
		System.out.println("CATEGORY: " + category.getId()
				+ category.getName());

		System.out.println("CATEGOTY: " + category.getId()
				+ category.getName());
		mContext.startActivity(intent);
	}
	
	public static void openProduct(Context context, int productId, ArrayList<ProductsGroup> categoriesForBar) {
		Intent myIntent = new Intent(context, HTMLPlayerActivity.class);
		myIntent.putExtra("productId", productId);
		myIntent.putExtra("headerBar", categoriesForBar);
		context.startActivity(myIntent);
	}
	public static void startSecond(Context mContext,TextView headerLabel2,TextView headerLabel, boolean noSeries) {
		Intent intent = new Intent(mContext,
				SubCategoriesActivity.class);
		CategoryDataSource ds = new CategoryDataSource(mContext);
		ds.open();

		Category category = ds.getCategory(headerLabel2
				.getText() + "");
		ds.close();

		intent.putExtra("subCatName", headerLabel2.getText());
		intent.putExtra("categoryName", headerLabel.getText());
		intent.putExtra("categoryId", category.getId());
		if(noSeries){
			intent.putExtra("noSeries", true);
		}

		System.out.println(headerLabel2.getText());
		System.out.println(headerLabel.getText());
		System.out.println(category.getId());
		
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		mContext.startActivity(intent);
	}

}