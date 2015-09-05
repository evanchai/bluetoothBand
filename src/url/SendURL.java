package url;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SendURL {
	// private String localusername;
	// private String localpassword;
	// private final String BASEURL="192.168.15.154";
	// private final String BASEURL="10.0.2.2";
	// private final String BASEURL="www.wingsoft.fudan.edu.cn";
	private final String BASEURL = "121.40.114.82";
	private final String PORT = "8087";
	private final String PROJECT = "BTOnlineSystem";
	// private final String PROJECT="WF2";
	String response = "";

	public SendURL() {

	}

	public boolean SendtoServer(Map<String, String> rawParams) throws Exception {
		// try {

		String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
				+ "/servlet/ReceiverServlet?param=BT";
		HttpUtil myhttp = new HttpUtil();
		rawParams.put("enduserid", "JK00000011");

		response = myhttp.postRequest(url, rawParams);

		// } catch (Exception e) {
		//
		// e.printStackTrace();
		// return false;
		// }
		if (response.equals("发送成功")) {
			return true;
		} else {
			return false;
		}
	}

	// ����û��������
	public void getLocalUserInfo() {
		// localusername="525381239@qq.com";
		// localpassword="1234";

	}

	public boolean login(String username, String password) {
		try {
			getLocalUserInfo();
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/LoginServlet?param=login";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("username", username);
			rawParams.put("password", password);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response.equals("登陆成功")) {
			return true;
		} else {
			return false;
		}
		// return true;
	}

	public boolean updateAndroidid(String username, String androidid) {
		try {
			getLocalUserInfo();
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/UpdateAndroididServlet?param=androidid";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("username", username);
			rawParams.put("androidid", androidid);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response.equals("更新成功")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean register(String username, String password, String deviceId,
			String telephone, String email, String age, String sex) {
		try {
			// getLocalUserInfo();
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/RegisterServlet?param=Register";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("username", username);
			rawParams.put("password", password);
			rawParams.put("deviceId", deviceId);
			rawParams.put("telephone", telephone);
			rawParams.put("email", email);
			rawParams.put("age", age);
			rawParams.put("sex", sex);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response.equals("注册成功")) {
			return true;
		} else {
			return false;
		}
		// return true;
	}

	// ȥ��������������û���µ���Ϣ
	public String checkMsg(String username, String anId) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/CheckMsgServlet?param=checkMsg";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("username", username);
			rawParams.put("anId", anId);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response == null) {
			return null;
		} else {
			return response;
		}
	}

	// ÿ�����ʷ��¼
	public String dailyhistory(String username, String date, String date2) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/DailyhistoryServlet?param=+dailyhistory";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("date", date);
			rawParams.put("date2", date2);
			// �Ȱ��û���д��boat
			rawParams.put("username", "boat");
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response == null) {
			return null;
		} else {
			return response;
		}
	}

	// ĳ��ĳ�µ���ʷ��¼
	public String monthhistory(String username, int year, int month) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/MonthHistoryServlet?param=+monthhistory";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();
			rawParams.put("year", "" + year);
			rawParams.put("month", "" + month);
			// �Ȱ��û���д��boat
			rawParams.put("username", "boat");
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response == null) {
			return null;
		} else {
			return response;
		}
	}

	public String tophistory(String username, int flag) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/LineChartServlet?param=+tophistory";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("username", username);
			rawParams.put("flag", flag + "");
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response == null) {
			return null;
		} else {
			return response;
		}
	}

	public String history(String username, String from, String to, int flag) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/LineChartServletHistory?param=+history";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("username", "JK00000011");
			rawParams.put("from", from);
			rawParams.put("to", to);
			rawParams.put("flag", flag + "");
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response == null) {
			return null;
		} else {
			return response;
		}
	}

	// 查找陌生人
	public String searchfriend(String searchusername) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/SearchFriendServlet?param=+searchfriend";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("searchusername", searchusername);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response.equals("该账户不存在！")) {
			return null;
		} else {
			return response;
		}
	}

	// 申请好友
	public boolean applyfriend(String username, String friendusername) {
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/ApplyFriendServlet?param=+applyfriend";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("username", username);
			rawParams.put("applyusername", friendusername);
			response = myhttp.postRequest(url, rawParams);
			if (response.equals("已申请！"))
				return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return false;
	}

	// 查找申请的人
	public String searchnewfriend(String username) {
		try {
			String url = "http://"
					+ BASEURL
					+ ":"
					+ PORT
					+ "/"
					+ PROJECT
					+ "/servlet/SearchNewFriendServlet?param=+SearchNewFriendServlet";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("username", username);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response.equals("没有人申请好友！")) {
			return null;
		} else {
			return response;
		}
	}

	// 同意或拒绝好友请求
	public boolean processfriendapply(String username, String applyname,
			int flag) {
		try {
			String url = "http://"
					+ BASEURL
					+ ":"
					+ PORT
					+ "/"
					+ PROJECT
					+ "/servlet/ProcessFriendApplyServlet?param=+ProcessFriendApplyServlet";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("username", username);
			rawParams.put("applyname", applyname);
			rawParams.put("addorignore", flag + "");
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

		return true;

	}

	// 获取好友列表
	public String getFriends(String online_username) {
		// TODO Auto-generated method stub
		try {
			String url = "http://" + BASEURL + ":" + PORT + "/" + PROJECT
					+ "/servlet/GetFriendsServlet?param=+GetFriendsServlet";
			HttpUtil myhttp = new HttpUtil();
			Map<String, String> rawParams = new HashMap<String, String>();

			rawParams.put("username", online_username);
			response = myhttp.postRequest(url, rawParams);

		} catch (Exception e) {

			e.printStackTrace();
		}
		if (response.equals("没有好友！")) {
			return null;
		} else {
			return response;
		}
	}
}
