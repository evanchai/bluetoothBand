package com.example.bootstartdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BootStartService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private String TAG="BootStartService";
	
  
    @Override  
    public void onCreate() {  
        Log.e(TAG, "start onCreate~~~"); 
        Log.v("测试Create", "手机启动完成了");
        Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();  
        super.onCreate();  
        
       
    }  
      
    @Override  
    public void onStart(Intent intent, int startId) {  
        Log.e(TAG, "start onStart~~~");  
        Log.v("测试Start", "手机启动完成了");
        Toast.makeText(getBaseContext(), "onStart", Toast.LENGTH_LONG).show();  
        super.onStart(intent, startId);   
    }  
      
    @Override  
    public void onDestroy() {  
        Log.e(TAG, "start onDestroy~~~");  
        super.onDestroy();  
    }  
      
      
    @Override  
    public boolean onUnbind(Intent intent) {  
        Log.e(TAG, "start onUnbind~~~");  
        return super.onUnbind(intent);  
    }  
      
    
      
    public class MyBinder extends Binder{  
    	BootStartService getService()  
        {  
            return BootStartService.this;  
        }  
    }  

}
