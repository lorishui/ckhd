package me.ckhd.opengame.gamecode.entity;

import java.util.Date;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.excel.annotation.ExcelField;

public class GameCode extends DataEntity<GameCode> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String ckAppId;		//游戏id
	
	private String code;		//礼包码
	
	private String status;		//状态   0:未发放  1：已发放
	
	private String valid;		//状态   0：有效  1：无效
	
	private Date date;			//导入时间
	
	private String remark;		//备注
	
	private String verId;		//批次id
	
	public static final String CODE_NOT_USED = "0";
	public static final String CODE_USED = "1";
	public static final String CODE_VALID = "0";
	public static final String CODE_INVALID = "1";

	public GameCode() {
		super();
	}

	public GameCode(String id) {
		super(id);
	}

	

	public GameCode(String ckAppId, String code, String status, String valid,
			Date date, String remark, String verId) {
		super();
		this.ckAppId = ckAppId;
		this.code = code;
		this.status = status;
		this.valid = valid;
		this.date = date;
		this.remark = remark;
		this.verId = verId;
	}

	public String getCkAppId() {
		return ckAppId;
	}

	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	@ExcelField(title="code",type=2, align=2, sort=30)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public String getVerId() {
		return verId;
	}

	public void setVerId(String verId) {
		this.verId = verId;
	}
	
	
}
