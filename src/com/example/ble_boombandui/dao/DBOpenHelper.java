package com.example.ble_boombandui.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBOpenHelper extends SQLiteOpenHelper {

	private final static int VERSION = 1;
	
	public DBOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public DBOpenHelper(Context context, String name, CursorFactory factory){
		this(context,name,null,VERSION);
	}
	
	public DBOpenHelper(Context context,String name){
		this(context,name,null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS schedule(scheduleID integer primary key autoincrement,scheduleTypeID integer,remindID integer,scheduleContent text,scheduleDate text)");
		db.execSQL("CREATE TABLE IF NOT EXISTS scheduletagdate(tagID integer primary key autoincrement,year integer,month integer,day integer,scheduleID integer)");
	    db.execSQL("CREATE TABLE IF NOT EXISTS androidpnmsg(msgID integer primary key autoincrement,date datetime,msg text,title text,isread integer)");
	    //db.execSQL("CREATE TABLE IF NOT EXISTS b_wave_data(waveId integer primary key autoincrement,waveType interger,waveByte text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS schedule");
		db.execSQL("DROP TABLE IF EXISTS scheduletagdate");
		db.execSQL("DROP TABLE IF EXISTS androidpnmsg");
		//db.execSQL("DROP TABLE IF EXISTS b_wave_data");
		onCreate(db);
	}
	

}
