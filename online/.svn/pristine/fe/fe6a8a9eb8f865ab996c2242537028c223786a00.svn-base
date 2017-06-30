package me.ckhd.opengame.online.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.online.dao.OnlinePayDao;
import me.ckhd.opengame.online.entity.OnlinePay;

@Service("OnlinePayService")
@Transactional(readOnly = true)
public class OnlinePayService extends CrudService<OnlinePayDao, OnlinePay> {
	@Autowired
	private OnlinePayDao onlinePayDao;  //支onlinePayDao
	public List<Object> stats(OnlinePay onlinePay) {
		return onlinePayDao.stats(onlinePay);
	}
	
	public List<Object> statsByChannel(OnlinePay onlinePay) {
		return onlinePayDao.statsByChannel(onlinePay);
	}
	public List<Object> statsByOnlinePaycode(OnlinePay onlinePay) {
		return onlinePayDao.statsByOnlinePaycode(onlinePay);
	}
	public List<Object> statsByChannelPaycode(OnlinePay onlinePay) {
		return onlinePayDao.statsByChannelPaycode(onlinePay);
	}
}
