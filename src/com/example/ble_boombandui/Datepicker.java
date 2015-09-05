package com.example.ble_boombandui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;     
import java.util.Date;

import com.xxmassdeveloper.mpchartexample.MultiLineChartActivityMonth;



import android.app.Activity;     
import android.app.DatePickerDialog;     
import android.app.Dialog;     
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;     
import android.text.format.DateUtils;
import android.view.View;     
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;     
import android.widget.DatePicker;     
import android.widget.TextView;     
    
public class Datepicker extends Activity {     
         
    private TextView mDateDisplay;     
    private Button mPickDate;     
    private Button btn_PickDatecheck;
    
    private int mYear;     
    private int mMonth;     
    private int mDay;     
    
    static final int DATE_DIALOG_ID = 0;     
    
    @Override    
    protected void onCreate(Bundle savedInstanceState) {     
        super.onCreate(savedInstanceState);     
        setContentView(R.layout.history_select);     
    
      
        mDateDisplay = (TextView) findViewById(R.id.tx_dateDisplay);     
        mPickDate = (Button) findViewById(R.id.btn_pickDate);     
        btn_PickDatecheck=(Button)findViewById(R.id.btn_pickDatecheck);
          
        mPickDate.setOnClickListener(new View.OnClickListener() {     
            public void onClick(View v) {     
//                showDialog(DATE_DIALOG_ID);     
            	 showMonPicker();
            }     
        });     
        
        btn_PickDatecheck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Datepicker.this,MultiLineChartActivityMonth.class);
				intent.putExtra("year", mYear+"");
				intent.putExtra("month", (mMonth+1)+"");
				startActivity(intent);
			}
		});
    
        //获得当前时间  
        final Calendar c = Calendar.getInstance();     
        mYear = c.get(Calendar.YEAR);     
        mMonth = c.get(Calendar.MONTH);     
        mDay = c.get(Calendar.DAY_OF_MONTH);     
    
        //显示当前时间  
        updateDisplay();     
    }     
         
    @Override    
    protected Dialog onCreateDialog(int id) {     
        switch (id) {     
        case DATE_DIALOG_ID:     
//            return new DatePickerDialog(this,     
//                        mDateSetListener,     
//                        mYear, mMonth, mDay);     
        	 showMonPicker();
        }     
        return null;     
    }     
    
    public void showMonPicker() {  
        final Calendar localCalendar = Calendar.getInstance();  
        localCalendar.setTime(strToDate("yyyy-MM", mDateDisplay.getText().toString()));  
        
        new MonPickerDialog(this,new DatePickerDialog.OnDateSetListener() {  
                    @Override  
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {  
                        localCalendar.set(1, year);  
                        localCalendar.set(2, monthOfYear);  
                        mDateDisplay.setText(clanderTodatetime(localCalendar, "yyyy-MM"));  
                        mYear=year;
                        mMonth=monthOfYear;
                    }  
                }, localCalendar.get(1), localCalendar.get(2),localCalendar.get(5)).show();  
    } 
      
    public static Date strToDate(String style, String date) {  
        SimpleDateFormat formatter = new SimpleDateFormat(style);  
        try {  
            return formatter.parse(date);  
        } catch (ParseException e) {  
            e.printStackTrace();  
            return new Date();  
        }  
    }  
      
   public static String clanderTodatetime(Calendar calendar,String dateString){
	   String date = "";
	   DateFormat dateFormat = new SimpleDateFormat(dateString);
	   Date date2=calendar.getTime();
	   date=dateFormat.format(date2);
	   return date;
   }
    public static String dateToStr(String style, Date date) {  
        SimpleDateFormat formatter = new SimpleDateFormat(style);  
        return formatter.format(date);  
    }  
    
    // updates the date we display in the TextView     
    private void updateDisplay() {     
        mDateDisplay.setText(     
            new StringBuilder()     
                    // Month is 0 based so add 1    
            		.append(mYear).append("-") 
                    .append(mMonth + 1) );    
                       
    }     
    
   
    private DatePickerDialog.OnDateSetListener mDateSetListener =     
        new DatePickerDialog.OnDateSetListener() {     
    
            public void onDateSet(DatePicker view, int year,      
                                  int monthOfYear, int dayOfMonth) {     
                mYear = year;     
                mMonth = monthOfYear;     
//                mDay = dayOfMonth;     
                updateDisplay();     
            }     
    };     
         
}    

class MonPickerDialog extends DatePickerDialog {  
    public MonPickerDialog(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {  
        super(context, callBack, year, monthOfYear, dayOfMonth);  
        this.setTitle(year + "年" + (monthOfYear + 1) + "月");  
          
        ((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE); 
        this.getDatePicker().setCalendarViewShown(false);  
    }  
  
    @Override  
    public void onDateChanged(DatePicker view, int year, int month, int day) {  
        super.onDateChanged(view, year, month, day);  
        this.setTitle(year + "年" + (month + 1) + "月");  
    }  
  
}  