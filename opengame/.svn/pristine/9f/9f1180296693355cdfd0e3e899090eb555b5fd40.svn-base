package me.ckhd.opengame.woapi.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.woapi.dao.WoAppOrderDao;
import me.ckhd.opengame.woapi.entity.WoAppOrder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WoAppOrderService extends CrudService<WoAppOrderDao, WoAppOrder> {

	public boolean isExistByOrderId(WoAppOrder woAppOrder) {
		long number = dao.countByOrderId(woAppOrder);
		return number == 0 ? false : true;
	}

}
