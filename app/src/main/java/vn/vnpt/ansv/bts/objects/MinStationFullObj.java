package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinStationFullObj {
	private MinStationInfoObj stationInfo;
	private MinStationDataObj stationData;
	
	public MinStationFullObj() {
		super();
		this.stationInfo = null;
		this.stationData = null;
	}
	public MinStationFullObj(MinStationInfoObj stationInfo, MinStationDataObj stationData) {
		super();
		this.stationInfo = stationInfo;
		this.stationData = stationData;
	}
	public MinStationInfoObj getStationInfo() {
		return stationInfo;
	}
	public void setStationInfo(MinStationInfoObj stationInfo) {
		this.stationInfo = stationInfo;
	}
	public MinStationDataObj getStationData() {
		return stationData;
	}
	public void setStationData(MinStationDataObj stationData) {
		this.stationData = stationData;
	}
}
