package me.ckhd.opengame.api.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AppOrderForward extends DataEntity<AppOrderForward>{

 
	private static final long serialVersionUID = 1L;
	/**
	 * 创酷appid
	 */
	private String ckappId;
	/**
	 * 运营商appid
	 */
	private String appId;
	/**
	 * 订单流水表id
	 */
	private String orderId;
	/**
	 * 订单类型（和游戏，MM ..)
	 */
	private String orderType;
	
	/**
	 * 订单消息xml内容
	 */
	private String content;
	/**
	 * 已经转发CP次数 default 1   7次后不再转发
	 */
	private Integer sendNum; 
	/**
	 * 下次转发时间
	 */
	private Date  nextSendTime;
	/**
	 * 0-发送完成;1-等待重发或发送中;2-七次重发失效
	 */
	private Integer status;
	public String getCkappId() {
		return ckappId;
	}
	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public Date getNextSendTime() {
		return nextSendTime;
	}
	public void setNextSendTime(Date nextSendTime) {
		this.nextSendTime = nextSendTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
}
