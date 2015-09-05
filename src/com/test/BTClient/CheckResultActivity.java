package com.test.BTClient;


import java.util.HashMap;
import java.util.Map;

import com.example.ble_boombandui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CheckResultActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_result);
		
		Button btn_title_left=(Button)findViewById(R.id.btn_title_left);
		btn_title_left.setOnClickListener(this);
		Button btn_title_right=(Button)findViewById(R.id.btn_title_right);
		btn_title_right.setVisibility(View.INVISIBLE);
		
		final TextView Tx_SYS=(TextView)findViewById(R.id.tx_SYS);
		final TextView Tx_DIA=(TextView)findViewById(R.id.tx_DIA);
		final TextView Tx_PUL=(TextView)findViewById(R.id.tx_PUL);
		final TextView Tx_SYS_title=(TextView)findViewById(R.id.tx_SYS_title);
		final TextView Tx_DIA_title=(TextView)findViewById(R.id.tx_DIA_title);
		final TextView Tx_PUL_title=(TextView)findViewById(R.id.tx_PUL_title);
		
		Intent user_intent=getIntent();
		final String online_username=user_intent.getStringExtra("online_username");
		final String online_password=user_intent.getStringExtra("online_password");
		
		final String resultMsg=user_intent.getStringExtra("resultMsg");
		Map map=new HashMap(MsgProcess(resultMsg));		
		final String SYS=(String) map.get("SYS");
		final String DIA=(String) map.get("DIA");
		final String PUL=(String) map.get("PUL");
		final String ERR=(String) map.get("ERR");
		
		if(SYS!=null){
			Tx_SYS.setText(SYS);
			Tx_DIA.setText(DIA);
			Tx_PUL.setText(PUL);
		}else{
			Tx_SYS_title.setText("测量错误");
			Tx_SYS.setText(ERR+" "+getERRMsg(ERR));
			Tx_DIA.setVisibility(View.INVISIBLE);
			Tx_DIA_title.setVisibility(View.INVISIBLE);
			Tx_PUL.setVisibility(View.INVISIBLE);
			Tx_PUL_title.setVisibility(View.INVISIBLE);
		}
		
		//设置用户登录状态
				if(online_username!=null)
				{
					TextView online_user=(TextView)findViewById(R.id.tv_top_title);
					online_user.setText(online_username);
				}
	}
	
	private String getERRMsg(String ERR){
		String err_msg = null;
		if(ERR.equals("E-1"))
		{
			err_msg="人体心跳信号太小或压力突降";
		}else if(ERR.equals("E-2"))
		{
			err_msg="杂讯干扰";
		}else if(ERR.equals("E-3"))
		{
			err_msg="充气时间过长";
		}else if(ERR.equals("E-4"))
		{
			err_msg="测得的结果异常";
		}else if(ERR.equals("E-C"))
		{
			err_msg="校正异常";
		}else if(ERR.equals("E-B"))
		{
			err_msg="电源低电压";
		}else if(ERR.equals("E-E"))
		{
			err_msg="EEPROM异常";
		}else 
		{
			err_msg="未知错误";
		}
		
		return err_msg;
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
        	CheckResultActivity.this.finish();
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
			CheckResultActivity.this.finish();
			overridePendingTransition( R.drawable.in_from_left,R.drawable.out_of_right); 
			break;
			
		}
	}
	
	 public static Map<String,String> MsgProcess(String receiveMsg)
	    {
		 	Map<String,String> rawParams=new HashMap<String,String>();
	    	int i=receiveMsg.indexOf("FDFDFC");
	    	if(i!=-1){
		    	String str=receiveMsg.substring(i,receiveMsg.length());
		    	String temp=str.substring(6,8);
		    	String SYS=""+Integer.parseInt(str.substring(6,8),16);
		    	String DIA=""+Integer.parseInt(str.substring(8,10),16);
		    	String PUL=""+Integer.parseInt(str.substring(10,12),16);
				rawParams.put("SYS", SYS);
				rawParams.put("DIA",DIA);
				rawParams.put("PUL", PUL);
	    	}
	    	i =receiveMsg.indexOf("FDFDFD");
	    	if(i!=-1)
	    	{
	    		String str=receiveMsg.substring(i,receiveMsg.length());
	        	String etype;
	    		etype=str.substring(6,8);
	    		if(etype.equals("0E"))
	    		{
	    			rawParams.put("ERR", "E-E");
	    		}else if(etype.equals("01")){
	    			rawParams.put("ERR", "E-1");
	    		}else if(etype.equals("02")){
	    			rawParams.put("ERR", "E-2");
	    		}else if(etype.equals("03")){
	    			rawParams.put("ERR", "E-3");
	    		}else if(etype.equals("05")){
	    			rawParams.put("ERR", "E-4");
	    		}else if(etype.equals("0C")){
	    			rawParams.put("ERR", "E-C");
	    		}else if(etype.equals("0B")){
	    			rawParams.put("ERR", "E-B");
	    		}
	    	}   	
	    	return rawParams;
	    }
}
