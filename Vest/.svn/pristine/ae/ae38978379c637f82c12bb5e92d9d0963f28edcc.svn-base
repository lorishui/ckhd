package me.ckhd.opengame.online.version;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.service.IOSInitCfgService;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.BaseHandle;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.reyun.task.RenyunTaskBoot;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("onlineVersion110")
public class OnlineVersion110 extends Version{
	
	@Autowired
	public OnlineService onlineService;
	
	@Autowired
	protected IOSInitCfgService iOSInitCfgService;
	
	public static Map<String,String> validChannel = new HashMap<String, String>();
	static{
		validChannel.put("appstore", "1");
		validChannel.put("googleplay", "1");
	}

	//pay start
	@Override
	public String pay(JSONObject codeJson,HttpServletRequest request) {
		String engName = codeJson.getString("a");
		JSONObject result = new JSONObject();
		String resultStr = null;
		OnlinePay onlinePay = null;
		try{
			String ckAppid = codeJson.containsKey("ckAppId")?codeJson.getString("ckAppId"):null;
			String ckChannelId = codeJson.containsKey("ckChannelId")?codeJson.getString("ckChannelId"):null;
			String productId = codeJson.containsKey("productId")?codeJson.getString("productId"):null;
			String child_id = codeJson.containsKey("subCkAppId")?codeJson.getString("subCkAppId"):null;
			//控制是否开启appstore支付start
			if( "200".equals(ckChannelId) && engName.equals("appstore") ){

				// 支付开关
				String paySwitch = DictUtils.getDictLabel(ckAppid+","+child_id, "ios_pay_control_switch", null);
				if(paySwitch != null){
					String[] paySwitchs = paySwitch.split(";");
					// ip
					if(paySwitchs.length >= 1){
						String ip = me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request);
						for(String disableIp:paySwitchs[0].split(",")){
							if(disableIp.equals(ip)){
								result.put("resultCode", 3100);
								result.put("errMsg", "支付控制");
								throw new Exception("3100");
							}
						}
					}
					if(paySwitchs.length >= 2){
						for(String disableIdfa:paySwitchs[1].split(",")){
							if(disableIdfa.equalsIgnoreCase(codeJson.getString("idfa"))){
								result.put("resultCode", 3100);
								result.put("errMsg", "支付控制");
								throw new Exception("3100");
							}
						}
					}
				}
//				String is = DictUtils.getDictLabel(ckAppid+","+child_id, "ios_pay_control_switch", "");//ios支付控制开关
//				if( StringUtils.isNotBlank(is) && !"0".equals(is)){
//					boolean isArea = false;
//					boolean isTime = false;
//					//1:地區2:時間段3:兩者
//					if("1".equals(is)){
//						isArea = isArealimit(request, ckAppid,child_id);
//						if(isArea){
//							result.put("resultCode", 3100);
//							result.put("errMsg", "支付控制");
//							throw new Exception("3100");
//						}
//					}else if("2".equals(is)){
//						isTime = isTimelimit(ckAppid,child_id);
//						if(isTime){
//							result.put("resultCode", 3100);
//							result.put("errMsg", "支付控制");
//							throw new Exception("3100");
//						}
//					}else if("3".equals(is)){
//						isArea = isArealimit(request, ckAppid,child_id);
//						isTime = isTimelimit(ckAppid,child_id);
//						if( isArea && isTime ){
//							result.put("resultCode", 3100);
//							result.put("errMsg", "支付控制");
//							throw new Exception("3100");
//						}
//					}
//				}
			}
			//end
			
			if( ckAppid == null || ckChannelId == null || productId == null ){
				result.put("resultCode", 3001);
				result.put("errMsg", "必要参数缺失");
				throw new Exception("3001");
			}
			//获取游戏信息
			APPCk appck = AppCkUtils.getAppCkByIdAndChild(ckAppid,codeJson.getString("subCkAppId"));
			if(appck==null){
				result.put("resultCode", 3002);
				result.put("errMsg", "游戏不存在");
				throw new Exception("3002");
			}
			//获取已配置的计费点信息
			PayCodeConfig payCodeConfig = getPayCodeConfig(codeJson);
			if( payCodeConfig == null ){
				result.put("resultCode", 3003);
				result.put("errMsg", "未配置计费点信息");
				throw new Exception("3003");
			}
			
			String price = codeJson.containsKey("price")?codeJson.getString("price"):null;
			// ### 网游的金额使用服务器的配置
			/*if( "141".equals(codeJson.get("payType")) ){
				price = payCodeConfig.getPrice();
			}*/
			//白名单		
			String imei = codeJson.containsKey("imei")?codeJson.getString("imei"):"";
			if( StringUtils.isNotBlank(imei) ){
				String white_imei = DictUtils.getDictLabel(imei, "online_imei_white_list", "");
				if( !"1".equals(white_imei) ){
					price = payCodeConfig.getPrice();
				}
			}else{
				price = payCodeConfig.getPrice();
			}

			String year=DateUtils.formatDateToStr("YYMMdd");
			//获取Order id
			Integer orderId = onlineService.getOrderId(ckAppid);
			//创建订单对象
			onlinePay = getOnliePay(codeJson,orderId,year,appck,price);
			//获取已配置的支付信息
			PayInfoConfig payInfoConfig = null;
			if( !"141".equals(onlinePay.getPayType()) ){
				payInfoConfig = getPayInfoConfigByChild(onlinePay);
			}else{
				payInfoConfig = getPayInfoConfig(onlinePay);
			}
			//过滤掉配置关闭苹果支付的
			if( "200".equals(ckChannelId) && payInfoConfig != null ){
				String childCkAppId = payInfoConfig.getAppid();
				if( childCkAppId != null ){
					String[] caArray = childCkAppId.split(",");
					String childAppId = codeJson.getString("subCkAppId");
					if(StringUtils.isNotBlank(childAppId)){
						for(String key:caArray){
							if(key.equals(childAppId)){
								result.put("resultCode", 3005);
								result.put("errMsg", "未配置支付配置信息");
								throw new Exception("3005");
							}
						}
					}
				}
			}
			
			onlinePay.setPayCodeConfig(payCodeConfig);
			onlinePay.setPayInfoConfig(payInfoConfig);
			
			BaseHandle handle = getHandle(engName);
			if( handle != null ){
				onlinePay.setHttpServletRequest(request);
				onlinePay.setClientIp(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(onlinePay.getHttpServletRequest()));
				resultStr = handle.pay(onlinePay, codeJson);
			}else{
				result.put("resultCode", 2011);
				result.put("errMsg", "请求的内容不存在!");
			}
		}catch (Throwable e) {
			logger.error("下单失败!!!! ", e);
			if( result.size()  == 0){
				result.put("resultCode", 3004);
				result.put("errMsg", "内部错误");
			}
		}finally{
			if(onlinePay!=null){
				// 将服务器获取到的数据保存到数据库
				onlineService.savePayInfo(onlinePay);
			}
		}
		if( StringUtils.isBlank(resultStr) ){
			resultStr = result.toJSONString();
		}
		logger.info(String.format("支付返回客户端的数据:[%s]", resultStr));
		return resultStr;
	}

	/**
	 * 地区限制
	 * @param request
	 * @param ckAppid
	 * @param childAppId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private boolean isArealimit(HttpServletRequest request, String ckAppid,String childAppId) throws Exception {
		String area = DictUtils.getDictLabels(ckAppid+"-"+childAppId, "ios_pay_control_province", "");//ios支付控制地区
		if(!"".equals(area)){
			String ip = me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request);
			String regex = "\\d+.\\d+.\\d+.\\d+";
			Pattern patern = Pattern.compile(regex);
			if( patern.matcher(ip).matches() ){
				String city = IPUtils.get(ip);
				logger.info("appstore pay city:"+city);
				if( city.indexOf(area) != -1 ){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 时间限制
	 * @param ckAppid
	 * @param childAppId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private boolean isTimelimit( String ckAppid,String childAppId) throws Exception {
		String period = DictUtils.getDictLabels(ckAppid+"-"+childAppId, "ios_pay_control_period", "");//ios支付控制地区
		String regex = "(\\d{4}-\\d{4},*)+";
		Pattern patern = Pattern.compile(regex);
		if( !"".equals(period) && patern.matcher(period).matches() ){
			String timeDt = DateUtils.formatDateToStr("HHmm");
			String[] periodArr = period.split(",");
			for(String timeD : periodArr){
				String[] timeDArr = timeD.split("-");
				if(timeDArr.length > 1){
					if( timeDt.compareTo(timeDArr[0]) >= 0 && timeDt.compareTo(timeDArr[1]) <= 0 ){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public PayCodeConfig getPayCodeConfig(JSONObject condeJson){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(condeJson.containsKey("ckAppId")?condeJson.getString("ckAppId"):"");
		payCodeConfig.setChannelId(condeJson.containsKey("ckChannelId")?condeJson.getString("ckChannelId"):"");
/*		if( condeJson.containsKey("payType") && StringUtils.isNotBlank(condeJson.getString("payType")) ){
			Integer payType = Integer.valueOf(condeJson.getString("payType"));
			payCodeConfig.setPaytype("141");
		}else{
			payCodeConfig.setPaytype("");
		}*/
		payCodeConfig.setPaytype("141");
		if( condeJson.containsKey("productId") && StringUtils.isNotBlank(condeJson.getString("productId")) ){
			payCodeConfig.setProductId(condeJson.containsKey("productId")?condeJson.getString("productId"):"");
			return onlineService.getPayCode(payCodeConfig);
		}else{
			return null;
		}
	}
	
	public OnlinePay getOnliePay(JSONObject codeJson,Integer orderId,String year,APPCk appCk,String price) throws Exception{
		OnlinePay onlinePay = new OnlinePay();
		String channelId = codeJson.getString("ckChannelId");
		onlinePay.setChannelId(codeJson.getString("ckChannelId"));
		onlinePay.setCkAppId(codeJson.getString("ckAppId"));
		onlinePay.setAppId(codeJson.containsKey("appId")?codeJson.getString("appId"):"");
		onlinePay.setVersion(codeJson.containsKey("version")?codeJson.getString("version"):"");
		onlinePay.setPayType(codeJson.containsKey("payType")?codeJson.getString("payType"):"");
		onlinePay.setExtension(codeJson.containsKey("extension")?codeJson.getString("extension"):"");
		onlinePay.setGameOnline(codeJson.containsKey("gameOnline")?codeJson.getInteger("gameOnline"):0);
		if( price != null ){
			int prices = Integer.valueOf(price);
			double d = prices*appCk.getDiscount();
			onlinePay.setPrices((int)d);
		}else{
			onlinePay.setPrices(0);
		}
		onlinePay.setUserId(codeJson.containsKey("userId")?codeJson.getString("userId"):"");
		onlinePay.setProductId(codeJson.containsKey("productId")?codeJson.getString("productId"):"");
		onlinePay.setAppPayContent(codeJson.toJSONString());
		onlinePay.setSdkType(codeJson.containsKey("sdkType")?codeJson.getString("sdkType"):"");
		if(codeJson.containsKey("payNotifyUrl")){
			onlinePay.setCallBackUrl(codeJson.getString("payNotifyUrl"));
		}else if(codeJson.containsKey("callBackUrl")){
			onlinePay.setCallBackUrl(codeJson.getString("callBackUrl"));
		}
		onlinePay.setOrderStatus(OrderStatus.NON_PAYMENT);
		onlinePay.setOrderId(genOderId(orderId,onlinePay.getCkAppId(),year));
		//新加字段
		onlinePay.setChildCkAppId(codeJson.containsKey("subCkAppId")?codeJson.getString("subCkAppId"):"1");
		onlinePay.setChildChannelId(codeJson.containsKey("subCkChannelId")?codeJson.getString("subCkChannelId"):"1");
		onlinePay.setRoleId(codeJson.containsKey("roleId")?codeJson.getString("roleId"):"00000000");
		onlinePay.setZoneId(codeJson.containsKey("zoneId")?codeJson.getString("zoneId"):"1");
		onlinePay.setServerId(codeJson.containsKey("serverId")?codeJson.getString("serverId"):"1");
		//增加新字段uuid
		if( codeJson.containsKey("imei") || codeJson.containsKey("idfa") ){
			if(codeJson.containsKey("imei")){
				onlinePay.setDeviceId(codeJson.getString("imei"));
			}
			if(codeJson.containsKey("idfa")){
				onlinePay.setDeviceId(codeJson.getString("idfa"));
			}
			if(StringUtils.isBlank(onlinePay.getDeviceId())){
				onlinePay.setDeviceId("");
			}
		}
		onlineService.savePayInfo(onlinePay);
		if(channelId != null && "2".equals(onlinePay.getSdkType()) && 
				("22".equals(channelId) || "23".equals(channelId) || 
						"24".equals(channelId) || "25".equals(channelId) )){
			onlinePay.setOrderIndex(saveIndex(channelId, 0, onlinePay.getOrderId())+"");
		}
		if( codeJson != null && codeJson.containsKey("notifyUrl") && codeJson.get("notifyUrl")!=null ){
			onlinePay.setNotifyUrl(codeJson.getString("notifyUrl"));
		}
		return onlinePay;
	}
	
	private int saveIndex(String channelId,int index,String orderId) throws Exception{
		int orderIndex=0;
		try {
			orderIndex = onlineService.saveOrderIndex(orderId);
		} catch (Exception e) {
			if (index<3) {
				orderIndex=saveIndex(channelId,index++,orderId);
			}else{
				throw e;
			}
		}
		return orderIndex;
	}
	
	private String genOderId(int orderId,String ckAppId,String year) {
		StringBuffer strBuff = new StringBuffer();
		// 游戏编号超过1099修改，改成36进制，相当于1099对应"99",1100对应"9a" update@2016-12-26
		String appCode = null;
		if (ckAppId.startsWith("10")) {
			appCode = ckAppId.substring(ckAppId.length()-2);
		} else {
			int y = Integer.parseInt(ckAppId);
			// 36进制100等于334（增加334-100 = 234）
			y = y - 1000 + 234;
			appCode = Integer.toString(y, Character.MAX_RADIX);
		}
		// update@2016-12-26 end
		strBuff.append(appCode);
		strBuff.append(year);

		String orderStr = "000000" + Integer.toHexString(orderId);
		orderStr = orderStr.substring(orderStr.length() - 6);
		strBuff.append(orderStr);//序号 
		
		Random random = new Random();
		strBuff.append(String.valueOf(10+random.nextInt(90)));
		return strBuff.toString();
	}
	//pay end
	
	//login登录开始
	@Override
	public String login(JSONObject codeJson, HttpServletRequest request) {
		String engName = codeJson.getString("a");
		JSONObject json = new JSONObject();
		// 获取已配置支付信息
		PayInfoConfig payInfo = getLoginInfo(codeJson);
		String resultStr = null;
		if(payInfo==null){
			json.put("resultCode", 2010);
			json.put("errMsg", "网游基本信息未配置!");
		}else{
			BaseHandle handle = getHandle(engName);
			if( handle != null ){
				OnlineUser user = new OnlineUser();
				handle.setOnlineUser(user, codeJson);
				resultStr = handle.login( user, codeJson, payInfo);
				JSONObject result = JSONObject.parseObject(resultStr);
				if( result.getInteger("resultCode") == 0){
				    try {
				        //设置其他参数
	                    setOnlineUserParam(user,codeJson);
    					OnlineUser oldUser = onlineService.get(user);
    					if (oldUser != null) {
    						user.setId("1");
    					} else {
    					    user.setIsNewRecord(true);
    					}
    					// 将服务器获取到的数据保存到数据库
    					onlineService.saveUserInfo(user);
				    } catch (Throwable t) {
				        logger.warn("渠道登录信息保存失败！- (该异常用户登录)", t);
				    }
				}
			}else{
				json.put("resultCode", 2011);
				json.put("errMsg", "请求的内容不存在!");
			}
		}
		if( StringUtils.isBlank(resultStr) ){
			resultStr = json.toJSONString();
		}
		logger.info(String.format("返回客户端的数据:[%s]", resultStr));
		return resultStr;
	}
	
	private void setOnlineUserParam(OnlineUser user, JSONObject codeJson) {

	    if (codeJson.containsKey("imei") || codeJson.containsKey("idfa")) {
            if (codeJson.containsKey("imei")) {
                user.setDeviceId(codeJson.getString("imei"));
            }
            if (codeJson.containsKey("idfa")) {
                user.setDeviceId(codeJson.getString("idfa"));
            }
            if (StringUtils.isBlank(user.getDeviceId())) {
                user.setDeviceId("");
            }
        }
    }

    /**
	 * 获取登录的参数
	 * @param map
	 * @return
	 */
	public PayInfoConfig getLoginInfo(JSONObject codeJson) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(codeJson.containsKey("ckAppId")?codeJson.getString("ckAppId"):"");
		payInfo.setChannelId(codeJson.containsKey("ckChannelId")?codeJson.getString("ckChannelId"):"");
		payInfo.setCarrierAppId(codeJson.containsKey("sdkVersion")?codeJson.getString("sdkVersion"):"");
		if( codeJson.containsKey("payType") && 121 == codeJson.getInteger("payType")){
			payInfo.setPaytype(codeJson.containsKey("payType")?codeJson.getString("payType"):"");
			payInfo.setChildCkAppId(codeJson.containsKey("subCkAppId")?codeJson.getString("subCkAppId"):"");
			return onlineService.getPayInfoByChild(payInfo);
		}
		return  onlineService.getLoginInfo(payInfo);
	}
	//login登陆结束
	
    @Override
	public String callback(String code,String engName,HttpServletRequest request,HttpServletResponse response) {
		boolean isExistChannelOutId = false;
		JSONObject json = new JSONObject();
		String returnStr = null;
		OnlinePay newPay = null;
		OnlinePay onlinePay = null;
		boolean isSuccess = false;
		try {
			//处理逻辑
			BaseHandle handle = SpringContextHolder.getBean(engName);
			if( handle == null ){
				logger.info(engName+"提示：地址非法");
				return "地址非法";
			}
			onlinePay = new OnlinePay();
			if("anzhi".equals(engName)){
				OnlinePay ob = new OnlinePay();
				ob.setCkAppId(request.getParameter("ckAppId"));
				ob.setChannelId("9");
				ob.setPayType("141");
				PayInfoConfig payInfo = getPayInfoConfig(ob);
				onlinePay.setPayInfoConfig(payInfo);
			}
			handle.parseParamter(code, request,onlinePay);
			if(StringUtils.isNotBlank(onlinePay.getOrderId())){
				//百度处理
				if("baidu".equals(engName)){
					newPay = onlineService.getOrderById(onlinePay.getOrderId());
					if( newPay == null ){
						newPay = onlineService.getOrderByOrderId(onlinePay.getOrderId());
					}
				}else{
					newPay = onlineService.getOrderByOrderId(onlinePay.getOrderId());
				}
				//appstore支付，googleplay支付验证
				if( newPay != null && !OrderStatus.PAY_SUCCESS.equals(newPay.getOrderStatus()) ){
					if(validChannel.containsKey(engName)){
						if( StringUtils.isNotBlank(onlinePay.getChannelOrderId()) ){
							OnlinePay validPay = onlineService.getOrderByChannelOrderIdOther(onlinePay.getChannelOrderId());
							if(validPay != null){
								isExistChannelOutId = true;
								returnStr = handle.getReturnSuccess();
								logger.info("提示：订单已经验证！外部订单号["+newPay.getChannelOrderId()+"] 返回值"+engName+":"+returnStr);
								return returnStr;
							}
						}
					}
				}
			}else if(StringUtils.isNotBlank(onlinePay.getChannelOrderId())){
				newPay = onlineService.getOrderByChannelOrderIdOther(onlinePay.getChannelOrderId());
				if(newPay == null){
					newPay = onlineService.getOrderByPrepayid(onlinePay.getChannelOrderId());
					if(newPay == null){
						logger.info(String.format("外部订单号订为[%s]的订单不存在!", onlinePay.getChannelOrderId()));
						json.put("resultCode", 4003);
						json.put("errMsg", "无效订单");
						logger.info(engName+"提示：无效订单");
					}
				}
			}else{
				json.put("resultCode", 4004);
				json.put("errMsg", "无法定位记录");
				logger.info(engName+"提示：无法定位记录");
			}
			if( newPay != null ){
				//已成功的订单处理
				if(OrderStatus.PAY_SUCCESS.equals(newPay.getOrderStatus())){
					returnStr = handle.getReturnSuccess();
					logger.info("提示：订单已经验证！订单号["+newPay.getOrderId()+"] 返回值"+engName+":"+returnStr);
					return returnStr;
				}
				//获取计费点信息
				PayCodeConfig payCodeConfig = getPayCodeConfig(newPay);
				newPay.setPayCodeConfig(payCodeConfig);
				PayInfoConfig payInfoConfig = null;
				if( "121".equals(newPay.getPayType())){
					payInfoConfig = getPayInfoConfigByChild(newPay);
				}else{
					payInfoConfig = getPayInfoConfig(newPay);
				}
				newPay.setPayInfoConfig(payInfoConfig);
				JSONObject result = new JSONObject();
				returnStr = handle.verifyData(newPay,result,response);
				if( result.getInteger("code") == 2000){
					newPay.setProductName(payCodeConfig==null?null:payCodeConfig.getProductName());
					APPCk appCk =AppCkUtils.getAppCkByIdAndChild(newPay.getCkAppId(),newPay.getChildCkAppId());
					//5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
					if( StringUtils.isNotBlank(appCk.getPayCallbackUrl()) || StringUtils.isNotBlank(newPay.getCallBackUrl()) ){
						newPay.setSercetKey(appCk.getSecretKey());
						newPay.setIsTest(onlinePay.getIsTest());
						if(StringUtils.isBlank(newPay.getActualAmount())){
							newPay.setActualAmount(onlinePay.getActualAmount());
						}
						newPay.setChannelOrderId(onlinePay.getChannelOrderId());
						newPay.setCallBackContent(onlinePay.getCallBackContent());
						Map<String, Object> map = handle.getSendOrder(newPay);
						newPay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
						newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						newPay.setSendStatus(OrderStatus.SEND_DOWN_ING);
						newPay.setErrMsg("");
						newPay.setContent(map.get("content").toString());
						onlineService.savePayInfo(newPay);
						isSuccess = true;
						//加入发货队列
						OrderSenderBoot.getInstance().add(newPay);
					}else{
						newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						newPay.setErrMsg("");
						newPay.setIsTest(onlinePay.getIsTest());
						newPay.setCallBackContent(onlinePay.getCallBackContent());
						if(StringUtils.isBlank(newPay.getActualAmount())){
							newPay.setActualAmount(onlinePay.getActualAmount());
						}
						newPay.setChannelOrderId(onlinePay.getChannelOrderId());
					}
					//####热云支付成功报备接口
					if (StringUtils.isNotBlank(newPay.getChannelId()) && "200".equals(newPay.getChannelId())) {
					    JSONObject task = new JSONObject();
					    /*if(newPay.getPayInfoConfig()!=null){
					        task.put("renyunKey", newPay.getPayInfoConfig().getAppkey());
					    }*/
					    Cfgparam cfgparam = new Cfgparam();
					    cfgparam.setCkAppId(newPay.getCkAppId());
					    cfgparam.setChildCkAppId(newPay.getChildCkAppId());
					    cfgparam.setCkChannelId(newPay.getChannelId());
					    Map<String, Object> resultMap = iOSInitCfgService.getCfg(cfgparam);
					    //reyun
					    if (resultMap!= null && resultMap.size()>0 && resultMap.containsKey("reyun")) {
					        Object obj = resultMap.get("reyun");
					        if (obj != null && obj instanceof Map) {
    					        @SuppressWarnings("unchecked")
                                Map<String, Object> reyun = (Map<String, Object>)obj;
    					        if (reyun.containsKey("key")) {
    					            task.put("renyunKey", reyun.get("key"));
    					            task.put("userId", newPay.getUserId());
			                        task.put("currencyAmount", Integer.parseInt(newPay.getActualAmount())/100d);
			                        task.put("deviceid", newPay.getDeviceId());
			                        task.put("paymentType", newPay.getChannelId());//其他的处理传paytype
			                        task.put("orderId", newPay.getOrderId());
			                        RenyunTaskBoot.addTask(task);
    					        }
					        }
					    }
					}
				}else{
					onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
					onlinePay.setCallBackContent(code);
					returnStr = handle.getReturnFailure();
					logger.info("提示：验证不通过!订单号["+newPay.getOrderId()+"] 返回值"+engName+":"+returnStr);
				}
			}
		} catch (Throwable e) {
			if( json.size() == 0 ){
				json.put("resultCode", 4002);
				json.put("errMsg", "内部错误");
			}
			logger.info("支付异常提示："+json.toJSONString());
			logger.error("支付发生错误："+e.getMessage(),e);
		}finally{
			//出错的保存
			if( newPay != null && StringUtils.isNotBlank(newPay.getId()) && !isSuccess){
				if(StringUtils.isBlank(newPay.getActualAmount())){
					newPay.setActualAmount(onlinePay.getActualAmount());
				}
				if(!isExistChannelOutId){
					newPay.setCallBackContent(onlinePay.getCallBackContent());
				}
				newPay.setChannelOrderId(onlinePay.getChannelOrderId());
				newPay.setIsTest(onlinePay.getIsTest());
				newPay.setErrMsg(StringUtils.isNotBlank(returnStr)?returnStr:json.toJSONString());
				onlineService.savePayInfo(newPay);
			}
		}
		if( returnStr == null ){
			returnStr = json.toJSONString();
		}
		logger.info(String.format("返回渠道的数据%s:[%s]",engName, returnStr));
		return returnStr;
	}
	
	public PayCodeConfig getPayCodeConfig(OnlinePay onlinePay){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(onlinePay.getCkAppId());
		payCodeConfig.setChannelId(onlinePay.getChannelId());
		payCodeConfig.setPaytype("141");
		payCodeConfig.setProductId(onlinePay.getProductId());
		return onlineService.getPayCode(payCodeConfig);
	}
	
	public PayInfoConfig getPayInfoConfig(OnlinePay onlinePay){
		PayInfoConfig payInfo=new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());
		payInfo.setChildCkAppId(onlinePay.getChildCkAppId());
		payInfo.setPaytype(onlinePay.getPayType());
		payInfo.setChannelId(onlinePay.getChannelId());
		return onlineService.getPayInfo(payInfo);
	}
	
	/**
	 * 微信专用
	 * @param onlinePay
	 * @return
	 */
	public PayInfoConfig getPayInfoConfigByChild(OnlinePay onlinePay){
		PayInfoConfig payInfo=new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());
		payInfo.setPaytype(onlinePay.getPayType());
		payInfo.setChannelId(onlinePay.getChannelId());
		payInfo.setChildCkAppId(StringUtils.isNotBlank(onlinePay.getChildCkAppId())?onlinePay.getChildCkAppId():"1");
		return onlineService.getPayInfoByChild(payInfo);
	}
	
	/**
	 * 苹果专用
	 * @param onlinePay
	 * @return
	 */
	public PayInfoConfig getPayInfoConfigByChildByApple(OnlinePay onlinePay){
		PayInfoConfig payInfo=new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());
		payInfo.setPaytype(onlinePay.getPayType());
		payInfo.setChannelId(onlinePay.getChannelId());
		payInfo.setChildCkAppId(StringUtils.isNotBlank(onlinePay.getChildCkAppId())?onlinePay.getChildCkAppId():"1");
		return onlineService.getPayInfoByChildApple(payInfo);
	}
	
	/**
	 *通过bean.name 获取对象
	 * 
	 * @param className
	 * @param code
	 * @return
	 */
	private BaseHandle getHandle(String engName) {

		BaseHandle BaseHandle = null;
		try {
			BaseHandle = (BaseHandle) SpringContextHolder.getBean(engName);
		} catch (Throwable e) {
			logger.error("spring获取bean出问题!!!!", e);
		}
		return BaseHandle;
	}

	/**
	 * 客户端支付成功回调
	 */
	@Override
	public String callbackbc(String code, String engName,
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String returnStr = null;
		OnlinePay newPay = null;
		OnlinePay onlinePay = null;
		boolean isSuccess = false;
		try {
			//处理逻辑
			BaseHandle handle = SpringContextHolder.getBean(engName);
			onlinePay = new OnlinePay();
			handle.parseParamter(code, request,onlinePay);
			if(StringUtils.isNotBlank(onlinePay.getOrderId())){
				newPay = onlineService.getOrderByOrderId(onlinePay.getOrderId());
			}else if(StringUtils.isNotBlank(onlinePay.getChannelOrderId())){
				newPay = onlineService.getOrderByChannelOrderIdOther(onlinePay.getChannelOrderId());
				if(newPay == null){
					newPay = onlineService.getOrderByPrepayid(onlinePay.getChannelOrderId());
					if(newPay == null){
						logger.info(String.format("外部订单号订为[%s]的订单不存在!", onlinePay.getChannelOrderId()));
						json.put("resultCode", 4003);
						json.put("errMsg", "无效订单");
						throw new Exception("无效订单");
					}
				}
			}else{
				json.put("resultCode", 4004);
				json.put("errMsg", "无法定位记录");
				throw new Exception("无法定位记录");
			}
			//已成功的订单处理
			if(OrderStatus.PAY_SUCCESS.equals(newPay.getOrderStatus())){
				returnStr = handle.getReturnSuccess();
				throw new Exception("已经处理的订单");
			}
			//获取计费点信息
			PayCodeConfig payCodeConfig = getPayCodeConfig(newPay);
			newPay.setPayCodeConfig(payCodeConfig);
			PayInfoConfig payInfoConfig = getPayInfoConfig(newPay);
			newPay.setPayInfoConfig(payInfoConfig);
			JSONObject result = new JSONObject();
			returnStr = handle.verifyData(newPay,result,response);
			logger.info("支付验证问题提示："+result.toJSONString());
			if( result.getInteger("code") == 2000 ){
				newPay.setProductName(payCodeConfig==null?null:payCodeConfig.getProductName());
				APPCk appCk = AppCkUtils.getAppCkByIdAndChild(newPay.getCkAppId(),newPay.getChildCkAppId());
				//5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
				newPay.setSercetKey(appCk.getSecretKey());
				newPay.setActualAmount(onlinePay.getActualAmount());
				Map<String, Object> map = handle.getSendOrder(newPay);
				newPay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
				newPay.setContent(map.get("content").toString());
				onlineService.savePayInfo(newPay);
			}else{
				onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
				onlinePay.setCallBackContent(code);
				returnStr = handle.getReturnFailure();
				throw new Exception("验证不通过！");
			}
		} catch (Exception e) {
			if( json.size() == 0 ){
				json.put("resultCode", 4002);
				json.put("errMsg", "内部错误");
			}
			logger.info("支付异常提示："+json.toJSONString());
			logger.error("支付发生错误：",e);
		}finally{
			//出错的保存或单机成功的保存
			if( newPay != null && StringUtils.isNotBlank(newPay.getId()) && !isSuccess){
				newPay.setActualAmount(onlinePay.getActualAmount());
				newPay.setCallBackContent(onlinePay.getCallBackContent());
				newPay.setErrMsg(StringUtils.isNotBlank(returnStr)?returnStr:json.toJSONString());
				onlineService.savePayInfo(newPay);
			}
		}
		if( returnStr == null ){
			returnStr = json.toJSONString();
		}
		logger.info(String.format("返回渠道的数据%s:[%s]",engName, returnStr));
		return returnStr;
	}

	/**
	 * 查询订单状态
	 * @param codeJson
	 * @param request
	 */
	public Result<Void> queryOrderState(JSONObject codeJson,HttpServletRequest request) {
		try{
			return queryOrderState0(codeJson, request);
		}
		catch (Throwable e) {
			logger.error(e.getMessage(), e);
			
			Result<Void> result = new Result<Void>();
			result.setCode(3004);
			result.setMessage("内部错误");
			
			return result;
		}
	}
	private Result<Void> queryOrderState0(JSONObject codeJson,HttpServletRequest request) {
		Result<Void> result = new Result<Void>();

		String engName = codeJson.getString("a");
		
		String ckChildAppId = codeJson.getString("subCkAppId");
		String ckChannelId = codeJson.getString("ckChannelId");
		String productId = codeJson.getString("productId");
		String orderId = codeJson.getString("orderId");
		String ckAppid = codeJson.getString("ckAppId");
		
		if( isBlank(ckAppid) || isBlank(ckChannelId) || isBlank(productId) || isBlank(orderId) ){
			result.setCode(3001);
			result.setMessage("必要参数缺失");
			return result;
		}
		
		OnlinePay onlinePay = onlineService.getOrderByOrderId(orderId);
		
		if( onlinePay == null ) {
			result.setCode(3005);
			result.setMessage("订单不存在");
			return result;
		}
		if( OrderStatus.PAY_SUCCESS.equals(onlinePay.getOrderStatus()) ) {
			result.setCode(0);
			result.setMessage("支付成功");
			return result;
		}
		if( OrderStatus.PAY_FAIL.equals(onlinePay.getOrderStatus()) ) {
			result.setCode(3006);
			result.setMessage("支付失败");
			return result;
		}
		
		//获取游戏信息
		APPCk appck = AppCkUtils.getAppCkByIdAndChild(ckAppid, ckChildAppId);
		if(appck==null){
			result.setCode(3002);
			result.setMessage("游戏不存在");
			return result;
		}
		//获取已配置的计费点信息
		PayCodeConfig payCodeConfig = getPayCodeConfig(codeJson);
		if( payCodeConfig == null ){
			result.setCode(3003);
			result.setMessage("未配置计费点信息");
			return result;
		}

		//获取已配置的支付信息
		PayInfoConfig payInfoConfig = null;
		if( !"141".equals(onlinePay.getPayType()) ){
			payInfoConfig = getPayInfoConfigByChild(onlinePay);
		}
		else{
			payInfoConfig = getPayInfoConfig(onlinePay);
		}
		
		onlinePay.setPayCodeConfig(payCodeConfig);
		onlinePay.setPayInfoConfig(payInfoConfig);
		
		BaseHandle handle = getHandle(engName);
		if( handle == null ){
			result.setCode(2011);
			result.setMessage("请求的内容不存在");
			return result;
		}

		onlinePay.setHttpServletRequest(request);
		onlinePay.setClientIp(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(onlinePay.getHttpServletRequest()));
		Result<Void> osret = handle.queryOrderState(onlinePay, codeJson);
		if( osret.getCode() == 0 ) {
			result.setCode(0);
			result.setMessage("支付成功");
		}
		else {
			result.setCode(osret.getCode());
			result.setMessage(isBlank(osret.getMessage()) ? "支付失败" : osret.getMessage());
		}
		return result;
	}
	
	private boolean isBlank(Object str){ return str == null || StringUtils.isBlank(str.toString()); }

	/**
	 * mycard補储
	 */
	@Override
	public String addorder(String code, String engName, HttpServletRequest request, HttpServletResponse response) {
		boolean isExistChannelOutId = false;
		JSONObject json = new JSONObject();
		String returnStr = null;
		OnlinePay newPay = null;
		boolean isSuccess = false;
		// 处理逻辑
		BaseHandle handle = SpringContextHolder.getBean(engName);
		if (handle == null) {
			logger.info(engName + "提示：地址非法");
			return "地址非法";
		}
		// 根据json查找订单list
		List<OnlinePay> list = handle.getOrders(code, request);
		for (OnlinePay onlinePay : list) {
			try {
				if (StringUtils.isNotBlank(onlinePay.getOrderId())) {
					newPay = onlineService.getOrderByOrderId(onlinePay.getOrderId());

				} else if (StringUtils.isNotBlank(onlinePay.getChannelOrderId())) {
					newPay = onlineService.getOrderByChannelOrderIdOther(onlinePay.getChannelOrderId());
					if (newPay == null) {
						newPay = onlineService.getOrderByPrepayid(onlinePay.getChannelOrderId());
						if (newPay == null) {
							logger.info(String.format("外部订单号订为[%s]的订单不存在!", onlinePay.getChannelOrderId()));
							logger.info(engName + "提示：无效订单");
						}
					}
				} else {
					logger.info(engName + "提示：无法定位记录");
				}
				if (newPay != null) {
					// 已成功的订单处理
					if (OrderStatus.PAY_SUCCESS.equals(newPay.getOrderStatus())) {
						logger.info("提示：订单已经验证！订单号[" + newPay.getOrderId() + "] 返回值" + engName + ":" + returnStr);
					}
					// 获取计费点信息
					PayCodeConfig payCodeConfig = getPayCodeConfig(newPay);
					newPay.setPayCodeConfig(payCodeConfig);
					PayInfoConfig payInfoConfig = null;
					payInfoConfig = getPayInfoConfig(newPay);
					newPay.setPayInfoConfig(payInfoConfig);
					newPay.setProductName(payCodeConfig == null ? null : payCodeConfig.getProductName());
					APPCk appCk = AppCkUtils.getAppCkByIdAndChild(newPay.getCkAppId(), newPay.getChildCkAppId());
					// 5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
					if (StringUtils.isNotBlank(appCk.getPayCallbackUrl())
							|| StringUtils.isNotBlank(newPay.getCallBackUrl())) {
						newPay.setSercetKey(appCk.getSecretKey());
						newPay.setIsTest(onlinePay.getIsTest());
						if (StringUtils.isBlank(newPay.getActualAmount())) {
							newPay.setActualAmount(onlinePay.getActualAmount());
						}
						newPay.setChannelOrderId(onlinePay.getChannelOrderId());
						newPay.setCallBackContent(onlinePay.getCallBackContent());
						Map<String, Object> map = handle.getSendOrder(newPay);
						newPay.setSendNum(
								Integer.valueOf(map.get("sendNum") == null ? "0" : map.get("sendNum").toString()));
						newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						newPay.setSendStatus(OrderStatus.SEND_DOWN_ING);
						newPay.setErrMsg("");
						newPay.setContent(map.get("content").toString());
						onlineService.savePayInfo(newPay);
						isSuccess = true;
						// 加入发货队列
						OrderSenderBoot.getInstance().add(newPay);
					} else {
						newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						newPay.setErrMsg("");
						newPay.setIsTest(onlinePay.getIsTest());
						newPay.setCallBackContent(onlinePay.getCallBackContent());
						if (StringUtils.isBlank(newPay.getActualAmount())) {
							newPay.setActualAmount(onlinePay.getActualAmount());
						}
						newPay.setChannelOrderId(onlinePay.getChannelOrderId());
					}
					// ####热云支付成功报备接口
					if (StringUtils.isNotBlank(newPay.getChannelId()) && "200".equals(newPay.getChannelId())) {
						JSONObject task = new JSONObject();
						Cfgparam cfgparam = new Cfgparam();
						cfgparam.setCkAppId(newPay.getCkAppId());
						cfgparam.setChildCkAppId(newPay.getChildCkAppId());
						cfgparam.setCkChannelId(newPay.getChannelId());
						Map<String, Object> resultMap = iOSInitCfgService.getCfg(cfgparam);
						// reyun
						if (resultMap != null && resultMap.size() > 0 && resultMap.containsKey("reyun")) {
							Object obj = resultMap.get("reyun");
							if (obj != null && obj instanceof Map) {
								@SuppressWarnings("unchecked")
								Map<String, Object> reyun = (Map<String, Object>) obj;
								if (reyun.containsKey("key")) {
									task.put("renyunKey", reyun.get("key"));
									task.put("userId", newPay.getUserId());
									task.put("currencyAmount", Integer.parseInt(newPay.getActualAmount()) / 100d);
									task.put("deviceid", newPay.getDeviceId());
									task.put("paymentType", newPay.getChannelId());// 其他的处理传paytype
									task.put("orderId", newPay.getOrderId());
									RenyunTaskBoot.addTask(task);
								}
							}
						}
					}
				}
			} catch (Throwable e) {
				logger.info("支付异常提示：" + json.toJSONString());
				logger.error("支付发生错误：" + e.getMessage(), e);
				// 出错的保存
				if (newPay != null && StringUtils.isNotBlank(newPay.getId()) && !isSuccess) {
					if (StringUtils.isBlank(newPay.getActualAmount())) {
						newPay.setActualAmount(onlinePay.getActualAmount());
					}
					if (!isExistChannelOutId) {
						newPay.setCallBackContent(onlinePay.getCallBackContent());
					}
					newPay.setChannelOrderId(onlinePay.getChannelOrderId());
					newPay.setIsTest(onlinePay.getIsTest());
					newPay.setErrMsg(StringUtils.isNotBlank(returnStr) ? returnStr : json.toJSONString());
					onlineService.savePayInfo(newPay);
				}
			}
		}

		return "SUCCESS";
	}

	@Override
	public String query(Map<String, String> param, String engName, HttpServletRequest request,
			HttpServletResponse response) {
		BaseHandle handle = SpringContextHolder.getBean(engName);
		if (handle == null) {
			logger.info(engName + "提示：地址非法");
			return "地址非法";
		}
		handle.queryOrder(param);
		return null;
	}
}
