package com.exzample.ble_boombandui.friend;

import org.json.JSONException;
import org.json.JSONObject;

import url.SendURL;

import com.example.ble_boombandui.R;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
//用户详细资料
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class FriendDetailActivity extends Activity{
	private String online_username;
	private String online_password;
	String friendusername;
	int age;
	int sex;
	Button addfriendButton;
	TextView ageTextView;
	TextView sexTextView;
	TextView usernameTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_detail);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
//		获取用户名等信息
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		Bundle bundle=user_intent.getExtras();
		String payload=(String) bundle.getSerializable("jsonpayload");
		try {
			JSONObject jsonObject=new JSONObject(payload);
			friendusername=jsonObject.getString("username");
			age=jsonObject.getInt("age");
			sex=jsonObject.getInt("sex");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		显示
		usernameTextView=(TextView) findViewById(R.id.textView_friendname);
		ageTextView=(TextView) findViewById(R.id.textView_age);
		sexTextView=(TextView) findViewById(R.id.textView_sex);
		addfriendButton=(Button) findViewById(R.id.btn_addfriend);
		
		usernameTextView.setText(friendusername);
		ageTextView.setText("年龄  "+age);
		if(sex==0){
			sexTextView.setText("性别 男");
		
		}else{
			sexTextView.setText("性别 女");
		}
		
		addfriendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//添加好友
				boolean response=(new SendURL()).applyfriend(online_username, friendusername);
				if(response)
				{
					Toast.makeText(getApplicationContext(), "已申请！", Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(getApplicationContext(), "申请失败！", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
}
