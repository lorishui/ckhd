package me.ckhd.opengame.app.service;

import java.util.List;

import me.ckhd.opengame.app.dao.ChannelCarriersDao;
import me.ckhd.opengame.app.entity.ChannelCarriers;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChannelCarriersService extends  CrudService<ChannelCarriersDao, ChannelCarriers>{

	public ChannelCarriers getByChannelIdAndCarriersType(String channelId,String carriersType) {
		ChannelCarriers channelCarriers = new ChannelCarriers();
		channelCarriers.setChannelId(channelId);
		channelCarriers.setCarriersType(carriersType);
		return dao.getByChannelIdAndCarriersType(channelCarriers);
	}
	//add some service method here 4  extend .
   
	public ChannelCarriers getByCarriersChannelidAndCarriersType(String carriersChannelId,String carriersType) {
		ChannelCarriers channelCarriers = new ChannelCarriers();
		channelCarriers.setChannelId(carriersChannelId);
		channelCarriers.setCarriersType(carriersType);
		return dao.getByCarriersChannelidAndCarriersType(channelCarriers);
	}
	
	public List<ChannelCarriers> findAllChannelOneType(String carriersType) {
		ChannelCarriers channelCarriers = new ChannelCarriers();
		channelCarriers.setCarriersType(carriersType);
		return dao.findAllChannelOneType(channelCarriers);
	}
}
