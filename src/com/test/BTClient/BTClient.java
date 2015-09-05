package com.test.BTClient;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.ble_boombandui.R;

import url.SendURL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
//import android.view.Menu;            //如使用菜单加入此三包
//import android.view.MenuInflater;
//import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class BTClient extends Service {
	
	static String online_username;
	static String online_password;
	private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
	
	private boolean stateSend=false;
	
	private InputStream is;    //输入流，用来接收蓝牙数据
	//private TextView text0;    //提示栏解句柄
//    private EditText edit0;    //发送数据输入句柄
//    private TextView dis;       //接收数据显示句柄
//    private ArrayAdapter<String> mConversationArrayAdapter;
//    private ListView mConversationView;
//    private ScrollView sv;      //翻页句柄
    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存

    public String filename=""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;
	
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
	
    private MsgReceiver deviceReceiver;
	private Intent startIntent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate() {
    	if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);   //设置画面为主画面 main.xml
        
        //text0 = (TextView)findViewById(R.id.Text0);  //得到提示栏句柄
//        edit0 = (EditText)findViewById(R.id.Edit0);   //得到输入框句柄
//        sv = (ScrollView)findViewById(R.id.ScrollView01);  //得到翻页句柄
//        dis = (TextView) findViewById(R.id.in);      //得到数据显示句柄
//        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
//        mConversationView = (ListView) findViewById(R.id.in);
//        mConversationView.setAdapter(mConversationArrayAdapter);

        
       //如果打开本地蓝牙设备不成功，提示信息，结束程序 写到onStartCommand里面去了
//        if (_bluetooth == null){
//        	Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
////            finish();
//        	//2014-4-28
////            return;
//        	stopSelf();
//        }
        
//        // 打开蓝牙  
//       new Thread(){
//    	   public void run(){
//    		   if(_bluetooth.isEnabled()==false){
//        		_bluetooth.enable();
//    		   }
//    	   }   	   
//       }.start();      
       
     //动态注册广播接收器
     		deviceReceiver = new MsgReceiver();
     		IntentFilter intentFilter = new IntentFilter();
     		intentFilter.addAction("com.test.BTClient.DeviceListService");
     		registerReceiver(deviceReceiver, intentFilter);
     		
     //查找设备 写到onStartCommand里面去了
//     		startDeviceListService();
    }

    //发送按键响应
    public void onSendButtonClicked(View v){
    	int i=0;
    	int n=0;
    	try{
    		OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
//    		String str=edit0.getText().toString();
//    		byte[] bos = edit0.getText().toString().getBytes();
//    		byte[] bos = edit0.getText().toString().getBytes();
//    		for(i=0;i<bos.length;i++){
//    			if(bos[i]==0x0a)n++;
//    		}
//    		byte[] bos_new = new byte[bos.length+n];
//    		n=0;
//    		for(i=0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a后再发送
//    			if(bos[i]==0x0a){
//    				bos_new[n]=0x0d;
//    				n++;
//    				bos_new[n]=0x0a;
//    			}else{
//    				bos_new[n]=bos[i];
//    			}
//    			n++;
//    		}
    		
//    		os.write(bos_new);	
//    		byte BTConnect[]={0xFD,0xFD,0xFA,0x05,0x0D,0x0A};
//    		byte BTConnect[]=hexStringToBytes("0xFD0xFD0xFA0x050x0D0x0A");
    		byte BTConnect[]={(byte) 0xFD,(byte) 0xFD,(byte) 0xFA,0x05,0x0D,0x0A};
    		os.write(BTConnect);
    	}catch(IOException e){  		
    	}  	
    }
    
  //发送命令
    public boolean Send(String cmd){
    	try{
//    		
    		if(_socket==null)
    			return false;
    		OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流

    		byte BTConnect[]={(byte) 0xFD,(byte) 0xFD,(byte) 0xFA,0x05,0x0D,0x0A};
    		byte BTDisconnect[]={(byte) 0xFD,(byte) 0xFD,(byte) 0xFE,0x06,0x0D,0x0A};
    		
    		
    		if(cmd.equals("connect")){
//    			for(int i=0;i<5;i++){
//    				
//    				os.write(BTConnect);
//    				Thread.sleep(100);
//    			}
    			os.write(BTConnect);
    		}
    		else if(cmd.equals("disconnect")){
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("shut down","shut down");
    			//os.write(BTDisconnect);
    		}
    	}catch(IOException e){  
    		return false;
    		//e.printStackTrace();
    	}  	
    	return true;
    }
    
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    

    private static byte charToByte(char c) {
       return (byte) "0123456789ABCDEF".indexOf(c);
   }

    
    
    //接收活动结果，响应startActivityForResult()
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	switch(requestCode){
//    	case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
//    		// 响应返回结果
//            if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
//                // MAC地址，由DeviceListActivity设置返回
////                String address = data.getExtras()
////                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                // 得到蓝牙设备句柄      
//                _device = _bluetooth.getRemoteDevice(address);
// 
//                // 用服务号得到socket
//                try{
//                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
//                }catch(IOException e){
//                	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
//                }
//                //连接socket
//            	Button btn = (Button) findViewById(R.id.Button03);
//                try{
//                	_socket.connect();
//                	Toast.makeText(this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();
//                	btn.setText("断开");
//                }catch(IOException e){
//                	try{
//                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
//                		_socket.close();
//                		_socket = null;
//                	}catch(IOException ee){
//                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
//                	}
//                	
//                	return;
//                }
//                
//                //打开接收线程
//                try{
//            		is = _socket.getInputStream();   //得到蓝牙数据输入流
//            		}catch(IOException e){
//            			Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
//            			return;
//            		}
//            		if(bThread==false){
//            			ReadThread.start();
//            			bThread=true;
//            		}else{
//            			bRun = true;
//            		}
//            }
//    		break;
//    	default:break;
//    	}
//    }
    
    /**
	 * 广播接收器
	 *
	 */
	public class MsgReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//拿到进度，更新UI
			Bundle bundle=intent.getExtras();
			String deviceName=bundle.getString("deviceName");
			String deviceAddress=bundle.getString("deviceAddress");
			
			if(_bluetooth==null){
			 _bluetooth = BluetoothAdapter.getDefaultAdapter();
			 }
			 _device = _bluetooth.getRemoteDevice(deviceAddress);
			 
             // 用服务号得到socket
             try{
             	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
             }catch(IOException e){
//             	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
             }
             //连接socket
//         	Button btn = (Button) findViewById(R.id.Button03);
             try{
             	_socket.connect();
//             	Toast.makeText(this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();
//             	btn.setText("断开");
             }catch(IOException e){
             	try{
//             		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
             		_socket.close();
             		_socket = null;
             	}catch(IOException ee){
//             		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
             	}
             	
             	return;
             }
             
             //打开接收线程
             try{
         		is = _socket.getInputStream();   //得到蓝牙数据输入流
         		}catch(IOException e){
//         			Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
         			return;
         		}
         		if(bThread==false){
         			ReadThread.start();
         			bThread=true;
         		}else{
         			bRun = true;
         		}
         }
		
		
	}
    
    //接收数据线程
    Thread ReadThread=new Thread(){
    	
    	public void run(){
    		int num = 0;
//    		byte[] buffer = new byte[256];
       
    		bRun = true;
    		
    		//接收线程
    		while(true){
    			try{
    				while(is.available()==0){
    					while(bRun == false){}
    				}
    				while(true){
    					byte[] buffer = new byte[1024];
    					String s00;
    					num = is.read(buffer);         //读入数据
    			
    					s00=bytesToHexString(buffer);
    					s00=s00.substring(0,num*2);
    					Log.d("buffer",s00);

    					fmsg+=s00;    //保存收到数据
    					smsg+=new String(s00);   //写入接收缓存
    					
        				//发送显示消息，进行显示刷新
        				Message msg = handler.obtainMessage();
        				msg.obj=s00;
        				handler.sendMessage(msg);   
     
        				if(is.available()==0)break;  //短时间没有数据才跳出进行显示
    				}    	    		
    	    	}catch(IOException e){
    	    	}
    		}
    	}
    };
    
    public static String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xFF);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            result += hexString.toUpperCase();
        }
        return result;
    }
    
    //消息处理队列
    Handler handler= new Handler(){
    	public void handleMessage(Message msg){
    		super.handleMessage(msg);
    		String receiveMsg=msg.obj.toString();

    		if(receiveMsg.startsWith("A5")||receiveMsg.startsWith("FDFD060D0A")){
    			int temp=5;
    			while(!Send("connect")){
    				if(temp<=0)break;
    				temp--;
    				try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    				String tempString = "";
    				try {
    					if(_socket==null)
    						_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
						_socket.connect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						tempString=e.toString();
						Toast.makeText(getBaseContext(), tempString, Toast.LENGTH_LONG).show();  
					}
    				
    			}
    		}
    		else if(receiveMsg.contains("FDFDFC")||receiveMsg.contains("FDFDFD"))
    		{
    			Intent intent = new Intent();  
    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
    			intent.setClass(getApplicationContext(),CheckResultActivity.class);
    			intent.putExtra("resultMsg", receiveMsg);
    			intent.putExtra("online_username", online_username);
    			intent.putExtra("online_password", online_password);
    			startActivity(intent);  
//    			Send("disconnect");
    			//判断是否是结果，发送到servlet去！	true表示发送成功 开一个新的线程
        		boolean response = false;
        		String tempString = null;
				try {
					response = new SendURL().SendtoServer(MsgProcess(receiveMsg));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					tempString=e.toString();
					Toast.makeText(getBaseContext(), tempString, Toast.LENGTH_LONG).show();  
				} 
				
        		if(response==true)
        		{	//service 没有context 显示不出
        			 Toast.makeText(getBaseContext(), "发送成功！", Toast.LENGTH_LONG).show();  
        		}else
        		{
        			 Toast.makeText(getBaseContext(), "发送失败！", Toast.LENGTH_LONG).show(); 
        		}
        		//循环搜索，在有界面的时候会崩，注释了
//        		try {
////					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    			startDeviceListService();
    		}
//    		else if(receiveMsg.contains("FDFD07"))
//    		{
//    			startDeviceListService();
//    		}
    		
    	}
    };
    
    
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
    	
    	rawParams.put("username", online_username);
    	rawParams.put("password", online_password);
    	return rawParams;
    }


    
    //关闭程序掉用处理部分
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //关闭连接socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
    //	_bluetooth.disable();  //关闭蓝牙服务
    	
    	//注销广播
    	//停止服务
//    	stopService(startIntent);
    	unregisterReceiver(deviceReceiver);
    }
    
    //菜单处理部分
  /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {//建立菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) { //菜单响应函数
        switch (item.getItemId()) {
        case R.id.scan:
        	if(_bluetooth.isEnabled()==false){
        		Toast.makeText(this, "Open BT......", Toast.LENGTH_LONG).show();
        		return true;
        	}
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.quit:
            finish();
            return true;
        case R.id.clear:
        	smsg="";
        	ls.setText(smsg);
        	return true;
        case R.id.save:
        	Save();
        	return true;
        }
        return false;
    }*/
    
//    //连接按键响应函数 改成了下面这个函数
//    public void onConnectButtonClicked(View v){ 
//    	if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用则提示
//    		Toast.makeText(this, " 手机未开启蓝牙  ", Toast.LENGTH_LONG).show();
//    		return;
//    	}
//    	
//    	
//        //如未连接设备则打开DeviceListActivity进行设备搜索
////    	Button btn = (Button) findViewById(R.id.Button03);
//    	if(_socket==null){
////    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
////    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
//    		
//    		//开始查找设备的服务
//    		startIntent = new Intent("com.test.BTClient.DeviceListService");
//			startService(startIntent);
//    	}
//    	else{
//    		 //关闭连接socket
//    	    try{
//    	    	
//    	    	is.close();
//    	    	_socket.close();
//    	    	_socket = null;
//    	    	bRun = false;
////    	    	btn.setText("连接");
//    	    }catch(IOException e){}   
//    	}
//    	return;
//    }
    
  //启动DeviceListService 关闭socket
    public void startDeviceListService(){ 
    	//2014-4-28
//    	if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用则提示
//    		Toast.makeText(this, " 手机未开启蓝牙  ", Toast.LENGTH_LONG).show();
//    		return;
//    	}
    	
    	
        //如未连接设备则打开DeviceListActivity进行设备搜索
//    	Button btn = (Button) findViewById(R.id.Button03);
//    	if(_socket==null){
//    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
//    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
    		
    		//开始查找设备的服务
    		startIntent = new Intent("com.test.BTClient.DeviceListService");
			startService(startIntent);
//    	}
//    	else{
    		 //关闭连接socket
//    	    try{
//    	    	if(is!=null)
//    	    		is.close();
//    	    	_socket.close();
//    	    	_socket = null;
//    	    	bRun = false;
//    	    	btn.setText("连接");
//    	    }catch(IOException e){
//    	    	e.printStackTrace();}   
    		
//    		try {
//				_socket.close();
//				 InputStream in = _socket.getInputStream();  
//	    		 OutputStream ou = _socket.getOutputStream();  
//	    		in.close();  
//  		        ou.close();  
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}  
    		              
    		   
//    	}
    	return;
    }
    
    
    
    
    //保存按键响应函数
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //清除按键响应函数
    public void onClearButtonClicked(View v){
    	smsg="";
    	fmsg="";
//    	dis.setText(smsg);
//    	 mConversationArrayAdapter.clear();
    	return;
    }
    
    //退出按键响应函数
    public void onQuitButtonClicked(View v){
//    	finish();
    }
    
    //保存功能实现
	private void Save() {
		//显示对话框输入文件名
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //图层模板生成器句柄
		final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
		new AlertDialog.Builder(BTClient.this)
								.setTitle("文件名")
								.setView(DialogView)   //设置视图模板
								.setPositiveButton("确定",
								new DialogInterface.OnClickListener() //确定按键响应函数
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
										filename = text1.getText().toString();  //得到文件名
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
												
												filename =filename+".txt";   //在文件名末尾加上.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
												File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
												FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
												stream.write(fmsg.getBytes());
												stream.close();
												Toast.makeText(BTClient.this, "存储成功！", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "没有存储卡！", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}
										
										
										
									}
								})
								.setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //显示对话框
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return new MsgBinder();
	} 
	public class MsgBinder extends Binder{
		/**
		 * 获取当前Service的实例
		 * @return
		 */
		public BTClient getService(){
			return BTClient.this;
		}
	}
	@Override  
    public int onStartCommand(Intent intent, int flags, int startId) {
		online_username=intent.getStringExtra("username");
		online_password=intent.getStringExtra("paswword");
		 //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
        	Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
//            finish();
        	//2014-4-28
//            return;
        }
        else{
        	BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        	if(mBtAdapter.isEnabled()){
        		//查找设备 写到onStartCommand里面去了
        		startDeviceListService();
        	}else{
        		Toast.makeText(getBaseContext(), "请打开手机蓝牙功能！", Toast.LENGTH_SHORT).show();
        	}
 		}
		return startId;  
// 		return START_REDELIVER_INTENT;
		
	}
}