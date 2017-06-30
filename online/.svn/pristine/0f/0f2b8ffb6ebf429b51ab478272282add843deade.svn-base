package me.ckhd.opengame.app.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CfgparamVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3560104705532679376L;
	private String childCkAppId;
	private String carriers;
	private String mmAppId;
	private String ckChannelId;
	private String versionName;
	private String province;
	private Map<String, Object> exInfoMap = new HashMap<String, Object>();

	public CfgparamVO(Cfgparam cfgparam) {
		this.childCkAppId = cfgparam.getChildCkAppId();
		this.carriers = cfgparam.getCarriers();
		this.mmAppId = cfgparam.getMmAppId();
		this.ckChannelId = cfgparam.getCkChannelId();
		this.versionName = cfgparam.getVersionName();
		this.province = cfgparam.getProvince();
		this.exInfoMap.putAll(cfgparam.getExInfoMap());
	}

	public String getCarriers() {
		return carriers;
	}

	public void setCarriers(String carriers) {
		this.carriers = carriers;
	}

	public String getMmAppId() {
		return mmAppId;
	}

	public void setMmAppId(String mmAppId) {
		this.mmAppId = mmAppId;
	}

	public String getCkChannelId() {
		return ckChannelId;
	}

	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Map<String, Object> getExInfoMap() {
		return exInfoMap;
	}

	public void setExInfoMap(Map<String, Object> exInfoMap) {
		this.exInfoMap = exInfoMap;
	}

	public String getChildCkAppId() {
		return childCkAppId;
	}

	public void setChildCkAppId(String childCkAppId) {
		this.childCkAppId = childCkAppId;
	}
	
}
