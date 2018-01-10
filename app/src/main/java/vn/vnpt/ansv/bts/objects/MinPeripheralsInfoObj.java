package vn.vnpt.ansv.bts.objects;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinPeripheralsInfoObj {
	private int periId;
	private String periName;
	private String periSerial;
	private int periTypeId;
	private int statusId;
	private int value;
	
	public MinPeripheralsInfoObj() {
		super();
		this.periId = -1;
		this.periName = null;
		this.periSerial = null;
		this.periTypeId = -1;
		this.statusId = -1;
		this.value = -1;
	}
	public MinPeripheralsInfoObj(int periId, String periName, String periSerial, int periTypeId, int statusId,
								 int value) {
		super();
		this.periId = periId;
		this.periName = periName;
		this.periSerial = periSerial;
		this.periTypeId = periTypeId;
		this.statusId = statusId;
		this.value = value;
	}
	public int getPeriId() {
		return periId;
	}
	public void setPeriId(int periId) {
		this.periId = periId;
	}
	public String getPeriName() {
		return periName;
	}
	public void setPeriName(String periName) {
		this.periName = periName;
	}
	public String getPeriSerial() {
		return periSerial;
	}
	public void setPeriSerial(String periSerial) {
		this.periSerial = periSerial;
	}
	public int getPeriTypeId() {
		return periTypeId;
	}
	public void setPeriTypeId(int periTypeId) {
		this.periTypeId = periTypeId;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
}
