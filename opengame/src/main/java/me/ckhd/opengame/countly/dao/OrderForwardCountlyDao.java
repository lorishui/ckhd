package me.ckhd.opengame.countly.dao;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.countly.entity.OrderForwardCountly;

import org.springframework.stereotype.Repository;
/**
 * 
 *
 * @author leo
 * @TIME 2016年2月24日
 *
 */
@Repository
public  interface OrderForwardCountlyDao extends  CrudDao<OrderForwardCountly>{
	// add the method  
	
	/**
	 * 查询待转发的记录
	 * @return 等待转发的记录
	 */
	List<OrderForwardCountly>  selectWaitSend(Date  nextSendTime);
	/**
	 * 更新发送失败的记录 next_send_time =   status =  
	 * @param appOrderForward
	 * @return
	 */
	int updateSendFail(OrderForwardCountly orderForwardCountly); 
	/**
	 * 更新发送成功的记录 status = '0'
	 * @param appOrderForward
	 * @return
	 */
	int updateSendSucess(String id);
	
}
