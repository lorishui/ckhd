package me.ckhd.opengame.tianyiapi.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class TianyiOrder extends DataEntity<TianyiOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private int chargeResult;
	private String orderId;
	private int payType;		
	private String payTime;		
	private String IMSI;
	private String channel;
	private String price;
	private String version;
	private String sig;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getChargeResult() {
		return chargeResult;
	}
	public void setChargeResult(int chargeResult) {
		this.chargeResult = chargeResult;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getIMSI() {
		return IMSI;
	}
	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	@Override
	public String toString() {
		return "AppThreeNet [id=" + id + ", chargeResult=" + chargeResult
				+ ", orderId=" + orderId + ", payType=" + payType
				+ ", payTime=" + payTime + ", IMSI=" + IMSI + ", channel="
				+ channel + ", price=" + price + ", version=" + version
				+ ", sig=" + sig + "]";
	}

}
