package me.ckhd.opengame.app.entity;

import org.hibernate.validator.constraints.Length;

import me.ckhd.opengame.common.persistence.DataEntity;
/**
 * 渠道运营商关系  Entity
 * @author wesley
 * @version 2015-07-01
 */
public class ChannelCarriers extends  DataEntity<ChannelCarriers>{
 
	private static final long serialVersionUID = 1L;
	/**
	 * channel_id
	 */
	private String channelId;
	/**
	 * channel 's  NAME.
	 */
	private String name;
	
	/**
	 * 运营商类型标识
	 */
	private String carriersType;
	
	
	/**
	 * 运营商类型名称 
	 */
	private String carriersName;
	
	/**
	 * 运营商中对应的渠道id 
	 */
	private String carriersChannelId;
	
	
 
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Length(min=1,max=64,message="渠道ID的长度必须介于 1 和 64 之间")
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
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
	
	@Length(min=1,max=30,message="运营商中渠道名称的长度必须介于1 和 30 之间")
	public String getCarriersChannelId() {
		return carriersChannelId;
	}
	public void setCarriersChannelId(String carriersChannelId) {
		this.carriersChannelId = carriersChannelId;
	}

}
