/*
 * 
 */
package me.ckhd.opengame.countly.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.countly.entity.OrderForwardCountly;
import me.ckhd.opengame.countly.service.OrderForwardCountlyService;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * 下发countly
 * @author leo
 * @TIME 2016年2月24日
 *
 */
public class Send2CoutlyOrder implements Runnable{

	private final static Logger LOG = LoggerFactory.getLogger(Send2CoutlyOrder.class);
	
	private static String ROOT_NAME = "SyncAppOrderResp";
	
	private static String HRET_NAME = "hRet";
	
	private static String HRET_OK_VALUE = "0";
	
	private static String HRET_OK_VALUE_REPEAT = "9015"; // 重复订单

	// 间隔时间为：2 分钟,10 分钟,10分钟,1 小时,2 小时,6 小时,15 小时共 7 次。重发7次发送失败后，不再发起订单通知。(规则来源MM)
	private static Map<Integer,Integer> NUM_TIME = new HashMap<Integer,Integer>();
	
	static{
		NUM_TIME.put(1, 2);
		NUM_TIME.put(2, 10);
		NUM_TIME.put(3, 10);
		NUM_TIME.put(4, 60);
		NUM_TIME.put(5, 120);
		NUM_TIME.put(6, 360);
		NUM_TIME.put(7, 900);
	}
	
	private OrderForwardCountly orderForwardCountly;
	
	private OrderForwardCountlyService orderForwardCountlyService;
	
	public Send2CoutlyOrder(OrderForwardCountly orderForwardCountly){
		this.orderForwardCountly = orderForwardCountly;
		orderForwardCountlyService = SpringContextHolder.getBean(OrderForwardCountlyService.class);
	}
	
	@Override
	public void run() {
		LOG.info("content==="+orderForwardCountly.getContent());
		String serverUrl = Global.getConfig("COUNTLY_FORWARD_URL")+"/i/payevent?"+orderForwardCountly.getContent();
		boolean sucess = false;
		if(serverUrl != null && serverUrl.trim().length() != 0){
			String responseXml = HttpClientUtils.post(serverUrl, orderForwardCountly.getContent(), 2000, 2000);
			LOG.info("responseXml="+responseXml);
			sucess = validateResonseXml(responseXml);
		} else {
			LOG.error("serverUrl is null");
		}
		
		if(sucess){
			orderForwardCountlyService.updateSendSucess(orderForwardCountly.getId());
		}else{
			OrderForwardCountly vo = new OrderForwardCountly();
			vo.setId(orderForwardCountly.getId());
			int sendNum = orderForwardCountly.getSendNum();
			if(sendNum >= 7){
				vo.setStatus(2); // 2-7次重发失败
				vo.setSendNum(7);
			}else{
				vo.setStatus(1); // 1-等待重发或发送中
				vo.setSendNum(sendNum + 1);
				vo.setNextSendTime(calcNextSendTime(orderForwardCountly.getNextSendTime(),sendNum + 1));
			}
			orderForwardCountlyService.updateSendFail(vo);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private boolean validateResonseXml(String responseXml){
		
		return true;
	}
	
	public static Date calcNextSendTime(Date date,int sendNum){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, NUM_TIME.get(sendNum));
		return c.getTime();
	}
	
}
