package me.ckhd.opengame.game.dao;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.game.entity.GiftDesc;

import org.springframework.stereotype.Repository;

@Repository
public interface GiftDescDao extends CrudDao<GiftDesc>{
	public int updateGenerate(GiftDesc giftDesc);
}
