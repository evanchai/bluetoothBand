package com.example.ble_boombandui;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

public class ChangeUserActivity extends Activity implements OnItemClickListener, OnClickListener {
	
	String online_username;
	String online_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_user);
		
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		//�����û���¼״̬
		if(online_username!=null)
		{
			TextView online_user=(TextView)findViewById(R.id.tv_top_title);
			online_user.setText(online_username);
		}
		
		Button btn_title_left=(Button)findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		Button btn_title_right=(Button)findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
		
		ListView lst_user=(ListView)findViewById(R.id.listView2);
		String array[]=getUsers();
		
		if(array!=null){
		ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array);
		lst_user.setAdapter(arrayAdapter);
		lst_user.setOnItemClickListener(this);
		}

	
	}
	
	//�ӱ��ػ�ȡ�����˻�����Ϣ
	public String[] getUsers()
	{
		HashMap<String,String> map=LoadUserList();
		if(map==null)
			return null;
		
		String[] userlist=new String[map.size()];
//		String[] userList={"С��","СС��","�һ�"};
		Set<String> keys=map.keySet();
		Iterator<String> it=keys.iterator();
		int i=0;
		while(it.hasNext())
		{
			userlist[i++]=it.next();
		}
		
		return userlist;
	}
	//�����û�����ȡ����
	public String getLocalpassword(String username){
//		String pass="12345";
		HashMap<String,String> map=LoadUserList();
		String pass=map.get(username);
		return pass;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		ListView list=(ListView)arg0;
		Object user=list.getItemAtPosition(arg2);
		String username=((TextView)arg1).getText().toString();
		
		Intent intent=new Intent(ChangeUserActivity.this,FirstPageActivity.class);
		intent.putExtra("username", username);
		String password=getLocalpassword(username);
		intent.putExtra("password", password);
		//����ǰ�û���Ϣ���浽�����ļ�
		SaveCurrentUser(username,password);
		ChangeUserActivity.this.finish();
		startActivity(intent);
		overridePendingTransition( R.drawable.out_of_left,R.drawable.in_from_right); 
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.btn_title_left:
			ChangeUserActivity.this.finish();
			overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
			break;
			
		}
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	ChangeUserActivity.this.finish();
        	overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
        	return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
        
    }
	
	//��ȡ�����ļ���ȡuser�б�
	public HashMap<String,String> LoadUserList()

	{
		HashMap<String,String> userlist=new HashMap<String,String>();
	    try {
	        FileInputStream inStream=this.openFileInput("user_info.txt");
	        ByteArrayOutputStream stream=new ByteArrayOutputStream();
	        byte[] buffer=new byte[1024];
	        int length=-1;
	        while((length=inStream.read(buffer))!=-1)   {
	            stream.write(buffer,0,length);
	        }
	        stream.close();
	        inStream.close();
	        String user_info=stream.toString();
	        Log.d("user_info", user_info);
	        
	        StringTokenizer token=new StringTokenizer(user_info,"#");
	        while(token.hasMoreElements()){     
	        String username=token.nextToken();
	        String password=token.nextToken();
	        
	        userlist.put(username, password);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	        return null;
	    }
	    catch (IOException e){
	        return null;
	    }
	    return userlist;
	}
	
	//�洢��ǰ���û���Ϣ
	void SaveCurrentUser(String username, String password) {
        SharedPreferences preferences = this.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

}
