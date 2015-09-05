package com.xxmassdeveloper.mpchartexample;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.ble_boombandui.FirstPageActivity;
import com.example.ble_boombandui.R;
import com.example.ble_boombandui.R.id;
import com.example.ble_boombandui.health.IntentTab;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.data.filter.Approximator.ApproximatorType;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.kankan.wheel.widget.WheelView;
import com.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import url.SendURL;

public class MultiLineChartActivityMonth extends DemoBase implements 
        OnChartValueSelectedListener, OnClickListener  {

    private LineChart mChart;
    private static int DURATION=30;             //时间区间
    private static final int REDLINE_SYS=150;				//红 橙 黄 警戒线
    private static final int ORANGELINE_SYS=140;
    private static final int YELLOWLINE_SYS=130;
    private static final int REDLINE_DIA=110;				//红 橙 黄 警戒线
    private static final int ORANGELINE_DIA=100;
    private static final int YELLOWLINE_DIA=90;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter = null;  
    private static final String[] SPINNER_SELECT={"今天","7天","30天"};
    private  YAxis yAxis;
    private YAxis yAxis2;
    private String year;
    private String month;
    private static WheelView listWheelView;
    ArrayList list;
    private int mYear;     //日历选择器
    private int mMonth;     
    private int mDay;  
    private boolean ISFIRSTINIT=true;
//    private ReceiveBroadCast receiveBroadCast;  //广播实例
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart2_1);  
        if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

              
//        ------------初始化下拉框-------------
//        spinner=(Spinner) findViewById(R.id.spinner_chart1);
//        spinner_adapter=new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item,SPINNER_SELECT);
//        spinner.setAdapter(spinner_adapter);
//        spinner.setOnItemSelectedListener(this);
        
      //-----------------横屏还是竖屏-------------------------
        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
        {  
        	setContentView(R.layout.activity_linechart2_1);
        	mChart = (LineChart) findViewById(R.id.chart_history1);
        	listWheelView = (WheelView) findViewById(R.id.chart2_list1);
        	Button btn_recent=(Button)findViewById(R.id.btn_recent1);
            btn_recent.setOnClickListener(this);
            Button btn_pickdate=(Button)findViewById(R.id.btn_pickDate1);
            btn_pickdate.setOnClickListener(this);
        }  else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
         {  
        	setContentView(R.layout.activity_linechart2_2);
        	mChart = (LineChart) findViewById(R.id.chart_history2);
        	listWheelView = (WheelView) findViewById(R.id.chart2_list2);
        	Button btn_recent=(Button)findViewById(R.id.btn_recent2);
            btn_recent.setOnClickListener(this);
            Button btn_pickdate=(Button)findViewById(R.id.btn_pickDate2);
            btn_pickdate.setOnClickListener(this);
         }
        
//        ------------初始化图表----------------
        initChart();
        
        initDataSet(0);                 //0 月    1 周    2 天
        
        
//       	放动画
//        mChart.animateXY(3000, 3000);
        
//        -------------注册广播接收---Markview中点击事件无响应--------------
      
//        receiveBroadCast = new ReceiveBroadCast();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("MyMarkerView");    //只有持有相同的action的接受者才能接收此广播
//        registerReceiver(receiveBroadCast, filter);

        
        //获得当前时间  
        final Calendar c = Calendar.getInstance();     
        mYear = c.get(Calendar.YEAR);     
        mMonth = c.get(Calendar.MONTH);     
        mDay = c.get(Calendar.DAY_OF_MONTH);  
        
    }
   private void initChart(){

//       mChart.setChartactivity(this);                  //2015 设置双击放大进入下一级
     
       mChart.setOnChartValueSelectedListener(this);  
       
       mChart.setDrawGridBackground(false);

//        mChart.setStartAtZero(false);

       // enable value highlighting
       mChart.setHighlightEnabled(true);

       // enable touch gestures
       mChart.setTouchEnabled(true);

       // enable scaling and dragging
       mChart.setDragEnabled(true);
       mChart.setScaleEnabled(true);

       // if disabled, scaling can be done on x- and y-axis separately
       mChart.setPinchZoom(false);
       mChart.setScaleXEnabled(true);          //x可以移动  y不可以
       mChart.setScaleYEnabled(false);


       Legend l = mChart.getLegend();
       l.setPosition(LegendPosition.RIGHT_OF_CHART_INSIDE);  //dataset位置
       


//       ---------------初始化随机数-------------------
       mChart.resetTracking();
      
       //-----------------x标签显示位置-----------------
       XAxis xAxis = mChart.getXAxis();
       xAxis.setPosition(XAxisPosition.BOTTOM);
//     -------------------初始时放大 -----------------
       if(ISFIRSTINIT){
	      WindowManager wm = this.getWindowManager();
	      
	      int width = wm.getDefaultDisplay().getWidth();
	      int height = wm.getDefaultDisplay().getHeight();
	      mChart.zoom(3.0f, 0, width/4*3, height/2);       //以屏幕3/4处放大
	      ISFIRSTINIT=false;
       }
       
//     ---------------移到x=5处---------------
       mChart.moveViewToX(20);
//       mChart.moveViewToY(3, mChart.getAxisLeft().getAxisDependency());
       
     
       yAxis=mChart.getAxisLeft();
       yAxis.setStartAtZero(false);                  //y不是从0开始
//       yAxis.setAxisMinValue(20);
//       yAxis.setAxisMaxValue(200);
//       mChart.moveViewToY(150,yAxis.getAxisDependency());
       yAxis.setDrawGridLines(false);       
       yAxis2=mChart.getAxisRight();
       yAxis2.setStartAtZero(false);                  //y不是从0开始
//       yAxis2.setAxisMinValue(20);
//       yAxis2.setAxisMaxValue(200);
       yAxis2.setDrawGridLines(false);
       xAxis.setDrawGridLines(false);              //背景线不画
       
       LimitLine limitLine1=new LimitLine(REDLINE_SYS);  	//加一条线
       limitLine1.setLineColor(mColors[4]);
       limitLine1.enableDashedLine(10, 10, 0);
//       limitLine1.setTextStyle(Paint.Style.STROKE);
//       limitLine1.setLineWidth(400);
       yAxis.addLimitLine(limitLine1);
//       LimitLine limitLine2=new LimitLine(ORANGELINE_SYS);  	//加一条线
//       limitLine2.setLineColor(Color.YELLOW);
////       limitLine2.setLineWidth(400);
//       yAxis.addLimitLine(limitLine2);
//       LimitLine limitLine3=new LimitLine(YELLOWLINE_SYS);  	//加一条线
//       limitLine3.setLineColor(Color.GREEN);
////       limitLine3.setLineWidth(400);
//       yAxis.addLimitLine(limitLine3);
//       LimitLine limitLine4=new LimitLine(REDLINE_DIA);  	 //加一条线
//       limitLine4.setLineColor(mColors[4]);
////       limitLine4.setLineWidth(400);
//       yAxis.addLimitLine(limitLine4);
//       LimitLine limitLine5=new LimitLine(ORANGELINE_DIA);  	//加一条线
//       limitLine5.setLineColor(Color.YELLOW);
////       limitLine5.setLineWidth(400);
//       yAxis.addLimitLine(limitLine5);
//       LimitLine limitLine6=new LimitLine(YELLOWLINE_DIA);  	//加一条线
//       limitLine6.setLineColor(Color.GREEN);
////       limitLine6.setLineWidth(400);
//       yAxis.addLimitLine(limitLine6);
//     ----------------设置弹出框-------------------
//     MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//     mChart.setMarkerView(mv);
//     mChart.setHighlightIndicatorEnabled(false);
   }
    
   public boolean initDataSet(int flag) {
	  
//	   initChart();
	  
	   try {
		   list = new ArrayList();
		   listorder=new ArrayList<Integer>();
		   int YAxisMax=100;
		   int YAxisMin=100;
		   String resultString = null;
		   String from = null;
		   String to=null;
		   int movetox=0;                               //x轴移到最后有数值的那一天
		   int movetox1=0;
		   if(flag==0){                                 //最近21天  未改
			   Date date=new Date();
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			   String dateString = sdf.format(date);
			   Calendar cal = Calendar.getInstance();
			   cal.setTime(date);
			   int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			   cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-20 );
			   Date fromDate=cal.getTime();
			   from=sdf.format(fromDate);
			   to=sdf.format(date);
		        
			   resultString=(new SendURL()).history("JK00000011", from, to,0);
		   }else if(flag==1)                             //前后10天
		   {
			   String current=mYear+"-"+(mMonth+1)+"-"+mDay;
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		       Date date = sdf.parse(current);
		       Calendar cal = Calendar.getInstance();
			   cal.setTime(date);
		       int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			   cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-9 );
			   Date fromDate=cal.getTime();
			   from=sdf.format(fromDate);
			   cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear+10);
	           Date toDate=cal.getTime();
			   to=sdf.format(toDate);
//			   mChart.clear();                       //清空之前的数据
			   resultString=(new SendURL()).history("JK00000011", from, to,1);
		   }else if(flag==2)                             //日
		   {
			   LineData data=mChart.getData();
		       List<String> list=data.getXVals();
		       String current=list.get(currentX);
		       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		       Date date = sdf.parse(current);
		       Calendar cal = Calendar.getInstance();
			   cal.setTime(date);
		       int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
			   cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear+1);
	           Date toDate=cal.getTime();
			   to=sdf.format(toDate);
			   
			   resultString=(new SendURL()).history("JK00000011", current, to,2);
		   }else{
			   return false;                                   //什么都不做
		   }
		  
//		   resultString="{\"tophistory\":[{\"Systolic\":180,\"date\":\"2015-06-15 06:20:00\",\"diastolic\":130,\"pulserate\":90}," +
//		   		"{\"Systolic\":130,\"date\":\"2015-06-16 12:20:00\",\"diastolic\":90,\"pulserate\":90}," +
//		   		"{\"Systolic\":135,\"date\":\"2015-06-17 08:20:00\",\"diastolic\":85,\"pulserate\":87},"+
//		   		"{\"Systolic\":145,\"date\":\"2015-06-18 08:20:00\",\"diastolic\":95,\"pulserate\":87},"+
//		   		"{\"Systolic\":105,\"date\":\"2015-06-19 08:20:00\",\"diastolic\":65,\"pulserate\":87},"+
//		   		"{\"Systolic\":100,\"date\":\"2015-06-20 08:20:00\",\"diastolic\":62,\"pulserate\":87},"+
//		   		"{\"Systolic\":130,\"date\":\"2015-06-22 08:20:00\",\"diastolic\":90,\"pulserate\":87},"+
//		   		"{\"Systolic\":150,\"date\":\"2015-06-24 08:20:00\",\"diastolic\":110,\"pulserate\":87},"+
//		   		"{\"Systolic\":180,\"date\":\"2015-06-25 08:20:00\",\"diastolic\":135,\"pulserate\":87},"+
//		   		"{\"Systolic\":135,\"date\":\"2015-06-26 08:20:00\",\"diastolic\":95,\"pulserate\":87},"+
//		   		"{\"Systolic\":129,\"date\":\"2015-06-30 08:20:00\",\"diastolic\":89,\"pulserate\":87},"+
//		   		"{\"Systolic\":125,\"date\":\"2015-07-01 08:20:00\",\"diastolic\":85,\"pulserate\":87},"+
//		   		"{\"Systolic\":112,\"date\":\"2015-07-01 08:20:00\",\"diastolic\":72,\"pulserate\":87},"+
//		   		"{\"Systolic\":130,\"date\":\"2015-07-02 08:20:00\",\"diastolic\":90,\"pulserate\":87}]}";
		   if(resultString==null)
		   {
			   return false;
		   }
		   
		   if(resultString!=null){
			   if(!resultString.equals("")){
		   if(flag==0||flag==1){                                       //月
			    JSONObject jsonObjectfromDB;
				jsonObjectfromDB = new JSONObject(resultString);
				JSONArray dataSeriesJSONArray = jsonObjectfromDB.getJSONArray("tophistory");				
				if(dataSeriesJSONArray.length()==0)
					return false;
				SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
				SimpleDateFormat dateFormat2=new SimpleDateFormat("MM-dd");
				Date fromdate = dateFormat.parse(from);
				Calendar cal = Calendar.getInstance();
				cal.setTime(fromdate);
				int startdate=cal.get(Calendar.DAY_OF_YEAR);
				Date todate=dateFormat.parse(to);
				cal.setTime(todate);
				int enddate=cal.get(Calendar.DAY_OF_YEAR);
				DURATION=enddate-startdate+1;                 //计算这个月的长度
				  
				ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
				ArrayList<Entry> Diavalues = new ArrayList<Entry>();
				ArrayList<Entry> Sysvalues = new ArrayList<Entry>();
				ArrayList<String> Datevalues = new ArrayList<String>();
				int []SysColors=new int[DURATION];        //点的颜色
				int []DiaColors=new int[DURATION];
				int []SysLineColors=new int[DURATION];    //线的颜色  0就是没有
				int []DiaLineColors=new int[DURATION];
				
				int k=0;
				int kmax=dataSeriesJSONArray.length();
				
				for(int i=0;i<DURATION;i++)
				{	
					cal.set(Calendar.DAY_OF_YEAR , startdate+i);
				    Date datetemp=cal.getTime();
				    String datetempString=dateFormat.format(datetemp);
				    String dateMonthDay=dateFormat2.format(datetemp);
				    Datevalues.add(dateMonthDay);
				    if(k<kmax)
				    {
	  
					JSONObject jsonObject=(JSONObject) dataSeriesJSONArray.get(k);
					String dateString=jsonObject.getString("date").substring(0,10);
					
					if(dateString.equals(datetempString)){
					
						k++;
						int SYS=jsonObject.getInt("Systolic");
						int DIA=jsonObject.getInt("diastolic");
						int PUL=jsonObject.getInt("pulserate");
						Sysvalues.add(new Entry(SYS, i));
						Diavalues.add(new Entry(DIA, i));
						
						String dailyrecord;
						if(k<10){
							dailyrecord="0"+(k)+". "+dateString+" 高压:"+SYS+" 低压:"+DIA+" 心率:"+PUL;
						}
						else {
							dailyrecord=(k)+". "+dateString+" 高压:"+SYS+" 低压:"+DIA+" 心率:"+PUL;
						}
						list.add(dailyrecord);
						if(SYS>YAxisMax)
							YAxisMax=SYS;
						if(DIA<YAxisMin)
							YAxisMin=DIA;
						
						SysColors[i]=mColors[1];
						DiaColors[i]=mColors[1];
						SysLineColors[i]=mColors[5];
						DiaLineColors[i]=mColors[5];
						if(flag==0)
							movetox=i;
						
					}else {
						Sysvalues.add(new Entry(-1, i));
						Diavalues.add(new Entry(-1, i));
						if(i>0){
							SysLineColors[i-1]=0;
							DiaLineColors[i-1]=0;
						}
					}
					}else {
						Sysvalues.add(new Entry(-1, i));
						Diavalues.add(new Entry(-1, i));
						if(i>0){
							SysLineColors[i-1]=0;
							DiaLineColors[i-1]=0;
						}
					}
					listorder.add(k-1);                             //日期与记录      第1天对应第几条记录
					
				}
				LineDataSet SysLineDataSet = new LineDataSet(Sysvalues, "高压");
				SysLineDataSet.setLineWidth(3f);
				SysLineDataSet.setCircleSize(10f);
				SysLineDataSet.setDrawCircleHole(false);				
				
	            SysLineDataSet.setColors(SysLineColors);
	            SysLineDataSet.setCircleColors(SysColors);
	    
	            LineDataSet DiaLineDataSet = new LineDataSet(Diavalues, "低压");
	            DiaLineDataSet.setLineWidth(3f);
	            DiaLineDataSet.setCircleSize(10f);
	            DiaLineDataSet.setDrawCircleHole(false);

	            
	            DiaLineDataSet.setColors(DiaLineColors);
	            DiaLineDataSet.setCircleColors(DiaColors);
	//            DiaLineDataSet.enableDashedLine(10, 10, 0);
	            
	            dataSets.add(SysLineDataSet);
	            dataSets.add(DiaLineDataSet);
	            
	            LineData data = new LineData(Datevalues, dataSets);
	            
	            YAxisMax=(YAxisMax/10)*10+20;
	 		    YAxisMin=(YAxisMin/10)*10-20;
	 	   
	 		    yAxis.setAxisMaxValue(YAxisMax);
	 		    yAxis.setAxisMinValue(YAxisMin);
	 		    yAxis2.setAxisMaxValue(YAxisMax);
	 		    yAxis2.setAxisMinValue(YAxisMin);
	            
	            mChart.setData(data);							//if flag==1 or 2  7天或一个月

		  } else{                                            //flag==0 今天
			  	JSONObject jsonObjectfromDB;
				jsonObjectfromDB = new JSONObject(resultString);
				JSONArray dataSeriesJSONArray = jsonObjectfromDB.getJSONArray("tophistory");
				if(dataSeriesJSONArray.length()==0)
					return false;
				ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
				ArrayList<Entry> Diavalues = new ArrayList<Entry>();
				ArrayList<Entry> Sysvalues = new ArrayList<Entry>();
				ArrayList<String> Datevalues = new ArrayList<String>();
				int length=dataSeriesJSONArray.length();
				int []SysColors=new int[length];        //点的颜色
				int []DiaColors=new int[length];
				int []SysLineColors=new int[length];    //线的颜色  0就是没有
				int []DiaLineColors=new int[length];
				 
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				  
//				int kmax=dataSeriesJSONArray.length()-1;
				int i=0;
				while(i<length){
				    
					JSONObject jsonObject=(JSONObject) dataSeriesJSONArray.get(i);
					String dateString=jsonObject.getString("date").substring(11);
					Datevalues.add(dateString);
						int SYS=jsonObject.getInt("Systolic");
						int DIA=jsonObject.getInt("diastolic");
						Sysvalues.add(new Entry(SYS, i));
						Diavalues.add(new Entry(DIA, i));
						
						if(SYS>YAxisMax)
							YAxisMax=SYS;
						if(DIA<YAxisMin)
							YAxisMin=DIA;
						
						if(SYS>=REDLINE_SYS){                                    //根据值的大小，点的颜色不同
							SysColors[i]=mColors[4];
						}else if(SYS>=ORANGELINE_SYS)
						{
							SysColors[i]=mColors[2];
						}else if(SYS>=YELLOWLINE_SYS)
						{
							SysColors[i]=mColors[1];
						}else {
							SysColors[i]=mColors[0];
						}
						
						if(DIA>=REDLINE_DIA){
							DiaColors[i]=mColors[4];
						}else if(DIA>=ORANGELINE_DIA)
						{
							DiaColors[i]=mColors[2];
						}else if(DIA>=YELLOWLINE_DIA)
						{
							DiaColors[i]=mColors[1];
						}else {
							DiaColors[i]=mColors[0];
						}
						SysLineColors[i]=mColors[3];
						DiaLineColors[i]=mColors[3];
						
						i++;
					}
	
				LineDataSet SysLineDataSet = new LineDataSet(Sysvalues, "高压");
				SysLineDataSet.setLineWidth(3f);
				SysLineDataSet.setCircleSize(10f);
				SysLineDataSet.setDrawCircleHole(false);	
	            SysLineDataSet.setColors(SysLineColors);
	            SysLineDataSet.setCircleColors(SysColors);
	            
	            LineDataSet DiaLineDataSet = new LineDataSet(Diavalues, "低压");
	            DiaLineDataSet.setLineWidth(3f);
	            DiaLineDataSet.setCircleSize(10f);
	            DiaLineDataSet.setDrawCircleHole(false);	
	            DiaLineDataSet.setColors(DiaLineColors);
	            DiaLineDataSet.setCircleColors(DiaColors);

	            dataSets.add(SysLineDataSet);
	            dataSets.add(DiaLineDataSet);
	            
	            LineData data = new LineData(Datevalues, dataSets);
	            
	            YAxisMax=(YAxisMax/10)*10+20;
	 		    YAxisMin=(YAxisMin/10)*10-20;
	 	   
	 		    yAxis.setAxisMaxValue(YAxisMax);
	 		    yAxis.setAxisMinValue(YAxisMin);
	 		    yAxis2.setAxisMaxValue(YAxisMax);
	 		    yAxis2.setAxisMinValue(YAxisMin);
	 		   
	            mChart.setData(data);		
		  } 		//else flag==0   7天
		 }
		 }               									 //if(!resultString.equals("")||resultString!=null)
		   if(flag==0)
			   mChart.moveViewToX(movetox-6);
		   else if(flag==1)
			   mChart.moveViewToX(6);
		   mChart.invalidate();
		   initWheelList();
	   	}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   	}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    return true;
   }
    
	private void initWheelList(){
		   
	      
	      listWheelView.setVisibleItems(7);  
	      listWheelView.setViewAdapter(new CountryAdapter(this,list));
	       
	      listWheelView.setCurrentItem(0);
	}
   

	private int[] mColors = new int[] {
    		ColorTemplate.VORDIPLOM_COLORS[3], //蓝
            ColorTemplate.VORDIPLOM_COLORS[0], //绿
            ColorTemplate.VORDIPLOM_COLORS[1], //黄
            ColorTemplate.VORDIPLOM_COLORS[2], //橙
            ColorTemplate.VORDIPLOM_COLORS[4], //红
            ColorTemplate.VORDIPLOM_COLORS[5]  //线
    };

    static int currentX;
    List listorder;
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
        
         currentX=e.getXIndex();
         listWheelView.setCurrentItem((Integer) listorder.get(e.getXIndex()));                 //列表
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }
    
//    btn_pickdate btn_recent
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_recent1:
			initChart();
			initDataSet(0);
//			initWheelList();
			break;
		case R.id.btn_recent2:
			initChart();
			initDataSet(0);
//			initWheelList();
			break;
		case R.id.btn_pickDate2:
			 DatePickerDialog dpd=new DatePickerDialog(getParent(),Datelistener,mYear,mMonth,mDay);
             dpd.show();
            
			break;
		case R.id.btn_pickDate1:
			 DatePickerDialog dpd2=new DatePickerDialog(getParent(),Datelistener,mYear,mMonth,mDay);
             dpd2.show();
      
			break;
		}
	}
	
	private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener()
    {
        /**params：view：该事件关联的组件
         * params：myyear：当前选择的年
         * params：monthOfYear：当前选择的月
         * params：dayOfMonth：当前选择的日
         */
        @Override
        public void onDateSet(DatePicker view, int myyear, int monthOfYear,int dayOfMonth) {
            
            
            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
        	mYear=myyear;
            mMonth=monthOfYear;
            mDay=dayOfMonth;
            //图表
            initChart();
			initDataSet(1);
//			initWheelList();
        }
       
    };

  
//  横竖屏切换
  @Override
  public void onConfigurationChanged(Configuration newConfig) {
          super.onConfigurationChanged(newConfig);
          if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
          {  
          	setContentView(R.layout.activity_linechart2_1);
          	mChart = (LineChart) findViewById(R.id.chart_history1);
          	listWheelView = (WheelView) findViewById(R.id.chart2_list1);
          	Button btn_recent=(Button)findViewById(R.id.btn_recent1);
              btn_recent.setOnClickListener(this);
              Button btn_pickdate=(Button)findViewById(R.id.btn_pickDate1);
              btn_pickdate.setOnClickListener(this);
          }  else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
           {  
          	setContentView(R.layout.activity_linechart2_2);
          	mChart = (LineChart) findViewById(R.id.chart_history2);
          	listWheelView = (WheelView) findViewById(R.id.chart2_list2);
          	Button btn_recent=(Button)findViewById(R.id.btn_recent2);
              btn_recent.setOnClickListener(this);
              Button btn_pickdate=(Button)findViewById(R.id.btn_pickDate2);
              btn_pickdate.setOnClickListener(this);
           }
  }
	
	
	//    列表适配器
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
    	ArrayList list;
        protected CountryAdapter(Context context,ArrayList list) {
        	 super(context, R.layout.tempitem, NO_RESOURCE);
             this.list = list;
             setItemTextResource(R.id.tempValue);    //修改字体大小在这个布局文件里面
        }


               
        @Override
        public int getItemsCount() {
            return list.size();
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index)+"";
        }

		@Override
		public int getTextSize() {
			// TODO Auto-generated method stub
			return super.getTextSize();
		}

		@Override
		public void setTextSize(int textSize) {
			// TODO Auto-generated method stub
			super.setTextSize(textSize);
		}
        
        
    }
}


//spinner select
//	@Override
//	public void onItemSelected(AdapterView<?> parent, View view, int position,
//			long id) {
//		switch (position){
//		case 0:
//			DURATION=1;
//			initDataSet(0);
//			break;
//		case 1:
//			DURATION=7;
//			initDataSet(1);			
//			break;
//		case 2:
//			DURATION=30;
//			initDataSet(2);
//			break;
//		}
//		
//	}

//	@Override
//	public void onNothingSelected(AdapterView<?> parent) {
//		// TODO Auto-generated method stub
//		
//	}
   

//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//    }

// class ReceiveBroadCast extends BroadcastReceiver
//{
//
//       @Override
//       public void onReceive(Context context, Intent intent)
//       {
//           //得到广播中得到的数据，并显示出来
//           String message = intent.getStringExtra("data");
//           Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//       }
//
//}
