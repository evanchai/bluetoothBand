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
//import android.view.Menu;            //��ʹ�ò˵����������
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
	private final static int REQUEST_CONNECT_DEVICE = 1;    //�궨���ѯ�豸���
	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP����UUID��
	
	private boolean stateSend=false;
	
	private InputStream is;    //������������������������
	//private TextView text0;    //��ʾ������
//    private EditText edit0;    //��������������
//    private TextView dis;       //����������ʾ���
//    private ArrayAdapter<String> mConversationArrayAdapter;
//    private ListView mConversationView;
//    private ScrollView sv;      //��ҳ���
    private String smsg = "";    //��ʾ�����ݻ���
    private String fmsg = "";    //���������ݻ���

    public String filename=""; //��������洢���ļ���
    BluetoothDevice _device = null;     //�����豸
    BluetoothSocket _socket = null;      //����ͨ��socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;
	
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //��ȡ�����������������������豸
	
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
//        setContentView(R.layout.main);   //���û���Ϊ������ main.xml
        
        //text0 = (TextView)findViewById(R.id.Text0);  //�õ���ʾ�����
//        edit0 = (EditText)findViewById(R.id.Edit0);   //�õ��������
//        sv = (ScrollView)findViewById(R.id.ScrollView01);  //�õ���ҳ���
//        dis = (TextView) findViewById(R.id.in);      //�õ�������ʾ���
//        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
//        mConversationView = (ListView) findViewById(R.id.in);
//        mConversationView.setAdapter(mConversationArrayAdapter);

        
       //����򿪱��������豸���ɹ�����ʾ��Ϣ���������� д��onStartCommand����ȥ��
//        if (_bluetooth == null){
//        	Toast.makeText(this, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�", Toast.LENGTH_LONG).show();
////            finish();
//        	//2014-4-28
////            return;
//        	stopSelf();
//        }
        
//        // ������  
//       new Thread(){
//    	   public void run(){
//    		   if(_bluetooth.isEnabled()==false){
//        		_bluetooth.enable();
//    		   }
//    	   }   	   
//       }.start();      
       
     //��̬ע��㲥������
     		deviceReceiver = new MsgReceiver();
     		IntentFilter intentFilter = new IntentFilter();
     		intentFilter.addAction("com.test.BTClient.DeviceListService");
     		registerReceiver(deviceReceiver, intentFilter);
     		
     //�����豸 д��onStartCommand����ȥ��
//     		startDeviceListService();
    }

    //���Ͱ�����Ӧ
    public void onSendButtonClicked(View v){
    	int i=0;
    	int n=0;
    	try{
    		OutputStream os = _socket.getOutputStream();   //�������������
//    		String str=edit0.getText().toString();
//    		byte[] bos = edit0.getText().toString().getBytes();
//    		byte[] bos = edit0.getText().toString().getBytes();
//    		for(i=0;i<bos.length;i++){
//    			if(bos[i]==0x0a)n++;
//    		}
//    		byte[] bos_new = new byte[bos.length+n];
//    		n=0;
//    		for(i=0;i<bos.length;i++){ //�ֻ��л���Ϊ0a,�����Ϊ0d 0a���ٷ���
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
    
  //��������
    public boolean Send(String cmd){
    	try{
//    		
    		if(_socket==null)
    			return false;
    		OutputStream os = _socket.getOutputStream();   //�������������

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

    
    
    //���ջ�������ӦstartActivityForResult()
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    	switch(requestCode){
//    	case REQUEST_CONNECT_DEVICE:     //���ӽ������DeviceListActivity���÷���
//    		// ��Ӧ���ؽ��
//            if (resultCode == Activity.RESULT_OK) {   //���ӳɹ�����DeviceListActivity���÷���
//                // MAC��ַ����DeviceListActivity���÷���
////                String address = data.getExtras()
////                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                // �õ������豸���      
//                _device = _bluetooth.getRemoteDevice(address);
// 
//                // �÷���ŵõ�socket
//                try{
//                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
//                }catch(IOException e){
//                	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
//                }
//                //����socket
//            	Button btn = (Button) findViewById(R.id.Button03);
//                try{
//                	_socket.connect();
//                	Toast.makeText(this, "����"+_device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
//                	btn.setText("�Ͽ�");
//                }catch(IOException e){
//                	try{
//                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
//                		_socket.close();
//                		_socket = null;
//                	}catch(IOException ee){
//                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
//                	}
//                	
//                	return;
//                }
//                
//                //�򿪽����߳�
//                try{
//            		is = _socket.getInputStream();   //�õ���������������
//            		}catch(IOException e){
//            			Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
	 * �㲥������
	 *
	 */
	public class MsgReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			//�õ����ȣ�����UI
			Bundle bundle=intent.getExtras();
			String deviceName=bundle.getString("deviceName");
			String deviceAddress=bundle.getString("deviceAddress");
			
			if(_bluetooth==null){
			 _bluetooth = BluetoothAdapter.getDefaultAdapter();
			 }
			 _device = _bluetooth.getRemoteDevice(deviceAddress);
			 
             // �÷���ŵõ�socket
             try{
             	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
             }catch(IOException e){
//             	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
             }
             //����socket
//         	Button btn = (Button) findViewById(R.id.Button03);
             try{
             	_socket.connect();
//             	Toast.makeText(this, "����"+_device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
//             	btn.setText("�Ͽ�");
             }catch(IOException e){
             	try{
//             		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
             		_socket.close();
             		_socket = null;
             	}catch(IOException ee){
//             		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
             	}
             	
             	return;
             }
             
             //�򿪽����߳�
             try{
         		is = _socket.getInputStream();   //�õ���������������
         		}catch(IOException e){
//         			Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
    
    //���������߳�
    Thread ReadThread=new Thread(){
    	
    	public void run(){
    		int num = 0;
//    		byte[] buffer = new byte[256];
       
    		bRun = true;
    		
    		//�����߳�
    		while(true){
    			try{
    				while(is.available()==0){
    					while(bRun == false){}
    				}
    				while(true){
    					byte[] buffer = new byte[1024];
    					String s00;
    					num = is.read(buffer);         //��������
    			
    					s00=bytesToHexString(buffer);
    					s00=s00.substring(0,num*2);
    					Log.d("buffer",s00);

    					fmsg+=s00;    //�����յ�����
    					smsg+=new String(s00);   //д����ջ���
    					
        				//������ʾ��Ϣ��������ʾˢ��
        				Message msg = handler.obtainMessage();
        				msg.obj=s00;
        				handler.sendMessage(msg);   
     
        				if(is.available()==0)break;  //��ʱ��û�����ݲ�����������ʾ
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
    
    //��Ϣ�������
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
    			//�ж��Ƿ��ǽ�������͵�servletȥ��	true��ʾ���ͳɹ� ��һ���µ��߳�
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
        		{	//service û��context ��ʾ����
        			 Toast.makeText(getBaseContext(), "���ͳɹ���", Toast.LENGTH_LONG).show();  
        		}else
        		{
        			 Toast.makeText(getBaseContext(), "����ʧ�ܣ�", Toast.LENGTH_LONG).show(); 
        		}
        		//ѭ�����������н����ʱ������ע����
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


    
    //�رճ�����ô�����
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //�ر�����socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
    //	_bluetooth.disable();  //�ر���������
    	
    	//ע���㲥
    	//ֹͣ����
//    	stopService(startIntent);
    	unregisterReceiver(deviceReceiver);
    }
    
    //�˵�������
  /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {//�����˵�
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }*/

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) { //�˵���Ӧ����
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
    
//    //���Ӱ�����Ӧ���� �ĳ��������������
//    public void onConnectButtonClicked(View v){ 
//    	if(_bluetooth.isEnabled()==false){  //����������񲻿�������ʾ
//    		Toast.makeText(this, " �ֻ�δ��������  ", Toast.LENGTH_LONG).show();
//    		return;
//    	}
//    	
//    	
//        //��δ�����豸���DeviceListActivity�����豸����
////    	Button btn = (Button) findViewById(R.id.Button03);
//    	if(_socket==null){
////    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //��ת��������
////    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //���÷��غ궨��
//    		
//    		//��ʼ�����豸�ķ���
//    		startIntent = new Intent("com.test.BTClient.DeviceListService");
//			startService(startIntent);
//    	}
//    	else{
//    		 //�ر�����socket
//    	    try{
//    	    	
//    	    	is.close();
//    	    	_socket.close();
//    	    	_socket = null;
//    	    	bRun = false;
////    	    	btn.setText("����");
//    	    }catch(IOException e){}   
//    	}
//    	return;
//    }
    
  //����DeviceListService �ر�socket
    public void startDeviceListService(){ 
    	//2014-4-28
//    	if(_bluetooth.isEnabled()==false){  //����������񲻿�������ʾ
//    		Toast.makeText(this, " �ֻ�δ��������  ", Toast.LENGTH_LONG).show();
//    		return;
//    	}
    	
    	
        //��δ�����豸���DeviceListActivity�����豸����
//    	Button btn = (Button) findViewById(R.id.Button03);
//    	if(_socket==null){
//    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //��ת��������
//    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //���÷��غ궨��
    		
    		//��ʼ�����豸�ķ���
    		startIntent = new Intent("com.test.BTClient.DeviceListService");
			startService(startIntent);
//    	}
//    	else{
    		 //�ر�����socket
//    	    try{
//    	    	if(is!=null)
//    	    		is.close();
//    	    	_socket.close();
//    	    	_socket = null;
//    	    	bRun = false;
//    	    	btn.setText("����");
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
    
    
    
    
    //���水����Ӧ����
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //���������Ӧ����
    public void onClearButtonClicked(View v){
    	smsg="";
    	fmsg="";
//    	dis.setText(smsg);
//    	 mConversationArrayAdapter.clear();
    	return;
    }
    
    //�˳�������Ӧ����
    public void onQuitButtonClicked(View v){
//    	finish();
    }
    
    //���湦��ʵ��
	private void Save() {
		//��ʾ�Ի��������ļ���
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //ͼ��ģ�����������
		final View DialogView =  factory.inflate(R.layout.sname, null);  //��sname.xmlģ��������ͼģ��
		new AlertDialog.Builder(BTClient.this)
								.setTitle("�ļ���")
								.setView(DialogView)   //������ͼģ��
								.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() //ȷ��������Ӧ����
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //�õ��ļ����������
										filename = text1.getText().toString();  //�õ��ļ���
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
												
												filename =filename+".txt";   //���ļ���ĩβ����.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
												File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
												FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������
												stream.write(fmsg.getBytes());
												stream.close();
												Toast.makeText(BTClient.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "û�д洢����", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}
										
										
										
									}
								})
								.setNegativeButton("ȡ��",   //ȡ��������Ӧ����,ֱ���˳��Ի������κδ��� 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //��ʾ�Ի���
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		return new MsgBinder();
	} 
	public class MsgBinder extends Binder{
		/**
		 * ��ȡ��ǰService��ʵ��
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
		 //����򿪱��������豸���ɹ�����ʾ��Ϣ����������
        if (_bluetooth == null){
        	Toast.makeText(this, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�", Toast.LENGTH_LONG).show();
//            finish();
        	//2014-4-28
//            return;
        }
        else{
        	BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        	if(mBtAdapter.isEnabled()){
        		//�����豸 д��onStartCommand����ȥ��
        		startDeviceListService();
        	}else{
        		Toast.makeText(getBaseContext(), "����ֻ��������ܣ�", Toast.LENGTH_SHORT).show();
        	}
 		}
		return startId;  
// 		return START_REDELIVER_INTENT;
		
	}
}