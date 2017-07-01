package me.ckhd.opengame.ad.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdUser extends DataEntity<AdUser>{
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String type;
	private String name;
	private String secretKey;
	private String userAccount;
	private String pwd;
	private int isUse;
	private int iosUse;
	private int androidUse;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	public int getIosUse() {
		return iosUse;
	}
	public void setIosUse(int iosUse) {
		this.iosUse = iosUse;
	}
	public int getAndroidUse() {
		return androidUse;
	}
	public void setAndroidUse(int androidUse) {
		this.androidUse = androidUse;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
