package me.ckhd.opengame.app.entity;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AppleUser extends DataEntity<AppleUser>{

	private static final long serialVersionUID = 1L;
	
	private Integer ckAppId;
	private String version;
	private String deviceToken;
	private int badge;
	
	public AppleUser(){}
	
	public AppleUser(JSONObject json){
		if(json !=null){
			if(json.containsKey("ckAppId")){
				setCkAppId(json.getInteger("ckAppId"));
			}
			if(json.containsKey("version")){
				setVersion(json.getString("version"));
			}
			if(json.containsKey("deviceToken")){
				setDeviceToken(json.getString("deviceToken").replace(" ", "").replace("<", "").replace(">", ""));
			}
		}
	}
	
	public Integer getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(Integer ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public int getBadge() {
		return badge;
	}

	public void setBadge(int badge) {
		this.badge = badge;
	}
	
}
