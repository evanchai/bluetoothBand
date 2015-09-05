package com.example.ble_boombandui;

import com.example.ble_boombandui.dao.WaveReaderDbHelper;
import com.example.ble_boombandui.dao.WaveReaderContract.WaveEntry;
import com.example.ble_boonbandui.bluetooth.BluetoothLeService_BKG;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;  
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//启动ble后台服务
public class BandConnectionActivity extends Activity{

	private TextView blue_data;
    private MsgReceiver msgReceiver; 
    private StringBuffer blueBuffer = new StringBuffer();
    private Intent ootStartIntent;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boombandconnection);

		msgReceiver = new MsgReceiver();
		registerReceiver(msgReceiver, new IntentFilter(BluetoothLeService_BKG.ACTION_DATA_AVAILABLE));    
		
		ootStartIntent = new Intent(this, BluetoothLeService_BKG.class);
		startService(ootStartIntent);
				
		blue_data = (TextView) findViewById(R.id.blue_data);
		
		//saveToDatabase();
	}

	private void saveToDatabase() {
		//write to database
		WaveReaderDbHelper wDbHelper = new WaveReaderDbHelper(getBaseContext());
		// Gets the data repository in write mode
		SQLiteDatabase dbWrite = wDbHelper.getWritableDatabase();
		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(WaveEntry.COLUMN_NAME_WAVE_ID, 1);
		values.put(WaveEntry.COLUMN_NAME_WAVE_TYPE, 2);
		values.put(WaveEntry.COLUMN_NAME_WAVE_DATA, 3);
		// Insert the new row, returning the primary key value of the new row
		dbWrite.insert(WaveEntry.TABLE_NAME, null, values);
		
		//read from database
		SQLiteDatabase dbRead = wDbHelper.getReadableDatabase();
		
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    WaveEntry._ID,
		    WaveEntry.COLUMN_NAME_WAVE_ID,
		    WaveEntry.COLUMN_NAME_WAVE_TYPE,
		    WaveEntry.COLUMN_NAME_WAVE_DATA
		    };
		
		// Define 'where' part of query.
		String selection = WaveEntry.COLUMN_NAME_WAVE_TYPE + " LIKE ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { String.valueOf(3) };
		
		// How you want the results sorted in the resulting Cursor
		String sortOrder = WaveEntry.COLUMN_NAME_WAVE_ID + " DESC";
		Cursor c = dbRead.query(WaveEntry.TABLE_NAME, // The table to query
				projection, // The columns to return
				//selection, // The columns for the WHERE clause
				//selectionArgs, // The values for the WHERE clause
				null,
				null,
				null, // don't group the rows
				null, // don't filter by row groups
				sortOrder // The sort order
				);
		while(c.moveToNext()){
			int waveId = c.getInt(c.getColumnIndex(projection[0]));
			String waveType = c.getString(c.getColumnIndex(projection[2]));
			String waveData = c.getString(c.getColumnIndex(projection[3]));
			String resultString = waveId + waveType + waveData;
			blue_data.setText(resultString);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub 
		unregisterReceiver(msgReceiver);  
		super.onDestroy();
	}

	public boolean isOnline() {
	    ConnectivityManager connMgr = (ConnectivityManager) 
	            getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}  
	
	/** 
     * 广播接收器 
     * @author Ning 
     * 
     */  
    public class MsgReceiver extends BroadcastReceiver{  
  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String blue_text = intent.getStringExtra(BluetoothLeService_BKG.EXTRA_DATA);
            blueBuffer.append(blue_text);
            blue_data.setText(blue_text);
        }     
    }
}
