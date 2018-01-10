package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinStationDataObj {
	private MinSensorFullListObj sensorList;
	private MinPeripheralsInfoListObj peripheralsList;
	
	public MinStationDataObj() {
		super();
		this.sensorList = null;
		this.peripheralsList = null;
	}
	public MinStationDataObj(MinSensorFullListObj sensorList, MinPeripheralsInfoListObj peripheralsList) {
		super();
		this.sensorList = sensorList;
		this.peripheralsList = peripheralsList;
	}
	public MinSensorFullListObj getSensorList() {
		return sensorList;
	}
	public void setSensorList(MinSensorFullListObj sensorList) {
		this.sensorList = sensorList;
	}
	public MinPeripheralsInfoListObj getPeripheralsList() {
		return peripheralsList;
	}
	public void setPeripheralsList(MinPeripheralsInfoListObj peripheralsList) {
		this.peripheralsList = peripheralsList;
	}
}
