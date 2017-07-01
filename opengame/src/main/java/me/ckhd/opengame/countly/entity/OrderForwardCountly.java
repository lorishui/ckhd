package me.ckhd.opengame.countly.entity;

import java.util.Calendar;
import java.util.Date;

import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.woapi.entity.WoAppOrder;

public class OrderForwardCountly extends DataEntity<OrderForwardCountly>{

 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 转发的类型(mm,andgame,union)
	 */
	private String type; 
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
	
	private String appid;
	
	private String ckappid;
	/**
	 * 下发字符串
	 */
	private String content;
	
	/**
	 * 拼装数据所需字段
	 */
	private String provinces;
	
	private String returnStatus;
	
	private boolean success;
	
	private String payCode;
	
	private String channel;
	
	private String appKey="85caccc1949543cc5da230654bfc654a23bfec44";
	
	private String timestamp;
	
	private String eventTimestamp;
	
	private String device_id="opengame";
	
	private String hour;
	
	private String dow;
	
	private String key="OPENGAME_PAY_EVENT";
	
	private String prices;
	
	public OrderForwardCountly(){}

	public OrderForwardCountly(DataEntity dataEntity){
		if(dataEntity instanceof AndAPPOrder){
			this.setType("ANDGAME");
			setContent((AndAPPOrder)dataEntity);
		}else if (dataEntity instanceof MmAppOrder) {
			this.setType("MM");
			setContent((MmAppOrder)dataEntity);
		}else if(dataEntity instanceof WoAppOrder){
			this.setType("WO");
		}else if(dataEntity instanceof OnlinePay){
			this.setType("ONLINE");
		}else{
			this.setType("EGAME");
		}
		this.setStatus(1);
		this.setSendNum(0);
	}
	
	/**
	 * 根据mm订单信息组装
	 * app_key=cdb03bab0616718a7b24284028f3148e4d7c5d1f&timestamp=1457507630
	 * &device_id=353867058565057460010208653157&hour=15&dow=3&
	 * events=[{"timestamp":1457507590,"sum":1,
	 * "segmentation":{"app_version":"1.2","country":"china"},
	 * "dow":3,"hour":15,"count":1,"key":"purchase"}]
	 */
	private void setContent(MmAppOrder mmAppOrder){
		String actionTime = mmAppOrder.getActionTime();
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(DateUtils.parseDateByParsePatterns(actionTime, "yyyyMMddHHmmssSS"));
		dow = calendar.get(Calendar.WEEK_OF_MONTH)+"";
		hour = calendar.get(Calendar.HOUR_OF_DAY)+"";
		timestamp = calendar.getTimeInMillis()+"";
		eventTimestamp = calendar.getTimeInMillis()+1+"";
		prices = mmAppOrder.getPrice()+"";
		payCode = mmAppOrder.getPaycode();
		returnStatus =mmAppOrder.getReturnStatus();
		provinces = mmAppOrder.getProvinceID();
		channel = mmAppOrder.getChannelId();
		if ("00000000000000000000".equals(mmAppOrder.getOrderId())) {
			success=false;
		} else {
			success=true;
		}
		genContent();
	}
	
	private void genContent(){
		String contentStr = "app_key="+appKey+"&timestamp="+timestamp+"&device_id="+device_id+
				"&hour="+hour+"&dow="+dow+"&appid="+appid+"&type="+type+"&payEvents=[{\"timestamp\":"+eventTimestamp+",\"sum\":"+prices+",\"success\":"+success+","
				+ "\"segmentation\":{\"status\":\""+returnStatus+"\",\"payCode\":\""+payCode+"\",\"channel\":\""+channel+"\","
				+ "\"provinces\":\""+provinces+"\"},\"dow\":"+dow+",\"hour\":"+hour+",\"count\":1}]";
//		String eventStr = "app_key="+appKey+"&timestamp="+timestamp+"&device_id="+device_id+
//				"&hour="+hour+"&dow="+dow+"&appid="+appid+"&events=[{\"timestamp\":"+eventTimestamp+",\"sum\":"+prices+","
//				+ "\"segmentation\":{\"status\":\""+returnStatus+"\",\"payCode\":\""+payCode+"\",\"channel\":\""+channel+"\","
//				+ "\"provinces\":\""+provinces+"\"},\"dow\":"+dow+",\"hour\":"+hour+",\"count\":1,\"key\":\"OPENGAME_PAY_EVENT\"}]";
//		
		this.setContent(contentStr);
	}
	
	/**
	 * 根据和游戏订单信息组装
	 */
	private void setContent(AndAPPOrder andAppOrder){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(andAppOrder.getCreateDate());
		
		dow = calendar.get(Calendar.WEEK_OF_MONTH)+"";
		hour = calendar.get(Calendar.HOUR_OF_DAY)+"";
		timestamp = calendar.getTimeInMillis()+"";
		eventTimestamp = calendar.getTimeInMillis()+1+"";
		appid = andAppOrder.getCpId();
		prices = (StringUtils.isBlank(andAppOrder.getPrice())?"0":andAppOrder.getPrice()+"");
		payCode = andAppOrder.getConsumeCode();
		returnStatus =andAppOrder.getStatus();
		provinces = (andAppOrder.getProvinceId()==null?"":andAppOrder.getProvinceId());
		channel =(andAppOrder.getChannelId()==null?"":andAppOrder.getChannelId());
		if (!"0".equals(andAppOrder.gethRet())) {
			success=false;
		} else {
			success=true;
		}
		genContent();
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventTimestamp() {
		return eventTimestamp;
	}

	public void setEventTimestamp(String eventTimestamp) {
		this.eventTimestamp = eventTimestamp;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getDow() {
		return dow;
	}

	public void setDow(String dow) {
		this.dow = dow;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getPrices() {
		return prices;
	}

	public void setPrices(String prices) {
		this.prices = prices;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCkappid() {
		return ckappid;
	}

	public void setCkappid(String ckappid) {
		this.ckappid = ckappid;
	}
}
