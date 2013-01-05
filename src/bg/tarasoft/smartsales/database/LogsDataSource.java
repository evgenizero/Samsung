package bg.tarasoft.smartsales.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import bg.tarasoft.smartsales.bean.LoggedActivity;
import bg.tarasoft.smartsales.bean.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;

public class LogsDataSource {

//	private static final String dateTemplate = "dMyyyy";
//	private static SimpleDateFormat formatter = new SimpleDateFormat(
//			dateTemplate);

	public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;
	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_DATE, MySQLiteOpenHelper.COLUMN_LOG_ID, MySQLiteOpenHelper.COLUMN_LOG_TYPE };

	public LogsDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertLog(LoggedActivity loggedActivity) {
		CharSequence chSeq = formatter.format(
				loggedActivity.getDate());

		
		
		ContentValues values = new ContentValues();
		values.put(MySQLiteOpenHelper.COLUMN_DATE, String.valueOf(chSeq));

		values.put(MySQLiteOpenHelper.COLUMN_LOG_ID,
				loggedActivity.getActivityId());
		values.put(MySQLiteOpenHelper.COLUMN_LOG_TYPE, loggedActivity.getType());

		database.insert(MySQLiteOpenHelper.TABLE_LOGS, null, values);

	}

	
	public List<LoggedActivity> getLog()  {


		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_LOGS,
				allColumns,null,
				null, null, null, null);
		cursor.moveToFirst();

		List<LoggedActivity> logs = new ArrayList<LoggedActivity>();
		while(!cursor.isAfterLast()){
			try {
				logs.add(cursorToLog(cursor));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cursor.moveToNext();
		}

		cursor.close();

		return logs;
	}

	
	private LoggedActivity cursorToLog(Cursor cursor) throws ParseException {
		LoggedActivity loggedActivity = new LoggedActivity();
		loggedActivity.setDate(formatter.parse(cursor.getString(1)));
		loggedActivity.setActivityId(cursor.getInt(2));
		loggedActivity.setType(cursor.getInt(3));
		return loggedActivity;
	}

}
