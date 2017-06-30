package me.ckhd.opengame.stats.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 
 * @author llbas
 *
 */
public class StatMoney extends DataEntity<StatMoney> implements Cloneable{

	private static final long serialVersionUID = -218121959171913440L;
	
	private String timeframe;
	private String timeframes;
	private String ckAppId; 
	private String childCkAppId;
	private String ckChannelId; 
	private String childChannelId; 
	
	private int money;				//流水
	private int successMoney;		//成功支付金额
	private int payPeopleNum;		//支付人数
	private int paySuccessPeople;	//成功支付人数
	private int payTimes;			//支付次数
	private int paySuccessTimes;	//成功支付次数
	private int dataType = 0;		//1.成功金额   2.总金额
	
	private String timeFt="%Y%m%d%H";
	private int length=10;
	private String addStr="0000";//补充字段
	//条件
	private String startTime;
	private String endTime;
	private String groupBy = "timeframes";
	private int groupCkAppId;
	private int groupChildCkAppId;
	private int groupChannel;
	private int groupChildChannel;
	private int timeStyle;//0:无意义,1:小时,2:天,3:月
	
	private String permissionCkAppId;		//权限id
	private String permissionChannel;	//权限渠道
	
	public String getPermissionCkAppId() {
		return permissionCkAppId;
	}
	public void setPermissionCkAppId(String permissionCkAppId) {
		this.permissionCkAppId = permissionCkAppId;
	}
	public String getPermissionChannel() {
		return permissionChannel;
	}
	public void setPermissionChannel(String permissionChannel) {
		this.permissionChannel = permissionChannel;
	}
	public String getTimeframe() {
		return timeframe;
	}
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getChildCkAppId() {
		return childCkAppId;
	}
	public void setChildCkAppId(String childCkAppId) {
		this.childCkAppId = childCkAppId;
	}
	public String getCkChannelId() {
		return ckChannelId;
	}
	public void setCkChannelId(String ckChannelId) {
		this.ckChannelId = ckChannelId;
	}
	public String getChildChannelId() {
		return childChannelId;
	}
	public void setChildChannelId(String childChannelId) {
		this.childChannelId = childChannelId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTimeframes() {
		return timeframes;
	}
	public void setTimeframes(String timeframes) {
		this.timeframes = timeframes;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getTimeFt() {
		return timeFt;
	}
	public void setTimeFt(String timeFt) {
		this.timeFt = timeFt;
	}
	public int getGroupChildCkAppId() {
		return groupChildCkAppId;
	}
	public void setGroupChildCkAppId(int groupChildCkAppId) {
		this.groupChildCkAppId = groupChildCkAppId;
	}
	public int getGroupChannel() {
		return groupChannel;
	}
	public void setGroupChannel(int groupChannel) {
		this.groupChannel = groupChannel;
	}
	public int getGroupChildChannel() {
		return groupChildChannel;
	}
	public void setGroupChildChannel(int groupChildChannel) {
		this.groupChildChannel = groupChildChannel;
	}
	public int getTimeStyle() {
		return timeStyle;
	}
	public void setTimeStyle(int timeStyle) {
		this.timeStyle = timeStyle;
	}
	
	public int getGroupCkAppId() {
		return groupCkAppId;
	}
	public void setGroupCkAppId(int groupCkAppId) {
		this.groupCkAppId = groupCkAppId;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getAddStr() {
		return addStr;
	}
	
	public void setAddStr(String addStr) {
		this.addStr = addStr;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getSuccessMoney() {
		return successMoney;
	}
	public void setSuccessMoney(int successMoney) {
		this.successMoney = successMoney;
	}
	public int getPayPeopleNum() {
		return payPeopleNum;
	}
	public void setPayPeopleNum(int payPeopleNum) {
		this.payPeopleNum = payPeopleNum;
	}
	public int getPaySuccessPeople() {
		return paySuccessPeople;
	}
	public void setPaySuccessPeople(int paySuccessPeople) {
		this.paySuccessPeople = paySuccessPeople;
	}
	public int getPayTimes() {
		return payTimes;
	}
	public void setPayTimes(int payTimes) {
		this.payTimes = payTimes;
	}
	public int getPaySuccessTimes() {
		return paySuccessTimes;
	}
	public void setPaySuccessTimes(int paySuccessTimes) {
		this.paySuccessTimes = paySuccessTimes;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	
	
}
