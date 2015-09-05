package com.example.ble_boombandui.health;

import com.example.ble_boombandui.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class IntentTab extends TabActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//���ر���
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
		setContentView(R.layout.health_tab);
		TabHost tabHost = getTabHost();
		//ʹ��Intent��ӵ�һ��Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab1")
			.setIndicator("����״̬")
			.setContent(new Intent(this, HealthStateActivity.class)));
		//ʹ��Intent��ӵڶ���Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab2")
			.setIndicator("���ս�������")
			.setContent(new Intent(this, DailyDataActivity.class)));
		//ʹ��Intent��ӵ�����Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab3")
			.setIndicator("��ʷ��������")
			.setContent(new Intent(this, HistoryActivity.class)));		
		//ʹ��Intent��ӵ�����Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab4")
			.setIndicator("������ʾ��Ϣ")
			.setContent(new Intent(this, HealthInfoActivity.class)));		
	}
}