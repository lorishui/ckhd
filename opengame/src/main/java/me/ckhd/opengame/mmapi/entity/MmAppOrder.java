/*
 * www.ckhd.me
 */
package me.ckhd.opengame.mmapi.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.entity.Role;
/**
 * 
 */
public class MmAppOrder extends DataEntity<MmAppOrder> {

	private static final long serialVersionUID = 1L;

	private String transactionId;
	private String msgType;
	private String version;

	private String sendDeviceType;
	private String sendDeviceId;
	private String destDeviceType;
	private String destDeviceId;

	private String orderId;
	private String tradeId;
	private int checkId;
	private int price;
	private String actionTime;
	private int actionId;
	private String msisdn;
	private String feeMsisdn;
	private String appId;
	private String programId;
	private String paycode;
	private int totalPrice;
	private int subsNumber;
	private int subsSeq;
	private String channelId;
	private String extendData;
	private int orderType;
	private int orderPayment;
	private String MD5sign;

	private String returnStatus;
	
	private String ckappId;

	private String provinceID;

	private String operatorType;
	private String startDate;
	private String endDate;
	private int mmUser;
	private int succMmUser;
	private int andUser;
	private int succAndUser;
	private int random;
	//查询条件
	private String filterRole;
	
	private int filterRate;
	
	public int getMmUser() {
		return mmUser;
	}

	public void setMmUser(int mmUser) {
		this.mmUser = mmUser;
	}

	public int getSuccMmUser() {
		return succMmUser;
	}

	public void setSuccMmUser(int succMmUser) {
		this.succMmUser = succMmUser;
	}

	public int getAndUser() {
		return andUser;
	}

	public void setAndUser(int andUser) {
		this.andUser = andUser;
	}

	public int getSuccAndUser() {
		return succAndUser;
	}

	public void setSuccAndUser(int succAndUser) {
		this.succAndUser = succAndUser;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSendDeviceType() {
		return sendDeviceType;
	}

	public void setSendDeviceType(String sendDeviceType) {
		this.sendDeviceType = sendDeviceType;
	}

	public String getSendDeviceId() {
		return sendDeviceId;
	}

	public void setSendDeviceId(String sendDeviceId) {
		this.sendDeviceId = sendDeviceId;
	}

	public String getDestDeviceType() {
		return destDeviceType;
	}

	public void setDestDeviceType(String destDeviceType) {
		this.destDeviceType = destDeviceType;
	}

	public String getDestDeviceId() {
		return destDeviceId;
	}

	public void setDestDeviceId(String destDeviceId) {
		this.destDeviceId = destDeviceId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public int getCheckId() {
		return checkId;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getActionTime() {
		return actionTime;
	}

	public void setActionTime(String actionTime) {
		this.actionTime = actionTime;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getFeeMsisdn() {
		return feeMsisdn;
	}

	public void setFeeMsisdn(String feeMsisdn) {
		this.feeMsisdn = feeMsisdn;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getSubsNumber() {
		return subsNumber;
	}

	public void setSubsNumber(int subsNumber) {
		this.subsNumber = subsNumber;
	}

	public int getSubsSeq() {
		return subsSeq;
	}

	public void setSubsSeq(int subsSeq) {
		this.subsSeq = subsSeq;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getExtendData() {
		return extendData;
	}

	public void setExtendData(String extendData) {
		this.extendData = extendData;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderPayment() {
		
		return orderPayment;
	}

	public void setOrderPayment(int orderPayment) {
		this.orderPayment = orderPayment;
	}

	public String getMD5sign() {
		return MD5sign;
	}

	public void setMD5sign(String mD5sign) {
		MD5sign = mD5sign;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getCkappId() {
		return ckappId;
	}

	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	
	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}
	
	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
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

	public int getRandom() {
		return random;
	}

	public void setRandom(int random) {
		this.random = random;
	}

	public String getFilterRole() {
		return filterRole;
	}

	public void setFilterRole(List<Role> roles,List<Dict> filterRoles) {
//		if(filterRole == null){
			for(Role r:roles){
//				System.out.println(r.getId());
				for(Dict filterRole:filterRoles){
					if(r.getName().equals(filterRole.getValue())){
						this.filterRole = "10";//此角色数据已被过滤
						return;
					}
				}
			}
//		}
	}

	public int getFilterRate() {
		return filterRate;
	}

	public void setFilterRate(int filterRate) {
		this.filterRate = filterRate;
	}

}
