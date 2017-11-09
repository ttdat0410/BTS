package vn.vnpt.ansv.bts.objects;

import java.util.ArrayList;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinStationFullListObj {
	private int total;
	private ArrayList<MinStationFullObj> list;
	
	public MinStationFullListObj() {
		super();
		this.total = 0;
		this.list = null;
	}
	public MinStationFullListObj(ArrayList<MinStationFullObj> list) {
		super();
		if(list == null) {
			this.total = 0;
		} else {
			this.total = list.size();
		}
		this.list = list;
	}
	public ArrayList<MinStationFullObj> getList() {
		return list;
	}
	public void setList(ArrayList<MinStationFullObj> list) {
		this.list = list;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
