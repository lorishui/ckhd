package me.ckhd.opengame.buyflow.dao;

import me.ckhd.opengame.buyflow.entity.BuyFlow;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

/**
 * 
 */
@Repository
public interface BuyFlowDao extends CrudDao<BuyFlow> {

	int existBuyFlow(BuyFlow buyFlow);
	
	BuyFlow queryBuyFlow24H(BuyFlow buyFlow);
	
	void updateState(BuyFlow buyFlow);
	
}
