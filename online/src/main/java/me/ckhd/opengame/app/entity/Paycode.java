package me.ckhd.opengame.app.entity;


import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Length;

import me.ckhd.opengame.common.persistence.DataEntity;
import me.ckhd.opengame.common.utils.excel.annotation.ExcelField;
/**
 * 运营商app  Entity
 * @author wesley
 * @version 2015-07-03
 */
public class Paycode extends  DataEntity<Paycode>{
 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 计费编码 
	 */
	private String paycode;
	

	/**
	 * 计费类型 
	 */
	private String paytype;
	
	/**
	 * 计费点名称 
	 */
	private String name;
	
	/**
	 * 计费点价格 （分)
	 * 
	 */
	@Digits(integer=5, fraction=0, message="注意价格单位必须为分,且必须是不超过5位的整形")
    private String  price;
	/**
	 * 运营商类型标识
	 */
	private String carriersType;
	
	
	/**
	 * 运营商中对应appid 
	 */
	private String appId;
	

	
	/************************************ 附加 ****/
	/**
	 * 运营商类型名称 
	 */
	private String carriersName;
	
	
	/**
	 *   appName.
	 */
	private String appName;
	
	@ExcelField(title="应用id", type=2, align=2, sort=1) 
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	@ExcelField(title="运营商类型", type=2, align=2, sort=10,dictType="carriers") 
	public String getCarriersType() {
		return carriersType;
	}
	public void setCarriersType(String carriersType) {
		this.carriersType = carriersType;
	}
	public String getCarriersName() {
		return carriersName;
	}
	public void setCarriersName(String carriersName) {
		this.carriersName = carriersName;
	}
	@ExcelField(title="计费点名称", type=2, align=2, sort=30) 
	@Length(min=1,max=45,message="计费点名称长度必须介于1 和 45 之间")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ExcelField(title="计费点代码", type=2, align=2, sort=20) 
	@Length(min=1,max=100,message="计费点编码长度必须介于1 和 100 之间")
	public String getPaycode() {
		return paycode;
	}
	public void setPaycode(String paycode) {
		this.paycode = paycode;
	}
	@ExcelField(title="计费类型", type=2, align=2, sort=50) 
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	@ExcelField(title="计费价格(单位分)", type=2, align=2, sort=40) 
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	 
	
	 

}
