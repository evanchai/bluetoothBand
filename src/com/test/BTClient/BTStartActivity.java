package com.test.BTClient;
/*
 * 
 * 
 *��Activity����PushActivity�ϲ�
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
		
		//�����û���¼״̬
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
//						//��ʼBTservice
//						Intent intent = new Intent(this,BTClient.class);
//						intent.putExtra("username", online_username);
//						intent.putExtra("password", online_password);
////						bindService(intent, conn, Context.BIND_AUTO_CREATE);
//						startService(intent);
//					}
//				});
		
		Button btn_title_left=(Button)findViewById(R.id.btn_title_left);
		btn_title_left.setVisibility(View.INVISIBLE);  //���óɿ�����
		
		Button btn_title_right=(Button)findViewById(R.id.btn_title_right);
//		btn_title_right.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				//��ʼsettingActivity
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
//			//����һ��MsgService����
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
//            Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
//            mHandler.sendEmptyMessageDelayed(0, 2000);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_MAIN); ///ָ������ϵͳ����
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //�����һ������
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
