package me.ckhd.opengame.hsapi.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.hsapi.dao.HsAppOrderDao;
import me.ckhd.opengame.hsapi.entity.HsAppOrder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class HsAppOrderService extends CrudService<HsAppOrderDao, HsAppOrder> {
	public boolean isExitOrderId(String tradeId){
		if(tradeId == null || tradeId.length() == 0 ){
			return true;
		}else{
			long n = dao.isExitOrderId(tradeId);
			return n==0?false:true;
		}
	}
}
