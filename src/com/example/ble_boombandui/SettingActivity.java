package com.example.ble_boombandui;

import url.SendURL;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	String online_username;
	String online_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		//设置用户登录状态
		if(online_username!=null)
		{
			TextView online_user=(TextView)findViewById(R.id.tv_top_title);
			online_user.setText(online_username);
		}
		
		ListView lst_setting=(ListView)findViewById(R.id.listView1);
		String array[]={"添加新账号","切换账号","退出登录"};
		ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
		lst_setting.setAdapter(arrayAdapter);
		lst_setting.setOnItemClickListener(this);
		
		
		Button btn_title_left=(Button)findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		Button btn_title_right=(Button)findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.btn_title_left:
			SettingActivity.this.finish();
			overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
			break;
			
		}
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// 0表示list中第一个,1表示list中第二个
		Log.d("position", Integer.toString(arg2));
		switch(arg2){
		case 0:
			Intent register_intent=new Intent(SettingActivity.this,RegisterActivity.class);
			startActivity(register_intent);
			overridePendingTransition( R.drawable.out_of_left,R.drawable.in_from_right); 
			break;
		case 1:
			Intent changeuser_intent=new Intent(SettingActivity.this,ChangeUserActivity.class);
			changeuser_intent.putExtra("username", online_username);
			changeuser_intent.putExtra("password", online_password);
			startActivity(changeuser_intent);
			overridePendingTransition( R.drawable.out_of_left,R.drawable.in_from_right); 
			break;
		case 2:
			Intent login_intent=new Intent(SettingActivity.this,LoginActivity.class);
			SettingActivity.this.finish();
			startActivity(login_intent);
//			overridePendingTransition( R.drawable.out_of_left,R.drawable.in_from_right); 
			break;
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	SettingActivity.this.finish();
        	overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
        	return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
        
    }
}
