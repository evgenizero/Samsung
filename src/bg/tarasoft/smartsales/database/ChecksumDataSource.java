package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Checksum;
import bg.tarasoft.smartsales.bean.Product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ChecksumDataSource {

	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_TYPE, MySQLiteOpenHelper.COLUMN_CHECKSUM };
	private Context context;

	public ChecksumDataSource(Context context) {
		
		dbHelper = new MySQLiteOpenHelper(context);
		this.context = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	
	public void insertChecksum(Checksum checksum) {
		if (getChecksum(checksum.getType()) != null) {
			deleteChecksum(checksum);
		}
		ContentValues values = null;
		values = new ContentValues();

		values.put(MySQLiteOpenHelper.COLUMN_TYPE, checksum.getType());
		values.put(MySQLiteOpenHelper.COLUMN_CHECKSUM, checksum.getValue());
		database.insert(MySQLiteOpenHelper.TABLE_CHECKSUMS, null, values);
		System.out.println("CHECKSUM INSTERTED");
	}

	public boolean deleteChecksum(Checksum checksum) {
		return database
				.delete(MySQLiteOpenHelper.TABLE_CHECKSUMS,
						MySQLiteOpenHelper.COLUMN_TYPE + "= '"
								+ checksum.getType() + "'", null) > 0;
	}

	private void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_PRODUCTS + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_PRODUCTS, null, null);
		}
	}

	public Checksum getChecksum(String type) {
		Checksum result = null;
		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CHECKSUMS,
				allColumns, MySQLiteOpenHelper.COLUMN_TYPE + " = '" + type
						+ "'", null, null, null, null);
		if (cursor.moveToLast()) {
			result = cursorToChecksum(cursor);
		}

		cursor.close();
		return result;
	}

	private Checksum cursorToChecksum(Cursor cursor) {
		Checksum checksum = new Checksum();
		checksum.setId(cursor.getInt(0));
		checksum.setType(cursor.getString(1));
		checksum.setValue(cursor.getString(2));
		return checksum;
	}
}