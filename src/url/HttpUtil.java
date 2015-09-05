package url;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpUtil {

	public static HttpClient httpClient = new DefaultHttpClient();

	public static String getRequest(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(get);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	public static String postRequest(String url, Map<String, String> rawParams)
			throws Exception {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		// post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
		post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
		// HttpResponse httpResponse = httpClient.execute(post);

		HttpClient client = new DefaultHttpClient();
		// client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// 15000);
		HttpParams httpparams = null;
		httpparams = client.getParams();
		HttpConnectionParams
				.setConnectionTimeout((HttpParams) httpparams, 5000);
		HttpConnectionParams.setSoTimeout((HttpParams) httpparams, 15000);

		HttpResponse httpResponse = client.execute(post);
		int temp = httpResponse.getStatusLine().getStatusCode();
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());

			return result;
		} else
			return null;
	}

	public static Bitmap postRequestPicture(String url,
			Map<String, String> rawParams) throws Exception {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		// post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
		post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
		// HttpResponse httpResponse = httpClient.execute(post);

		HttpClient client = new DefaultHttpClient();
		// client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// 15000);
		HttpParams httpparams = null;
		httpparams = client.getParams();
		HttpConnectionParams
				.setConnectionTimeout((HttpParams) httpparams, 5000);
		HttpConnectionParams.setSoTimeout((HttpParams) httpparams, 15000);

		HttpResponse httpResponse = client.execute(post);
		int temp = httpResponse.getStatusLine().getStatusCode();
		if (httpResponse.getStatusLine().getStatusCode() == 200) {

			InputStream inputStream = httpResponse.getEntity().getContent();
			byte buffer[] = StreamUtil.convertStreamToByte(inputStream);
			Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0,
					buffer.length);

			return bitmap;
		} else
			return null;
	}
}
