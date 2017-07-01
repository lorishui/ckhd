package me.ckhd.opengame.ad.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdApplication extends DataEntity<AdApplication>{
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String appid;
	private String name;
	private int type;
	private int isUse;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}
