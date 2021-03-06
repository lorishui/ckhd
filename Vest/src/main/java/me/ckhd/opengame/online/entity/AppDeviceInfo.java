package me.ckhd.opengame.online.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AppDeviceInfo extends  DataEntity<AppDeviceInfo>{

	private static final long serialVersionUID = -855006282826450510L;
	private String ckAppId;
	private String childCkAppId;
	private String ckChannelId;
	private String childChannelId;
	private String deviceId;
	private String MD5DeviceId;
	private String idfv;
	private String roleId;
	private String serverId;
	private String zoneId;
	private String source;
	private int isAct;
	private String createTime;
	private Date createDate;
	private String actTime;
	private Date actDate;
	private String clientIp;
	private String media;
	private String adItem;
	
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getChildCkAppId() {
		return childCkAppId;
	}
	public void setChildCkAppId(String childCkAppId) {
		this.childCkAppId = childCkAppId;
	}
	public String getCkChannelId() {
		return ckChannelId;
	}
	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}
	public String getChildChannelId() {
		return childChannelId;
	}
	public void setChildChannelId(String childChannelId) {
		this.childChannelId = childChannelId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getMD5DeviceId() {
		return MD5DeviceId;
	}
	public void setMD5DeviceId(String mD5DeviceId) {
		MD5DeviceId = mD5DeviceId;
	}
	public String getIdfv() {
		return idfv;
	}
	public void setIdfv(String idfv) {
		this.idfv = idfv;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getIsAct() {
		return isAct;
	}
	public void setIsAct(int isAct) {
		this.isAct = isAct;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getActTime() {
		return actTime;
	}
	public void setActTime(String actTime) {
		this.actTime = actTime;
	}
	public Date getActDate() {
		return actDate;
	}
	public void setActDate(Date actDate) {
		this.actDate = actDate;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getAdItem() {
		return adItem;
	}
	public void setAdItem(String adItem) {
		this.adItem = adItem;
	}
	
}
