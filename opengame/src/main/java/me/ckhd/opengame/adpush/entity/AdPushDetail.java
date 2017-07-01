package me.ckhd.opengame.adpush.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AdPushDetail extends DataEntity<AdPushDetail>{
	
	//AdPush的id
	private AdPush adPush;
	//负责人
	private String userId;
	private String userName;
	//广告位
	private String adPlace;
	//推广链接
	private String adUrl;
	//推广任务(描述)
	private String description;
	public AdPushDetail() {
		super();
	}
	public AdPushDetail(String id) {
		super(id);
	}
	public AdPushDetail(AdPush adPush, String userId, String userName,
			String adPlace, String adUrl, String description) {
		super();
		this.adPush = adPush;
		this.userId = userId;
		this.userName = userName;
		this.adPlace = adPlace;
		this.adUrl = adUrl;
		this.description = description;
	}
	public AdPush getAdPush() {
		return adPush;
	}
	public void setAdPush(AdPush adPush) {
		this.adPush = adPush;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAdPlace() {
		return adPlace;
	}
	public void setAdPlace(String adPlace) {
		this.adPlace = adPlace;
	}
	public String getAdUrl() {
		return adUrl;
	}
	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
