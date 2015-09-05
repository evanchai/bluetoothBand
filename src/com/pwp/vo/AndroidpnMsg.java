package com.pwp.vo;

import java.util.Date;

public class AndroidpnMsg {
	
	private int msgID;
	private int isread;
	private String date;
	private String msg;
	private String title;
	
	public AndroidpnMsg(){}
	
	public AndroidpnMsg(int msgID, String date, String title, String msg,int isread) {
		super();
		this.isread = isread;
		this.date = date;
		this.msg = msg;
		this.title = title;
		this.msgID=msgID;
	}
	public int getIsread() {
		return isread;
	}
	public void setIsread(int isread) {
		this.isread = isread;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public int getMsgID() {
		return msgID;
	}

	public void setMsgID(int msgID) {
		this.msgID = msgID;
	}
	
	
}
