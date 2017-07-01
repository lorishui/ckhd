package me.ckhd.opengame.egameapi.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.egameapi.dao.EGameAppOrderDao;
import me.ckhd.opengame.egameapi.entity.EGameAppOrder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class EGameAppOrderService extends CrudService<EGameAppOrderDao, EGameAppOrder> {

	public boolean isExistByOrderId(EGameAppOrder eGameAppOrder) {
		long number = dao.countByOrderId(eGameAppOrder);
		return number == 0 ? false : true;
	}
}
