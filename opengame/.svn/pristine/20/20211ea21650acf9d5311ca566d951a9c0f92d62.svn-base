package me.ckhd.opengame.game.dao;

import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.game.entity.Goods;

import org.springframework.stereotype.Repository;

@Repository
public interface GoodsDao extends CrudDao<Goods>{
	public List<Goods> findList(Goods goods);
	
	public int use(Goods goods);
}
