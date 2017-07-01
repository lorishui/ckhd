/*
 * www.ckhd.me
 */
package me.ckhd.opengame.rdapi.entity;

import java.lang.reflect.Field;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.rdapi.bean.RdRequest;
/**
 * 
 */
public class RdAppOrder extends DataEntity<RdAppOrder> {
	Logger log = LoggerFactory.getLogger(RdRequest.class);

	private static final long serialVersionUID = 1L;
	
	private String id;//id
	
	private String orderId; // 订单号 String
	
	private String channelid;//mm渠道id
	
	private String ckChannelId;//mm渠道id

	private String ckAppId; // 游戏id

	private String mmAppId; // 移动mm的appId

	private String rdAppId; // 容大的appId

	private int isOnline; // 游戏类型:0:单机,1:网游

	private String payType; // 计费方式:1:普通mm 2:一次确认 3:二次确认  

	private String ackType; //支付类型,1:一次确认 2:二次确认

	private Integer payCount;//第几次支付

	private String sid;// 合作方渠道号

	private String cid;	// 合作方子渠道号

	private String price;	// 资费（单位：分）

	private String imsi;// 国际移动用户识别码

	private String feetype;	// 支付方式：0：短信，1：支付宝，2：银联

	private String paycode;	// 计费指令

	private String resulttime;// 计费时间

	private String hRet;// 计费标识：1：计费成功，其他：计费失败

	private String uniqueid;// 支付流水号, 唯一id

	private String cpparam;// 客户自定义透传参数，不超过50字节的英文、数字

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
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

	public String getRdAppId() {
		return rdAppId;
	}

	public void setRdAppId(String rdAppId) {
		this.rdAppId = rdAppId;
	}

	public int getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getAckType() {
		return ackType;
	}

	public void setAcType(String ackType) {
		this.ackType = ackType;
	}

	public Integer getPayCount() {
		return payCount;
	}

	public void setPayCount(Integer payCount) {
		this.payCount = payCount;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getFeetype() {
		return feetype;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	public String getResulttime() {
		return resulttime;
	}

	public void setResulttime(String resulttime) {
		this.resulttime = resulttime;
	}

	public String gethRet() {
		return hRet;
	}

	public void sethRet(String hRet) {
		this.hRet = hRet;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getCpparam() {
		return cpparam;
	}

	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
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
