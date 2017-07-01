package me.ckhd.opengame.game.dao;


import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.game.entity.Gift;

import org.springframework.stereotype.Repository;

@Repository
public interface GiftDao extends CrudDao<Gift>{
	public List<Map<String,Object>> getSub(Gift gift);
	public List<Gift> findList(Gift gift);
	public String getDesc(Gift gift);
}