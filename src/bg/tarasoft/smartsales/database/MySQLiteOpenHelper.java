package bg.tarasoft.smartsales.database;

import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_MODELS = "models";
	public static final String TABLE_PRODUCT_SERIE = "product_series";
	public static final String TABLE_SERIES = "series";
	public static final String TABLE_CATEGORIES = "categories";
	public static final String TABLE_PRODUCTS = "products";
	public static final String TABLE_CHECKSUMS = "checksums";
	public static final String TABLE_LOGS = "logs";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORY_ID = "id";
	public static final String COLUMN_PARENT_ID = "parent_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SORT_ORDER = "sort_order";
	public static final String COLUMN_PIC = "pic";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_IS_SHOWN = "is_shown";
	public static final String COLUMN_PRODUCT_PRICE = "price";
	public static final String COLUMN_MODEL_ID = "model_id";
	public static final String TABLE_MODEL_SERIE = "model_series";

	public static final String COLUMN_PARENT_CATEGORY_ID = "category_id";
	public static final String COLUMN_CHECKSUM = "checksum";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_SERIE_ID = "serie_id";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_LOG_ID = "log_id";
	public static final String COLUMN_LOG_TYPE = "log_type";

	public static final String TABLE_STORES = "stores";
	public static final String COLUMN_STORE_ID = "store_id";
	public static final String COLUMN_STORE_NAME = "store_name";
	public static final String COLUMN_HALL_ID = "hall_id";

	public static final String TABLE_STORE_RETAILS = "store_retails";
	public static final String COLUMN_STORE_RETAIL_ID = "retail_id";

	private static final String DATABASE_NAME = "samsung_app.db";
	private static final int DATABASE_VERSION = 2;

	private static final String CREATE_TABLE_STORE_RETAILS = "create table "
			+ TABLE_STORE_RETAILS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_STORE_RETAIL_ID
			+ " integer not null, " + COLUMN_NAME + " text);";

	private static final String CREATE_TABLE_STORES = "create table "
			+ TABLE_STORES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_STORE_ID
			+ " integer not null, " + COLUMN_STORE_NAME + " text, "
			+ COLUMN_HALL_ID + " integer, " + COLUMN_STORE_RETAIL_ID
			+ " integer not null);";

	private static final String CREATE_TABLE_SERIE_PRODUCT = "create table "
			+ TABLE_PRODUCT_SERIE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY_ID
			+ " integer not null, " + COLUMN_SERIE_ID + " integer not null);";

	private static final String CREATE_TABLE_MODEL_SERIE = "create table "
			+ TABLE_MODEL_SERIE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY_ID
			+ " integer not null, " + COLUMN_SERIE_ID + " integer not null);";

	private static final String CREATE_TABLE_SERIES = "create table "
			+ TABLE_SERIES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY_ID
			+ " integer not null, " + COLUMN_SERIE_ID + " integer not null, "
			+ COLUMN_NAME + " text, " + COLUMN_PIC + " text);";

	private static final String CREATE_TABLE_CATEGORIES = "create table "
			+ TABLE_CATEGORIES + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY_ID
			+ " integer not null, " + COLUMN_PARENT_ID + " integer not null, "
			+ COLUMN_NAME + " text not null, " + COLUMN_SORT_ORDER
			+ " integer not null, " + COLUMN_PIC + " text not null, "
			+ COLUMN_IS_SHOWN + " integer not null);";

	private static final String CREATE_TABLE_PRODUCTS = "create table "
			+ TABLE_PRODUCTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CATEGORY_ID
			+ " integer not null, " + COLUMN_NAME + " text not null, "
			+ COLUMN_PARENT_CATEGORY_ID + " integer not null, " + COLUMN_PIC
			+ " text not null, " + COLUMN_STATUS + " integer not null, "
			+ COLUMN_PRODUCT_PRICE + " integer not null);";

	private static final String CREATE_TABLE_MODELS = "create table "
			+ TABLE_MODELS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_MODEL_ID
			+ " integer not null, " + COLUMN_NAME + " text not null, "
			+ COLUMN_PIC + " text not null);";

	private static final String CREATE_TABLE_CHECKSUMS = "create table "
			+ TABLE_CHECKSUMS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_TYPE
			+ " text not null, " + COLUMN_CHECKSUM + " text not null);";

	private static final String CREATE_TABLE_LOGS = "create table "
			+ TABLE_LOGS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DATE
			+ " text not null, " + COLUMN_LOG_ID + " integer not null, "
			+ COLUMN_LOG_TYPE + " inteer not null);";

	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_MODELS);
		db.execSQL(CREATE_TABLE_MODEL_SERIE);
		db.execSQL(CREATE_TABLE_STORE_RETAILS);
		db.execSQL(CREATE_TABLE_STORES);
		db.execSQL(CREATE_TABLE_PRODUCTS);
		db.execSQL(CREATE_TABLE_CATEGORIES);
		db.execSQL(CREATE_TABLE_CHECKSUMS);
		db.execSQL(CREATE_TABLE_SERIES);
		db.execSQL(CREATE_TABLE_SERIE_PRODUCT);
		db.execSQL(CREATE_TABLE_LOGS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteOpenHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_RETAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKSUMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_SERIE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODEL_SERIE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODELS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
		onCreate(db);
	}

}
