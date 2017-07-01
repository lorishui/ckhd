package me.ckhd.opengame.app.entity;

import org.hibernate.validator.constraints.Length;

import me.ckhd.opengame.common.persistence.DataEntity;
/**
 * 渠道  Entity
 * @author wesley
 * @version 2015-07-01
 */
public class Channel extends  DataEntity<Channel>{
 
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private String id;
	/**
	 * 渠道id
	 */
	private String channelId;
	/**
	 * channel 's  NAME.
	 */
	private String name;
	
	/**
	 * channel 's  company.
	 */
	private String company;
	/**
	 * 是否cps .
	 */
	private String isCPS;
	
	/**
	 * 英文名称,便于网游使用
	 */
	@Length(min=1,max=45,message="渠道英文名称的长度必须介于 1 和 45 之间")
	private String engName;
	
	/**
	 * 修改前名称
	 */
	private String oldName;
	/**
	 * 判断是否新增,新增：空    修改:不为空
	 */
	private String action;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	@Length(min=1,max=45,message="渠道名称的长度必须介于 1 和 45 之间")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	@Length(min=1,max=100,message="渠道所属公司的长度必须介于 1 和 100 之间")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getIsCPS() {
		return isCPS;
	}
	public void setIsCPS(String isCPS) {
		this.isCPS = isCPS;
	}
	public String getEngName() {
		return engName;
	}
	public void setEngName(String engName) {
		this.engName = engName;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
 
	 
	

}
