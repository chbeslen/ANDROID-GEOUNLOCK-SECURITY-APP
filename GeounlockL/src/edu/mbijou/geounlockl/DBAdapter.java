package edu.mbijou.geounlockl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class DBAdapter {
 private static final String TAG = "DBAdapter";
 
 public static final String KEY_ROWID = "_id";
 public static final String KEY_TIME = "time";
 public static final String KEY_LAT= "lat";
 public static final String KEY_LNG = "lng";
 
 public static final int COL_ROWID = 0;
 public static final int COL_TIME = 1;
 public static final int COL_LAT = 2;
 public static final int COL_LNG = 3;
 public static final String[] ALL_MAIN_KEYS = new String[] { KEY_ROWID, 
                KEY_TIME, 
                KEY_LAT, 
                KEY_LNG};
 public static final String KEY_NUMBER = "number";
 public static final String KEY_STREET = "street";
 public static final String KEY_CITY = "city";
 public static final String KEY_STATE = "state";
 public static final String KEY_ZIP = "zip";
 public static final String KEY_COUNTRY = "country";
 
 public static final int COL_NUMBER = 2;
 public static final int COL_STREET = 3;
 public static final int COL_CITY = 4;
 public static final int COL_STATE = 5;
 public static final int COL_ZIP = 6;
 public static final int COL_COUNTRY = 7;
 
 public static final String[] ALL_ADDRESS_KEYS = new String[] { KEY_ROWID, 
                 KEY_TIME,
                 KEY_NUMBER, 
                 KEY_STREET,
                 KEY_CITY,
                 KEY_STATE,
                 KEY_ZIP,
                 KEY_COUNTRY};
 public static final String DATABASE_NAME = "MyLocLog";
 public static final String DATABASE_MAIN_TABLE = "mainTable";
 public static final String DATABASE_ADDRESS_TABLE = "addressTable";
 
 public static final int DATABASE_VERSION = 2; 
 private static final String DATABASE_CREATE_MAIN_TABLE = 
   "create table " + DATABASE_MAIN_TABLE 
   + " (" + KEY_ROWID + " integer primary key autoincrement, "
   + KEY_TIME + " long not null, "
   + KEY_LAT + " real not null, "
   + KEY_LNG + " real not null"
   + ");";
 private static final String DATABASE_CREATE_ADDRESS_TABLE = 
   "create table " + DATABASE_ADDRESS_TABLE 
   + " (" + KEY_ROWID + " integer primary key autoincrement, "
   + KEY_TIME + " long not null, "
   + KEY_NUMBER + " string not null, "
   + KEY_STREET + " string not null, "
   + KEY_CITY + " string not null, "
   + KEY_STATE + " string not null, "
   + KEY_ZIP + " string not null, "
   + KEY_COUNTRY + " string not null"
   + ");";
 private final Context context;
 
 private DatabaseHelper myDBHelper;
 private SQLiteDatabase db;
 public DBAdapter(Context ctx) {
  this.context = ctx;
  myDBHelper = new DatabaseHelper(context);
 }
 public DBAdapter open() {
  db = myDBHelper.getWritableDatabase();
  return this;
 }
 public void close() {
  myDBHelper.close();
 }
 public long insertRowG(long d, double lat, double lng) {
  ContentValues initialValues = new ContentValues();
  initialValues.put(KEY_TIME, d);
  initialValues.put(KEY_LAT, lat);
  initialValues.put(KEY_LNG, lng);
  return db.insert(DATABASE_MAIN_TABLE, null, initialValues);
 }
 public Cursor getAllRowsG() {
  String where = null;
  Cursor c =  db.query(true, DATABASE_MAIN_TABLE, ALL_MAIN_KEYS, 
    where, null, null, null, null, null);
  if (c != null) {
   c.moveToFirst();
  }
  return c;
 }
 public Cursor getRowG(long rowId) {
  String where = KEY_ROWID + "=" + rowId;
  Cursor c =  db.query(true, DATABASE_MAIN_TABLE, ALL_MAIN_KEYS, 
    where, null, null, null, null, null);
  if (c != null) {
   c.moveToFirst();
  }
  return c;
 }
 public boolean updateRow(long rowId, long loctime, double lat, double lng) {
  String where = KEY_ROWID + "=" + rowId;
  ContentValues newValues = new ContentValues();
  newValues.put(KEY_TIME, loctime);
  newValues.put(KEY_LAT, lat);
  newValues.put(KEY_LNG, lng);
  return db.update(DATABASE_MAIN_TABLE, newValues, where, null) != 0;
 }
 
 public long insertRowA(long loctime, String number,String street,String city,String state,String zip,String country) {
  ContentValues initialValues = new ContentValues();
  initialValues.put(KEY_TIME, loctime);
  initialValues.put(KEY_NUMBER, number);
  initialValues.put(KEY_STREET, street);
  initialValues.put(KEY_CITY, city);
  initialValues.put(KEY_ZIP, zip);
  initialValues.put(KEY_STATE, state);
  initialValues.put(KEY_COUNTRY, country);
  return db.insert(DATABASE_ADDRESS_TABLE, null, initialValues);
 }
 public Cursor getAllRowsA() {
  String where = null;
  Cursor c =  db.query(true, DATABASE_ADDRESS_TABLE, ALL_ADDRESS_KEYS, 
       where, null, null, null, null, null);
  if (c != null) {
   c.moveToFirst();
  }
  return c;
 }
 public Cursor getRowA(long rowId) {
  String where = KEY_ROWID + "=" + rowId;
  Cursor c =  db.query(DATABASE_ADDRESS_TABLE, ALL_ADDRESS_KEYS, 
      where, null, null, null, null, null);
  if (c != null) {
   c.moveToFirst();
  }
  return c;
 }
 public boolean updateRow(long rowId, long loctime, String number,String street,String city,String state,String zip,String country) {
  String where = KEY_ROWID + "=" + rowId;
  ContentValues newValues = new ContentValues();
  newValues.put(KEY_TIME, loctime);
  newValues.put(KEY_NUMBER, number);
  newValues.put(KEY_STREET, street);
  newValues.put(KEY_CITY, city);
  newValues.put(KEY_STATE, state);
  newValues.put(KEY_ZIP, zip);
  newValues.put(KEY_COUNTRY, country);
  return db.update(DATABASE_ADDRESS_TABLE, newValues, where, null) != 0;
 }

 public boolean deleteRow(long rowId) {
  String where = KEY_ROWID + "=" + rowId;
  return db.delete(DATABASE_ADDRESS_TABLE, where, null) != 0;
 }
 
 public void deleteAll() {
  Cursor c = getAllRowsA();
  long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
  if (c.moveToFirst()) {
   do {
    deleteRow(c.getLong((int) rowId));    
   } while (c.moveToNext());
  }
  c.close();
 }
 
 
 
 /////////////////////////////////////////////////////////////////////
 // Private Helper Classes:
 /////////////////////////////////////////////////////////////////////
 
 /**
  * Private class which handles database creation and upgrading.
  * Used to handle low-level database access.
  */
 private static class DatabaseHelper extends SQLiteOpenHelper
 {
  DatabaseHelper(Context context) {
   super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase _db) {
   _db.execSQL(DATABASE_CREATE_MAIN_TABLE);
   _db.execSQL(DATABASE_CREATE_ADDRESS_TABLE); 
  }

  @Override
  public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
   Log.w(TAG, "Upgrading application's database from version " + oldVersion
     + " to " + newVersion + ", which will destroy all old data!");
   
   // Destroy old database:
   _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_MAIN_TABLE);
   _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_CREATE_ADDRESS_TABLE);
   // Recreate new database:
   onCreate(_db);
  }
 }
}