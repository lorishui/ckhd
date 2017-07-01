package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;
/**
 * 移动支付通道  Entity
 * @author wesley
 * @version 2015-06-29
 */
public class PaymentWay extends  DataEntity<PaymentWay>{

 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 创酷appid
	 * 
	 */
	private String ckappId;
	
	/**
	 * 运营商类型
	 */
	private String carriers;
	/**
	 * APP_ID
	 */
	private String appIds;
	/**
	 * 游戏版本
	 */
	private String version;
	
	/**
	 * 渠道号
	 */
	private String channelId;
	
	
	/**
	 * 省份代码
	 */
	private String provinceCode;
	
	/**
	 * 支付方式标志
	 */
	private String payWaySign;
	
	
	/****************附加********************/
	
	private  String appName;
	private  String channelName;
	private  String provinceName;
	
	private  String mmAppId;
	
	private  String andgameAppId;
	
	
	private  String simNO;
	
	public String getAppIds() {
		return appIds;
	}

	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getPayWaySign() {
		return payWaySign;
	}

	public void setPayWaySign(String payWaySign) {
		this.payWaySign = payWaySign;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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

	public String getAndgameAppId() {
		return andgameAppId;
	}

	public void setAndgameAppId(String andgameAppId) {
		this.andgameAppId = andgameAppId;
	}

	public String getCkappId() {
		return ckappId;
	}

	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}

	public String getSimNO() {
		return simNO;
	}

	public void setSimNO(String simNO) {
		this.simNO = simNO;
	}
	
	
	
}
