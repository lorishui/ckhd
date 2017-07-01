package me.ckhd.opengame.egameapi.dao;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.egameapi.entity.EGameAppOrder;

import org.springframework.stereotype.Repository;

@Repository
public interface EGameAppOrderDao extends CrudDao<EGameAppOrder>{
	public long countByOrderId(EGameAppOrder eGameAppOrder);
}
