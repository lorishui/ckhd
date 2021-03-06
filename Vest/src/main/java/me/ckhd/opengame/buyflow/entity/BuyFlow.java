package me.ckhd.opengame.buyflow.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * @version 2017-05-03
 */
public class BuyFlow extends DataEntity<BuyFlow> {

	private static final long serialVersionUID = 1L;

	// appid
	private String ckAppId;
	// 子appid
	private String childCkAppId;
	// 广告位
	private String adItem;
	
	// 媒体标识
	private String media;
	// 设备号
	private String deviceId;
	//MD5设备号
	private String MD5DeviceId;
	// ip
	private String ip;
	// 监控时间-媒体传或取接口调用时间
	private Date monitorTime;
	// 回调url
	private String callback;
	// 状态，0：媒体通知入库；1：激活；2：注册（此时会回调媒体url）;3：已经注册
	private int state;
	
	public static enum STATE {
		NEW(0), ACTIVATE(1), REGISTER(2), OLDREGISTER(3);
		private int value;

		private STATE(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}
	
	public String getMD5DeviceId() {
		return MD5DeviceId;
	}

	public void setMD5DeviceId(String mD5deviceId) {
		MD5DeviceId = mD5deviceId;
	}

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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getMonitorTime() {
		return monitorTime;
	}

	public void setMonitorTime(Date monitorTime) {
		this.monitorTime = monitorTime;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getAdItem() {
		return adItem;
	}

	public void setAdItem(String adItem) {
		this.adItem = adItem;
	}

}
