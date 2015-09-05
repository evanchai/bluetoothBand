package com.example.bootstartdemo;

import org.androidpn.client.ServiceManager;

import com.example.ble_boombandui.R;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//检查服务和守护服务是否在运行，没有运行就跑起
public class TIMETICKReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean isServiceRunning = false;   
		boolean isServiceGuardRunning=false;
		boolean isAndroidPnRunning=false;
	    if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {   
	        
		    //检查Service状态   	        
		    ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);   
		    
		    for (RunningServiceInfo service :manager.getRunningServices(Integer.MAX_VALUE)) {   
			    if("com.example.android.bluetoothlegatt.BluetoothLeService_BKG".equals(service.service.getClassName()))       
			    {   
			    	isServiceRunning = true;   
			    }else if("com.example.bootstartdemo.ServiceGuard".equals(service.service.getClassName()))   
			    {
			    	isServiceGuardRunning=true;
			    }else if("org.androidpn.client.NotificationService".equals(service.service.getClassName())) 
			    {
			    	isAndroidPnRunning=true;
			    }
		     }   
		    if (!isServiceRunning) {   
//		    	Intent i = new Intent(context, com.example.android.bluetoothlegatt.BluetoothLeService_BKG.class);   
//		        context.startService(i);   
		    }   
		    if(!isServiceGuardRunning)
		    {
		    	Intent i = new Intent(context, ServiceGuard.class);   
		        context.startService(i); 
		    }
		    
		    if(!isAndroidPnRunning)
		    {
		    	ServiceManager serviceManager = new ServiceManager(context);
	            serviceManager.setNotificationIcon(R.drawable.notification);
	            serviceManager.startService();
		    }
	    }   
	}

}
