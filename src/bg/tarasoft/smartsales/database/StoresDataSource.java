package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Store;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StoresDataSource {

	private MySQLiteOpenHelper dbHelper;
	private SQLiteDatabase database;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_STORE_ID,
			MySQLiteOpenHelper.COLUMN_STORE_NAME, MySQLiteOpenHelper.COLUMN_HALL_ID };
	
	public StoresDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public List<Store> getStores() {
		List<Store> stores = new ArrayList<Store>();

		Cursor cursor = null;
			cursor = database.query(
					MySQLiteOpenHelper.TABLE_STORES,
					allColumns,
					null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Store store = cursorToStore(cursor);
			stores.add(store);
			cursor.moveToNext();
		}
		cursor.close();
		return stores;
	}
	
	private Store cursorToStore(Cursor cursor) {
		Store store = new Store();
		store.setStoreID(cursor.getInt(1));
		store.setStoreName(cursor.getString(2));
		store.setHallId(cursor.getInt(3));
		return store;
	}

	public void insertStores(List<Store> stores) {

		deleteRowsIfTableExists();
		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Store store : stores) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_STORE_ID, store.getStoreID());
			values.put(MySQLiteOpenHelper.COLUMN_STORE_NAME, store.getStoreName());
			values.put(MySQLiteOpenHelper.COLUMN_HALL_ID, store.getHallId());
			database.insert(MySQLiteOpenHelper.TABLE_STORES, null, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}
	
	private void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_STORES + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_STORES, null, null);
		}
	}
}
