package url;

import java.util.HashMap;
import java.util.Map;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MsgProcess("FDFDFC1111110D0A");
		MsgProcess("FDFDFD0E0D0A");
		MsgProcess("FDFDFD010D0A");
		MsgProcess("FDFDFD0C0D0A");
		MsgProcess("FDFDFD0B0D0A");
		MsgProcess("FDFDFD020D0A");
		MsgProcess("FDFDFD040D0A");
	}
	
	 public static Map<String,String> MsgProcess(String receiveMsg)
	    {
		 	Map<String,String> rawParams=new HashMap<String,String>();
	    	int i=receiveMsg.indexOf("FDFD");
	    	String str=receiveMsg.substring(i+1,receiveMsg.length()-1);
	    	String type=str.substring(0,1);
	    	if(type.equals("FC"))
	    	{
	    		String SYS=str.substring(2,3);
	    		String DIA=str.substring(4,5);
	    		String PUL=str.substring(5,6);
	    		
				   rawParams.put("SYS", SYS);
				   rawParams.put("DIA",DIA);
				   rawParams.put("PUL", PUL);
	    	}else if(type.equals("FD"))
	    	{
	    		String etype=str.substring(2,3);
	    		if(etype.equals("0E"))
	    		{
	    			rawParams.put("ERR", "E-E");
	    		}else if(etype.equals("01")){
	    			rawParams.put("ERR", "E-1");
	    		}else if(etype.equals("02")){
	    			rawParams.put("ERR", "E-2");
	    		}else if(etype.equals("03")){
	    			rawParams.put("ERR", "E-3");
	    		}else if(etype.equals("04")){
	    			rawParams.put("ERR", "E-4");
	    		}else if(etype.equals("0C")){
	    			rawParams.put("ERR", "E-C");
	    		}else if(etype.equals("0B")){
	    			rawParams.put("ERR", "E-B");
	    		}
	    	}
	    	
	    	return rawParams;
	    }

}
