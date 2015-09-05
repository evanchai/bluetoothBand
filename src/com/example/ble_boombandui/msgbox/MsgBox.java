package com.example.ble_boombandui.msgbox;

import java.util.ArrayList;
import java.util.List;

import org.androidpn.client.NotificationDetailsActivity;

import com.example.ble_boombandui.FirstPageActivity;
import com.example.ble_boombandui.R;
import com.example.ble_boombandui.dao.AndroidpnMsgDao;
import com.example.ble_boombandui.model.AndroidpnMsg;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
//打开日历某天所测的记录
public class MsgBox extends Activity{
	private ListView lstView_msgbox;
	 private List<AndroidpnMsg> data;
	 private AndroidpnMsgDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.msgbox);
	
		lstView_msgbox=(ListView) findViewById(R.id.msgbox_list);
		
		dao=new AndroidpnMsgDao(this);
		data=new ArrayList<AndroidpnMsg>();
        data=dao.getAllMsg();
		if(data!=null){
	        MyAdapter adapter = new MyAdapter(this);
	        lstView_msgbox.setAdapter(adapter);
	        
	        lstView_msgbox.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
	
					AndroidpnMsg msgitem=data.get(position);
					int msgid=msgitem.getMsgID();
					String msgString=msgitem.getMsg();
					String msgtitleString=msgitem.getTitle();
					
					Intent intent3=new Intent(MsgBox.this,NotificationDetailsActivity.class);
					intent3.putExtra("NOTIFICATION_MESSAGE", msgString);
					intent3.putExtra("msgid", msgid+"");
					intent3.putExtra("NOTIFICATION_TITLE", msgtitleString);
					startActivity(intent3);
					
					
				}  
			});
		}
//        注册更新广播
//        IntentFilter intentFilter = new IntentFilter();  
//        intentFilter.addAction("action.refreshFriend");  
//        registerReceiver(mRefreshBroadcastReceiver, intentFilter);  
        
	}
	
//	private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {  
//		  
//	      @Override  
//	      public void onReceive(Context context, Intent intent) {  
//	          String action = intent.getAction();  
//	          if (action.equals("action.refreshFriend"))  
//	          {  
//	        	  refreshMsgList();  
//	          }  
//	      }  
//	  };  
	 
	protected void refreshMsgList() {
		// TODO Auto-generated method stub
		dao=new AndroidpnMsgDao(this);
		data=new ArrayList<AndroidpnMsg>();
        data=dao.getAllMsg();
		
        MyAdapter adapter = new MyAdapter(this);
        lstView_msgbox.setAdapter(adapter);
	}


		//ViewHolder静态类
	    static class ViewHolder
	    {
	        public TextView Msgbox_date;
	        public TextView Msgbox_msg;
	        public TextView Msgbox_id;
	      
	    }
	    
	    public class MyAdapter extends BaseAdapter
	    {    
	        private LayoutInflater mInflater = null;
	        private MyAdapter(Context context)
	        {
	            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
	            this.mInflater = LayoutInflater.from(context);
	        }

	        @Override
	        public int getCount() {
	            //How many items are in the data set represented by this Adapter.
	            //在此适配器中所代表的数据集中的条目数
	            return data.size();
	        }

	        @Override
	        public Object getItem(int position) {
	            // Get the data item associated with the specified position in the data set.
	            //获取数据集中与指定索引对应的数据项
	            return position;
	        }

	        @Override
	        public long getItemId(int position) {
	            //Get the row id associated with the specified position in the list.
	            //获取在列表中与指定索引对应的行id
	            return position;
	        }
	        
	        //Get a View that displays the data at the specified position in the data set.
	        //获取一个在数据集中指定索引的视图来显示数据
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            ViewHolder holder = null;
	            //如果缓存convertView为空，则需要创建View
	            if(convertView == null)
	            {
	                holder = new ViewHolder();
	                //根据自定义的Item布局加载布局
	    
	                convertView = mInflater.inflate(R.layout.msgbox_item, null);
	                holder.Msgbox_date = (TextView)convertView.findViewById(R.id.msgbox_date);
	                holder.Msgbox_msg = (TextView)convertView.findViewById(R.id.msgbox_msg);
	                holder.Msgbox_id = (TextView)convertView.findViewById(R.id.msg_id);
	                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
	                convertView.setTag(holder);
	            }else
	            {
	                holder = (ViewHolder)convertView.getTag();
	            }

	            holder.Msgbox_date.setText(data.get(position).getDate());
	            holder.Msgbox_msg.setText(data.get(position).getMsg()+" isread="+data.get(position).getIsread());
	            int temp=data.get(position).getMsgID();
	            holder.Msgbox_id.setText(temp+"");
	            holder.Msgbox_id.setVisibility(View.INVISIBLE);
	            
	            AndroidpnMsg androidpnMsg=data.get(position);
	            int id=androidpnMsg.getMsgID();
	            int isread=androidpnMsg.getIsread();
	            if(data.get(position).getIsread()==0){
//	            	没看过，设置提示图片
//	            	convertView.setBackgroundColor(Color.argb(200, 255, 255, 150));
	            }
	            
	            return convertView;
	        }

			
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
}
