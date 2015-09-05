/**
 * 
 */
package com.example.ble_boombandui.health;


import com.example.ble_boombandui.R;
import com.xxmassdeveloper.mpchartexample.MultiLineChartActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Description:
 * <br/>网站: <a href="http://www.crazyit.org">疯狂Java联盟</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class DailyDataActivity extends TabActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_dailydata);
		
		TabHost tabHost = getTabHost();
		//使用Intent添加第一个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab1")
			.setIndicator("血压脉率")
			.setContent(new Intent(this, MultiLineChartActivity.class)));
		//使用Intent添加第二个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab2")
			.setIndicator("运动健康")
			.setContent(R.id.tab22));
		//使用Intent添加第三个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab3")
			.setIndicator("体重")
			.setContent(R.id.tab23));		
		//使用Intent添加第三个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab4")
			.setIndicator("血糖")
			.setContent(R.id.tab24));		
	}
}
