package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.bean.StoreType;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.SyncStateContract.Columns;

public class StoreRetailDataSource {

	private MySQLiteOpenHelper dbHelper;
	private SQLiteDatabase database;
	private StoresDataSource storesDataSource;
	private Context context;
	
	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_STORE_RETAIL_ID,
			MySQLiteOpenHelper.COLUMN_NAME };

	public StoreRetailDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
		this.context = context;
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public List<StoreType> getStoreTypes() {
		List<StoreType> storeRetails = new ArrayList<StoreType>();

		storesDataSource = new StoresDataSource(context, database);
		
		Cursor cursor = null;
		cursor = database.query(MySQLiteOpenHelper.TABLE_STORE_RETAILS, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			StoreType storeRetail = cursorToStoreRetail(cursor);
			storeRetails.add(storeRetail);
			cursor.moveToNext();
		}
		cursor.close();

		List<Store> stores = null;
		for(StoreType storeRetail : storeRetails) {
			stores = storesDataSource.getStores(storeRetail.getId());
			storeRetail.setStores(stores);
		}
		
		return storeRetails;
	}

	private StoreType cursorToStoreRetail(Cursor cursor) {
		StoreType store = new StoreType();
		store.setId(cursor.getInt(1));
		store.setName(cursor.getString(2));
		return store;
	}

	public void insertStoreRetails(List<StoreType> storeRetails) {
		storesDataSource = new StoresDataSource(context, database);
		deleteRowsIfTableExists();
		storesDataSource.deleteRowsIfTableExists();
		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (StoreType storeRetail : storeRetails) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_STORE_RETAIL_ID,
					storeRetail.getId());
			values.put(MySQLiteOpenHelper.COLUMN_NAME, storeRetail.getName());
			database.insert(MySQLiteOpenHelper.TABLE_STORE_RETAILS, null, values);
			
			storesDataSource.insertStores(storeRetail.getStores(), storeRetail.getId());
			
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		
	}

	private void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_STORE_RETAILS + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_STORE_RETAILS, null, null);
		}
	}
}
