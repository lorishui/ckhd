/*
 * 
 */
package me.ckhd.opengame.online.sendOrder.task;

import java.util.Map;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @author leo
 *
 */
public class SendOrder implements Runnable{

	private final static Logger LOG = LoggerFactory
			.getLogger(SendOrder.class);
	
	private OnlinePay onlinePay;
	
	private OnlineService onlineService;
	
	
	public SendOrder(OnlinePay onlinePay){
		this.onlinePay = onlinePay;
		onlineService = SpringContextHolder.getBean(OnlineService.class);
	}
	
	public void run() {
		try{
			OnlinePay vo = new OnlinePay();
			vo.setId(onlinePay.getId());
			vo.setSendStatus(OrderStatus.SEND_DOWN_ING); // 1-等待重发或发送中
			vo.setSendNum(onlinePay.getSendNum());
			onlineService.updateSendFail(vo);
			
			//lp 从订单表中获取 发货地址
			String serverUrl = onlinePay.getCallBackUrl();
			if(serverUrl==null || serverUrl.length() <=0){
				PayInfoConfig payInfo = new PayInfoConfig();
				payInfo.setCkAppId(onlinePay.getAppId());
				payInfo.setChannelId(onlinePay.getChannelId());
				payInfo.setPaytype(onlinePay.getPayType());
				payInfo.setCarrierAppId(onlinePay.getAppId());
				APPCk appCk =AppCkUtils.getAppCkByIdAndChild(onlinePay.getCkAppId(),onlinePay.getChildCkAppId());
				serverUrl=appCk.getPayCallbackUrl();
			}
	
			Map<String, Object> success = null;
			if(serverUrl != null && serverUrl.trim().length() != 0){
				String content = onlinePay.getContent();
				LOG.info("下发CP的数据为:"+content);
				String responseJson = "";
				try{
					responseJson = HttpClientUtils.post(serverUrl,content, 10000, 10000);
				}catch (RuntimeException e) {
					LOG.error("通知CP接口出错小：",e);
			    }catch (Exception e) {
					LOG.error("通知CP接口出错中：",e);
			    }catch(Throwable t){
					LOG.error("通知CP接口出错大：", t);
				}
				LOG.info("CP返回的数据为:"+responseJson);
				success = validateResonseJson(responseJson);
			} else {
				LOG.error("serverUrl is null");
			}
			
			if(success!=null){
				if("1".equals(success.get("resultCode").toString())){
					onlineService.updateSendSucess(onlinePay.getId());
				}else{
					String errMsg = success.get("errMsg")==null?"发货失败":success.get("errMsg").toString();
					onlinePay.setSendStatus(OrderStatus.DELIVER_GOODS_FAIL);
					onlinePay.setSendErrMsg(errMsg);
					onlineService.updateSendFail(onlinePay);
				}
			}else{
				vo.setSendStatus(OrderStatus.SEND_DOWN_FAIL); // 2-3次重发失败
				vo.setSendNum( onlinePay.getSendNum() + 1);
				onlineService.updateSendFail(vo);
			}
		}catch(Throwable e){
			LOG.error("发货失败！",e);
		}
	}
	
	private Map<String, Object> validateResonseJson(String responseJson){
		Map<String,Object> map=null;
		try {
			map = MyJsonUtils.jsonStr2Map(responseJson);
		} catch (Throwable e) {
			LOG.error(String.format("返回的数据有误!返回的数据是[%s]", responseJson));
		}
		return map;
	}
	
}
