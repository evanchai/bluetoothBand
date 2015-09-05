package com.example.ble_boombandui;

import org.androidpn.client.Constants;

import url.SendURL;
import android.R.bool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnCheckedChangeListener {
	
	boolean isExit=false;
	EditText Et_username;
	EditText Et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Et_username=(EditText)findViewById(R.id.et_qqNum);
		Et_password=(EditText)findViewById(R.id.et_qqPwd);
		Button Bt_login=(Button)findViewById(R.id.btn_login);
		Button Bt_register=(Button)findViewById(R.id.btn_login_regist);
		CheckBox CB_box=(CheckBox)findViewById(R.id.CB_box);
		CB_box.setOnCheckedChangeListener(this);
		
		Bt_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//开始BTStartActivity
				String username=Et_username.getText().toString();
				String password=Et_password.getText().toString();
				boolean response=(new SendURL()).login(username,password);
				
				if(response==true){
					Intent intent=new Intent(LoginActivity.this,FirstPageActivity.class);
					intent.putExtra("username", username);
					intent.putExtra("password", password);
					SaveCurrentUser(username,password);
					LoginActivity.this.finish();
					startActivity(intent);
//					更新用户androidid
					UpdateAndroidid(username);
				}else{
					Toast.makeText(getApplicationContext(), "用户名不存在或密码错误！", Toast.LENGTH_LONG).show();  
				}
				
				
			}
		});
		
		Bt_register.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition( R.drawable.out_of_left,R.drawable.in_from_right); 
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
	
	public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN); ///指定跳到系统桌面
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //清除上一步缓存
            startActivity(intent);
            System.exit(0);
        }
    }
	
	Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }

    };
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
	
	}
	
	//存储当前的用户信息
	void SaveCurrentUser(String username, String password) {
        SharedPreferences preferences = this.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }
	
//	更新用户androidid
	void UpdateAndroidid(String username)
	{
		 SharedPreferences preferences = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		 String androidid = preferences.getString(Constants.XMPP_USERNAME, "");
		 boolean updated=(new SendURL()).updateAndroidid(username,androidid);
	}
}
