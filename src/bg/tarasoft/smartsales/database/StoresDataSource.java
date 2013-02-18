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
import android.provider.SyncStateContract.Columns;

public class StoresDataSource {

	private MySQLiteOpenHelper dbHelper;
	private SQLiteDatabase database;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_STORE_ID,
			MySQLiteOpenHelper.COLUMN_STORE_NAME,
			MySQLiteOpenHelper.COLUMN_HALL_ID };

	public StoresDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public StoresDataSource(Context context, SQLiteDatabase database) {
		this(context);
		this.database = database;
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public List<Store> getStores(int storeRetailId) {
		List<Store> stores = new ArrayList<Store>();

		Cursor cursor = null;
		cursor = database
				.query(MySQLiteOpenHelper.TABLE_STORES, allColumns,
						MySQLiteOpenHelper.COLUMN_STORE_RETAIL_ID + "="
								+ String.valueOf(storeRetailId), null, null,
						null, null);
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

	public void insertStores(List<Store> stores, int storeRetailId) {

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Store store : stores) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_STORE_ID, store.getStoreID());
			values.put(MySQLiteOpenHelper.COLUMN_STORE_NAME,
					store.getStoreName());
			values.put(MySQLiteOpenHelper.COLUMN_HALL_ID, store.getHallId());
			values.put(MySQLiteOpenHelper.COLUMN_STORE_RETAIL_ID, storeRetailId);
			database.insert(MySQLiteOpenHelper.TABLE_STORES, null, values);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void deleteRowsIfTableExists() {
		System.out.println("DATABASEEE: " + database);
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_STORES + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_STORES, null, null);
		}
	}
}
