package me.ckhd.opengame.app.service;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.dao.PaymentWayDao;
import me.ckhd.opengame.app.entity.PaymentWay;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaymentWayService extends  CrudService<PaymentWayDao, PaymentWay>{
   
	public List<Map<String,String>> paywayList(PaymentWay paymentWay) {
		return dao.paywayList(paymentWay);
	}
	
}
