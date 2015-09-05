package com.example.ble_boombandui;

import com.example.ble_boonbandui.bluetooth.BluetoothLeService_BKG;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.BroadcastReceiver;  
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
