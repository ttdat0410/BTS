package vn.vnpt.ansv.bts.objects;

import java.sql.Timestamp;

/**
 * Created by ANSV on 11/9/2017.
 */

public class SensorDataObj {
	private Timestamp time;
	private Timestamp createdTime;
	private int statusId;
	private int battery;
	private int signalIntensity;
	private int originalValue;
	private double value;
	
	public SensorDataObj() {
		super();
		this.time = null;
		this.createdTime = null;
		this.statusId = -1;
		this.battery = -1;
		this.signalIntensity = -1;
		this.originalValue = -1;
		this.value = -1;
	}
	public SensorDataObj(Timestamp time, Timestamp createdTime, int statusId, int battery, int signalIntensity,
						 int originalValue, double value) {
		super();
		this.time = time;
		this.createdTime = createdTime;
		this.statusId = statusId;
		this.battery = battery;
		this.signalIntensity = signalIntensity;
		this.originalValue = originalValue;
		this.value = value;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public Timestamp getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getBattery() {
		return battery;
	}
	public void setBattery(int battery) {
		this.battery = battery;
	}
	public int getSignalIntensity() {
		return signalIntensity;
	}
	public void setSignalIntensity(int signalIntensity) {
		this.signalIntensity = signalIntensity;
	}
	public int getOriginalValue() {
		return originalValue;
	}
	public void setOriginalValue(int originalValue) {
		this.originalValue = originalValue;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
}
