package me.ckhd.opengame.gamecode.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

public class GameCodeLog extends DataEntity<GameCodeLog> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ckAppId;
	private String code;
	private String phoneNum;
	private Date date;
	private String status;	//0 成功  1 失败
	
	private Date startDate;
	private Date endDate;
	
	public GameCodeLog() {
		super();
	}
	public GameCodeLog(String id) {
		super(id);
	}
	public GameCodeLog(GameCode gameCode, boolean b,String phoneNum) {
		super();
		this.ckAppId = gameCode.getCkAppId();
		this.code = gameCode.getCode();
		this.phoneNum = phoneNum;
		this.status = b?"0" : "1";
	}
	public GameCodeLog(String ckAppId, String code, String phoneNum, Date date,
			String status) {
		super();
		this.ckAppId = ckAppId;
		this.code = code;
		this.phoneNum = phoneNum;
		this.date = date;
		this.status = status;
	}
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getStartDate() {
		Date date2 = new Date();
		date2.setDate(date2.getDate() - 1);
		return date2;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return date;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}
