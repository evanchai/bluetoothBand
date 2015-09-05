package com.example.bootstartdemo;

//import com.example.android.bluetoothlegatt.BluetoothLeService_BKG;

import org.androidpn.client.ServiceManager;

import com.example.ble_boombandui.R;
import com.example.ble_boonbandui.bluetooth.BluetoothLeService_BKG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot="android.intent.action.BOOT_COMPLETED"; 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)){ 
//        	蓝牙service还没加进来
        	//Intent ootStartIntent=new Intent(context,BluetoothLeService_BKG.class); 
            //context.startService(ootStartIntent); 
            Toast.makeText(context, "BLE_BoomBand service has started!", Toast.LENGTH_LONG).show(); 
            
//          androidpn service
            ServiceManager serviceManager = new ServiceManager(context);
            serviceManager.setNotificationIcon(R.drawable.notification);
            serviceManager.startService();
            
//            守护进程
            Intent serviceguardIntent=new Intent(context,ServiceGuard.class);
            context.startService(serviceguardIntent);
        }
 
    }
 
}