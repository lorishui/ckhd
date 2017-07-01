package me.ckhd.opengame.gamecode.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;

public class GameCodeVer extends DataEntity<GameCodeVer> {
	
	/**
     * @Field @serialVersionUID : 序列化时使用
     */
    private static final long serialVersionUID = -263111965383106111L;

    private String ckAppId;
	
	private Date date;		//创建日期
	
	private String remark;	//备注
	
	private String status;	//状态 0有效  1无效

	public GameCodeVer() {
		super();
	}

	public GameCodeVer(String id) {
		super(id);
	}


	public GameCodeVer(String ckAppId, Date date, String remark, String status) {
		super();
		this.ckAppId = ckAppId;
		this.date = date;
		this.remark = remark;
		this.status = status;
	}

	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
