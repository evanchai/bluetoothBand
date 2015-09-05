package com.example.ble_boombandui.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.ble_boombandui.dao.WaveReaderContract.WaveEntry;
import com.example.ble_boombandui.model.WaveData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WaveDao {
	private DBOpenHelper dbOpenHelper = null;

	public WaveDao(Context context) {
		dbOpenHelper = new DBOpenHelper(context, "WaveReader.db");
	}

	public void save(WaveData waveData) {
		// Gets the data repository in write mode
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(WaveEntry.COLUMN_NAME_WAVE_ID, 1);
		values.put(WaveEntry.COLUMN_NAME_WAVE_TYPE, 2);
		values.put(WaveEntry.COLUMN_NAME_WAVE_DATA, 3);
		// Insert the new row, returning the primary key value of the new row
		db.beginTransaction();
		try {
			db.insert(WaveEntry.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO: handle exception
			db.endTransaction();
		}
	}

	public List<WaveData> read() {
		List<WaveData> waveList = new ArrayList<WaveData>();
		// read from database
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { WaveEntry._ID, WaveEntry.COLUMN_NAME_WAVE_ID,
				WaveEntry.COLUMN_NAME_WAVE_TYPE,
				WaveEntry.COLUMN_NAME_WAVE_DATA };

		// Define 'where' part of query.
		String selection = WaveEntry.COLUMN_NAME_WAVE_TYPE + " LIKE ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(3) };

		// How you want the results sorted in the resulting Cursor
		String sortOrder = WaveEntry.COLUMN_NAME_WAVE_ID + " DESC";
		Cursor c = db.query(WaveEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				// selection, // The columns for the WHERE clause
				// selectionArgs, // The values for the WHERE clause
				null, null, null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		while (c.moveToNext()) {
			int waveId = c.getInt(c.getColumnIndex(projection[0]));
			int waveType = c.getInt(c.getColumnIndex(projection[2]));
			String waveData = c.getString(c.getColumnIndex(projection[3]));
			waveList.add(new WaveData(waveId, waveType, waveData));
		}
		
		c.close();
		if(waveList != null && waveList.size() > 0){
			return waveList;
		}
		return null;
	}
	
	public void delete(){
		//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.beginTransaction();
		try{
			db.delete(WaveEntry.TABLE_NAME, null, null);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
}
