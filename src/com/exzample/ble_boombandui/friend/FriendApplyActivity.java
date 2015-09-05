package com.exzample.ble_boombandui.friend;

import java.util.ArrayList;
import java.util.List;

import org.androidpn.client.NotificationDetailsActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import url.SendURL;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
//��������б� �Ƿ�ͬ������
public class FriendApplyActivity extends Activity{
	private ListView lstView_friendapply;
	ArrayList<String> applyArrayList=new ArrayList<String>();
	private String online_username;
	private String online_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_apply);
	
		lstView_friendapply=(ListView) findViewById(R.id.list_friendapply);
		
		Intent user_intent=getIntent();
		online_username=user_intent.getStringExtra("username");
		online_password=user_intent.getStringExtra("password");
		Bundle bundle=user_intent.getExtras();
		JSONObject applypayload = null;
		try {
			applypayload = new JSONObject( bundle.getSerializable("payload").toString());
			JSONArray jsonArray=applypayload.getJSONArray("jsonlist");
			for(int i=0;i<jsonArray.length();i++)
			{
				applyArrayList.add(jsonArray.getString(i));
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
//		-----------------------------------------------
	        MyAdapter adapter = new MyAdapter(this);
	        lstView_friendapply.setAdapter(adapter);
	        
	        lstView_friendapply.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub ������ʱ����
				
					
				}  
			});
		

        
	}


		//ViewHolder��̬��
	    static class ViewHolder
	    {
	        public TextView friendApplyTextView;
	        public Button friendApplyokButton;
	        public Button friendApplyignoreButton;
	      
	    }
	    
	    public class MyAdapter extends BaseAdapter
	    {    
	        private LayoutInflater mInflater = null;
	        private MyAdapter(Context context)
	        {
	            //����context�����ļ��ز��֣��������Demo17Activity������this
	            this.mInflater = LayoutInflater.from(context);
	        }

	        @Override
	        public int getCount() {
	            //How many items are in the data set represented by this Adapter.
	            //�ڴ�������������������ݼ��е���Ŀ��
	            return applyArrayList.size();
	        }

	        @Override
	        public Object getItem(int position) {
	            // Get the data item associated with the specified position in the data set.
	            //��ȡ���ݼ�����ָ��������Ӧ��������
	            return position;
	        }

	        @Override
	        public long getItemId(int position) {
	            //Get the row id associated with the specified position in the list.
	            //��ȡ���б�����ָ��������Ӧ����id
	            return position;
	        }
	        
	        //Get a View that displays the data at the specified position in the data set.
	        //��ȡһ�������ݼ���ָ����������ͼ����ʾ����
	        @Override
	        public View getView(final int position, View convertView, ViewGroup parent) {
	            ViewHolder holder = null;
	            //�������convertViewΪ�գ�����Ҫ����View
	            if(convertView == null)
	            {
	                holder = new ViewHolder();
	                //�����Զ����Item���ּ��ز���
	    
	                convertView = mInflater.inflate(R.layout.friend_applyitem, null);
	                holder.friendApplyTextView = (TextView)convertView.findViewById(R.id.tx_friendapply);
	                holder.friendApplyokButton = (Button)convertView.findViewById(R.id.btn_friendapplyok);
	                holder.friendApplyignoreButton = (Button)convertView.findViewById(R.id.btn_friendapplyignore);
	                //�����úõĲ��ֱ��浽�����У�������������Tag��Ա���淽��ȡ��Tag
	                convertView.setTag(holder);
	            }else
	            {
	                holder = (ViewHolder)convertView.getTag();
	            }

	            holder.friendApplyTextView.setText(applyArrayList.get(position)+" �������Ϊ����");
	            holder.friendApplyokButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub ͬ��Ӻ���
						String applyname=applyArrayList.get(position);
						boolean response=(new SendURL()).processfriendapply(online_username, applyname, 1);

						if(response==true){
							applyArrayList.remove(position);
							notifyDataSetChanged();
						}
					}
				});
	            holder.friendApplyignoreButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub ���ԼӺ���
						String applyname=applyArrayList.get(position);
						boolean response=(new SendURL()).processfriendapply(online_username, applyname, 0);
		
						if(response==true){
							applyArrayList.remove(position);
							notifyDataSetChanged();
						}
					}
				});
	            
	            return convertView;
	        }

			
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
}
