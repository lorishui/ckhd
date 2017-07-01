/*
 * www.ckhd.me
 */
package me.ckhd.opengame.woapi.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.api.entity.AppOrderForward;
import me.ckhd.opengame.api.service.AppOrderForwardService;
import me.ckhd.opengame.api.task.OrderForwardBoot;
import me.ckhd.opengame.api.task.SendOrder;
import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCarriersUtils;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.ChannelCarriersUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.wo.PayCallBackResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.woapi.bean.WoCallBackReq;
import me.ckhd.opengame.woapi.bean.WoCallBackResp;
import me.ckhd.opengame.woapi.bean.WoValidateOrderResp;
import me.ckhd.opengame.woapi.entity.WoAppOrder;
import me.ckhd.opengame.woapi.service.WoAppOrderService;
import me.ckhd.opengame.woapi.util.MD5;
import me.ckhd.opengame.woapi.util.PayBeanUtils;

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
public class WoApiController {

	private static Logger log = LoggerFactory.getLogger(WoApiController.class);

	@Autowired
	private WoAppOrderService woAppOrderService;

	@Autowired
	private AppOrderForwardService appOrderForwardService;

	@Autowired
	private OnlineService onlineService;

	public WoAppOrderService getWoAppOrderService() {
		return woAppOrderService;
	}

	public void setWoAppOrderService(WoAppOrderService woAppOrderService) {
		this.woAppOrderService = woAppOrderService;
	}

	public AppOrderForwardService getAppOrderForwardService() {
		return appOrderForwardService;
	}

	public void setAppOrderForwardService(
			AppOrderForwardService appOrderForwardService) {
		this.appOrderForwardService = appOrderForwardService;
	}

	@RequestMapping(value = "woiap", method = RequestMethod.POST, produces = { "application/xml;charset=utf-8" })
	public void woiap(HttpServletRequest request, HttpServletResponse response) {
		String serviceid = request.getParameter("serviceid");
		request.getQueryString();
		String url = "/ckhd/woiapCallBack";
		if ("validateorderid".equals(serviceid)) {
			url = "/ckhd/woiapValidate?serviceid=" + serviceid;
		}
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * http://ip:port/cucc/woiap 支付回调
	 * 
	 * @param req
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "woiapCallBack", method = RequestMethod.POST, produces = { "application/xml;charset=utf-8" })
	@ResponseBody
	public String woiapCallBack(HttpServletRequest request, HttpServletResponse response) {
		//初始化返回对象
		WoCallBackResp resp = new WoCallBackResp();
		//获取request的body数据
		String res=getBodyStr(request);
		WoCallBackReq req=null;
		//判断是否获取到body数据,获取未空则返回
		if(StringUtils.isNotBlank(res)){
			req = new WoCallBackReq(res);
		}else{
			resp.setCallbackRsp(0);
			return PayBeanUtils.toPaynotifyResponse(0);
		}
		
		try {
			
			//获取woapporder数据
			WoAppOrder woAppOrder = req.genEntity();

			//判断订单数据是否存在存在则表示已经处理,则不再处理,防止多次通知
			if (woAppOrderService.isExistByOrderId(woAppOrder)) {
				log.info("联通强联网--此订单已处理!");
				resp.setCallbackRsp(0);
				return PayBeanUtils.toPaynotifyResponse(0);
			}
			
			woAppOrderService.save(woAppOrder);

			//获取订单信息
			OnlinePay onlinePay =  onlineService.getOrderByOrderId(woAppOrder.getOrderid().substring(8));
			//判断是否存在订单信息,无则直接返回
			if(onlinePay==null){
				log.info("联通强联网--未查询到相关订单数据!");
				resp.setCallbackRsp(0);
				return PayBeanUtils.toPaynotifyResponse(0);
			}
			
			PayInfoConfig payInfoConfig = getPayInfo(onlinePay);
			String key=null;
			if(payInfoConfig.getExInfoMap()!=null){
				key=payInfoConfig.getExInfoMap().containsKey("key")?payInfoConfig.getExInfoMap().get("key").toString():"";
			}
			
			//签名 MD5(orderid=XXX&ordertime=XXX&cpid=XXX&appid=XXX&fid=XXX&consumeCode=XXX&payfee=XXX&payType=XXX&hRet=XXX&status=XXX&Key=XXX)
	        String signMsg = woAppOrder.getSignMsg();
	        
	       //校验签名是否正确
	        String signPattern = "orderid=%s&ordertime=%s&cpid=%s&appid=%s&fid=%s&consumeCode=%s&payfee=%s&payType=%s&hRet=%s&status=%s&Key=%s";
	        String sign = String.format(signPattern, woAppOrder.getOrderid(),woAppOrder.getOrdertime(),woAppOrder.getCpid(),woAppOrder.getAppid(), woAppOrder.getFid(),woAppOrder.getConsumeCode(), woAppOrder.getPayfee(), woAppOrder.getPayType(), woAppOrder.gethRet(), woAppOrder.getStatus(), key);
	        String mySign = MD5.MD5Encode(sign);
	        //校验签名是否一致
	        if (mySign.equalsIgnoreCase(signMsg)) {
				// 完成
				resp.setCallbackRsp(1);
				String serverUrl = AppCarriersUtils.getServerUrlByAppId("CMPay_"
						+ woAppOrder.getAppid());
				if (serverUrl != null && serverUrl.trim().length() != 0) {
					AppOrderForward appOrderForward = new AppOrderForward();
					appOrderForward.setAppId(woAppOrder.getAppid());
					appOrderForward.setOrderId(woAppOrder.getOrderid());
					appOrderForward.setOrderType("WO");
					appOrderForward.setSendNum(0);
					appOrderForward.setNextSendTime(SendOrder.calcNextSendTime(
							new Date(), 1));
					appOrderForward.setContent("");
					appOrderForward.setStatus(1);
	
					appOrderForwardService.save(appOrderForward);
	
					OrderForwardBoot.getInstance().add(appOrderForward);
				}
	
				APPCk appCk = AppCkUtils.getAppCkById(onlinePay.getCkAppId());
				String onlinePayUrl = null;
				if (appCk != null) {
					onlinePayUrl = appCk.getPayCallbackUrl();
				}
				if (onlinePayUrl != null && onlinePayUrl.trim().length() > 0) {
					// wo网游,根据透传参数，load OnlinePay。更新，支付
					onlinePay.setSercetKey(appCk.getSecretKey());
					//判断支付是否成功
					if (!"00000".equals(woAppOrder.getStatus())) {
						onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
						onlinePay.setErrMsg(woAppOrder.getStatus());
						onlinePay.setChannelOrderId(woAppOrder.getOrderid());
					} else {
						onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						onlinePay.setErrMsg("");
						onlinePay.setChannelOrderId(woAppOrder.getOrderid());
						onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
						onlinePay.setSendNum(0);
						PayCallBackResponse wopayCallBackResponse = new PayCallBackResponse(onlinePay);
						onlinePay.setContent(wopayCallBackResponse.getSendOrder().get("content").toString());
						OrderSenderBoot.getInstance().add(onlinePay);
					}
					onlineService.savePayInfo(onlinePay);
				}
	        }else{
	        	log.info("联通支付回调....校验失败");
	        	resp.setCallbackRsp(0);
	        	return PayBeanUtils.toPaynotifyResponse(0);
	        }
		} catch (Exception e) {
			log.error("error", e);
			e.printStackTrace();
			log.info("联通支付回调....解析失败!");
			return PayBeanUtils.toPaynotifyResponse(0);
		}
		
		return PayBeanUtils.toPaynotifyResponse(resp.getCallbackRsp());
	}

	/**
	 * http://ip:port/ckhd/woiapValidate 支付校验
	 * 
	 * @param req
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "woiapValidate", method = RequestMethod.POST, produces = { "application/xml;charset=utf-8" })
	@ResponseBody
	public WoValidateOrderResp woiapValidate(HttpServletRequest request,
			HttpServletResponse response) {
		WoValidateOrderResp resp = new WoValidateOrderResp();
		try {
			//解析http请求体
	        Map<String, String> params = PayBeanUtils.parse(request.getInputStream());
	        
	        //cp订单号
	        String orderid = params.get("orderid");
	        //签名 MD5(orderid=XXX&Key=XXX)
	        String signMsg = params.get("signMsg");
	       
	        OnlinePay onlinePay = onlineService.getOrderByOrderId(orderid.substring(8));
	        
	        if (onlinePay == null) {
	        	log.info("联通强联网,未查询到订单数据,暂时不做处理");
				resp.setCheckOrderIdRsp(1);
				return resp;
			}
	        
	        PayInfoConfig loginInfo =  getPayInfo(onlinePay);
	        
	        String key =null;
	        if(loginInfo.getExInfoMap()!=null){
	        	key = loginInfo.getExInfoMap().containsKey("key")?loginInfo.getExInfoMap().get("key").toString():"";
	        }else{
	        	log.info("联通强联网,未配置key数据");
	        	resp.setCheckOrderIdRsp(1);
				return resp;
	        }
	        
	        //校验签名是否正确
	        String sign = String.format("orderid=%s&Key=%s", orderid, key);
	        String mySign = MD5.MD5Encode(sign);
	        if (mySign.equalsIgnoreCase(signMsg)) {
	        	resp.setCheckOrderIdRsp(0);
	        	String appid =  AppCarriersUtils.getAppIdByCkAppId(onlinePay.getCkAppId());
	        	String carrierChannelId = ChannelCarriersUtils.getAppIdByCkAppId(onlinePay.getChannelId());
	        	//获取支付时客户上传的数据
				Map<String, Object> map = MyJsonUtils.jsonStr2Map(onlinePay.getAppPayContent());
				if(map!=null){
					resp.setAppversion(map.containsKey("appVersion") ?map.get("appVersion").toString():"");
					resp.setChannelid(carrierChannelId);
					resp.setCpid(appid);
					resp.setImei(map.containsKey("imei") ? map.get("imei").toString():"");
					String ipAddress = map.containsKey("ipaddress") ? map.get(
							"ipaddress").toString():"";
					resp.setIpaddress(StringUtils.isNotBlank(ipAddress) ? null
							: genIpAddress(ipAddress));
					resp.setMacaddress(map.containsKey("macAddress") ?map.get(
							"macAddress").toString(): "" );
					resp.setOrdertime(DateUtils.formatDate(onlinePay.getCreateDate(),"yyyyMMddhhmmss"));
				}
	        }else{
	        	log.info("联通强联网,验证失败!");
	        	resp.setCheckOrderIdRsp(1);
	        }
		} catch (Exception e) {
			log.info("联通强联网,解析/组装数据有误!");
			e.printStackTrace();
			resp.setCheckOrderIdRsp(1);
		}
		return resp;
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
	
	public static String getBodyStr(HttpServletRequest request){
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
	}
	
	
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