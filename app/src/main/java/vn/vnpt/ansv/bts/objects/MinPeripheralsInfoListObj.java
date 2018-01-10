package vn.vnpt.ansv.bts.objects;

import java.util.ArrayList;

/**
 * Created by ANSV on 11/9/2017.
 */

public class MinPeripheralsInfoListObj {
	private int total;
	private ArrayList<MinPeripheralsInfoObj> list;
	
	public MinPeripheralsInfoListObj() {
		super();
		this.total = 0;
		this.list = null;
	}
	public MinPeripheralsInfoListObj(ArrayList<MinPeripheralsInfoObj> list) {
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
	public ArrayList<MinPeripheralsInfoObj> getList() {
		return list;
	}
	public void setList(ArrayList<MinPeripheralsInfoObj> list) {
		this.list = list;
	}
}
