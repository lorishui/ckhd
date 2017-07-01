package me.ckhd.opengame.ad.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdApplicationType extends DataEntity<AdApplicationType>{
	private static final long serialVersionUID = 1L;
	
	private String appid;
	private String resourceType;
	private String adType;
	private String signId;
	private String Package;
	private int isUse;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getAdType() {
		return adType;
	}
	public void setAdType(String adType) {
		this.adType = adType;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public int getIsUse() {
		return isUse;
	}
	public void setIsUse(int isUse) {
		this.isUse = isUse;
	}
	
}
