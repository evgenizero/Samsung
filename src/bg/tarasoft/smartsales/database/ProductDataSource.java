package bg.tarasoft.smartsales.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Product;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProductDataSource {

	private SQLiteDatabase database;
	private MySQLiteOpenHelper dbHelper;

	private String[] allColumns = { MySQLiteOpenHelper.COLUMN_ID,
			MySQLiteOpenHelper.COLUMN_CATEGORY_ID,
			MySQLiteOpenHelper.COLUMN_NAME,
			MySQLiteOpenHelper.COLUMN_PARENT_CATEGORY_ID,
			MySQLiteOpenHelper.COLUMN_PIC, MySQLiteOpenHelper.COLUMN_STATUS,
			MySQLiteOpenHelper.COLUMN_PRODUCT_PRICE,
			MySQLiteOpenHelper.COLUMN_MODEL_ID };

	public ProductDataSource(Context context) {
		dbHelper = new MySQLiteOpenHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public HashMap<Integer, Integer> getNumberOfProducts(
			List<Category> categories) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		try {
			StringBuilder query = new StringBuilder();
			for (int i = 0; i < categories.size() - 1; i++) {
				query.append(String.valueOf(categories.get(i).getId()) + ",");
			}
			if (categories.size() != 0) {
				query.append(String.valueOf(categories.get(
						categories.size() - 1).getId()));
			}
			Cursor cursor = database.rawQuery(
					"select category_id, count(*) from products where category_id in ("
							+ query.toString() + ") group by category_id;",
					null);

			System.out
					.println(":ALFHL:ADFHJADFH:A:DFHAL:HDF:  "
							+ "select category_id, count(*) from products where category_id in ("
							+ query.toString() + ") group by category_id;");

			System.out.println("SIZE: " + cursor.getCount());
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				System.out.println("IN IN IN IN IN ");
				map.put(cursor.getInt(0), cursor.getInt(1));
				cursor.moveToNext();
			}
			cursor.close();
			System.out.println("QUERYYYY:   " + map);

		} catch (ArrayIndexOutOfBoundsException exception) {
			exception.printStackTrace();
		}
		return map;
	}

	public void insertProducts(List<Product> products, ProgressDialog progress) {

		deleteRowsIfTableExists();

		int zipLenghtOfFile = products.size();
		System.out.println("NUMBER OF PROD: " + zipLenghtOfFile);

		progress.setMax(zipLenghtOfFile);

		long total = 0;

		ContentValues values = new ContentValues();
		database.beginTransaction();
		for (Product product : products) {
			values.clear();
			values.put(MySQLiteOpenHelper.COLUMN_CATEGORY_ID, product.getId());

			values.put(MySQLiteOpenHelper.COLUMN_NAME, product.getName());
			values.put(MySQLiteOpenHelper.COLUMN_PARENT_CATEGORY_ID,
					product.getCategoryId());
			values.put(MySQLiteOpenHelper.COLUMN_PIC, product.getImageUrl());
			values.put(MySQLiteOpenHelper.COLUMN_STATUS, product.getLabel());
			values.put(MySQLiteOpenHelper.COLUMN_PRODUCT_PRICE,
					product.getPrice());
			values.put(MySQLiteOpenHelper.COLUMN_MODEL_ID, product.getModelId());
			database.insert(MySQLiteOpenHelper.TABLE_PRODUCTS, null, values);

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
						+ MySQLiteOpenHelper.TABLE_PRODUCTS + "'", null);
		if (curs.getCount() == 1) {
			database.delete(MySQLiteOpenHelper.TABLE_PRODUCTS, null, null);
		}
	}

	public List<Product> getProductsByModel(int modelId) {
		List<Product> products = new ArrayList<Product>();

		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_PRODUCTS,
				allColumns, MySQLiteOpenHelper.COLUMN_MODEL_ID + "="
						+ String.valueOf(modelId), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = cursorToProduct(cursor);
			products.add(product);
			cursor.moveToNext();
		}
		cursor.close();
		return products;
	}
	
	public List<Product> getProductsBySerie(int serieId) {
		List<Product> products = new ArrayList<Product>();

		// Cursor cursor = database.query(
		// MySQLiteOpenHelper.TABLE_PRODUCTS,
		// allColumns,
		// MySQLiteOpenHelper.COLUMN_PARENT_CATEGORY_ID + "="
		// + String.valueOf(parentId), null, null, null, null);

		System.out.println("ID TO LOOOOK FOR: " + String.valueOf(serieId));

		Cursor cursor = database
				.rawQuery(
						"select * from products where id in (select id from product_series where serie_id=?);",
						new String[] { String.valueOf(serieId) });

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = cursorToProduct(cursor);
			products.add(product);
			cursor.moveToNext();
		}
		cursor.close();
		return products;
	}

	public List<Product> getProducts(int parentId) {
		List<Product> products = new ArrayList<Product>();

		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_PRODUCTS,
				allColumns, MySQLiteOpenHelper.COLUMN_PARENT_CATEGORY_ID + "="
						+ String.valueOf(parentId), null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = cursorToProduct(cursor);
			products.add(product);
			cursor.moveToNext();
		}
		cursor.close();
		return products;
	}

	public Product getProduct(int productId) {

		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_PRODUCTS,
				allColumns,
				MySQLiteOpenHelper.COLUMN_ID + "=" + String.valueOf(productId),
				null, null, null, null);

		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			return cursorToProduct(cursor);
		} else {
			return null;
		}
	}

	public List<Product> getProducts() {
		List<Product> products = new ArrayList<Product>();

		Cursor cursor = database.query(MySQLiteOpenHelper.TABLE_PRODUCTS,
				allColumns, MySQLiteOpenHelper.COLUMN_ID + "!=3242343", null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Product product = cursorToProduct(cursor);
			products.add(product);
			cursor.moveToNext();
		}
		cursor.close();
		return products;
	}

	private Product cursorToProduct(Cursor cursor) {
		Product product = new Product();
		product.setId(cursor.getInt(1));
		product.setName(cursor.getString(2));
		product.setCategoryId(cursor.getInt(3));
		product.setImageUrl(cursor.getString(4));
		product.setLabel(cursor.getInt(5));
		product.setPrice(cursor.getInt(6));
		product.setModelId(cursor.getInt(7));
		return product;
	}
}