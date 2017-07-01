/*
 * www.ckhd.me
 */
package me.ckhd.opengame.mmapi.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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
import me.ckhd.opengame.mmapi.bean.SyncAppOrderReq;
import me.ckhd.opengame.mmapi.bean.SyncAppOrderResp;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.mmapi.service.MmAppOrderService;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.mm.PayCallBackResponse;
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
 * MMIAP回调接口
 * 
 * @author qibiao
 */
@Controller
@RequestMapping("ckhd")
public class MMIAPApiController {

	private static Logger log = LoggerFactory.getLogger(MMIAPApiController.class);

	@Autowired
	private MmAppOrderService mmAppOrderService;
	
	@Autowired
	private AppOrderForwardService appOrderForwardService;
	
	@Autowired
	private OnlineService onlineService;
	
	public MmAppOrderService getMmAppOrderService() {
		return mmAppOrderService;
	}

	public void setMmAppOrderService(MmAppOrderService mmAppOrderService) {
		this.mmAppOrderService = mmAppOrderService;
	}

	public AppOrderForwardService getAppOrderForwardService() {
		return appOrderForwardService;
	}

	public void setAppOrderForwardService(
			AppOrderForwardService appOrderForwardService) {
		this.appOrderForwardService = appOrderForwardService;
	}

	/**
	 * http://ip:port/cmcc/mmiap
	 * @param req
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "mmiap", method = RequestMethod.POST,produces={"application/xml;charset=utf-8"})
	@ResponseBody
	public SyncAppOrderResp mmiap(@RequestBody SyncAppOrderReq req,HttpServletRequest request) {
		
		String orderStr = (String)request.getAttribute("POST_STR");
		log.info("mmiap request:" + orderStr);
		
		if (req == null) {
			SyncAppOrderResp resp = new SyncAppOrderResp();
			resp.sethRet(-1);
			return resp;
		}
		SyncAppOrderResp resp = new SyncAppOrderResp();
		try {
			resp.setTransactionID(req.getTransactionId());
			resp.setMsgType(req.getMsgType());
			resp.setVersion(req.getVersion());
			
			MmAppOrder mmAppOrder = req.genEntity();
			
			if(mmAppOrderService.isExistByTransactionId(mmAppOrder)){
				resp.sethRet(0);
				return resp;
			}
			//生成随机数
			mmAppOrder.setRandom(RandomUtils.nextInt(100));
			
			mmAppOrderService.save(mmAppOrder);

			// 完成
			resp.sethRet(0);
			
			String serverUrl = AppCarriersUtils.getServerUrlByAppId("MM_" + mmAppOrder.getAppId());
			if( serverUrl != null && serverUrl.trim().length() != 0){
				AppOrderForward appOrderForward = new AppOrderForward();
				appOrderForward.setAppId(req.getAppID());
				appOrderForward.setOrderId(mmAppOrder.getId());
				appOrderForward.setOrderType("MM");
				appOrderForward.setSendNum(0);
				appOrderForward.setNextSendTime(SendOrder.calcNextSendTime(new Date(), 1));
				appOrderForward.setContent(orderStr);
				appOrderForward.setStatus(1);
				
				appOrderForwardService.save(appOrderForward);
				
				OrderForwardBoot.getInstance().add(appOrderForward);
			}
			
			AppCarriers vo = AppCarriersUtils.getAppCarriers("MM_" + mmAppOrder.getAppId());
			// 按计费点转发
			if( vo != null && vo.getFlowServerUrl() != null && vo.getFlowServerUrl().trim().length() >0 ){
				if(mmAppOrder.getPaycode() != null || vo.getPaycodes()!= null && vo.getPaycodes().trim().equals(mmAppOrder.getPaycode())){
					FlowOrderForward flowOrderForward = new FlowOrderForward();
					flowOrderForward.setContent(orderStr);
					flowOrderForward.setUrl(vo.getFlowServerUrl().trim());
					FlowForwardBoot.getInstance().add(flowOrderForward);
				}
			}
			
			// 按渠道转发，配置格式例子：url1@2200165911,2200127261;url2@2200192421
			String forwardByChannel = vo.getForwardByChannel();
			if (forwardByChannel != null && forwardByChannel.trim().length() >0) {
				forwardByChannel = forwardByChannel.trim();
				try {
					String[] forwardItems = forwardByChannel.split(";");
					for (String forwardItem : forwardItems) {
						String[] datas = forwardItem.split("@");
						if (datas[0] != null
								&& datas[1] != null
								&& mmAppOrder.getChannelId() != null
								&& ("ALL".equals(datas[1]) || datas[1].indexOf(mmAppOrder.getChannelId()) >= 0)) {
							FlowOrderForward flowOrderForward = new FlowOrderForward();
							flowOrderForward.setContent(orderStr);
							flowOrderForward.setUrl(datas[0]);
							FlowForwardBoot.getInstance().add(flowOrderForward);
						}
					}
				} catch (Throwable t) {
					//
					log.error("forwardByChannel参数配置错误，请修改", t);
				}
			}
			
			String ckappid = AppCarriersUtils.getCkAppIdByAppId(mmAppOrder.getAppId());
			
			APPCk appCk = AppCkUtils.getAppCkById(ckappid);
			String onlinePayUrl = null;
			if(appCk != null){
				onlinePayUrl = appCk.getPayCallbackUrl();
			}
			if (onlinePayUrl != null && onlinePayUrl.trim().length() > 0) {
				// mm网游,根据透传参数，load OnlinePay。更新，支付
				OnlinePay onlinePay = onlineService
						.getOrderByOrderId(mmAppOrder.getExtendData());
				if(onlinePay != null){	// rd的是null的
					onlinePay.setSercetKey(appCk.getSecretKey());
					if ("00000000000000000000".equals(mmAppOrder.getOrderId())) {
						onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
						onlinePay.setErrMsg(mmAppOrder.getReturnStatus());
						onlinePay.setChannelOrderId("00000000000000000000");
					} else {
						onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						onlinePay.setErrMsg("");
						onlinePay.setChannelOrderId(mmAppOrder.getOrderId());
						onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
						onlinePay.setSendNum(0);
					}
	
					PayCallBackResponse mmpayCallBackResponse = new PayCallBackResponse(
							onlinePay);
					onlinePay.setContent(mmpayCallBackResponse.getSendOrder()
							.get("content").toString());
					onlineService.savePayInfo(onlinePay);
					OrderSenderBoot.getInstance().add(onlinePay);
				}
			}

		} catch (Exception e) {
			log.error("error" , e);
			resp.sethRet(-1);
		}
		
		return resp;
	}
	
}