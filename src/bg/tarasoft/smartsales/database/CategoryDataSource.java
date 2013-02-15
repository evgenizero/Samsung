package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Serie;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoryDataSource {

	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_CATEGORY_ID,
			MySQLiteOpenHelper.COLUMN_PARENT_ID,
			MySQLiteOpenHelper.COLUMN_NAME,
			MySQLiteOpenHelper.COLUMN_SORT_ORDER,
			MySQLiteOpenHelper.COLUMN_PIC, MySQLiteOpenHelper.COLUMN_IS_SHOWN };

	public CategoryDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void updateCategories(List<Category> categories) {

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Category category : categories) {
			
			System.out.println("INSERT: " + category.getName() + "   " + String.valueOf(category.getIsShown()));
			
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_IS_SHOWN,
					category.getIsShown());
			database.update(
					MySQLiteOpenHelper.TABLE_CATEGORIES,
					values,
					MySQLiteOpenHelper.COLUMN_CATEGORY_ID + "="
							+ category.getId(), null);

		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public void insertCategories(List<Category> categories,
			ProgressDialog progress) {

		deleteRowsIfTableExists();

		int zipLenghtOfFile = categories.size();
		System.out.println("NUMBER OF CAT: " + zipLenghtOfFile);

		progress.setMax(zipLenghtOfFile);

		long total = 0;

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Category category : categories) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_CATEGORY_ID, category.getId());
			values.put(MySQLiteOpenHelper.COLUMN_PARENT_ID,
					category.getParentId());
			values.put(MySQLiteOpenHelper.COLUMN_NAME, category.getName());
			values.put(MySQLiteOpenHelper.COLUMN_SORT_ORDER,
					category.getSortOrder());
			values.put(MySQLiteOpenHelper.COLUMN_PIC, category.getImageUrl());
			values.put(MySQLiteOpenHelper.COLUMN_IS_SHOWN, 1);
			database.insert(MySQLiteOpenHelper.TABLE_CATEGORIES, null, values);

			System.out.println("INSERTED");

			total++;

			System.out.println("CAT: " + total);

			progress.setProgress((int) (total * zipLenghtOfFile)
					/ zipLenghtOfFile);
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

	private void deleteRowsIfTableExists() {
		Cursor curs = database.rawQuery(
				"SELECT name FROM sqlite_master WHERE type='table' AND name='"
						+ MySQLiteOpenHelper.TABLE_CATEGORIES + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_CATEGORIES, null, null);
		}
	}

	public List<Category> getAllHiddenCategories(int parentId) {
		List<Category> categories = new ArrayList<Category>();

		Cursor cursor = null;
		if (parentId != -1) {
			cursor = database.query(
					MySQLiteOpenHelper.TABLE_CATEGORIES,
					allColumns,
					MySQLiteOpenHelper.COLUMN_PARENT_ID + "="
							+ String.valueOf(parentId), null, null, null, null);
		}
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return categories;
	}
	
	public List<Category> getCategories(int parentId) {
		List<Category> categories = new ArrayList<Category>();

		Cursor cursor = null;
		if (parentId != -1) {
			cursor = database.query(
					MySQLiteOpenHelper.TABLE_CATEGORIES,
					allColumns,
					MySQLiteOpenHelper.COLUMN_PARENT_ID + "="
							+ String.valueOf(parentId) + " and "
							+ MySQLiteOpenHelper.COLUMN_IS_SHOWN + "="
							+ String.valueOf(1), null, null, null, null);
		} else {
			cursor = database.query(MySQLiteOpenHelper.TABLE_CATEGORIES,
					allColumns, null, null, null, null, null);
		}

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Category category = cursorToCategory(cursor);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		return categories;
	}

	public Category getCategory(int categoryId) {
		Category category = null;
		Cursor cursor = database.query(
				MySQLiteOpenHelper.TABLE_CATEGORIES,
				allColumns,
				MySQLiteOpenHelper.COLUMN_CATEGORY_ID + "="
						+ String.valueOf(categoryId), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			category = cursorToCategory(cursor);
			cursor.moveToNext();
		}
		cursor.close();
		return category;
	}

	public Category getCategory(String categoryName) {
		Category category = null;
		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_CATEGORIES,
				allColumns,
				MySQLiteOpenHelper.COLUMN_NAME + "='" + categoryName + "'"
						+ " and " + MySQLiteOpenHelper.COLUMN_IS_SHOWN + "="
						+ String.valueOf(1), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			category = cursorToCategory(cursor);
			cursor.moveToNext();
		}
		cursor.close();
		return category;
	}

	public boolean containsSubCategories(int categoryId) {

		Cursor cur = database.rawQuery(
				"SELECT COUNT(*) FROM " + MySQLiteOpenHelper.TABLE_CATEGORIES
						+ " where " + MySQLiteOpenHelper.COLUMN_PARENT_ID
						+ " = " + String.valueOf(categoryId), null);
		if (cur != null) {
			cur.moveToFirst();
			if (cur.getInt(0) == 0) {
				return false;
			}
		}

		return true;
	}

	public boolean isEmpty() {
		Cursor cur = database.rawQuery("SELECT COUNT(*) FROM categories", null);
	    if (cur != null){
	        cur.moveToFirst();
	        if (cur.getInt(0) == 0) {
	        	return true;
	        }
	    }
	    return false;
	}
	
	private Category cursorToCategory(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getInt(1));
		category.setParentId(cursor.getInt(2));
		category.setName(cursor.getString(3));
		category.setSortOrder(cursor.getInt(4));
		category.setImageUrl(cursor.getString(5));
		category.setIsShown(cursor.getInt(6));
		return category;
	}
}
