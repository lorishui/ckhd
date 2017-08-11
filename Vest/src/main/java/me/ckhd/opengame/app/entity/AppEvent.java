package me.ckhd.opengame.app.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

public class AppEvent extends DataEntity<AppEvent>{

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private int level;
	private String args;
	private Date operateTime;
	private Date instoreTime;
	
	private String ckappId;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getArgs() {
		return args;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public Date getInstoreTime() {
		return instoreTime;
	}
	public void setInstoreTime(Date instoreTime) {
		this.instoreTime = instoreTime;
	}
	public String getCkappId() {
		return ckappId;
	}
	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	
}