package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Model;
import bg.tarasoft.smartsales.bean.Product;

public class ModelsDataSource {
	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;

	public ModelsDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void insertModels(List<Model> models, ProgressDialog progress) {

		deleteRowsIfTableExists();

		int zipLenghtOfFile = models.size();

		progress.setMax(zipLenghtOfFile);

		long total = 0;

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Model model : models) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_MODEL_ID, model.getId());
			values.put(MySQLiteOpenHelper.COLUMN_NAME, model.getModelName());
			values.put(MySQLiteOpenHelper.COLUMN_PIC, model.getModelPicUrl());
			database.insert(MySQLiteOpenHelper.TABLE_MODELS, null, values);

			total++;

			progress.setProgress((int) (total * zipLenghtOfFile)
					/ zipLenghtOfFile);
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	private void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_MODELS + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_MODELS, null, null);
		}
	}

	public List<Model> getModelsBySerie(int serieId) {
		List<Model> models = new ArrayList<Model>();

		Cursor cursor = database
				.rawQuery(
						"select * from models where model_id in (select id from model_series where serie_id=?);",
						new String[] { String.valueOf(serieId) });

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Model model = cursorToModel(cursor);
			models.add(model);
			cursor.moveToNext();
		}
		cursor.close();
		return models;
	}

	private Model cursorToModel(Cursor cursor) {
		Model model = new Model();
		model.setId(cursor.getInt(1));
		model.setModelName(cursor.getString(2));
		model.setModelPicUrl(cursor.getString(3));
		return model;
	}
}
