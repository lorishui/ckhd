package me.ckhd.opengame.game.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.game.entity.Gift;
import me.ckhd.opengame.game.entity.GiftCode;

import org.springframework.stereotype.Repository;

@Repository
public interface GiftCodeDao extends CrudDao<GiftCode>{
	public Map<String,Object> getCode(Gift gift);
	public Integer isExist(Map<String,Object> map);
	public void batchInsert(List<GiftCode> giftCode);
}
