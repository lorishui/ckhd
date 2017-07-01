package me.ckhd.opengame.egameapi.entity;

import me.ckhd.opengame.common.persistence.DataEntity;
/**
 * 
 */
public class EGameAppOrder extends DataEntity<EGameAppOrder> {

	private static final long serialVersionUID = 1L;
	
	private String ckapp_id; // 产品id
	private String cp_order_id;// 订单号 String
	private String correlator;//爱游戏平台的流水号
	private String order_time;//订单时间，格式(yyyyMMddHHmmss)
	private String result_code;//状态码,00表示成功,其他表示失败
	private int fee;//金额,单位:分
	private String pay_type;//计费类型，smsPay：短代；alipay：支付宝；ipay：爱贝
	private String method;//固定值 callback|check
	private String sign;//加密串
	private String version;//版本
	public String getCkapp_id() {
		return ckapp_id;
	}
	public void setCkapp_id(String ckapp_id) {
		this.ckapp_id = ckapp_id;
	}
	public String getCp_order_id() {
		return cp_order_id;
	}
	public void setCp_order_id(String cp_order_id) {
		this.cp_order_id = cp_order_id;
	}
	public String getCorrelator() {
		return correlator;
	}
	public void setCorrelator(String correlator) {
		this.correlator = correlator;
	}
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
