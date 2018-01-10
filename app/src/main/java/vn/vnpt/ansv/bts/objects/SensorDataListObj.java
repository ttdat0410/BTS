package vn.vnpt.ansv.bts.objects;

import java.util.ArrayList;

/**
 * Created by ANSV on 11/9/2017.
 */

public class SensorDataListObj {
	private int total;
	private ArrayList<SensorDataObj> list;
	
	public SensorDataListObj() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SensorDataListObj(ArrayList<SensorDataObj> list) {
		super();
		if(list == null) {
			this.total = 0;
		} else {
			this.total = list.size();
		}
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public ArrayList<SensorDataObj> getList() {
		return list;
	}
	public void setList(ArrayList<SensorDataObj> list) {
		this.list = list;
	}
}
