
package com.xxmassdeveloper.mpchartexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ble_boombandui.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView   {

    private TextView tvContent;
    private TextView xlabelTextView;
    private Button  btnmarkerButton;
    
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        
        
        tvContent = (TextView) findViewById(R.id.tvContent);
        xlabelTextView=(TextView)findViewById(R.id.xlabel);
     
    }
    
    
   
    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex,String xString) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("当前测量值: " + Utils.formatNumber(ce.getHigh(), 0, true));
            xlabelTextView.setText(xString);
        } else {

            tvContent.setText("当前测量值: " + Utils.formatNumber(e.getVal(), 0, true));
            xlabelTextView.setText(xString);
        }
    }

    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
