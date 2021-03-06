package me.ckhd.opengame.stats.entity;

import java.util.List;

import me.ckhd.opengame.common.persistence.DataEntity;

/**
 * 
 * @author llbas
 *
 */
public class StatRetention extends DataEntity<StatRetention> implements Cloneable{

	private static final long serialVersionUID = 8205310156652526950L;
	private String regTime;
	private int totalNum;
	private int actNum;
	private int d0Num;
	private int d1Num;
	private int d2Num;
	private int d3Num;
	private int d4Num;
	private int d5Num;
	private int d6Num;
	private int d7Num;
	private int d14Num;
	private int d30Num;
	private int d60Num;
	private int d90Num;
	
	private String ckAppId; 
	private String childCkAppId;
	private String ckChannelId; 
	private String childChannelId; 

	private String groupBy = "regTime";
	private int groupCkAppId;
	private int groupChildCkAppId;
	private int groupChannel;
	private int groupChildChannel;
	
	private String startTime;
	private String endTime;
	
	private String permissionCkAppId;		//游戏权限
	private List<String> permissionCkAppChildId;	//子游戏权限
	private String permissionChannel;		//权限渠道
	
	private int ltv0NewDevice;		//当天新增的付费人数
	
	
	
	
	public int getD60Num() {
		return d60Num;
	}

	public void setD60Num(int d60Num) {
		this.d60Num = d60Num;
	}

	public int getD90Num() {
		return d90Num;
	}

	public void setD90Num(int d90Num) {
		this.d90Num = d90Num;
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

	public int getActNum() {
		return actNum;
	}

	public void setActNum(int actNum) {
		this.actNum = actNum;
	}

	public int getD0Num() {
		return d0Num;
	}

	public void setD0Num(int d0Num) {
		this.d0Num = d0Num;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getD1Num() {
		return d1Num;
	}

	public void setD1Num(int d1Num) {
		this.d1Num = d1Num;
	}

	public int getD2Num() {
		return d2Num;
	}

	public void setD2Num(int d2Num) {
		this.d2Num = d2Num;
	}

	public int getD3Num() {
		return d3Num;
	}

	public void setD3Num(int d3Num) {
		this.d3Num = d3Num;
	}

	public int getD4Num() {
		return d4Num;
	}

	public void setD4Num(int d4Num) {
		this.d4Num = d4Num;
	}

	public int getD5Num() {
		return d5Num;
	}

	public void setD5Num(int d5Num) {
		this.d5Num = d5Num;
	}

	public int getD6Num() {
		return d6Num;
	}

	public void setD6Num(int d6Num) {
		this.d6Num = d6Num;
	}

	public int getD7Num() {
		return d7Num;
	}

	public void setD7Num(int d7Num) {
		this.d7Num = d7Num;
	}

	public int getD14Num() {
		return d14Num;
	}

	public void setD14Num(int d14Num) {
		this.d14Num = d14Num;
	}

	public int getD30Num() {
		return d30Num;
	}

	public void setD30Num(int d30Num) {
		this.d30Num = d30Num;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public int getGroupCkAppId() {
		return groupCkAppId;
	}

	public void setGroupCkAppId(int groupCkAppId) {
		this.groupCkAppId = groupCkAppId;
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

	public int getLtv0NewDevice() {
		return ltv0NewDevice;
	}

	public void setLtv0NewDevice(int ltv0NewDevice) {
		this.ltv0NewDevice = ltv0NewDevice;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
