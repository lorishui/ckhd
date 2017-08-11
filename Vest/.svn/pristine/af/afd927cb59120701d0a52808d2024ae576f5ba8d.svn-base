package me.ckhd.opengame.buyflow.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;

public class BuyFlowActivity extends DataEntity<BuyFlowActivity> implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	private String name;	//活动名称
	private String mediaId;	//媒体平台
	private String ckappId;
	private String childckappId;
	private String adItem;		//推广点  1，2，3，4
	private String adUrl;			//推广链接
	
	private String deviceType;		//推送设备类型      android/ios
	
	private String startDate;
	private String endDate;
	

	private String permissionCkAppId;//用于过滤游戏权限
	private String permissionCkAppChildId;//用于过滤游戏权限
	private List<String> permissionMediaId;//用于过滤媒体平台权限
	
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getCkappId() {
		return ckappId;
	}
	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	public String getChildckappId() {
		return childckappId;
	}
	public void setChildckappId(String childckappId) {
		this.childckappId = childckappId;
	}
	public String getAdItem() {
		return adItem;
	}
	public void setAdItem(String adItem) {
		this.adItem = adItem;
	}
	public String getAdUrl() {
		return adUrl;
	}
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getPermissionCkAppId() {
		return permissionCkAppId;
	}
	public void setPermissionCkAppId(String permissionCkAppId) {
		this.permissionCkAppId = permissionCkAppId;
	}
	public String getPermissionCkAppChildId() {
		return permissionCkAppChildId;
	}
	public void setPermissionCkAppChildId(String permissionCkAppChildId) {
		this.permissionCkAppChildId = permissionCkAppChildId;
	}
	public List<String> getPermissionMediaId() {
		return permissionMediaId;
	}
	public void setPermissionMediaId(List<String> permissionMediaId) {
		this.permissionMediaId = permissionMediaId;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
