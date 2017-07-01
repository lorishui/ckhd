package me.ckhd.opengame.game.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.game.dao.GiftDescDao;
import me.ckhd.opengame.game.entity.GiftDesc;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiftDescService extends CrudService<GiftDescDao, GiftDesc>{
	
	@Transactional(readOnly=false)
	public int updateGenerate(GiftDesc giftDesc){
		return dao.updateGenerate(giftDesc);
	}
}
