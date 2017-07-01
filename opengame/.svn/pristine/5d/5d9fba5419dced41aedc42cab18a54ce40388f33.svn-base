package me.ckhd.opengame.app.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;
/**
 * channel DAO接口
 * @author wesley
 * @version 2015-07-01
 */
@Repository
public interface ChannelDao extends  CrudDao<Channel>{
	
	/**
	 * 查询全部渠道数目
	 * @return
	 */
	public long findAllCount(Channel channel);
	
	/**
	 * 根据名称获取渠道信息
	 * @param Channel
	 * @return
	 */
	Channel  getByName(Channel channel);
	
	/**
	 * 根据运营商渠道号获取渠道名称 
	 * @param channelId
	 * @return
	 */
	public String getNameByCarriersChannelId(String channelId);
	/**
	 * 根据id获取渠道名称 
	 * @param channelId
	 * @return
	 */
	public String findChannelName(String id);
	
	/**
	 * 运营商渠道列表 
	 * @param carriersType
	 * @return
	 */
	public List<Channel> findChannelByCarriersType(String carriersType);
	
	/**
	 * 运营商渠道列表 
	 * @param carriersType
	 * @return
	 * @author yong
	 */
	public List<Channel> findChannelType(String carriersType);
	
	/**
	 * 根据英文名称获取渠道信息
	 * @param engName
	 * @return
	 */
	public List<Channel> getChannelByEngName(String engName);
	
	public List<Map<String,String>> getList(Channel channel);
}
