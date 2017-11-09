package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinStationInfoObj {
	private int stationId;
	private String stationName;
	private String gatewaySerial;
	
	public MinStationInfoObj() {
		super();
		this.stationId = -1;
		this.stationName = null;
		this.gatewaySerial = null;
	}
	public MinStationInfoObj(int stationId, String stationName, String gatewaySerial) {
		super();
		this.stationId = stationId;
		this.stationName = stationName;
		this.gatewaySerial = gatewaySerial;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getGatewaySerial() {
		return gatewaySerial;
	}
	public void setGatewaySerial(String gatewaySerial) {
		this.gatewaySerial = gatewaySerial;
	}
}
