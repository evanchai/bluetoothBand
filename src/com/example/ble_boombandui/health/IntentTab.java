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
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.health_tab);
		TabHost tabHost = getTabHost();
		//使用Intent添加第一个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab1")
			.setIndicator("健康状态")
			.setContent(new Intent(this, HealthStateActivity.class)));
		//使用Intent添加第二个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab2")
			.setIndicator("当日健康数据")
			.setContent(new Intent(this, DailyDataActivity.class)));
		//使用Intent添加第三个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab3")
			.setIndicator("历史健康数据")
			.setContent(new Intent(this, HistoryActivity.class)));		
		//使用Intent添加第三个Tab页面
		tabHost.addTab(tabHost.newTabSpec("tab4")
			.setIndicator("健康提示信息")
			.setContent(new Intent(this, HealthInfoActivity.class)));		
	}
}