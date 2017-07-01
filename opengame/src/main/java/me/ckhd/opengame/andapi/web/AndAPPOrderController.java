package me.ckhd.opengame.andapi.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.andapi.bean.OrderRequest;
import me.ckhd.opengame.andapi.bean.OrderResponse;
import me.ckhd.opengame.andapi.entity.AndAPPOrder;
import me.ckhd.opengame.andapi.service.AndAPPOrderService;
import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.api.entity.FlowOrderForward;
import me.ckhd.opengame.api.service.AppOrderForwardService;
import me.ckhd.opengame.api.task.FlowForwardBoot;
import me.ckhd.opengame.api.task.OrderForwardBoot;
import me.ckhd.opengame.api.task.SendOrder;
import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.AppCarriers;
import me.ckhd.opengame.app.utils.AppCarriersUtils;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.andgame.PayCallBackResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 和游戏订单接口    - Controller
 * @author wesley
 * @version 2015-06-29
 */
@Controller
@RequestMapping("ckhd")
public class AndAPPOrderController extends  BaseController{
	private static Logger log = LoggerFactory.getLogger(AndAPPOrderController.class);
	@Autowired
	private AndAPPOrderService andAPPOrderService;
	@Autowired
	private AppOrderForwardService appOrderForwardService;
	 
	@Autowired
	private OnlineService onlineService;
	
	@RequestMapping(value = "andiap", method = RequestMethod.POST)
	@ResponseBody
	public String andiap(@RequestBody OrderRequest request,HttpServletRequest httpServletRequest) {
		
		String orderStr = (String)httpServletRequest.getAttribute("POST_STR");
		log.info("andiap request:" + orderStr);
		OrderResponse resp = null;
		if (request == null) {
			resp = new OrderResponse();
			resp.sethRet(-1);
			return resp.toPaynotifyResponse();
		}
		
		resp = new OrderResponse();
		try {
			AndAPPOrder andAPPOrder = request.genEntity();
			//随机数添加
			andAPPOrder.setRandom(RandomUtils.nextInt(100));
			andAPPOrderService.save(andAPPOrder);
			//resp.setTransactionID(request.getTransactionId());
			// 成功完成
			resp.sethRet(0);
			resp.setMessage(OrderResponse.SUCCESS_MSG);
			
			String serverUrl = AppCarriersUtils.getServerUrlByAppId("ANDGAME_" + andAPPOrder.getContentId());
			if( serverUrl != null && serverUrl.trim().length() != 0){
				AppOrderForward appOrderForward = new AppOrderForward();
				appOrderForward.setAppId(request.getContentId());
				appOrderForward.setOrderId(andAPPOrder.getId());
				appOrderForward.setOrderType("ANDGAME");
				appOrderForward.setSendNum(0);
				appOrderForward.setNextSendTime(SendOrder.calcNextSendTime(new Date(), 1));
				appOrderForward.setContent(orderStr);
				appOrderForward.setStatus(1);
				
				appOrderForwardService.save(appOrderForward);
				
				OrderForwardBoot.getInstance().add(appOrderForward);
			}
			
			String ckappid = AppCarriersUtils.getCkAppIdByAppId(andAPPOrder.getContentId());
			
			APPCk appCk = AppCkUtils.getAppCkById(ckappid);
			String onlinePayUrl = null;
			if(appCk != null){
				onlinePayUrl = appCk.getPayCallbackUrl();				
			}
			log.info("onlinePayUrl = " + onlinePayUrl);
			if ("0".equals(andAPPOrder.gethRet()) && onlinePayUrl != null && onlinePayUrl.trim().length() > 0) {
				// andgame网游,根据透传参数，load OnlinePay。更新，支付
				OnlinePay onlinePay = onlineService.getOrderByOrderId(andAPPOrder.getCpparam());
				if(onlinePay != null){
					onlinePay.setSercetKey(appCk.getSecretKey());
					if (!"0".equals(andAPPOrder.gethRet())) {
						onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
						onlinePay.setErrMsg(andAPPOrder.getStatus());
						onlinePay.setChannelOrderId(andAPPOrder.getCpparam());
					} else {
						onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						onlinePay.setErrMsg("");
						onlinePay.setChannelOrderId(andAPPOrder.getId());
						onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
						onlinePay.setSendNum(0);
					}
	
					PayCallBackResponse andgamepayCallBackResponse = new PayCallBackResponse(onlinePay);
					onlinePay.setContent(andgamepayCallBackResponse.getSendOrder()
							.get("content").toString());
					log.info("content = " + onlinePay.getContent());
					onlineService.savePayInfo(onlinePay);
					if("0".equals(andAPPOrder.gethRet())){
						OrderSenderBoot.getInstance().add(onlinePay);
					}
				}
			}
			
			AppCarriers vo = AppCarriersUtils.getAppCarriers("ANDGAME_"
					+ andAPPOrder.getContentId());
			// 按渠道转发，配置格式例子：url1@2200165911,2200127261;url2@2200192421
			if (vo != null) {
				String forwardByChannel = vo.getForwardByChannel();
				if (forwardByChannel != null
						&& forwardByChannel.trim().length() > 0) {
					forwardByChannel = forwardByChannel.trim();
					try {
						String[] forwardItems = forwardByChannel.split(";");
						for (String forwardItem : forwardItems) {
							String[] datas = forwardItem.split("@");
							if (datas[0] != null
									&& datas[1] != null
									&& andAPPOrder.getChannelId() != null
									&& ("ALL".equals(datas[1]) || datas[1]
											.indexOf(andAPPOrder.getChannelId()) >= 0)) {
								FlowOrderForward flowOrderForward = new FlowOrderForward();
								flowOrderForward.setContent(orderStr);
								flowOrderForward.setUrl(datas[0]);
								FlowForwardBoot.getInstance().add(
										flowOrderForward);
							}
						}
					} catch (Throwable t) {
						//
						log.error("forwardByChannel参数配置错误，请修改", t);
					}
				}
			}
			//发送到countly后台
			//OrderForwardCountlyBoot.getInstance().add(andAPPOrder);
			
		} catch (Exception e) {
			logger.error("error" , e);
			resp.setMessage(e.getMessage());
			resp.sethRet(-1);
		}
		return resp.toPaynotifyResponse();
	}
}