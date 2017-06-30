package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.ChannelDao;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.common.service.CrudService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChannelService extends CrudService<ChannelDao, Channel>{

	public Channel getByName(String name) {
		Channel channel = new Channel();
		channel.setName(name);
		return dao.getByName(channel);
	}
	//add some service method here 4  extend .
    public List<Channel> findChannelType(String carriersType){
	   return dao.findChannelType(carriersType);
    }
    /**
	  * 保存数据（插入或更新）
	  * @param entity
	  */
	@Transactional(readOnly = false)
	public void saveChanne(Channel channel) {
		if (StringUtils.isBlank(channel.getAction())){
			channel.preInsert();
			channel.setId(channel.getChannelId());
			dao.insert(channel);
		}else{
			channel.preUpdate();
			channel.setId(channel.getChannelId());
			dao.update(channel);
		}
	}
	
	public List<Map<String,String>> getList(Channel channel){
		return dao.getList(channel);
	}
}
