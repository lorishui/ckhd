package me.ckhd.opengame.app.entity;

import org.hibernate.validator.constraints.Length;

import me.ckhd.opengame.common.persistence.DataEntity;
/**
 * 运营商app  Entity
 * @author wesley
 * @version 2015-07-03
 */
public class AppCarriers extends  DataEntity<AppCarriers>{
 
	private static final long serialVersionUID = 1L;
	/**
	 * ckapp_id
	 */
	private String ckappId;
	
	/**
	 * 运营商类型标识
	 */
	private String carriersType;
	
	
	/**
	 * 运营商中对应appid 
	 */
	private String appId;
	
	/**
	 *    appName.
	 */
	private String appName;
	
	/**
	 * cp_server_url
	 */
	private String cpServerUrl;
	/************************************ 附加 ****/
	/**
	 * 运营商类型名称 
	 */
	private String carriersName;
	
	/**
	 * 创酷app name
	 */
	private String name;
	
	/**
	 * 刷量商服务器URL
	 */
	private String flowServerUrl;
	
	/**
	 * 向刷量商转发计费点列表
	 */
	private String paycodes;
	
	// 指定渠道转发
	private String forwardByChannel;
	
	@Length(min=1,max=20,message="运营商appid长度必须介于1 和 64 之间")
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	@Length(min=1,max=20,message="运营商app名称长度必须介于1 和 45 之间")
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	@Length(min=1,max=20,message="运营商类型长度必须介于1 和 20 之间")
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCkappId() {
		return ckappId;
	}
	public void setCkappId(String ckappId) {
		this.ckappId = ckappId;
	}
	public String getCpServerUrl() {
		return cpServerUrl;
	}
	public void setCpServerUrl(String cpServerUrl) {
		this.cpServerUrl = cpServerUrl;
	}
	public String getFlowServerUrl() {
		return flowServerUrl;
	}
	public void setFlowServerUrl(String flowServerUrl) {
		this.flowServerUrl = flowServerUrl;
	}
	public String getPaycodes() {
		return paycodes;
	}
	public void setPaycodes(String paycodes) {
		this.paycodes = paycodes;
	}
	public String getForwardByChannel() {
		return forwardByChannel;
	}
	public void setForwardByChannel(String forwardByChannel) {
		this.forwardByChannel = forwardByChannel;
	}
	
}
