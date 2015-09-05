/**
 * 
 */
package com.example.ble_boombandui.health;

import com.example.ble_boombandui.R;
import com.xxmassdeveloper.mpchartexample.MultiLineChartActivityMonth;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Description:
 * <br/>��վ: <a href="http://www.crazyit.org">���Java����</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class HistoryActivity extends TabActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_dailydata);
		
		TabHost tabHost = getTabHost();
		//ʹ��Intent��ӵ�һ��Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab31")
			.setIndicator("Ѫѹ����")
			.setContent(new Intent(this, MultiLineChartActivityMonth.class)));
		//ʹ��Intent��ӵڶ���Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab32")
			.setIndicator("�˶�����")
			.setContent(R.id.tab22));
		//ʹ��Intent��ӵ�����Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab33")
			.setIndicator("����")
			.setContent(R.id.tab23));		
		//ʹ��Intent��ӵ�����Tabҳ��
		tabHost.addTab(tabHost.newTabSpec("tab34")
			.setIndicator("Ѫ��")
			.setContent(R.id.tab24));		
	}
}
