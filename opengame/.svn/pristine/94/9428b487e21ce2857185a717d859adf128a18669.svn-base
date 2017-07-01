/*
 * www.ckhd.me
 */
package me.ckhd.opengame.hsapi.entity;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.rdapi.bean.RdRequest;
/**
 * 
 */
public class HsAppOrder extends DataEntity<HsAppOrder> {
	Logger log = LoggerFactory.getLogger(RdRequest.class);

	private static final long serialVersionUID = 1L;
	
	private String id;//id
	private String channelId;
	private String ckChannelId;//mm渠道id
	private String ckAppId; // 游戏id
	private String mmAppId; // 移动mm的appId
	private String appId;
	private int fee;
	private String paystatus;
	private String orderid;
	private String tradeId;
	private int isOnline;
	
	private int ackType;
	private int payCount;
	private String imsi;
	private String imei;
	//转换
	private String HsOrderId;
	private String OrderId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCkChannelId() {
		return ckChannelId;
	}

	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}

	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public String getMmAppId() {
		return mmAppId;
	}

	public void setMmAppId(String mmAppId) {
		this.mmAppId = mmAppId;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public int getAckType() {
		return ackType;
	}

	public void setAckType(int ackType) {
		this.ackType = ackType;
	}

	public int getPayCount() {
		return payCount;
	}

	public void setPayCount(int payCount) {
		this.payCount = payCount;
	}

	public String getHsOrderId() {
		return HsOrderId;
	}

	public void setHsOrderId(String hsOrderId) {
		HsOrderId = hsOrderId;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public void setValue(Map<String,Object> map){
		Field[] field = this.getClass().getDeclaredFields();
		for(Field f : field){
			if( map.get(f.getName()) != null && map.get(f.getName()).toString().trim().length() > 0){
				try{
					f.setAccessible(true);
					if( f.getType().equals(Integer.class.getName()) || f.getType().equals(int.class) ){
						f.setInt(this, Integer.parseInt(map.get(f.getName()).toString()));
					}else if( f.getType().equals(Float.class.getName()) || f.getType().equals(float.class) ){
						f.setFloat(this, (float)map.get(f.getName()));
					}else if( f.getType().equals(Double.class.getName()) || f.getType().equals(double.class) ){
						f.setDouble(this, (double)map.get(f.getName()));
					}else if( f.getType().equals(Long.class.getName()) || f.getType().equals(long.class) ){
						f.setLong(this, (long)map.get(f.getName()));
					}else {
						f.set(this, (String)map.get(f.getName()));
					}
				}catch(Exception e){
					log.error("map change "+this.getClass().getName()+" failure!!!", e);
				}
			}
		}
	}
}
