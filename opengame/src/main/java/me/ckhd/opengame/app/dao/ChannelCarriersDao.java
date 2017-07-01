package me.ckhd.opengame.app.dao;

import java.util.List;

import me.ckhd.opengame.app.entity.ChannelCarriers;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;
/**
 * channelcarriers  DAO接口
 * @author wesley
 * @version 2015-07-01
 */
@Repository
public interface ChannelCarriersDao extends  CrudDao<ChannelCarriers>{
	
	/**
	 * 查询全部数目
	 * @return
	 */
	public long findAllCount(ChannelCarriers channelCarriers);
	
	/**
	 * 根据渠道名称  获取运营商渠道信息
	 * @param channelCarriers
	 * @return
	 */
	ChannelCarriers  getByName(ChannelCarriers channelCarriers);
	
	
	/**
	 * 根据渠道id,运营商标识  获取运营商渠道信息
	 * @param channelCarriers
	 * @return
	 */
	ChannelCarriers  getByChannelIdAndCarriersType(ChannelCarriers channelCarriers);
	
	/**
	 * 根据运营商渠道id,运营商标识  获取运营商渠道信息
	 * @param channelCarriers
	 * @return
	 */
	ChannelCarriers  getByCarriersChannelidAndCarriersType(ChannelCarriers carriersChannelCarriers);
	
	/**
	 * 
	 */
	List<ChannelCarriers> findAllChannelOneType(ChannelCarriers carriersChannelCarriers);
}
