package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class PurchaseSwitch extends  DataEntity<AppCarriers>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5162701478871875040L;

	private String dates;
	
	private String times;

	// 是否启用：0-启用，1-停用
	private int usable;
	
	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public int getUsable() {
		return usable;
	}

	public void setUsable(int usable) {
		this.usable = usable;
	}
	
	/**
	 * 是否启用
	 * @return
	 */
	public boolean isUsable() {
		return usable == 0 ? true : false;
	}
	
}
