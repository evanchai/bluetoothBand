package com.exzample.ble_boombandui.friend;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import url.SendURL;

import com.example.ble_boombandui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
//查找账号
public class SearchFriendActivity extends Activity   {
	Button searchfriendButton;
	Button newfriendButton;
	EditText searchusernameEditText;
	private String online_username;
	private String online_password;
	private String newfriendpayload;
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_add);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		

		
		searchfriendButton=(Button) findViewById(R.id.btn_searchfriend);
		searchusernameEditText=(EditText) findViewById(R.id.et_searchusername);
//		查找用户名 添加好友
		searchfriendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchname=searchusernameEditText.getText().toString();
				String response=(new SendURL()).searchfriend(searchname);
//				启动新FriendDetailActivity
				if(response!=null){
					Intent intent=new Intent(SearchFriendActivity.this,FriendDetailActivity.class);
					intent.putExtra("username", online_username);
					intent.putExtra("password", online_password);
					Bundle bundle=new Bundle();
					bundle.putSerializable("jsonpayload", response);
					intent.putExtras(bundle);
					startActivity(intent);
				}else {
					{
						Toast.makeText(getApplicationContext(), "该账户不存在！", Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
//		如果有新朋友申请 显示
		newfriendButton=(Button) findViewById(R.id.btn_newfriend);
		getNewFriend();
			if(!newfriendpayload.equals("{\"jsonlist\":[]}"))
			{
				newfriendButton.setVisibility(View.VISIBLE);
			
			newfriendButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent intent=new Intent(SearchFriendActivity.this,FriendApplyActivity.class);
					intent.putExtra("username", online_username);
					intent.putExtra("password", online_password);
					Bundle bundle=new Bundle();
					bundle.putSerializable("payload",  newfriendpayload);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
//		显示朋友列表
			 listView=(ListView) findViewById(R.id.list_friend);
			 String [] strs=getFriends();
			 if(strs!=null){
				 ListAdapter adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,strs);
				 listView.setAdapter(adapter);
			 }
			
		}
	}
	private String[] getFriends() {
		// TODO Auto-generated method stub
		String response=(new SendURL()).getFriends(online_username);
		if(response==null)
			return null;
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			JSONArray jsonArray=jsonObject.getJSONArray("jsonlist");
			String []data=new String[jsonArray.length()];
			for(int i=0;i<jsonArray.length();i++)
			{
				data[i]=(jsonArray.getString(i));
			}
			return data;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	private void getNewFriend() {
		// TODO Auto-generated method stub
		String responseString=(new SendURL()).searchnewfriend(online_username);
		if(responseString==null)
			return;
	
			newfriendpayload=responseString;
		
	}
	
}
