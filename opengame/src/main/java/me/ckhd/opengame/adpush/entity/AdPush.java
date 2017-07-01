package me.ckhd.opengame.adpush.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdPush extends DataEntity<AdPush> {
	//游戏id
	private String appId;
	//游戏名称
	private String appName;
	//平台(ios/android)
	private String platform;
	//媒体
	private String mediaName;
	//账户
	private String account;
	public AdPush() {
		super();
	}
	public AdPush(String id) {
		super(id);
	}
	public AdPush(String appId, String appName, String platform,
			String mediaName, String account) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.platform = platform;
		this.mediaName = mediaName;
		this.account = account;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
	
}
