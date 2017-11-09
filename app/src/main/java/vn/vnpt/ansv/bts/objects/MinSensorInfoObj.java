package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinSensorInfoObj {
	private int sensorId;
	private String sensorName;
	private String sensorSerial;
	private int sensorTypeId;
	private String measurementUnit;
	
	public MinSensorInfoObj() {
		super();
		this.sensorId = -1;
		this.sensorName = null;
		this.sensorSerial = null;
		this.sensorTypeId = -1;
		this.measurementUnit = null;
		
	}
	public MinSensorInfoObj(int sensorId, String sensorName, String sensorSerial, int sensorTypeId, String measurementUnit) {
		super();
		this.sensorId = sensorId;
		this.sensorName = sensorName;
		this.sensorSerial = sensorSerial;
		this.sensorTypeId = sensorTypeId;
		this.measurementUnit = measurementUnit;
	}
	public int getSensorId() {
		return sensorId;
	}
	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}
	public String getSensorName() {
		return sensorName;
	}
	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	public String getSensorSerial() {
		return sensorSerial;
	}
	public void setSensorSerial(String sensorSerial) {
		this.sensorSerial = sensorSerial;
	}
	public int getSensorTypeId() {
		return sensorTypeId;
	}
	public void setSensorTypeId(int sensorTypeId) {
		this.sensorTypeId = sensorTypeId;
	}
	public String getMeasurementUnit() {
		return measurementUnit;
	}
	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}
}
