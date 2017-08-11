package me.ckhd.opengame.online.task;

import java.util.HashMap;
import java.util.List;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.dao.MyCardOrderDao;
import me.ckhd.opengame.online.entity.MyCardOrder;
import me.ckhd.opengame.online.handle.common.mycard.HTTPSClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


@Component
@Lazy(false)
public class MyCardGetMoney {
	
	@Autowired
	private MyCardOrderDao dao;
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String GET_MONEY_URL_TEST = "https://test.b2b.mycard520.com.tw/MyBillingPay/api/PaymentConfirm";
	
	private static final String GET_MONEY_URL = "https://b2b.mycard520.com.tw/MyBillingPay/api/PaymentConfirm";
	/**
	 * 请款任务
	 * 每5min查询数据库表MyCardOrder,对未请款订单进行请款
	 */
//	@Scheduled(fixedDelay = 60 * 5000)
	public void getMoney(){
		log.info("mycard请款开始");
		MyCardOrder order = new MyCardOrder();
		order.setStatus("1");
		List<MyCardOrder> list = dao.findList(order);
		for (MyCardOrder mco : list) {
			String URL = GET_MONEY_URL;
			if("1".equals(mco.getIsTest())){
				URL = GET_MONEY_URL_TEST;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("AuthCode", mco.getAuthCode());
			String response = HTTPSClientUtils.doPost(URL, map, "utf-8");
			
			if(StringUtils.isNotBlank(response)){
				JSONObject resp = JSONObject.parseObject(response);
				if("1".equals(resp.getString("ReturnCode"))){
					mco.setStatus("2");
					mco.setReturnCode(resp.getString("ReturnCode"));
					mco.setReturnMsg(resp.getString("ReturnMsg"));
					dao.update(mco);
					log.info("myCard orderId="+mco.getOrderId()+"请款成功");
				}else{
					log.info("myCard orderId="+mco.getOrderId()+"请款失败");
				}
			}
		}
		log.info("mycard请款结束");
		
	}
	
}
