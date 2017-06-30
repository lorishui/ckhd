/*
 * 
 */
package me.ckhd.opengame.buyflow.service;

import me.ckhd.opengame.buyflow.dao.BuyFlowDao;
import me.ckhd.opengame.buyflow.entity.BuyFlow;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 */
@Service
public class BuyFlowService extends CrudService<BuyFlowDao, BuyFlow> {
	
	public boolean existBuyFlow(BuyFlow buyFlow) {
		return dao.existBuyFlow(buyFlow) == 0 ? false : true;
	}

	 public BuyFlow queryBuyFlow24H(BuyFlow buyFlow) {
		return dao.queryBuyFlow24H(buyFlow);
	}
	 @Transactional(readOnly = false)
	public void updateState(BuyFlow buyFlow) {
		dao.updateState(buyFlow);
	}
	
}
