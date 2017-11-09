package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinSensorFullObj {
	private MinSensorInfoObj sensorInfo;
	private SensorDataListObj sensorData;
	
	public MinSensorFullObj() {
		super();
		this.sensorInfo = null;
		this.sensorData = null;
	}
	public MinSensorFullObj(MinSensorInfoObj sensorInfo, SensorDataListObj sensorData) {
		super();
		this.sensorInfo = sensorInfo;
		this.sensorData = sensorData;
	}
	public MinSensorInfoObj getSensorInfo() {
		return sensorInfo;
	}
	public void setSensorInfo(MinSensorInfoObj sensorInfo) {
		this.sensorInfo = sensorInfo;
	}
	public SensorDataListObj getSensorData() {
		return sensorData;
	}
	public void setSensorData(SensorDataListObj sensorData) {
		this.sensorData = sensorData;
	}
}
