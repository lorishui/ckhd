/*
 * www.ckhd.me
 */
package me.ckhd.opengame.egameapi.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.api.service.AppOrderForwardService;
import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.egameapi.entity.EGameAppOrder;
import me.ckhd.opengame.egameapi.service.EGameAppOrderService;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.weixin.net.sourceforge.simcpux.MD5Util;
import me.ckhd.opengame.online.response.egame.PayCallBackResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 联通沃商店回调接口
 * 
 * @author leo
 */
@Controller
@RequestMapping("ckhd")
public class EGameApiController {
	private static Logger log = LoggerFactory.getLogger(EGameApiController.class);
	
	@Autowired
	private EGameAppOrderService eGameAppOrderService;

	@Autowired
	private AppOrderForwardService appOrderForwardService;

	@Autowired
	private OnlineService onlineService;

	public EGameAppOrderService geteGameAppOrderService() {
		return eGameAppOrderService;
	}

	public void seteGameAppOrderService(EGameAppOrderService eGameAppOrderService) {
		this.eGameAppOrderService = eGameAppOrderService;
	}

	public AppOrderForwardService getAppOrderForwardService() {
		return appOrderForwardService;
	}

	public void setAppOrderForwardService(
			AppOrderForwardService appOrderForwardService) {
		this.appOrderForwardService = appOrderForwardService;
	}

	/**
	 * http://ip:port/cucc/eGameapiCallBack 支付回调
	 * 
	 * @param req
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "eGameapiCallBack", method = RequestMethod.POST)
	@ResponseBody
	public String woiapCallBack(HttpServletRequest request, HttpServletResponse response) {
		//获取爱游戏POST过来反馈信息 格式:form-data
//		map = XmlUtils.decodeXml(_code);	
		Map<String,String> map = getRequestMap(request);
		String orderId = map.get("cp_order_id") != null?(String)map.get("cp_order_id"):"";
		//判断map
		if( map.size() > 0 ){
			//获取订单信息
			OnlinePay onlinePay =  onlineService.getOrderByOrderId(orderId);
			if( null != onlinePay && !onlinePay.getOrderStatus().equals(OrderStatus.PAY_SUCCESS)){
				//获取渠道配置信息
				PayInfoConfig payInfoConfig = getPayInfo(onlinePay);
				if( null != payInfoConfig && null != payInfoConfig.getAppkey() ){
					if( null != map.get("method") && "check".equals(map.get("method"))){
						//MD5(cp_order_id+correlator+order_time+method+appKey) check IF1
						String sign = map.get("sign")==null?"":(String)map.get("sign");
						log.info("woaiyouxi client IF1 sign:"+sign);
						StringBuffer sb = getSignText(map, payInfoConfig);
						String signNew = MD5Util.MD5Encode(sb.toString(), "utf-8");
						log.info("aiyouxi sign IF1 text:"+sb.toString());
						if(sign.equals(signNew)){
							return getResponseSuccessData(map);
						}
					}else if( null != map.get("method") && "callback".equals(map.get("method"))){
						//MD5(cp_order_id+correlator+result_code+fee+paytype+method+appKey) callbac IF2
						if( null != map.get("result_code") && "00".equals(map.get("result_code"))){
							String sign = map.get("sign")==null?"":(String)map.get("sign");
							log.info("aiyouxi client IF2 sign:"+sign);
							StringBuffer sb = getSignText(map, payInfoConfig);
							String signNew = MD5Util.MD5Encode(sb.toString(), "utf-8");
							log.info("aiyouxi sign IF2 text:" + sb.toString() );
							if(sign.equals(signNew)){
								/*EGameAppOrder eg = new EGameAppOrder();
								eg.setCkapp_id(onlinePay.getCkAppId());
								eg.setCp_order_id(map.get("cp_order_id"));
								if(!eGameAppOrderService.isExistByOrderId(eg)){
									eGameAppOrderService.save(fillEntity(map,onlinePay.getCkAppId()));
								}*/
								syncGameServer(map, onlinePay,payInfoConfig);
								return getResponseSuccessData(map);
							}else{
								log.info("callback egame verification failed");
								if(!onlinePay.getOrderStatus().equals(OrderStatus.PAY_FAIL)){
									// egame网游,更新，支付
									onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
									onlinePay.setErrMsg("验签失败");
									onlinePay.setChannelOrderId(map.get("cp_order_id"));
									onlineService.savePayInfo(onlinePay);
								}
							}
						}else{
							log.info("callback egame result_code is not 00,that is !"+map.get("result_code"));
							if(!onlinePay.getOrderStatus().equals(OrderStatus.PAY_FAIL)){
								// egame网游,更新，支付
								onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
								onlinePay.setErrMsg("支付失败的订单");
								onlinePay.setChannelOrderId(map.get("cp_order_id"));
								onlineService.savePayInfo(onlinePay);
							}
						}
					}
				}
				
			}
		}else{
			log.info("callback egame is not data body!");
		}
		return getResponseFailData(map);
	}

	/**
	 * 同步订单到游戏
	 * @param map
	 * @param onlinePay
	 * @param payInfoConfig
	 */
	private void syncGameServer(Map<String, String> map, OnlinePay onlinePay,
			PayInfoConfig payInfoConfig) {
		//网游同步
		APPCk appCk = AppCkUtils.getAppCkById(onlinePay.getCkAppId());
		String onlinePayUrl = null;
		if (appCk != null) {
			onlinePayUrl = appCk.getPayCallbackUrl();
		}
		onlinePay.setSercetKey(appCk.getSecretKey());
		onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
		onlinePay.setErrMsg("");
		onlinePay.setChannelOrderId(map.get("correlator"));
		onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
		onlinePay.setSendNum(0);
		PayCallBackResponse egamepayCallBackResponse = new PayCallBackResponse(onlinePay);
		onlinePay.setContent(egamepayCallBackResponse.getSendOrder().get("content").toString());
		onlineService.savePayInfo(onlinePay);
		if(onlinePayUrl != null && onlinePayUrl.trim().length() > 0){
			OrderSenderBoot.getInstance().add(onlinePay);
		}
	}
	
	private StringBuffer getSignText(Map<String, String> map,
			PayInfoConfig payInfoConfig) {
		StringBuffer sb = new StringBuffer();
		if( null != map.get("cp_order_id")){
			sb.append(map.get("cp_order_id"));
		}
		if( null != map.get("correlator")){
			sb.append(map.get("correlator"));
		}
		//IF1
		if( null != map.get("order_time")){
			sb.append(map.get("order_time"));
		}
		//F2				 result_code
		if( null != map.get("result_code")){
			sb.append(map.get("result_code"));
		}
		if( null != map.get("fee")){
			sb.append(map.get("fee"));
		}
		if( null != map.get("pay_type")){
			sb.append(map.get("pay_type"));
		}
		//end
		
		if( null != map.get("method")){
			sb.append(map.get("method"));
		}
		sb.append(payInfoConfig.getAppkey());
		return sb;
	}

	private EGameAppOrder fillEntity(Map<String, String> map,String ckapp_id) {
		EGameAppOrder egame = new EGameAppOrder();
		if( null != map.get("cp_order_id")){
			egame.setCp_order_id(map.get("cp_order_id"));
		}
		if( null != map.get("correlator")){
			egame.setCorrelator(map.get("correlator"));
		}
		//IF1
		if( null != map.get("order_time")){
			egame.setOrder_time(map.get("order_time"));
		}
		//F2
		if( null != map.get("result_code")){
			egame.setResult_code(map.get("result_code"));
		}
		if( null != map.get("fee")){
			egame.setFee(Integer.parseInt(map.get("fee")));
		}
		if( null != map.get("pay_type")){
			egame.setPay_type(map.get("pay_type"));
		}
		//end
		
		if( null != map.get("method")){
			egame.setMethod(map.get("method"));
		}
		
		if( null != ckapp_id){
			egame.setCkapp_id(ckapp_id);
		}
		return egame;
	}
	
	/**
	 * 获取resuest form-data 数据 
	 * @param request
	 */
	private Map<String,String> getRequestMap(HttpServletRequest request) {
		Map requestParams = request.getParameterMap();
		Map<String,String> data = new HashMap<String, String>();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			if( null != values && values.length > 0) valueStr = values[0];
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			data.put(name, valueStr);
		}
		return data;
	}

	/**
	 * Response SUCCESS 的响应数据
	 */
	private String getResponseSuccessData(Map<String,String> map) {
		StringBuffer sb = new StringBuffer();
		if( null != map.get("method") && "check".equals(map.get("method"))){
			sb.append("<sms_pay_check_resp>")
			.append("<cp_order_id>").append(map.get("cp_order_id")).append("</cp_order_id>")
			.append("<correlator>").append(map.get("correlator")).append("</correlator>")
			.append("<game_account>").append(map.get("game_account")).append("</game_account>")
			.append("<fee>").append(map.get("fee")).append("</fee>")
			.append("<if_pay>").append(0).append("</if_pay>")
			.append("<order_time>").append(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")).append("</order_time>")
			.append("</sms_pay_check_resp>");
		}else if(null != map.get("method") && "callback".equals(map.get("method"))){
			sb.append("<cp_notify_resp>")
			.append("<h_ret>").append(0).append("</h_ret>")
			.append("<cp_order_id>").append(map.get("cp_order_id")).append("</cp_order_id>")
			.append("</cp_notify_resp>");
		}
		return sb.toString();
	}
	
	/**
	 * Response FAIL 的响应数据
	 */
	private String getResponseFailData(Map<String,String> map) {
		StringBuffer sb = new StringBuffer();
		if( null != map.get("method") && "check".equals(map.get("method"))){
			sb.append("<sms_pay_check_resp>")
			.append("<cp_order_id>").append(map.get("cp_order_id")).append("</cp_order_id>")
			.append("<correlator>").append(map.get("correlator")).append("</correlator>")
			.append("<game_account>").append(map.get("game_account")).append("</game_account>")
			.append("<fee>").append(map.get("fee")).append("</fee>")
			.append("<if_pay>").append(1).append("</if_pay>")
			.append("<order_time>").append(DateUtils.formatDate(new Date(), "yyyyMMddHHmmss")).append("</order_time>")
			.append("</sms_pay_check_resp>");
		}else if(null != map.get("method") && "callback".equals(map.get("method"))){
			sb.append("<cp_notify_resp>")
			.append("<h_ret>").append(1).append("</h_ret>")
			.append("<cp_order_id>").append(map.get("cp_order_id")).append("</cp_order_id>")
			.append("</cp_notify_resp>");
		}
		return sb.toString();
	}

	/**
	 * 获取去.补零的ip地址
	 * 
	 * @param ipAddress
	 * @return
	 */
	public String genIpAddress(String ipAddress) {
		String ip = ipAddress;
		if (StringUtils.isNotBlank(ipAddress)) {
			ip = "";
			String[] ipstr = ipAddress.split("\\.");
			for (String str : ipstr) {
				ip += String.format("%03d", Integer.valueOf(str));
			}
		}
		return ip;
	}

	
	public PayInfoConfig getPayInfo(OnlinePay onlinePay) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());
		payInfo.setChannelId(onlinePay.getChannelId());
		payInfo.setCarrierAppId(onlinePay.getAppId());
		payInfo.setPaytype(onlinePay.getPayType());
		return  onlineService.getPayInfo(payInfo);
	}
	
	/*public static String getBodyStr(HttpServletRequest request){
		String res=null;
		try {
			request.setCharacterEncoding("UTF-8");
			int size = request.getContentLength();
			InputStream is = request.getInputStream();
			byte[] reqBodyBytes = readBytes(is, size);
			res = new String(reqBodyBytes);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}*/
	
	
	
	
	public static final byte[] readBytes(InputStream is, int contentLen) {
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen
							- readLen);
					if (readLengthThisTime == -1) {// Should not happen.
						break;
					}
					readLen += readLengthThisTime;
				}
				return message;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new byte[] {};
	}
}