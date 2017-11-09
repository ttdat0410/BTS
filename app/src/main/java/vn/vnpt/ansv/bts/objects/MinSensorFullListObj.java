package vn.vnpt.ansv.bts.objects;

import java.util.ArrayList;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinSensorFullListObj {
	private int total;
	private ArrayList<MinSensorFullObj> list;
	
	public MinSensorFullListObj() {
		super();
		this.total = 0;
		this.list = null;
	}
	public MinSensorFullListObj(ArrayList<MinSensorFullObj> list) {
		super();
		if(list == null) {
			this.total = 0;
		} else {
			this.total = list.size();
		}
		this.list = list;
	}
	public ArrayList<MinSensorFullObj> getList() {
		return list;
	}
	public void setList(ArrayList<MinSensorFullObj> list) {
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
