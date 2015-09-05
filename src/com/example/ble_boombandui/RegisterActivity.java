package com.example.ble_boombandui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.test.BTClient.BTStartActivity;

import url.SendURL;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener , OnCheckedChangeListener{
	String androidId;
	String sex="0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		Button btn_title_left=(Button)findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		Button btn_title_right=(Button)findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
		
		final EditText Et_newusername=(EditText)findViewById(R.id.et_newusername);
		final EditText Et_newpassword=(EditText)findViewById(R.id.et_newpassword);
		final EditText Et_confirmpassword=(EditText)findViewById(R.id.et_confirmpassword);
		final EditText Et_telephone=(EditText)findViewById(R.id.et_telephone);
		final EditText Et_email=(EditText)findViewById(R.id.et_email);
		final EditText et_age=(EditText)findViewById(R.id.et_age);
		RadioGroup radioGroup_sex=(RadioGroup)findViewById(R.id.radioGroup_sex);
		radioGroup_sex.setOnCheckedChangeListener(this);
		
		
		Button Bt_register=(Button)findViewById(R.id.btn_register);
		
		
		Bt_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//开始BTStartActivity
				String username=Et_newusername.getText().toString();
				String password=Et_newpassword.getText().toString();
				String confirmpass=Et_confirmpassword.getText().toString();
				String telephone=Et_telephone.getText().toString();
				String email=Et_email.getText().toString();
				String age=et_age.getText().toString();
				
				if(password.equals(confirmpass)){
					//设备id从本地文件读取
					String deviceId=getDeviceId();					
					boolean response=(new SendURL()).register(username,password,deviceId,telephone,email,age,sex);
					//response=true用于调试
//					response=true;
					if(response==true){
						save(username,password);
						Intent intent=new Intent(RegisterActivity.this,BTStartActivity.class);
						intent.putExtra("username", username);
						intent.putExtra("password", password);
						SaveCurrentUser(username,password);
						RegisterActivity.this.finish();
						startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), "用户名已存在，注册失败！", Toast.LENGTH_LONG).show();  
					}
				}else
				{
					Toast.makeText(getApplicationContext(), "密码不相符", Toast.LENGTH_SHORT).show();
				}
							
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
        	RegisterActivity.this.finish();
        	overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
        	return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
        
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.btn_title_left:
			RegisterActivity.this.finish();
			overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
			break;
			
		}
	}
	
	//保存到本地文件
	public void save(String username,String password) {
        try {
            FileOutputStream outStream=this.openFileOutput("user_info.txt",Context.MODE_APPEND);
            String str=username+"#"+password+"#";
            outStream.write(str.getBytes());
            outStream.close();
            Toast.makeText(RegisterActivity.this,"Saved",Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            return;
        }
        catch (IOException e){
            return ;
        }
    }
	
	//从本地文件读取设备的deviceId 云推送分配的id
	public String getDeviceId(){
		String deivceId="";
		SAXReader saxReader = new SAXReader();  
	     Document doc;
			try {
					doc = saxReader.read(new File("/data/data/com.test.BTClient/shared_prefs/com.arrownock.push.PushService.xml"));				
					Element root = doc.getRootElement();  		          
					System.out.println("root element: " + root.getName());  
					
			        List childList = root.elements();  	          
			        System.out.println(childList.size());       

			        for(Iterator iter = root.elementIterator(); iter.hasNext();)  
			        {  
			            Element e = (Element)iter.next();  
			            
			            if(e.attributeValue("name").equals("ANID")){			            	
			            	deivceId=e.getText();
			            	Log.d("deviceId", deivceId);
			            }  
			        }  

			       
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
		
		return deivceId;
	}
	
	//读取手机本身的Id
	public void getLocalDeviceId(){
		 androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
	} 
	
	//存储当前的用户信息
		void SaveCurrentUser(String username, String password) {
	        SharedPreferences preferences = this.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
	        Editor editor = preferences.edit();
	        editor.putString("username", username);
	        editor.putString("password", password);
	        editor.commit();
	    }
		//选择性别
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			//获取变更后的选中项的ID
			 int radioButtonId = group.getCheckedRadioButtonId();
			//根据ID获取RadioButton的实例
			RadioButton rb = (RadioButton)RegisterActivity.this.findViewById(radioButtonId);
			String sexString= rb.getText().toString();
			if(sexString.equals("男"))
				sex="0";
			else {
				sex="1";
			}
		}
}
