/*
 * 
 */
package me.ckhd.opengame.api.task;

import java.io.StringReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.api.service.AppOrderForwardService;
import me.ckhd.opengame.app.utils.AppCarriersUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.util.HttpClientUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author qibiao
 *
 */
public class SendOrder implements Runnable{

	private final static Logger LOG = LoggerFactory
			.getLogger(SendOrder.class);
	
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
	
	private AppOrderForward appOrderForward;
	
	private AppOrderForwardService appOrderForwardService;
	
	public SendOrder(AppOrderForward appOrderForward){
		this.appOrderForward = appOrderForward;
		appOrderForwardService = SpringContextHolder.getBean(AppOrderForwardService.class);
	}
	
	@Override
	public void run() {
		String serverUrl = AppCarriersUtils.getServerUrlByAppId(appOrderForward.getOrderType() + "_" + appOrderForward.getAppId());
		boolean sucess = false;
		if(serverUrl != null && serverUrl.trim().length() != 0){
			String responseXml = HttpClientUtils.post(serverUrl, appOrderForward.getContent(), 2000, 2000);
			if(responseXml != null){
				sucess = validateResonseXml(responseXml);
			}
		} else {
			LOG.error("serverUrl is null");
		}
		
		if(sucess){
			appOrderForwardService.updateSendSucess(appOrderForward.getId());
		}else{
			AppOrderForward vo = new AppOrderForward();
			vo.setId(appOrderForward.getId());
			int sendNum = appOrderForward.getSendNum();
			if(sendNum >= 7){
				vo.setStatus(2); // 2-7次重发失败
				vo.setSendNum(7);
			}else{
				vo.setStatus(1); // 1-等待重发或发送中
				vo.setSendNum(sendNum + 1);
				vo.setNextSendTime(calcNextSendTime(appOrderForward.getNextSendTime(),sendNum + 1));
			}
			appOrderForwardService.updateSendFail(vo);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private boolean validateResonseXml(String responseXml){

		SAXReader saxReader = new SAXReader();

		Document document;
		try {
			document = saxReader.read(new StringReader(responseXml));
			// 获取根元素
			Element root = document.getRootElement();
			if (ROOT_NAME.equals(root.getName())) {
				List hrets = root.elements(HRET_NAME);
				if (hrets != null && hrets.size() > 0) {
					Node hret = (Node) hrets.get(0);
					String text = hret.getText();
					if (text != null && (HRET_OK_VALUE.equals(text) || HRET_OK_VALUE_REPEAT.equals(text))) {
						// 成功接收
						return true;
					}
				}
			}
			LOG.warn("This is fail responseXml:" + responseXml);
		} catch (DocumentException e) {
			LOG.error("Order.run() DocumentException", e);
		}
		return false;
	}
	
	public static Date calcNextSendTime(Date date,int sendNum){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, NUM_TIME.get(sendNum));
		return c.getTime();
	}
	
}
