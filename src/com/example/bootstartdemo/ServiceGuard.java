package com.example.bootstartdemo;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
//守护service 注册timetick系统广播
public class ServiceGuard extends Service{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
//		动态注册TIME_TICK广播
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);   
	    TIMETICKReceiver receiver = new TIMETICKReceiver();   
	    registerReceiver(receiver, filter);   
	    
		return  START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
