package me.ckhd.opengame.app.entity;

import me.ckhd.opengame.common.persistence.DataEntity;

public class QQActivity extends DataEntity<QQActivity>{

	private static final long serialVersionUID = 1L;
	private String imsi;
	private String qq;
	private String ckAppId;
	private String sign;
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getCkAppId() {
		return ckAppId;
	}
	public void setCkAppId(String ckAppId) {
		this.ckAppId = ckAppId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

}
