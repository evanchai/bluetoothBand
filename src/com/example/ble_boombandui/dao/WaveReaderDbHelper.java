package com.example.ble_boombandui.dao;

import com.example.ble_boombandui.dao.WaveReaderContract.WaveEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WaveReaderDbHelper extends SQLiteOpenHelper {
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ WaveEntry.TABLE_NAME + " (" + WaveEntry._ID
			+ " INTEGER PRIMARY KEY," + WaveEntry.COLUMN_NAME_WAVE_ID
			+ TEXT_TYPE + COMMA_SEP + WaveEntry.COLUMN_NAME_WAVE_TYPE
			+ TEXT_TYPE + COMMA_SEP + WaveEntry.COLUMN_NAME_WAVE_DATA
			+ TEXT_TYPE + " )";
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ WaveEntry.TABLE_NAME;

	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "WaveReader.db";

	public WaveReaderDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}