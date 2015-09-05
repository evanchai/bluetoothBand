package com.xxmassdeveloper.mpchartexample;


import android.R.integer;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ble_boombandui.R;
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

import com.kankan.wheel.widget.OnWheelChangedListener;
import com.kankan.wheel.widget.OnWheelScrollListener;
import com.kankan.wheel.widget.WheelView;
import com.kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.Duration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import url.SendURL;

public class MultiLineChartActivity extends DemoBase implements 
        OnChartValueSelectedListener  {

    private LineChart mChart;
    private static int DURATION=7;             //时间区间
    private static final int REDLINE_SYS=150;				//红 橙 黄 警戒线
    private static final int ORANGELINE_SYS=140;
    private static final int YELLOWLINE_SYS=130;
    private static final int REDLINE_DIA=110;				//红 橙 黄 警戒线
    private static final int ORANGELINE_DIA=100;
    private static final int YELLOWLINE_DIA=90;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinner_adapter = null;  
//    private static final String[] SPINNER_SELECT={"今天","7天","30天"};
    private  YAxis yAxis;
    private YAxis yAxis2;
    private static WheelView listWheelView;
    ArrayList list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}

//-----------------横屏还是竖屏-------------------------
        if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
        {  
        	setContentView(R.layout.activity_linechart1_1);
        	mChart = (LineChart) findViewById(R.id.chart1_1);
        	listWheelView = (WheelView) findViewById(R.id.chart1_list1);
        }  else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
         {  
        	setContentView(R.layout.activity_linechart1_2);
        	mChart = (LineChart) findViewById(R.id.chart1_2);
        	listWheelView = (WheelView) findViewById(R.id.chart1_list2);
         }
//      ------------初始化图表----------------
        initChart();
        
        initDataSet(0);
       
        
//       	放动画
//        mChart.animateXY(3000, 3000);
    }
    
    
   private void initChart(){
       
       mChart.setChartactivity(this);
   
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

       
//     ---------------移到x=5处---------------
      	mChart.moveViewToX(21);
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
    
   
   
   public void initDataSet(int flag){
	   try {
		   list = new ArrayList();
		   
		   int YAxisMax=100;
		   int YAxisMin=100;
		   String resultString = null;
		   if(flag==1){
		    resultString=(new SendURL()).tophistory("JK00000011",1);
		   }else if(flag==2)
		   {
			   resultString=(new SendURL()).tophistory("JK00000011",2);
		   }else {
			   resultString=(new SendURL()).tophistory("JK00000011",0);
		   }
		   
		   if(resultString!=null){
			   if(!resultString.equals("")){
		   if(flag==1||flag==2){
			    JSONObject jsonObjectfromDB;
				jsonObjectfromDB = new JSONObject(resultString);
				JSONArray dataSeriesJSONArray = jsonObjectfromDB.getJSONArray("tophistory");
				
				ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
				ArrayList<Entry> Diavalues = new ArrayList<Entry>();
				ArrayList<Entry> Sysvalues = new ArrayList<Entry>();
				ArrayList<String> Datevalues = new ArrayList<String>();
				int []SysColors=new int[DURATION];        //点的颜色
				int []DiaColors=new int[DURATION];
				int []SysLineColors=new int[DURATION];    //线的颜色  0就是没有
				int []DiaLineColors=new int[DURATION];
				 
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date now = new Date();		  
				Calendar cal = Calendar.getInstance();
				cal.setTime(now);
				int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
				cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-DURATION+1 );
				int DuarionAgo = cal.get(Calendar.DAY_OF_YEAR);
				  
				int k=0;
				int kmax=dataSeriesJSONArray.length();
				for(int i=0;i<DURATION;i++)
				{	
					cal.set(Calendar.DAY_OF_YEAR , DuarionAgo+i );
				    Date datetemp=cal.getTime();
				    String datetempString=dateFormat.format(datetemp);
				    Datevalues.add(datetempString);
				    if(k<kmax)
				    {
	  
					JSONObject jsonObject=(JSONObject) dataSeriesJSONArray.get(k);
					String dateString=jsonObject.getString("date").substring(0,10);
					
					if(dateString.equals(datetempString)){
						k++;
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
				
				ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
				ArrayList<Entry> Diavalues = new ArrayList<Entry>();
				ArrayList<Entry> Sysvalues = new ArrayList<Entry>();
				ArrayList<String> Datevalues = new ArrayList<String>();
				int length=dataSeriesJSONArray.length();
				int []SysColors=new int[length];        //点的颜色
				int []DiaColors=new int[length];
				int []SysLineColors=new int[length];    //线的颜色  0就是没有
				int []DiaLineColors=new int[length];
				 
//			     -----------------大于7次一屏7个 -----------------
			    if(length>7){  
					WindowManager wm = this.getWindowManager();
				      
				      int width = wm.getDefaultDisplay().getWidth();
				      int height = wm.getDefaultDisplay().getHeight();
				      float temp=((float)length)/7;
				      mChart.zoom(temp, 0, width/4*3, height/2);       //以屏幕3/4处放大
			    }
//				-----------------------------------------------
				DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				  
//				int kmax=dataSeriesJSONArray.length()-1;
				int i=0;
				while(i<length){
					JSONObject jsonObject=(JSONObject) dataSeriesJSONArray.get(i);
					String dateString=jsonObject.getString("date").substring(11);
					Datevalues.add(dateString);
						int SYS=jsonObject.getInt("Systolic");
						int DIA=jsonObject.getInt("diastolic");
						int PUL=jsonObject.getInt("pulserate");
						int WSP=jsonObject.getInt("wspcolor");
						int WDP=jsonObject.getInt("wdpcolor");
						Sysvalues.add(new Entry(SYS, i));
						Diavalues.add(new Entry(DIA, i));
						String dailyrecord;
						if(i<9){
							dailyrecord="0"+(i+1)+". "+dateString+" 高压:"+SYS+" 低压:"+DIA+" 心率:"+PUL;
						}
						else {
							dailyrecord=(i+1)+". "+dateString+" 高压:"+SYS+" 低压:"+DIA+" 心率:"+PUL;
						}
						list.add(dailyrecord);
						if(SYS>YAxisMax)
							YAxisMax=SYS;
						if(DIA<YAxisMin)
							YAxisMin=DIA;
						
						if(WSP==1){                                    //根据值的大小，点的颜色不同
							SysColors[i]=mColors[0];
						}else if(WSP==2)
						{
							SysColors[i]=mColors[1];
						}else if(WSP==3)
						{
							SysColors[i]=mColors[2];
						}else if(WSP==4){
							SysColors[i]=mColors[3];
						}else if(WSP==5)
						{
							SysColors[i]=mColors[4];
						}
						
						if(WDP==1){
							DiaColors[i]=mColors[0];
						}else if(WDP==2)
						{
							DiaColors[i]=mColors[1];
						}else if(WDP==3)
						{
							DiaColors[i]=mColors[2];
						}else if(WDP==4){
							DiaColors[i]=mColors[3];
						}else if(WDP==5)
						{
							DiaColors[i]=mColors[4];
						}
						SysLineColors[i]=mColors[5];
						DiaLineColors[i]=mColors[5];
						
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
		  } 														//else flag==0   7天
		}                     //判断resultString非空
		}
	   	}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   	}
	   
	    mChart.invalidate();
	    initWheelList();
   }
    
	private void initWheelList(){
		   
	      
	      listWheelView.setVisibleItems(7);  
	      listWheelView.setViewAdapter(new CountryAdapter(this,list));
	       
	      listWheelView.setCurrentItem(1);
	}
   

    private int[] mColors = new int[] {
    		ColorTemplate.VORDIPLOM_COLORS[3], //蓝
            ColorTemplate.VORDIPLOM_COLORS[0], //绿
            ColorTemplate.VORDIPLOM_COLORS[1], //黄
            ColorTemplate.VORDIPLOM_COLORS[2], //橙
            ColorTemplate.VORDIPLOM_COLORS[4], //红
            ColorTemplate.VORDIPLOM_COLORS[5]  //线
    };


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
        
        listWheelView.setCurrentItem(e.getXIndex());
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }
    
//    横竖屏切换
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // land do nothing is ok
            	setContentView(R.layout.activity_linechart1_1);
            	mChart=(LineChart) findViewById(R.id.chart1_1);
            	listWheelView = (WheelView) findViewById(R.id.chart1_list1);
            	initChart();
            	initDataSet(0);
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // port do nothing is ok
            	setContentView(R.layout.activity_linechart1_2);
            	mChart=(LineChart) findViewById(R.id.chart1_2);
            	listWheelView = (WheelView) findViewById(R.id.chart1_list2);
            	initChart();
            	initDataSet(0);
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

//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//    }

