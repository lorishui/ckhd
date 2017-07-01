package me.ckhd.opengame.api.service;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.api.dao.AppOrderForwardDao;
import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单信息转发到CP  Service
 * @author wesley
 * @version 2015-07-30
 */
@Service
@Transactional(readOnly = true)
public class AppOrderForwardService extends  CrudService<AppOrderForwardDao, AppOrderForward>{
	
	public List<AppOrderForward> selectWaitSend(Date nextSendTime) {
		return dao.selectWaitSend(nextSendTime);
	}
	@Transactional(readOnly = false)
	public int updateSendFail(AppOrderForward appOrderForward){
		return dao.updateSendFail(appOrderForward);
	}
	@Transactional(readOnly = false)
	public int updateSendSucess(String id){
		return dao.updateSendSucess(id);
	}

}
