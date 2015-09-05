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
import com.pwp.dao.AndroidpnMsgDao;
import com.pwp.vo.AndroidpnMsg;

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
//申请好友列表 是否同意或忽略
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
					// TODO Auto-generated method stub 这里暂时不用
				
					
				}  
			});
		

        
	}


		//ViewHolder静态类
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
	            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
	            this.mInflater = LayoutInflater.from(context);
	        }

	        @Override
	        public int getCount() {
	            //How many items are in the data set represented by this Adapter.
	            //在此适配器中所代表的数据集中的条目数
	            return applyArrayList.size();
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
	        public View getView(final int position, View convertView, ViewGroup parent) {
	            ViewHolder holder = null;
	            //如果缓存convertView为空，则需要创建View
	            if(convertView == null)
	            {
	                holder = new ViewHolder();
	                //根据自定义的Item布局加载布局
	    
	                convertView = mInflater.inflate(R.layout.friend_applyitem, null);
	                holder.friendApplyTextView = (TextView)convertView.findViewById(R.id.tx_friendapply);
	                holder.friendApplyokButton = (Button)convertView.findViewById(R.id.btn_friendapplyok);
	                holder.friendApplyignoreButton = (Button)convertView.findViewById(R.id.btn_friendapplyignore);
	                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
	                convertView.setTag(holder);
	            }else
	            {
	                holder = (ViewHolder)convertView.getTag();
	            }

	            holder.friendApplyTextView.setText(applyArrayList.get(position)+" 请求加您为好友");
	            holder.friendApplyokButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub 同意加好友
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
						// TODO Auto-generated method stub 忽略加好友
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
