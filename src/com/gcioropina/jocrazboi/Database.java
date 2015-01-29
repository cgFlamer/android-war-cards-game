/**
 * 
 */
package com.gcioropina.jocrazboi;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author gcioropina
 *
 */
public class Database extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "wargame.db";
	public static final String PROFILES_TABLE_NAME = "profiles";
	public static final String PROFILES_COLUMN_ID = "id";
	public static final String PROFILES_COLUMN_NAME = "name";

	/**
	 * Constructor method.
	 * 
	 * @Override
	 * @param context
	 */
	public Database(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table  " + PROFILES_TABLE_NAME
				+ "(id integer primary key,"
				+ " name text not null unique)"
				+ ";");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PROFILES_TABLE_NAME);
		onCreate(db);
	}

	/**
	 * Get all profiles from database.
	 * @return
	 */
	public ArrayList<String> getAllProfiles() {
		ArrayList<String> profiles = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + PROFILES_TABLE_NAME, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			profiles.add(res.getString(res.getColumnIndex(PROFILES_COLUMN_NAME)));
			res.moveToNext();
		}
		return profiles;
	}
	
	/**
	 * Get all profiles as hash map.
	 * @return
	 */
	public HashMap<Integer, String> getAllProfilesMap() {
		HashMap<Integer, String> map = new HashMap<>();
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from " + PROFILES_TABLE_NAME, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			map.put(Integer.getInteger(res.getString(res.getColumnIndex(PROFILES_COLUMN_ID))),
					res.getString(res.getColumnIndex(PROFILES_COLUMN_NAME)));
			res.moveToNext();
		}
		
		return map;
		
	}
	
	/**
	 * Add profile in datbaase.
	 * @param name
	 */
	public void addProfile(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", name);
		db.insert(PROFILES_TABLE_NAME, null, contentValues);
	}
	
	/**
	 * Delete profile by id.
	 * @param id
	 */
	public void deleteProfile(Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PROFILES_TABLE_NAME, "id = ? ", new String[] { Integer.toString(id) });
	}

	/**
	 * Delete profile by name.
	 * @param item
	 */
	public void deleteProfile(String item) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(PROFILES_TABLE_NAME, "name = ? ", new String[] { item });
	}
}
