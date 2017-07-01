package me.ckhd.opengame.mmapi.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.mmapi.dao.MmAppOrderDao;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MmAppOrderService extends CrudService<MmAppOrderDao, MmAppOrder> {

	public boolean isExistByTransactionId(MmAppOrder mmAppOrder) {
		long number = dao.countByTransactionId(mmAppOrder);
		return number == 0 ? false : true;
	}

}
