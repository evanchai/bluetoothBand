package com.exzample.ble_boombandui.friend;


import com.example.ble_boombandui.FirstPageActivity;
import com.example.ble_boombandui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
//好友状态 
public class FriendStateActivity extends Activity{
	Button addButton;
	ListView frienListView;
	private String online_username;
	private String online_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_state);
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		addButton=(Button) findViewById(R.id.btn_add);
		frienListView=(ListView) findViewById(R.id.list_state);  //状态列表 未实现
		
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(FriendStateActivity.this,SearchFriendActivity.class);
				intent.putExtra("username", online_username);
				intent.putExtra("password", online_password);
				startActivity(intent);
			}
		});
		
		
		
	}
	
}
