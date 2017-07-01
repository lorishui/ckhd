package me.ckhd.opengame.app.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.app.entity.PaymentWay;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

/**
 * 支付通道配置DAO接口
 * @author wesley
 * @version 2015-06-30
 */
@Repository
public interface PaymentWayDao  extends CrudDao<PaymentWay> {
	// add some method .
	/**
	 * 查询记录总数 
	 * @return
	 */
	public long findAllCount(PaymentWay paymentWay);
	
	public List<Map<String,String>> paywayList(PaymentWay paymentWay);
	
}
