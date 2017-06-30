package me.ckhd.opengame.buyflow.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class BuyFlowRetainIncomeStats extends DataEntity<BuyFlowRetainIncomeStats>{
	
	private static final long serialVersionUID = 1L;
	
	private String ckappId;
	private String childCkappId;
	private String media;
	private String adItem;
	private String statsDate;
	private int days;
	private int deviceNum;
	private int income;
	
	private String activityId;
	
	private String startDate;
	private String endDate;
	
	
	
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
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
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public int getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(int deviceNum) {
		this.deviceNum = deviceNum;
	}
	public int getIncome() {
		return income;
	}
	public void setIncome(int income) {
		this.income = income;
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
	
}
