package me.ckhd.opengame.buyflow.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdPush extends DataEntity<AdPush> {
    /**
     * @Field @serialVersionUID : 序列化id
     */
    private static final long serialVersionUID = 1964675627909332029L;
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
	

	private String permissionCkAppId;//用于过滤游戏权限
	private List<String> permissionMediaId;//用于过滤媒体平台权限
	
	
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
	public String getPermissionCkAppId() {
		return permissionCkAppId;
	}
	public void setPermissionCkAppId(String permissionCkAppId) {
		this.permissionCkAppId = permissionCkAppId;
	}
	public List<String> getPermissionMediaId() {
		return permissionMediaId;
	}
	public void setPermissionMediaId(List<String> permissionMediaId) {
		this.permissionMediaId = permissionMediaId;
	}
	
}
