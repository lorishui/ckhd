/*
 * www.ckhd.me
 */
package me.ckhd.opengame.woapi.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.ckhd.opengame.woapi.entity.WoAppOrder;

/**
 * 响应沃商店订单校验bean
 */
@XmlRootElement(name = "paymessages")
public class WoValidateOrderResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer checkOrderIdRsp;// 0-验证成功1-验证失败，必填

	private String gameaccount;// 游戏账号，长度<=64，联网必填

	private String imei;// 设备标识，联网必填，单机尽量上报

	private String macaddress;// MAC地址去掉冒号，联网必填，单机尽量

	private String ipaddress;// IP地址，去掉点号，补零到每地址段3位，如：192168000001，联网必填，单机尽量

	private String serviceid;// 12位沃商店计费点（业务代码），必填

	private String channelid;// 渠道ID，必填，如00012243

	private String cpid;// 沃商店CPID，必填

	private String ordertime;// 订单时间戳，14位时间格式，联网必填，单机尽量yyyyMMddhhmmss

	private String appversion;// 应用版本号，必填，长度<=32

	/**
	 * 转成对应entity
	 * 
	 * @return
	 */
	public WoAppOrder genEntity() {
		WoAppOrder woAppOrder = new WoAppOrder();

		return woAppOrder;
	}

	public Integer getCheckOrderIdRsp() {
		return checkOrderIdRsp;
	}

	@XmlElement(name = "checkOrderIdRsp")
	public void setCheckOrderIdRsp(Integer checkOrderIdRsp) {
		this.checkOrderIdRsp = checkOrderIdRsp;
	}

	public String getGameaccount() {
		return gameaccount;
	}

	@XmlElement(name = "gameaccount")
	public void setGameaccount(String gameaccount) {
		this.gameaccount = gameaccount;
	}

	public String getImei() {
		return imei;
	}

	@XmlElement(name = "imei")
	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMacaddress() {
		return macaddress;
	}

	@XmlElement(name = "macaddress")
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	@XmlElement(name = "ipaddress")
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getServiceid() {
		return serviceid;
	}

	@XmlElement(name = "serviceid")
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}

	public String getChannelid() {
		return channelid;
	}

	@XmlElement(name = "channelid")
	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String getCpid() {
		return cpid;
	}

	@XmlElement(name = "cpid")
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getOrdertime() {
		return ordertime;
	}

	@XmlElement(name = "ordertime")
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getAppversion() {
		return appversion;
	}

	@XmlElement(name = "appversion")
	public void setAppversion(String appversion) {
		this.appversion = appversion;
	}
}
