package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Serie;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SeriesDataSource {

	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_CATEGORY_ID,
			MySQLiteOpenHelper.COLUMN_SERIE_ID, MySQLiteOpenHelper.COLUMN_NAME,
			MySQLiteOpenHelper.COLUMN_PIC };

	public SeriesDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertSeries(List<Serie> series) {
		// deleteRowsIfTableExists();

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Serie serie : series) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_SERIE_ID, serie.getId());
			values.put(MySQLiteOpenHelper.COLUMN_CATEGORY_ID,
					serie.getCategoryId());
			if (database.insert(MySQLiteOpenHelper.TABLE_SERIES, null, values) != -1) {
				System.out.println("SERIE INSERTED");
			}

		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	// public void insertSeries(List<Serie> series, ProgressDialog progress) {
	//
	// deleteRowsIfTableExists();
	//
	// int zipLenghtOfFile = series.size();
	// System.out.println("NUMBER OF SER: " + zipLenghtOfFile);
	//
	// long total = 0;
	//
	// ContentValues values = null;
	// for (Serie serie : series) {
	// values = new ContentValues();
	// values.put(MySQLiteOpenHelper.COLUMN_CATEGORY_ID, serie.getId());
	// values.put(MySQLiteOpenHelper.COLUMN_NAME, serie.getName());
	// values.put(MySQLiteOpenHelper.COLUMN_PIC, serie.getPicUrl());
	// database.insert(MySQLiteOpenHelper.TABLE_SERIES, null, values);
	//
	// System.out.println("INSERTED");
	//
	// total++;
	//
	// System.out.println("PROD: " + total);
	//
	// progress.setProgress((int) (total * 100) / zipLenghtOfFile);
	// }
	// }

	public void updateSeries(List<Serie> series, ProgressDialog progress) {
		int zipLenghtOfFile = series.size();
		System.out.println("NUMBER OF SER: " + zipLenghtOfFile);

		long total = 0;

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Serie serie : series) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_NAME, serie.getName());
			values.put(MySQLiteOpenHelper.COLUMN_PIC, serie.getPicUrl());
			database.update(MySQLiteOpenHelper.TABLE_SERIES, values,
					MySQLiteOpenHelper.COLUMN_SERIE_ID + "=" + serie.getId(),
					null);

			System.out.println("INSERTED");

			total++;

			System.out.println("PROD: " + total);

			progress.setProgress((int) (total * 100) / zipLenghtOfFile);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	// public void deleteTip(Tip tip) {
	// long id = tip.getId();
	// System.out.println("Comment deleted with id: " + id);
	// database.delete(MySQLiteOpenHelper.TABLE_TIPS,
	// MySQLiteOpenHelper.COLUMN_ID + " = " + id, null);
	// }

	public void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_SERIES + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_SERIES, null, null);
		}
	}

	public List<Serie> getSeries(int categoryId) {
		List<Serie> series = new ArrayList<Serie>();

		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_SERIES,
				allColumns, MySQLiteOpenHelper.COLUMN_CATEGORY_ID + "="
						+ String.valueOf(categoryId), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Serie serie = cursorToSerie(cursor);
			series.add(serie);
			cursor.moveToNext();
		}
		cursor.close();
		return series;
	}

	public Serie getSerie(int serieId) {
		List<Serie> series = new ArrayList<Serie>();
		Serie serie = null;
		Cursor cursor = database.query(
				MySQLiteOpenHelper.TABLE_SERIES,
				allColumns,
				MySQLiteOpenHelper.COLUMN_SERIE_ID + "="
						+ String.valueOf(serieId), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			serie = cursorToSerie(cursor);
			series.add(serie);
			cursor.moveToNext();
		}
		cursor.close();
		return serie;
	}

	private Serie cursorToSerie(Cursor cursor) {
		Serie serie = new Serie();
		serie.setCategoryId(cursor.getInt(1));
		serie.setId(cursor.getInt(2));
		serie.setName(cursor.getString(3));
		serie.setPicUrl(cursor.getString(4));
		return serie;
	}

	public boolean isEmpty() {
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM series", null);
	    if (cur != null){
	        cur.moveToFirst();
	        if (cur.getInt(0) == 0) {
	        	return true;
	        }
	    }
	    return false;
	}
	
	public boolean containsSeries(int categoryId) {

		Cursor cur = database.rawQuery(
				"SELECT COUNT(*) FROM " + MySQLiteOpenHelper.TABLE_SERIES
						+ " where id = " + String.valueOf(categoryId), null);
		if (cur != null) {
			cur.moveToFirst();
			if (cur.getInt(0) == 0) {
				return false;
			}
		}

		return true;
	}

}
