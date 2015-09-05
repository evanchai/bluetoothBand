package com.test.BTClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot="android.intent.action.BOOT_COMPLETED"; 
 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)){ 
//            Intent ootStartIntent=new Intent(context,BootStartDemo.class); 
//        	Intent ootStartIntent=new Intent(context,BTClient.class); 
//            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        	Intent ootStartIntent=new Intent("com.test.checkMsg.checkMsgService");
            context.startService(ootStartIntent); 
            Toast.makeText(context, "service has started!", Toast.LENGTH_LONG).show();  
        }
 
    }
 
}