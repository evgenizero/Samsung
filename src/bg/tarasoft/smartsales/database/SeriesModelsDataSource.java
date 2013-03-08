package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SeriesModelsDataSource {

	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_CATEGORY_ID,
			MySQLiteOpenHelper.COLUMN_SERIE_ID };

	public SeriesModelsDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertValues(List<Integer> series, int modelId,
			ProgressDialog progress) {

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Integer i : series) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_SERIE_ID, i);
			values.put(MySQLiteOpenHelper.COLUMN_MODEL_ID, modelId);
			if (database.insert(MySQLiteOpenHelper.TABLE_MODEL_SERIE, null,
					values) != -1) {
				System.out.println("PROD SERIE INSERTED");
			}

		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void insertValues(HashMap<Integer, List<Integer>> map,
			ProgressDialog progress) {

		int size = 0;

		Collection<List<Integer>> values2 = map.values();
		Iterator<List<Integer>> iter = values2.iterator();

		while (iter.hasNext()) {
			size += iter.next().size();
		}

		progress.setMax(size);

		long total = 0;

		Iterator<Entry<Integer, List<Integer>>> it = map.entrySet().iterator();
		ContentValues values = null;
		database.beginTransaction();
		values = new ContentValues();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();

			List<Integer> values3 = (List<Integer>) pairs.getValue();

			if (values3 != null) {

				for (Integer j : values3) {
					values.clear();
					values.put(MySQLiteOpenHelper.COLUMN_SERIE_ID, j);
					values.put(MySQLiteOpenHelper.COLUMN_CATEGORY_ID,
							(Integer) pairs.getKey());
					if (database.insert(MySQLiteOpenHelper.TABLE_MODEL_SERIE,
							null, values) != -1) {

						total++;

						progress.setProgress((int) (total * size) / size);

					}
				}

			}
			it.remove();
		}
		database.setTransactionSuccessful();
		database.endTransaction();

	}

	public void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_MODEL_SERIE + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_MODEL_SERIE, null, null);
		}
	}
}
