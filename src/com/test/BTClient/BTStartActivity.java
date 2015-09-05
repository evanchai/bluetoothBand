package com.test.BTClient;
/*
 * 
 * 
 *该Activity已与PushActivity合并
 * 
 * 
 * */

import com.example.ble_boombandui.R;
import com.test.BTClient.BTClient;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BTStartActivity extends Activity implements OnClickListener{
	
//	private BTClient btClient;
	boolean isExit=false;
	String online_username="";
	String online_password="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		
		//设置用户登录状态
		if(online_username!=null)
		{
			TextView online_user=(TextView)findViewById(R.id.tv_top_title);
			online_user.setText(online_username);
		}
		
		Button btnstart=(Button)findViewById(R.id.btnstart);
		btnstart.setOnClickListener(this);
//		btnstart.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						
//						//开始BTservice
//						Intent intent = new Intent(this,BTClient.class);
//						intent.putExtra("username", online_username);
//						intent.putExtra("password", online_password);
////						bindService(intent, conn, Context.BIND_AUTO_CREATE);
//						startService(intent);
//					}
//				});
		
		Button btn_title_left=(Button)findViewById(R.id.btn_title_left);
		btn_title_left.setVisibility(View.INVISIBLE);  //设置成看不见
		
		Button btn_title_right=(Button)findViewById(R.id.btn_title_right);
//		btn_title_right.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				//开始settingActivity
//				Intent intent=new Intent(BTStartActivity.this,SettingActivity.class);
//				intent.putExtra("username", online_username);
//				intent.putExtra("password", online_password);
//				startActivity(intent);
//				overridePendingTransition( R.drawable.out_of_left,R.drawable.in_from_right); 
//			}
//			
//			
//		});
		
	}
	
//	ServiceConnection conn = new ServiceConnection() {
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			
//		}
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			//返回一个MsgService对象
//			btClient  = ((BTClient.MsgBinder)service).getService();
//			
//		}
//	};

//	@Override
//	protected void onDestroy() {
//		
//		unbindService(conn);
//		super.onDestroy();
//	}
	
//	@Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exit();
//            return false;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
//	
//	public void exit(){
//        if (!isExit) {
//            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//            mHandler.sendEmptyMessageDelayed(0, 2000);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_MAIN); ///指定跳到系统桌面
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //清除上一步缓存
//            startActivity(intent);
//            System.exit(0);
//        }
//    }
	
	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		Intent intent = new Intent(this,BTClient.class);
		Intent intent = new Intent("com.test.BTClient.BTClient");
		intent.putExtra("username", online_username);
		intent.putExtra("password", online_password);
	////	bindService(intent, conn, Context.BIND_AUTO_CREATE);
		startService(intent);
	}
    
   
}
