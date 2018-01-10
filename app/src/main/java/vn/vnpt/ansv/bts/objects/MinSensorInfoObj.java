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
	private int warningModeId;
	private int warningValue1;
	private int warningValue2;
	private int warningComp;
	private String tempSensorName;
	
	public MinSensorInfoObj() {
		super();
		this.sensorId = -1;
		this.sensorName = null;
		this.sensorSerial = null;
		this.sensorTypeId = -1;
		this.measurementUnit = null;
		this.warningModeId = -1;
		this.warningValue1 = -1;
		this.warningValue2 = -1;
		this.warningComp = -1;
		this.tempSensorName = null;
		
	}
	public MinSensorInfoObj(int sensorId, String sensorName, String sensorSerial, int sensorTypeId,
							String measurementUnit, int warningModeId, int warningValue1,
							int warningValue2, int warningComp) {
		super();
		this.sensorId = sensorId;
		this.sensorName = sensorName;
		this.sensorSerial = sensorSerial;
		this.sensorTypeId = sensorTypeId;
		this.measurementUnit = measurementUnit;
		this.warningModeId = warningModeId;
		this.warningValue1 = warningValue1;
		this.warningValue2 = warningValue2;
		this.warningComp = warningComp;
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

	public int getWarningModeId() {
		return warningModeId;
	}

	public int getWarningValue1() {
		return warningValue1;
	}

	public int getWarningValue2() {
		return warningValue2;
	}

	public int getWarningComp() {
		return warningComp;
	}

	public void setWarningValue1(int warningValue1) {
		this.warningValue1 = warningValue1;
	}

	public void setWarningModeId(int warningModeId) {
		this.warningModeId = warningModeId;
	}

	public void setWarningValue2(int warningValue2) {
		this.warningValue2 = warningValue2;
	}

	public void setWarningComp(int warningComp) {
		this.warningComp = warningComp;
	}

	public String getTempSensorName() {
		return tempSensorName;
	}

	public void setTempSensorName(String tempSensorName) {
		this.tempSensorName = tempSensorName;
	}
}
