package me.ckhd.opengame.rdapi.bean;


public class RdRequest {
	
	// 合作方渠道号
	private String sid;

	// 合作方子渠道号
	private String cid;

	// 资费（单位：元）
	private String price;

	// 国际移动用户识别码
	private String imsi;

	// 支付方式：0：短信，1：支付宝，2：银联
	private String feetype;

	// 计费指令
	private String paycode;

	// 计费时间
	private String resulttime;

	// 计费标识：1：计费成功，其他：计费失败
	private String hRet;

	// 支付流水号, 唯一id
	private String uniqueid;

	// 客户自定义透传参数，不超过50字节的英文、数字
	private String cpparam;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getFeetype() {
		return feetype;
	}

	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}

	public String getPaycode() {
		return paycode;
	}

	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}

	public String getResulttime() {
		return resulttime;
	}

	public void setResulttime(String resulttime) {
		this.resulttime = resulttime;
	}

	public String gethRet() {
		return hRet;
	}

	public void sethRet(String hRet) {
		this.hRet = hRet;
	}

	public String getUniqueid() {
		return uniqueid;
	}

	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}

	public String getCpparam() {
		return cpparam;
	}

	public void setCpparam(String cpparam) {
		this.cpparam = cpparam;
	}

}
