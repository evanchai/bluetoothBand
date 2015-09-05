package url;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil{
	public static byte[] convertStreamToByte(InputStream in)
	{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		try{
			while ((len=in.read(buffer))!=-1) {
				baos.write(buffer,0,len);
				
			}
			byte []result=baos.toByteArray();
			return result;
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
}
