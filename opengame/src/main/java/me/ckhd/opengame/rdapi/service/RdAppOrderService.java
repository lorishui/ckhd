package me.ckhd.opengame.rdapi.service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.rdapi.dao.RdAppOrderDao;
import me.ckhd.opengame.rdapi.entity.RdAppOrder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RdAppOrderService extends CrudService<RdAppOrderDao, RdAppOrder> {
	public boolean isExitOrderId(String uniqueid){
		if(uniqueid == null || uniqueid.length() == 0 ){
			return true;
		}else{
			long n = dao.isExitOrderId(uniqueid);
			return n==0?false:true;
		}
	}
}
