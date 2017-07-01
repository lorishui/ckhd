package me.ckhd.opengame.adpush.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.excel.annotation.ExcelField;

public class AdPushCost extends DataEntity<AdPushCost> {
	// AdPushDetail的id
	private String adPushDetailId;
	// 消耗日期
	private Date date;
	// 当日消耗
	private Double dayCost;
	// 当日收入
	private Double dayEarn;
	// 注册设备数
	private Integer registNum;
	// 注册成本
	private Double registCost;
	// 平均收入
	private Double averageEarn;
	//搜索条件
	private Date startDate;
	private Date endDate;

	public AdPushCost() {
		super();
	}

	public AdPushCost(String id) {
		super(id);
	}

	
	public AdPushCost(String adPushDetailId, Date date, Double dayCost,
			Double dayEarn, Integer registNum, Double registCost,
			Date startDate, Date endDate) {
		super();
		this.adPushDetailId = adPushDetailId;
		this.date = date;
		this.dayCost = dayCost;
		this.dayEarn = dayEarn;
		this.registNum = registNum;
		this.registCost = registCost;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getAdPushDetailId() {
		return adPushDetailId;
	}
	
	public void setAdPushDetailId(String adPushDetailId) {
		this.adPushDetailId = adPushDetailId;
	}
	@ExcelField(title="消耗日期", type=2, align=2, sort=10)
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	@ExcelField(title="当日消耗",type=2, align=2, sort=30)
	public Double getDayCost() {
		return dayCost;
	}
	
	public void setDayCost(Double dayCost) {
		this.dayCost = dayCost;
	}
	@ExcelField(title="注册设备数",type=2, align=2, sort=20,fieldType=Integer.class)
	public Integer getRegistNum() {
		return registNum;
	}
	
	public void setRegistNum(Integer registNum) {
		this.registNum = registNum;
	}

	public Double getRegistCost() {
		return dayCost / registNum;
	}
	public void setRegistCost(Double registCost) {
		this.registCost = registCost;
	}
	
	public Double getAverageEarn() {
		return dayEarn / registNum;
	}
	public void setAverageEarn(Double averageEarn) {
		this.averageEarn = averageEarn;
	}


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@ExcelField(title="当日收入",type=2, align=2, sort=40)
	public Double getDayEarn() {
		return dayEarn;
	}

	public void setDayEarn(Double dayEarn) {
		this.dayEarn = dayEarn;
	}

	

}
