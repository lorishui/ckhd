/*
 * www.szckhd.com
 */
package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * @author qibiao
 */
public class WebAccess extends DataEntity<WebAccess> {

	private static final long serialVersionUID = 1L;

	private String ckappid;
	
	private String channelid;
	
	// 来源
	private String source;
	
	private String item;

	private String ip;

	private String country;
	
	private String province;
	
	private String city;
	
	private String ipfilter;
	
	private String startDate;
	
	private String endDate;
	
	public String getCkappid() {
		return ckappid;
	}

	public void setCkappid(String ckappid) {
		this.ckappid = ckappid;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIpfilter() {
		return ipfilter;
	}

	public void setIpfilter(String ipfilter) {
		this.ipfilter = ipfilter;
	}

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

}
