package me.ckhd.opengame.online.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.online.entity.OnlinePay;

import org.springframework.stereotype.Repository;

/**
 * 网游支付Dao
 * @author leo
 *
 */
@Repository
public interface OnlinePayDao extends CrudDao<OnlinePay> {
	
	public Integer getOrderId(Map<String, Object> map);
	
	public void updateOrderId(Map<String, Object> map);
	
	public void insertOrderId(Map<String, Object> map);
	
	public OnlinePay getOrderByOrderId(OnlinePay onlinePay);
	//容大使用
	public OnlinePay getRdOrderByOrderId(OnlinePay onlinePay);
	
	public OnlinePay getOrderByPrepayid(OnlinePay onlinePay);
	
	public OnlinePay getOrderByChannelOrderId(OnlinePay onlinePay);
	//百度使用
	public OnlinePay getOrderById(OnlinePay onlinePay);
	
	public List<Object> stats(OnlinePay onlinePay);
	
	public List<Object> statsByChannel(OnlinePay onlinePay);
	
	public List<Object> statsByOnlinePaycode(OnlinePay onlinePay);
	
	public List<Object> statsByChannelPaycode(OnlinePay onlinePay);
	/*public List<Object> statsByReturnStatus(OnlinePay onlinePay);

	public List<Object> statsByProvince(OnlinePay onlinePay);
	
	public List<Object> queryErrorOrders(OnlinePay onlinePay);*/
	

	/**
	 * 查询待转发的记录
	 * @return 等待转发的记录
	 */
	public List<OnlinePay>  selectWaitSend();
	/**
	 * 更新发送失败的记录 next_send_time =   status =  
	 * @param appOrderSender
	 * @return
	 */
	public int updateSendFail(OnlinePay onlinePay); 
	/**
	 * 更新发送成功的记录 status = '0'
	 * @param appOrderSender
	 * @return
	 */
	public int updateSendSucess(String id);
	
	/**
	 * 更新订单数据,增加下发信息
	 * @param onlinePay
	 * @return
	 */
	public int updateOnlinePayForSender(OnlinePay onlinePay);
	
	/**
	 * 通过订单号获取,订单序号
	 * @param orderId
	 * @return
	 */
	public int getOrderIndex(String orderId);
	
	/**
	 * 获取最大序号
	 * @return
	 */
	public int getMaxOrderIndex();
	
	/**
	 * 保存订单序号
	 * @param id
	 * @param orderId
	 * @return
	 */
	public int saveOrderIndex(Map<String, Object> map);
	
	/**
	 * 根据订单序号,获取订单号
	 * @param map
	 * @return
	 */
	public Integer getOrderIdByIndex(int index);
	
	/**
	 * 获取imsi的支付次数
	 * @param map
	 * @return
	 */
	public Integer getCountByImsi(Map<String,String> map);
	
	public OnlinePay getOrderByChannelOrderIdOther(OnlinePay onlinePay);
}
