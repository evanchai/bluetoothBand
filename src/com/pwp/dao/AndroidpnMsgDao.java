package com.pwp.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pwp.vo.AndroidpnMsg;
import com.pwp.vo.ScheduleVO;

public class AndroidpnMsgDao {
	private DBOpenHelper dbOpenHelper = null;
	//private Context context = null;
	
	public AndroidpnMsgDao(Context context){

		//this.context = context;
		dbOpenHelper = new DBOpenHelper(context, "androidpnmsg.db");
	}
	
	/**
	 * ������Ϣ
	 */
	public int save(AndroidpnMsg androidpnMsg){
		
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		String dateString=androidpnMsg.getDate();
		values.put("date", androidpnMsg.getDate());
		values.put("title", androidpnMsg.getTitle());
		values.put("msg", androidpnMsg.getMsg());
		values.put("isread", androidpnMsg.getIsread());
		db.beginTransaction();
		int msgID = -1;
		try{
			db.insert("androidpnmsg", null, values);
		    Cursor cursor = db.rawQuery("select max(msgID) from androidpnmsg", null);
		    if(cursor.moveToFirst()){
		    	msgID = (int) cursor.getLong(0);
		    }
		    cursor.close();
		    db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	    return msgID;
	}
	
	/**
	 * ��ѯ���е���Ϣ
	 * @return
	 */
	public ArrayList<AndroidpnMsg> getAllMsg(){
		ArrayList<AndroidpnMsg> list = new ArrayList<AndroidpnMsg>();
		//dbOpenHelper = new DBOpenHelper(context, "schedules.db");
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("androidpnmsg", new String[]{"msgID","date","title","msg","isread"}, null, null, null, null, "msgID desc");
		while(cursor.moveToNext()){
			int msgID = cursor.getInt(cursor.getColumnIndex("msgID")); 
			String date = cursor.getString(cursor.getColumnIndex("date"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String msg = cursor.getString(cursor.getColumnIndex("msg"));
			int isread = cursor.getInt(cursor.getColumnIndex("isread"));
			AndroidpnMsg vo = new AndroidpnMsg(msgID,date,title,msg,isread);
			list.add(vo);
		}
		cursor.close();
		if(list != null && list.size() > 0){
			return list;
		}
		return null;
		
	}
	
	/**
	 * ���±��
	 * @param vo
	 */
	public void update(AndroidpnMsg androidpnMsg){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("date", androidpnMsg.getDate());
		values.put("title", androidpnMsg.getTitle());
		values.put("msg", androidpnMsg.getMsg());
		values.put("isread", androidpnMsg.getIsread());
		db.update("androidpnmsg", values, "msgID=?", new String[]{String.valueOf(androidpnMsg.getMsgID())});
	}
	
	/**
	 * ���±��
	 * @param vo
	 */
	public void updateisread(int msgid){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update [androidpnmsg] set isread=1 where msgid="+msgid);
		}
}
