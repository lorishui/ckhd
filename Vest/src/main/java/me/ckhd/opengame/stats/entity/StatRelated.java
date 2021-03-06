package me.ckhd.opengame.stats.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 统计相关
 * @author ASUS
 *
 */
public class StatRelated extends DataEntity<StatRelated> implements Cloneable{

	private static final long serialVersionUID = -218121959171913440L;
	
	private String timeframe;
	private String timeframes;
	private String ckAppId; 
	private String childCkAppId;
	private String ckChannelId; 
	private String childChannelId; 
	private int newNum;
	private int actNum;
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
	
	private int money;				//流水
	private int successMoney;		//成功支付金额
	private int payPeopleNum;		//支付人数
	private int paySuccessPeople;	//成功支付人数
	private int payTimes;			//支付次数
	private int paySuccessTimes;	//成功支付次数
	
	private int reten1;				//次留
	private int reten2;				//2留
	private int reten3;				//3留
	private int reten4;				//4留
	private int reten5;				//5留
	private int reten6;				//6留
	private int reten7;				//7留
	private int reten14;				//14留
	private int reten30;				//月留

	private int ltv0;					//LTV0
	private int ltv1;					//LTV1
	private int ltv2;					//LTV2
	private int ltv3;					//LTV3
	private int ltv4;					//LTV4
	private int ltv5;					//LTV5
	private int ltv6;					//LTV6
	private int ltv7;					//LTV7
	private int ltv14;					//LTV14
	private int ltv30;					//LTV30
	private int ltv60;					//LTV60
	private int ltv90;					//LTV90
	
	private int ltv0NewDevice;		//当天新增的付费人数
	
	private String permissionCkAppId;		//游戏权限
	private List<String> permissionCkAppChildId;	//子游戏权限
	private String permissionChannel;		//权限渠道
	
	
	
	
	public int getLtv60() {
		return ltv60;
	}
	public void setLtv60(int ltv60) {
		this.ltv60 = ltv60;
	}
	public int getLtv90() {
		return ltv90;
	}
	public void setLtv90(int ltv90) {
		this.ltv90 = ltv90;
	}
	public String getPermissionCkAppId() {
		return permissionCkAppId;
	}
	public void setPermissionCkAppId(String permissionCkAppId) {
		this.permissionCkAppId = permissionCkAppId;
	}
	public List<String> getPermissionCkAppChildId() {
		return permissionCkAppChildId;
	}
	public void setPermissionCkAppChildId(List<String> permissionCkAppChildId) {
		this.permissionCkAppChildId = permissionCkAppChildId;
	}
	public String getPermissionChannel() {
		return permissionChannel;
	}
	public void setPermissionChannel(String permissionChannel) {
		this.permissionChannel = permissionChannel;
	}
	public int getReten1() {
		return reten1;
	}
	public void setReten1(int reten1) {
		this.reten1 = reten1;
	}
	public int getReten3() {
		return reten3;
	}
	public void setReten3(int reten3) {
		this.reten3 = reten3;
	}
	public int getReten7() {
		return reten7;
	}
	public void setReten7(int reten7) {
		this.reten7 = reten7;
	}
	public int getReten14() {
		return reten14;
	}
	public void setReten14(int reten14) {
		this.reten14 = reten14;
	}
	public int getReten30() {
		return reten30;
	}
	public void setReten30(int reten30) {
		this.reten30 = reten30;
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
	public int getNewNum() {
		return newNum;
	}
	public void setNewNum(int newNum) {
		this.newNum = newNum;
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
	
	public int getActNum() {
		return actNum;
	}
	
	public void setActNum(int actNum) {
		this.actNum = actNum;
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
	public int getLtv0() {
		return ltv0;
	}
	public void setLtv0(int ltv0) {
		this.ltv0 = ltv0;
	}
	public int getLtv1() {
		return ltv1;
	}
	public void setLtv1(int ltv1) {
		this.ltv1 = ltv1;
	}
	public int getLtv3() {
		return ltv3;
	}
	public void setLtv3(int ltv3) {
		this.ltv3 = ltv3;
	}
	public int getLtv7() {
		return ltv7;
	}
	public void setLtv7(int ltv7) {
		this.ltv7 = ltv7;
	}
	public int getLtv14() {
		return ltv14;
	}
	public void setLtv14(int ltv14) {
		this.ltv14 = ltv14;
	}
	public int getLtv30() {
		return ltv30;
	}
	public void setLtv30(int ltv30) {
		this.ltv30 = ltv30;
	}
	public int getLtv0NewDevice() {
		return ltv0NewDevice;
	}
	public void setLtv0NewDevice(int ltv0NewDevice) {
		this.ltv0NewDevice = ltv0NewDevice;
	}
	public int getLtv2() {
		return ltv2;
	}
	public void setLtv2(int ltv2) {
		this.ltv2 = ltv2;
	}
	public int getLtv4() {
		return ltv4;
	}
	public void setLtv4(int ltv4) {
		this.ltv4 = ltv4;
	}
	public int getLtv5() {
		return ltv5;
	}
	public void setLtv5(int ltv5) {
		this.ltv5 = ltv5;
	}
	public int getLtv6() {
		return ltv6;
	}
	public void setLtv6(int ltv6) {
		this.ltv6 = ltv6;
	}
	public int getReten2() {
		return reten2;
	}
	public void setReten2(int reten2) {
		this.reten2 = reten2;
	}
	public int getReten4() {
		return reten4;
	}
	public void setReten4(int reten4) {
		this.reten4 = reten4;
	}
	public int getReten5() {
		return reten5;
	}
	public void setReten5(int reten5) {
		this.reten5 = reten5;
	}
	public int getReten6() {
		return reten6;
	}
	public void setReten6(int reten6) {
		this.reten6 = reten6;
	}
	
}
