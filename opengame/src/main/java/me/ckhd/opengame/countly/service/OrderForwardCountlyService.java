package me.ckhd.opengame.countly.service;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.countly.dao.OrderForwardCountlyDao;
import me.ckhd.opengame.countly.entity.OrderForwardCountly;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单信息转发到Countly  Service
 *
 * @author leo
 * @TIME 2016年2月24日
 *
 */
@Service
@Transactional(readOnly = true)
public class OrderForwardCountlyService extends  CrudService<OrderForwardCountlyDao, OrderForwardCountly>{
	
	public List<OrderForwardCountly> selectWaitSend(Date nextSendTime) {
		return dao.selectWaitSend(nextSendTime);
	}
	@Transactional(readOnly = false)
	public int updateSendFail(OrderForwardCountly orderForwardCountly){
		return dao.updateSendFail(orderForwardCountly);
	}
	@Transactional(readOnly = false)
	public int updateSendSucess(String id){
		return dao.updateSendSucess(id);
	}

}
