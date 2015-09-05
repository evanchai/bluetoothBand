package com.example.ble_boombandui;

import org.androidpn.client.Constants;
import org.androidpn.client.ServiceManager;

import url.SendURL;

import com.example.ble_boombandui.health.IntentTab;
import com.example.ble_boombandui.msgbox.MsgBox;
import com.exzample.ble_boombandui.friend.FriendStateActivity;
import com.test.BTClient.BTClient;
import com.test.BTClient.BTStartActivity;
import com.xxmassdeveloper.mpchartexample.MultiLineChartActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class FirstPageActivity extends Activity implements OnClickListener{
	Button testImageButton;
	Button planImageButton;
	Button friendImageButton;
	Button historyImageButton;
	Button msgboxButton;
	Button linechartButton;
	private String online_username;
	private String online_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Òþ²Ø±êÌâ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);//ÉèÖÃÈ«ÆÁ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //ÊúÆÁ
		setContentView(R.layout.firstpage);
		
		 if (android.os.Build.VERSION.SDK_INT > 9) {
			    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
		}
		
		testImageButton=(Button) findViewById(R.id.imgbtn_test);
		planImageButton=(Button) findViewById(R.id.imgbtn_plan);
		friendImageButton=(Button) findViewById(R.id.imgbtn_friend);
		historyImageButton=(Button) findViewById(R.id.imgbtn_history);
		msgboxButton=(Button)findViewById(R.id.btn_msg);
		linechartButton=(Button) findViewById(R.id.btn_linechart);
		testImageButton.setOnClickListener(this);
		planImageButton.setOnClickListener(this);
		friendImageButton.setOnClickListener(this);
		historyImageButton.setOnClickListener(this);
		msgboxButton.setOnClickListener(this);
		linechartButton.setOnClickListener(this);
		
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		
//		------------------------ï¿½ï¿½ï¿½ï¿½androidpnï¿½ï¿½ï¿½ï¿½-------------------------------
		startAndroidPnService();
		
	}

	public void startAndroidPnService(){
		  Log.d("DemoAppActivity", "onCreate()...");
	        // Settings
	        Button okButton = (Button) findViewById(R.id.btn_settings);
	        okButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	                ServiceManager.viewNotificationSettings(FirstPageActivity.this);
	            }
	        });

	        // Start the service
	        ServiceManager serviceManager = new ServiceManager(this);
	        serviceManager.setNotificationIcon(R.drawable.notification);
	        serviceManager.startService();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.imgbtn_test:
//			Intent intent=new Intent(FirstPageActivity.this,BTStartActivity.class);
			Intent intent=new Intent(FirstPageActivity.this,IntentTab.class);
			intent.putExtra("username", online_username);
			intent.putExtra("password", online_password);
			startActivity(intent);
			break;
		case R.id.imgbtn_history:
//			Intent intent2=new Intent(FirstPageActivity.this,CalendarActivity.class);
//			Intent intent2=new Intent(FirstPageActivity.this,Datepicker.class);
			Intent intent2=new Intent(FirstPageActivity.this,BTStartActivity.class);
			intent2.putExtra("username", online_username);
			intent2.putExtra("password", online_password);
			startActivity(intent2);
			UpdateAndroidid("JK00000011");
			break;
		case R.id.imgbtn_plan:
			break;
		case R.id.imgbtn_friend:
			Intent intent5=new Intent(FirstPageActivity.this,BandConnectionActivity.class);
			intent5.putExtra("username", online_username);
			intent5.putExtra("password", online_password);
			startActivity(intent5);
			break;
		case R.id.btn_msg:
			Intent intent3=new Intent(FirstPageActivity.this,MsgBox.class);
			intent3.putExtra("username", online_username);
			intent3.putExtra("password", online_password);
			startActivity(intent3);
			break;
		case R.id.btn_linechart:
			Intent intent4=new Intent(FirstPageActivity.this,MultiLineChartActivity.class);
			intent4.putExtra("username", online_username);
			intent4.putExtra("password", online_password);
			startActivity(intent4);
			break;
		}
			
	}
	
//	¸üÐÂÓÃ»§androidid
	void UpdateAndroidid(String username)
	{
		 SharedPreferences preferences = this.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		 String androidid = preferences.getString(Constants.XMPP_USERNAME, "");
		 boolean updated=(new SendURL()).updateAndroidid(username,androidid);
	}
}
