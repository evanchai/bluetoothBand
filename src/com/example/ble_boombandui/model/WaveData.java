package com.example.ble_boombandui.model;

public class WaveData {
	private int waveId;
	private int type;
	private String data;

	public WaveData() {
	}

	public WaveData(int waveId, int type, String data) {
		super();
		this.waveId = waveId;
		this.type = type;
		this.data = data;
	}

	public int getWaveId() {
		return waveId;
	}

	public void setWaveId(int waveId) {
		this.waveId = waveId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Wava data: [ waveId: " + waveId + ", waveType: " + type
				+ ", waveData: " + data + " ]";
	}
}
