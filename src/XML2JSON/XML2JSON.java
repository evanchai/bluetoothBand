package XML2JSON;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;


public class XML2JSON {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
			try {
				String str="<chart bgColor='FFFFFF' bgAlpha='10' numberSuffix='条' yAxisName='' xAxisName='' labelDisplay='WRAP' unescapeLinks='0' palette='3' caption='近一月科研信息动态' useRoundEdges='1' formatNumberScale='0' rotateValues='0' showPercentValues='1' showLabels='1' showValues='0' placeValuesInside='1' decimals='0' showBorder='0' baseFont='STKaiti'  baseFontSize='12'><styles><definition><style name='myCaptionFont' type='font' font='Arial' size='21' color='FF99FF' bold='1' underline='0' italic='0'/><style name='myAxisTitlesFont' type='font' font='Arial' size='12' color='001100' bold='1' underline='0' italic='0'/></definition><application><apply toObject='Caption' styles='myCaptionFont' /><apply toObject='XAxisName' styles='myAxisTitlesFont' /><apply toObject='YAxisName' styles='myAxisTitlesFont' /></application></styles><set color='f6930e' label='科研成果' value='0'/><set color='c09d11' label='科研到款' value='0'/><set color='e6ef1f' label='科研队伍' value='0'/><set color='78e60f' label='科研办公' value='1'/></chart>";
				str="<chart Title='主要数据库分布情况' TitlePaint='0066CC' Subtitle='(XCL-Charts Demo)'SubtitlePaint='0066CC'LeftAxisTitle='数据库个数' LowerAxisTitle='分布位置' ><DataAxis AxisMax='100' AxisMin='0' AxisSteps='5'AxisPaint='0066CC' TickMarksPaint='0066CC' DetailModeSteps='5'/><chartlabels><set label='福田数据中心'><set label='西丽数据中心'></chartLabels><chartData><BarData name='Oracle' color='f6930e'><dataSeries  value='66'/><dataSeries  value='33'/></BarData><BarData name='SQL Server' color='f6930e'><dataSeries  value='33'/><dataSeries  value='22'/></BarData></chartData></chart>";
				JSONObject jsonobj=XML.toJSONObject(str);
				System.out.println(jsonobj);
				
//				JSONObject chart=jsonobj.getJSONObject("chart");
//				JSONObject styles=chart.getJSONObject("styles");
//				JSONObject definition=styles.getJSONObject("definition");
//				JSONArray style=definition.getJSONArray("style");
//				JSONObject Info=style.getJSONObject(0);
//				String name=Info.getString("name");
//				System.out.println(name);
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
	}
	private static void Json2Chart(String jsonString){
		
	}
	

}
