package me.ckhd.opengame.api.dao;

import java.util.Date;
import java.util.List;

import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;
/**
 * 订单信息转发到CP  Dao
 * @author wesley
 * @version 2015-07-30
 */

@Repository
public  interface AppOrderForwardDao extends  CrudDao<AppOrderForward>{
	// add the method  
	
	/**
	 * 查询待转发的记录
	 * @return 等待转发的记录
	 */
	List<AppOrderForward>  selectWaitSend(Date  nextSendTime);
	/**
	 * 更新发送失败的记录 next_send_time =   status =  
	 * @param appOrderForward
	 * @return
	 */
	int updateSendFail(AppOrderForward appOrderForward); 
	/**
	 * 更新发送成功的记录 status = '0'
	 * @param appOrderForward
	 * @return
	 */
	int updateSendSucess(String id);
	
}
