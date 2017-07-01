/*
 * www.ckhd.me
 */
package me.ckhd.opengame.woapi.bean;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.woapi.entity.WoAppOrder;

/**
 * 沃商店支付服务器通知支付结果信息bean
 * <p>
 * 对于需要校验的字段，可以在对应属性名称的set方法中进行
 * </p>
 */
@XmlRootElement(name = "callbackReq")
public class WoCallBackReq implements Serializable {

	/**
	 * @author leo
	 * @Time 2016-3-14
	 */
	private static final long serialVersionUID = 1L;

	private String orderid; // 订单号 String

	private String ordertime; // 订单交易时间 String

	private String cpid; // 开发者/开发商ID String

	private String appid; // 应用ID String

	private String fid; // 渠道ID String

	private String consumeCode; // 计费点ID String

	private Integer payfee; //支付金额，分	Integer

	private Integer payType;//支付方式	Integer	0-沃币支付，1-支付宝，2-VAC支付，3-神州付5-银行卡支付6-微信支付

	private Integer hRet;//支付结果	Integer	支付结果，0代表成功，其他代表失败

	private String status; //状态码	String    00000表示成功

	private String signMsg; //MD5加密串	String

	private String callBackContent; //返回的数据
	
	public WoCallBackReq(String requestBody){
		this.callBackContent=requestBody;
		Map<String,Object> map= XmlUtils.Str2Map(requestBody);
		this.orderid=map.containsKey("orderid")?map.get("orderid").toString():null;
		this.ordertime=map.containsKey("ordertime")?map.get("ordertime").toString():null;
		this.cpid=map.containsKey("cpid")?map.get("cpid").toString():null;
		this.appid=map.containsKey("appid")?map.get("appid").toString():null;
		this.fid=map.containsKey("fid")?map.get("fid").toString():null;
		this.consumeCode=map.containsKey("consumeCode")?map.get("consumeCode").toString():null;
		this.payfee=map.containsKey("payfee")?Integer.parseInt(map.get("payfee").toString()):0;
		this.payType=map.containsKey("payType")?Integer.parseInt(map.get("payType").toString()):0;
		this.hRet=map.containsKey("hRet")?Integer.parseInt(map.get("hRet").toString()):0;
		this.status=map.containsKey("status")?map.get("status").toString():null;
		this.signMsg=map.containsKey("signMsg")?map.get("signMsg").toString():null;
	}
	
	/**
	 * 转成对应entity
	 * @return
	 */
	public WoAppOrder genEntity() {
		WoAppOrder woAppOrder = new WoAppOrder();
		woAppOrder.setOrderid(orderid);
		woAppOrder.setOrdertime(ordertime);
		woAppOrder.setAppid(appid);
		woAppOrder.setConsumeCode(consumeCode);
		woAppOrder.setCpid(cpid);
		woAppOrder.setAppid(appid);
		woAppOrder.setFid(fid);
		woAppOrder.setPayfee(payfee);
		woAppOrder.setPayType(payType);
		woAppOrder.sethRet(hRet);
		woAppOrder.setStatus(status);
		woAppOrder.setSignMsg(signMsg);
		woAppOrder.setCallBackContent(callBackContent);
		return woAppOrder;
	}
	
	public String getOrderid() {
		return orderid;
	}

	@XmlElement(name = "orderid")
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getSignMsg() {
		return signMsg;
	}

	@XmlElement(name = "signMsg")
	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}

	public String getOrdertime() {
		return ordertime;
	}
	
	@XmlElement(name = "ordertime")
	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getCpid() {
		return cpid;
	}

	@XmlElement(name = "cpid")
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getAppid() {
		return appid;
	}

	@XmlElement(name = "appid")
	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getFid() {
		return fid;
	}

	@XmlElement(name = "fid")
	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getConsumeCode() {
		return consumeCode;
	}

	@XmlElement(name = "consumeCode")
	public void setConsumeCode(String consumeCode) {
		this.consumeCode = consumeCode;
	}

	public Integer getPayfee() {
		return payfee;
	}

	@XmlElement(name = "payfee")
	public void setPayfee(Integer payfee) {
		this.payfee = payfee;
	}

	public Integer getPayType() {
		return payType;
	}

	@XmlElement(name = "payType")
	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer gethRet() {
		return hRet;
	}

	@XmlElement(name = "hRet")
	public void sethRet(Integer hRet) {
		this.hRet = hRet;
	}

	public String getStatus() {
		return status;
	}

	@XmlElement(name = "status")
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
