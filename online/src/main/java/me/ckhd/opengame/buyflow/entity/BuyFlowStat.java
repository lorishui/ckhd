package me.ckhd.opengame.buyflow.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class BuyFlowStat extends DataEntity<BuyFlowStat> implements Cloneable{
	
	private String ckappId;
	private String childCkappId;
	private String media;
	private String adItem;
	private String buyFlowName;		//活动名称
	private String activityId;
	
	private String statsDate;		//统计日期
	private String startDate;
	private String endDate;
	
	private int clickNum;		//点击数
	private int deviceClickNum;	//设备点击数
	
	private int activeNum;		//激活数
	private int registerNum;		//设备注册数
	
	private int payDeviceNum;	//当日付费用户数
	private int payDeviceAmount;//当日付费金额
	
	private int retain1;		//1留用户数
	private int retainMoney1;	//1留付费金额
	private int retain3;		//3留用户数
	private int retainMoney3;	//3留付费金额
	private int retain7;		//7留用户数
	private int retainMoney7;	//7留付费金额
	private int retain30;		//30留用户数
	private int retainMoney30;	//30留付费金额
	private int retain60;		//60留用户数
	private int retainMoney60;	//60留付费金额
	
	private int activeDeviceNum;//新增付费设备数
	private int activeMoney;	//新增设备总付费
	private int totalDeviceNum;	//付费设备数
	private int totalMoney;		//总付费
	
	private int group;				//0 渠道  1 活动
	private int groupByDay;			//按天显示数据	0 no  1 yes
	
	private String groupBy;
	
	private int showRetain;		//显示留存相关
	private int show0Data;		//显示全零数据
	
	
	public int getShowRetain() {
		return showRetain;
	}
	public void setShowRetain(int showRetain) {
		this.showRetain = showRetain;
	}
	public int getShow0Data() {
		return show0Data;
	}
	public void setShow0Data(int show0Data) {
		this.show0Data = show0Data;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public int getDeviceClickNum() {
		return deviceClickNum;
	}
	public void setDeviceClickNum(int deviceClickNum) {
		this.deviceClickNum = deviceClickNum;
	}
	public int getRegisterNum() {
		return registerNum;
	}
	public void setRegisterNum(int registerNum) {
		this.registerNum = registerNum;
	}
	public String getBuyFlowName() {
		return buyFlowName;
	}
	public void setBuyFlowName(String buyFlowName) {
		this.buyFlowName = buyFlowName;
	}
	public int getGroup() {
		return group;
	}
	public void setGroup(int group) {
		this.group = group;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public int getGroupByDay() {
		return groupByDay;
	}
	public void setGroupByDay(int groupByDay) {
		this.groupByDay = groupByDay;
	}
	public String getCkappId() {
		return ckappId;
	}
	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	public String getChildCkappId() {
		return childCkappId;
	}
	public void setChildCkappId(String childCkappId) {
		this.childCkappId = childCkappId;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getAdItem() {
		return adItem;
	}
	public void setAdItem(String adItem) {
		this.adItem = adItem;
	}
	public String getStatsDate() {
		return statsDate;
	}
	public void setStatsDate(String statsDate) {
		this.statsDate = statsDate;
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
	public int getClickNum() {
		return clickNum;
	}
	public void setClickNum(int clickNum) {
		this.clickNum = clickNum;
	}
	public int getActiveNum() {
		return activeNum;
	}
	public void setActiveNum(int activeNum) {
		this.activeNum = activeNum;
	}
	public int getPayDeviceNum() {
		return payDeviceNum;
	}
	public void setPayDeviceNum(int payDeviceNum) {
		this.payDeviceNum = payDeviceNum;
	}
	public int getPayDeviceAmount() {
		return payDeviceAmount;
	}
	public void setPayDeviceAmount(int payDeviceAmount) {
		this.payDeviceAmount = payDeviceAmount;
	}
	public int getRetain1() {
		return retain1;
	}
	public void setRetain1(int retain1) {
		this.retain1 = retain1;
	}
	public int getRetainMoney1() {
		return retainMoney1;
	}
	public void setRetainMoney1(int retainMoney1) {
		this.retainMoney1 = retainMoney1;
	}
	public int getRetain3() {
		return retain3;
	}
	public void setRetain3(int retain3) {
		this.retain3 = retain3;
	}
	public int getRetainMoney3() {
		return retainMoney3;
	}
	public void setRetainMoney3(int retainMoney3) {
		this.retainMoney3 = retainMoney3;
	}
	public int getRetain7() {
		return retain7;
	}
	public void setRetain7(int retain7) {
		this.retain7 = retain7;
	}
	public int getRetainMoney7() {
		return retainMoney7;
	}
	public void setRetainMoney7(int retainMoney7) {
		this.retainMoney7 = retainMoney7;
	}
	public int getRetain30() {
		return retain30;
	}
	public void setRetain30(int retain30) {
		this.retain30 = retain30;
	}
	public int getRetainMoney30() {
		return retainMoney30;
	}
	public void setRetainMoney30(int retainMoney30) {
		this.retainMoney30 = retainMoney30;
	}
	public int getRetain60() {
		return retain60;
	}
	public void setRetain60(int retain60) {
		this.retain60 = retain60;
	}
	public int getRetainMoney60() {
		return retainMoney60;
	}
	public void setRetainMoney60(int retainMoney60) {
		this.retainMoney60 = retainMoney60;
	}
	public int getActiveDeviceNum() {
		return activeDeviceNum;
	}
	public void setActiveDeviceNum(int activeDeviceNum) {
		this.activeDeviceNum = activeDeviceNum;
	}
	public int getActiveMoney() {
		return activeMoney;
	}
	public void setActiveMoney(int activeMoney) {
		this.activeMoney = activeMoney;
	}
	public int getTotalDeviceNum() {
		return totalDeviceNum;
	}
	public void setTotalDeviceNum(int totalDeviceNum) {
		this.totalDeviceNum = totalDeviceNum;
	}
	public int getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
