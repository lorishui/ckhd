package me.ckhd.opengame.online.task;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.online.util.XmlUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 与SDK服务端对接
 * @author leo
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/online/channel")
public class ChannelPayApi extends BaseController {
	
	private String paycallbackpackagerequest="me.ckhd.opengame.online.request.%s.PayCallBackRequest";
	private String paycallbackpackageresponse="me.ckhd.opengame.online.response.%s.PayCallBackResponse";

	
	//private String loginCallbackpackagerequest="me.ckhd.opengame.online.request.%s.LoginCallBackRequest";
	//private String loginCallbackpackageresponse="me.ckhd.opengame.online.response.%s.LoginCallBackResponse";

	@Autowired
	private OnlineService onlineService;
	/**
	 * 前端调用后台验证订单状态
	 * @return
	 */
	@RequestMapping(value = "payValidate/{name}")
	@ResponseBody
	public void payValidate(@RequestBody String payCode, @PathVariable String name){
		
	}
	
	/**
	 * 回调转发,为了解决类似于优酷不能传递带参数的回调地址
	 * @param request
	 * @param response
	 * @param channelName
	 * @param ckappid
	 * @param channelid
	 */
	@RequestMapping(value = "callBack/{channelName}/{ckappid}/{channelid}")
	public void doPost(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String channelName,
			@PathVariable String ckappid, @PathVariable String channelid) {
		try {
			String url = "/ck/online/channel/payCallBack/"+channelName+"?ckappid="+ckappid+"&channelid="+channelid+"";
			request.getRequestDispatcher(url).forward(request, response);
		}  catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 最新的回掉接口
	 * @param request
	 * @param response
	 * @param channelName
	 * @param ckappid
	 * @param channelid
	 */
	@RequestMapping(value = "callBack/commonChannel/{sdkName}/{ckappid}/{channelid}/")
	@ResponseBody
	public String doPostNew(HttpServletRequest request,HttpServletResponse response,@RequestBody String payCode,
			@PathVariable String ckappid, @PathVariable String channelid ,@PathVariable String sdkName) {
		logger.info(String.format("渠道回调信息%s:[%s]",sdkName , payCode));
		String resultStr = "";
		try {
			PayInfoConfig payInfoConfig=null;
			BasePayCallBackRequest baseRequest= getRequest(sdkName, payCode,request);
			
			//1.获取到客户端提供的参数			
			PayInfoConfig payInfo = new PayInfoConfig();
			payInfo.setCkAppId(ckappid);
			payInfo.setPaytype("141");
			payInfo.setChannelId(channelid);
	
			payInfoConfig = onlineService.getPayInfo(payInfo);
			//4.如果订单已成功支付,则对于渠道的再次回调不予以处理
			
			if( baseRequest == null ){
				logger.info(String.format("渠道不存在,不进行后续处理,渠道名称为:[%s]", sdkName));
				return "FAILURE";
			}
			
			//2.获取orderId
			String orderId = baseRequest.getOrderId();
			String actualAmount = baseRequest.getActualAmount();
			String channelOrderId = baseRequest.getChannelOrderId();
			//安智需要执行的方法
			baseRequest.setPayInfoConfig(payInfoConfig);
			//3.调用渠道解析方法,验证订单正确与否
			OnlinePay onlinePay = onlineService.getOrderByOrderId(orderId);
			
			//判断订单是否存在,如果不存在则透传给CP进行处理
			if(onlinePay==null ){
				//百度特殊处理start
				if(channelid.equals("22") || channelid.equals("23") || channelid.equals("24") || channelid.equals("25") ){
					onlinePay = onlineService.getOrderById(orderId);
					if(onlinePay == null){
						logger.info(String.format("百度订单号订为[%s]的订单不存在!进行转发处理", channelOrderId));
						return "";
					}
				}
				//end
				if(StringUtils.isNotBlank(channelOrderId) && StringUtils.isBlank(orderId)){
					onlinePay = onlineService.getOrderByChannelOrderId(channelOrderId,channelid);
					if(onlinePay == null){
						onlinePay = onlineService.getOrderByPrepayid(channelOrderId);
						if(onlinePay == null){
							logger.info(String.format("外部订单号订为[%s]的订单不存在!进行转发处理", channelOrderId));
							return "";
						}
					}
				}else{
					logger.info(String.format("订单号订为[%s]的订单不存在!", orderId));
					return "";
				}
			}
			if(payInfoConfig==null){
				//获取支付信息
				payInfoConfig = getPayInfo(onlinePay);
			}
			onlinePay.setPayInfoConfig(payInfoConfig);
			onlinePay.setHttpServletRequest(request);
			//实际金额的传入
			onlinePay.setActualAmount((actualAmount==null?onlinePay.getPrices()+"":actualAmount));
			//设置渠道回调参数
			onlinePay.setCallBackMap(baseRequest.getCallBackCode());
			onlinePay.setCallBackContent(JSONObject.toJSONString(baseRequest.getCallBackCode()));
			
			//4.如果订单已成功支付,则对于渠道的再次回调不予以处理
			BasePayCallBackResponse payCallBackResponse = getResponse(sdkName, onlinePay);
			
			if(OrderStatus.PAY_SUCCESS.equals(onlinePay.getOrderStatus())){
				//7.返回结果
				resultStr = payCallBackResponse.getReturnSuccess();
				logger.info(String.format("订单已成功完成,直接返回成功回调处理结果:[%s]", resultStr));
				return resultStr;
			}
			
			if(!payCallBackResponse.isSuccess()){
				onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
				onlinePay.setCallBackContent(StringUtils.isEmpty(payCode)?JSONObject.toJSONString(onlinePay.getCallBackMap()):payCode);
				//5.更新数据库
				onlineService.savePayInfo(onlinePay);
				//7.返回结果
				resultStr = payCallBackResponse.getCallBackResult();
				logger.info(String.format("返回回调处理结果:[%s]", resultStr));
				return resultStr;
			}
			
			APPCk appCk =AppCkUtils.getAppCkById(onlinePay.getCkAppId());
			//5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
			if(onlinePay.getGameOnline()==1 && ( StringUtils.isNotBlank(appCk.getPayCallbackUrl()) || StringUtils.isNotBlank(onlinePay.getCallBackUrl()) ) ){
				onlinePay.setSercetKey(appCk.getSecretKey());
				onlinePay.setCallBackContent(payCode);
//				onlinePay.setActualAmount(actualAmount);
				onlinePay.setChannelOrderId(channelOrderId);
				
				//获取计费点信息
				PayCodeConfig payCodeConfig = getPayCodeConfig(onlinePay);
				onlinePay.setProductName(payCodeConfig==null?null:payCodeConfig.getProductName());
				
				Map<String, Object> map=payCallBackResponse.getSendOrder();
				onlinePay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
				onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
				onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
				onlinePay.setErrMsg("");
				onlinePay.setContent(map.get("content").toString());
				
				//6.更新数据库
				onlineService.savePayInfo(onlinePay);
				OrderSenderBoot.getInstance().add(onlinePay);
			}else{
				onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
				onlinePay.setErrMsg("");
				onlinePay.setCallBackContent(payCode);
//				onlinePay.setActualAmount(actualAmount);
				onlinePay.setChannelOrderId(channelOrderId);
				//6.更新数据库
				onlineService.savePayInfo(onlinePay);
			}
			
			//7.返回结果
			resultStr = payCallBackResponse.getCallBackResult();
			logger.info(String.format("返回回调处理结果:[%s]", resultStr));
		} catch (Exception e) {
			logger.error("channel callback error!", e);
			logger.info(String.format("返回回调处理结果:[%s]", resultStr));
		}
		return resultStr;
	}
	/**
	 * 渠道登陆回调
	 * @param payCode
	 * @return
	 */
	@RequestMapping(value = "loginCallBack/{ckappid}")
	@ResponseBody
	public String loginCallBack(@RequestBody String loginCode,HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable String ckappid){
		logger.info(String.format("渠道登陆回调的数据:[%s]", loginCode));
		return "0";
	}
	
	/**
	 * 渠道支付回调
	 * @param payCode
	 * @return
	 */
	@RequestMapping(value = "payCallBack/{name}")
	@ResponseBody
	public String payCallBack(@RequestBody String payCode, @PathVariable String name,HttpServletRequest httpRequest,HttpServletResponse response){
		logger.info(String.format("渠道回调信息%s:[%s]", name,payCode));
		try {
			PayInfoConfig payInfoConfig=null;
			BasePayCallBackRequest request= getRequest(name, payCode,httpRequest);
			//1.获取到客户端提供的参数
			String ckappid = httpRequest.getParameter("ckappid");
			String channelId = httpRequest.getParameter("channelid");
			Channel channel =null;
			if(StringUtils.isNotBlank(channelId)){
				channel = onlineService.getChannelById(channelId);
			}else{
				List<Channel> channels = onlineService.getChannelByName(name);
				if(channels!=null && channels.size()>0){
					channel = channels.get(0);
				}
			}
			
			if(channel!=null){
				PayInfoConfig payInfo=new PayInfoConfig();
				payInfo.setCkAppId(ckappid);
				payInfo.setPaytype("141");
				payInfo.setChannelId(channel.getId());
				payInfoConfig = onlineService.getPayInfo(payInfo);
				request.setPayInfoConfig(payInfoConfig);
			}
			
			logger.info(" channel request="+request);
			if(request==null){
				logger.info(String.format("渠道不存在,不进行后续处理,渠道名称为:[%s]", name));
				return "";
			}
			
			//2.获取orderId
			String orderId=request.getOrderId();
			String actualAmount = request.getActualAmount();
			String channelOrderId = request.getChannelOrderId();
			
			//3.调用渠道解析方法,验证订单正确与否
			OnlinePay onlinePay = onlineService.getOrderByOrderId(orderId);
			
			//判断订单是否存在,如果不存在则透传给CP进行处理
			if(onlinePay==null ){
				
				if(channelId.equals("4")){//华为单机处理
					JSONObject Hjson = new JSONObject();
					Hjson.put("result", 0);
					return Hjson.toJSONString();
				}
				//百度特殊处理start
				if(channelId.equals("22") || channelId.equals("23") || channelId.equals("24") || channelId.equals("25") ){
					onlinePay = onlineService.getOrderById(orderId);
					if(onlinePay == null){
						logger.info(String.format("百度订单号订为[%s]的订单不存在!", orderId));
						return "";
					}
				}
				//end
				if(StringUtils.isNotBlank(channelOrderId) && StringUtils.isBlank(orderId)){
					onlinePay = onlineService.getOrderByChannelOrderId(channelOrderId, channelId);
					if(onlinePay == null){
						onlinePay = onlineService.getOrderByPrepayid(channelOrderId);
						if(onlinePay == null){
							logger.info(String.format("外部订单号订为[%s]的订单不存在!", channelOrderId));
							return "";
						}
					}
				}else{
					logger.info(String.format("订单号为[%s]的订单不存在!进行转发处理", orderId));
					if(payInfoConfig!=null && payInfoConfig.getExInfoMap()!=null && payInfoConfig.getExInfoMap().containsKey("passthroughUrl")){
						String passthroughUrl =payInfoConfig.getExInfoMap().get("passthroughUrl").toString();
						return request.redirect(passthroughUrl);
					}
					logger.info("未配置透传地址,暂时不进行透传处理");
					return "";
				}
			}
			//获取计费点信息
			PayCodeConfig payCodeConfig = getPayCodeConfig(onlinePay);
			onlinePay.setPayCodeConfig(payCodeConfig);
			onlinePay.setActualAmount((actualAmount==null?onlinePay.getPrices()+"":actualAmount));
			if(StringUtils.isNotBlank(channelOrderId)){
				onlinePay.setChannelOrderId(channelOrderId);
			}
			if(payInfoConfig==null){
				//获取支付信息
				payInfoConfig = getPayInfo(onlinePay);
			}
			onlinePay.setPayInfoConfig(payInfoConfig);
			onlinePay.setHttpServletRequest(httpRequest);
			//设置渠道回调参数
			onlinePay.setCallBackMap(request.getCallBackCode());
			onlinePay.setCallBackContent(JSONObject.toJSONString(request.getCallBackCode()));
			
			//4.如果订单已成功支付,则对于渠道的再次回调不予以处理
			BasePayCallBackResponse payCallBackResponse = getResponse(name, onlinePay);
			if(channelId != null && channelId.equals("200") && StringUtils.isNotBlank(channelOrderId)){
				OnlinePay onlinePayOutOrder = onlineService.getOrderByChannelOrderId(channelOrderId, channelId);
				if(onlinePayOutOrder != null){
					//7.返回结果
					String resultStr = payCallBackResponse.getReturnFailure();
					logger.info(String.format("订单已成功完成,直接返回成功回调处理结果:[%s]", resultStr));
					if(onlinePayOutOrder.getOrderId().equals(orderId)){
						if(payCallBackResponse.isSuccess()){
							return payCallBackResponse.getReturnSuccess();
						}
					}else{
						return resultStr;
					}
				}
			}
			if(OrderStatus.PAY_SUCCESS.equals(onlinePay.getOrderStatus())){
				//7.返回结果
				String resultStr = payCallBackResponse.getReturnSuccess();
				/*if( channelId != null && channelId.equals("200") ){
					//7.返回结果
					resultStr = payCallBackResponse.getReturnFailure();
					logger.info(String.format("订单已成功完成,直接返回成功回调处理结果:[%s]", resultStr));
				}*/
				if( channelId != null && !channelId.equals("200") ){
					logger.info(String.format("订单已成功完成,直接返回成功回调处理结果:[%s]", resultStr));
					return resultStr;
				}else{
					//7.返回结果
					resultStr = payCallBackResponse.getReturnFailure();
					logger.info(String.format("订单已成功完成,直接返回成功回调处理结果:[%s]", resultStr));
					if(payCallBackResponse.isSuccess()){
						resultStr = payCallBackResponse.getReturnSuccess();
					}
					return resultStr;
				}
				
			}
			
			if(!payCallBackResponse.isSuccess()){
				onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
				onlinePay.setCallBackContent(StringUtils.isEmpty(payCode)?JSONObject.toJSONString(onlinePay.getCallBackMap()):payCode);
				//5.更新数据库
				onlineService.savePayInfo(onlinePay);
				//7.返回结果
				String resultStr = payCallBackResponse.getCallBackResult();
				logger.info(String.format("返回回调处理结果:[%s]", resultStr));
				return resultStr;
			}
			
			onlinePay.setProductName(payCodeConfig==null?null:payCodeConfig.getProductName());
			APPCk appCk =AppCkUtils.getAppCkById(onlinePay.getCkAppId());
			//5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
			if(onlinePay.getGameOnline()==1 && (StringUtils.isNotBlank(appCk.getPayCallbackUrl()) || StringUtils.isNotBlank(onlinePay.getCallBackUrl())) ){
				onlinePay.setSercetKey(appCk.getSecretKey());
				Map<String, Object> map=payCallBackResponse.getSendOrder();
				onlinePay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
				onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
				onlinePay.setSendStatus(OrderStatus.SEND_DOWN_ING);
				onlinePay.setErrMsg("");
				onlinePay.setContent(map.get("content").toString());
				//6.更新数据库
				onlineService.savePayInfo(onlinePay);
				OrderSenderBoot.getInstance().add(onlinePay);
			}else{
				onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
				onlinePay.setErrMsg("");
				onlinePay.setCallBackContent(payCode);
//				onlinePay.setActualAmount(actualAmount);
				onlinePay.setChannelOrderId(channelOrderId);
				//6.更新数据库
				onlineService.savePayInfo(onlinePay);
			}
			
			//7.返回结果
			String resultStr = payCallBackResponse.getCallBackResult();
			logger.info(String.format("返回回调处理结果%s:[%s]",name, resultStr));
			return resultStr;
		} catch (Exception e) {
			logger.error("渠道支付回调发生错误：", e);
			Map<String, Object> map = new HashMap<String, Object>();
			String result="";
			if("weixin".equals(name)){
				map.put("return_code", "FAIL");
				map.put("return_msg", "订单数据解析有误!");
				result = XmlUtils.toXml(map);
			}else{
				map.put("returnCode", "FAIL");
				map.put("returnMsg", "订单数据解析有误!");
				result =JSONObject.toJSONString(map);
			}
			logger.info(String.format("返回回调处理结果%s:[%s]",name, result));
			return result;
		}
	}
	
	public PayInfoConfig getPayInfo(OnlinePay onlinePay) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());
		payInfo.setChannelId(onlinePay.getChannelId());
		payInfo.setPaytype(onlinePay.getPayType());
		payInfo.setCarrierAppId(onlinePay.getAppId());
		return  onlineService.getPayInfo(payInfo);
	}

	public PayCodeConfig getPayCodeConfig(OnlinePay onlinePay){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(onlinePay.getCkAppId());
		payCodeConfig.setChannelId(onlinePay.getChannelId());
		payCodeConfig.setPaytype(onlinePay.getPayType());
		if( StringUtils.isNotBlank(onlinePay.getPayType())){
			Integer payType = Integer.valueOf(onlinePay.getPayType());
			if( payType > 120){
				payCodeConfig.setPaytype("141");
			}else{
				payCodeConfig.setPaytype(onlinePay.getPayType()==null?"":onlinePay.getPayType());
			}
		}else{
			payCodeConfig.setPaytype("");
		}
		payCodeConfig.setProductId(onlinePay.getProductId());
		return onlineService.getPayCode(payCodeConfig);
	}
	
	/**
	 * 反射获取request
	 * 
	 * @param channelEngName
	 * @param code
	 * @return
	 */
	private BasePayCallBackRequest getRequest(String name, String code,HttpServletRequest request) {

		BasePayCallBackRequest basePayCallBackRequest = null;
		try {
			Class<?> cla = Class.forName(String.format(paycallbackpackagerequest,
					name));
			basePayCallBackRequest = (BasePayCallBackRequest) cla.getConstructor(
					String.class,HttpServletRequest.class).newInstance(code,request);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return basePayCallBackRequest;
	}
	
	/**
	 * 反射获取request
	 * 
	 * @param channelEngName
	 * @param code
	 * @return
	 */
	private BasePayCallBackResponse getResponse(String name,OnlinePay onlinePay) {

		BasePayCallBackResponse basePayCallBackResponse = null;
		try {
			Class<?> cla = Class.forName(String.format(paycallbackpackageresponse,
					name));
			basePayCallBackResponse = (BasePayCallBackResponse) cla.getConstructor(
					OnlinePay.class).newInstance(onlinePay);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return basePayCallBackResponse;
	}
}