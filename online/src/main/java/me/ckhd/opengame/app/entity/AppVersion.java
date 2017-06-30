package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AppVersion extends DataEntity<AppVersion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private String id;
	/**
	 * 创酷App id
	 */
	private String ckAppId;
	/**
	 * 渠道号
	 */
	private String ckChannelId;
	/**
	 * mm版本号
	 */
	private String cmccMmVersion;
	/**
	 * 和游戏版本号
	 */
	private String cmccAndgameVersion;
	/**
	 * 电信爱游戏版本号
	 */
	private String ctccEgameVersion;
	/**
	 * 电信翼支付版本号
	 */
	private String ctccCteVersion;
	/**
	 * 联通沃支付版本号
	 */
	private String cuccWoVersion;
	/**
	 * 联通宽带版本号
	 */
	private String cuccKdVersion;
	/**
	 * MMApp id
	 */
	private String mmAppId;
	/**
	 * 和游戏App id
	 */
	private String andgameAppId;
	/**
	 * url
	 */
	private String url;
	/**
	 * 
	 * 渠道名称
	 */
	private String name;
	/**
	 * 
	 * 备注
	 */
	private String remarks;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getCkChannelId() {
		return ckChannelId;
	}
	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}
	public String getCmccAndgameVersion() {
		return cmccAndgameVersion;
	}
	public String getCmccMmVersion() {
		return cmccMmVersion;
	}
	public void setCmccMmVersion(String cmccMmVersion) {
		this.cmccMmVersion = cmccMmVersion;
	}
	public void setCmccAndgameVersion(String cmccAndgameVersion) {
		this.cmccAndgameVersion = cmccAndgameVersion;
	}
	public String getCtccEgameVersion() {
		return ctccEgameVersion;
	}
	public void setCtccEgameVersion(String ctccEgameVersion) {
		this.ctccEgameVersion = ctccEgameVersion;
	}
	public String getCtccCteVersion() {
		return ctccCteVersion;
	}
	public void setCtccCteVersion(String ctccCteVersion) {
		this.ctccCteVersion = ctccCteVersion;
	}
	public String getCuccWoVersion() {
		return cuccWoVersion;
	}
	public void setCuccWoVersion(String cuccWoVersion) {
		this.cuccWoVersion = cuccWoVersion;
	}
	public String getCuccKdVersion() {
		return cuccKdVersion;
	}
	public void setCuccKdVersion(String cuccKdVersion) {
		this.cuccKdVersion = cuccKdVersion;
	}
	public String getMmAppId() {
		return mmAppId;
	}
	public void setMmAppId(String mmAppId) {
		this.mmAppId = mmAppId;
	}
	public String getAndgameAppId() {
		return andgameAppId;
	}
	public void setAndgameAppId(String andgameAppId) {
		this.andgameAppId = andgameAppId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String toString() {
		return "AppVersion [id=" + id + ", ckAppId=" + ckAppId
				+ ", ckChannelId=" + ckChannelId + ", cmccMmVersion="
				+ cmccMmVersion + ", cmccAndgameVersion=" + cmccAndgameVersion
				+ ", ctccEgameVersion=" + ctccEgameVersion
				+ ", ctccCteVersion=" + ctccCteVersion + ", cuccWoVersion="
				+ cuccWoVersion + ", cuccKdVersion=" + cuccKdVersion
				+ ", mmAppId=" + mmAppId + ", andgameAppId=" + andgameAppId
				+ ", url=" + url + ", name=" + name + ", remarks=" + remarks
				+ "]";
	}
	
	
}
