package me.ckhd.opengame.online.task;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.Channel;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.service.CfgService;
import me.ckhd.opengame.app.service.MmextendCfgService;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.app.utils.IccidUtils;
import me.ckhd.opengame.app.web.ProviceCalcComponent;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.IdGen;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.Prefix;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.online.entity.OfflinePay;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BaseOtherRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.sys.entity.Dict;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 与前端对接
 * 
 * @author leo
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/online/app")
public class AppOnlineApi extends BaseController {

	@Autowired
	private OnlineService onlineService;
	
	@Autowired
	private MmextendCfgService mmextendCfgService;
	
	@Autowired
	private RedisClientTemplate redisClientTemplate;

	@Autowired
	private ProviceCalcComponent proviceCalcComponent;
	
	private String reqeustPackage = "me.ckhd.opengame.online.request.%s.LoginRequest";
	private String reqeustPayPackage = "me.ckhd.opengame.online.request.%s.PayRequest";
	private String reqeustOtherPackage = "me.ckhd.opengame.online.request.%s.OtherRequest";
	private String httpPackage = "me.ckhd.opengame.online.httpclient.%s.HttpClient";

	/**
	 * 前端调用后台获取数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "login")
	@ResponseBody
	public String login(@RequestBody String code) {
		logger.info(String.format("客户端上传的数据:[%s]", code));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 根据客户端上传数据解析成Map
			Map<String, Object> codeMap = MyJsonUtils.jsonStr2Map(code);
			if(codeMap.containsKey("loginType") && "2".equals(codeMap.get("loginType"))){
				OnlineUser user = new OnlineUser();
				user.setChannelId(codeMap.containsKey("ckChannelId")?codeMap.get("ckChannelId").toString():"");
				user.setCkAppId(codeMap.containsKey("ckAppId")?codeMap.get("ckAppId").toString():"");
				user.setLoginParam(codeMap);
				user.setVersion(codeMap.containsKey("ckAppId")?codeMap.get("version").toString():"");
				Map<String,Object> verifyInfo = (HashMap<String, Object>)codeMap.get("verifyInfo");
				user.setAppVerifyInfo(JSONObject.toJSONString(verifyInfo));
				user.setLoginType(codeMap.get("loginType").toString());
				user.setSid(verifyInfo.containsKey("userId")?verifyInfo.get("userId").toString():"");
				user.setUserName((verifyInfo.containsKey("userName")?verifyInfo.get("userName").toString():verifyInfo.get("userId").toString()));
				
				//判断数据库是否有相关用户信息
				OnlineUser oldUser = onlineService.get(user);
				OnlineUser newUser = user;
				if (oldUser != null) {
					newUser.setUid(oldUser.getUid());
					newUser.setToken(IdGen.uuid());
					newUser.setUpdateDate(new Date());
				}
				// 将服务器获取到的数据保存到数据库
				onlineService.saveUserInfo(newUser);
				
				
				//封装返回数据
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("resultCode", 0);
				resultMap.put("uid", user.getUid());
				resultMap.put("token",  user.getToken());
				resultMap.put("userName", user.getUserName());
				result.put("result", resultMap);
				String returnStr = (JSONObject.toJSONString(result));
				logger.info(String.format("返回CP登陆客户端的数据:[%s]", returnStr));
				return returnStr;
			}
			
			String channelEngName = null;
			//适用多个channelEngName
			if(codeMap.containsKey("sdkName") && codeMap.get("sdkName").toString().trim().length() > 0){
				channelEngName = codeMap.get("sdkName").toString();
			}else{
				// 获取channel
				channelEngName = getChannel(codeMap);
			}
			
			if(channelEngName == null){
				resultMap.put("resultCode", -3);
				resultMap.put("errMsg", "网游映射信息未配置!");
				String returnStr = (JSONObject.toJSONString(resultMap));
				logger.info(String.format("返回客户端的数据0:[%s]", returnStr));
				return returnStr;
			}
			
			// 获取已配置支付信息
			PayInfoConfig payInfo = getLoginInfo(codeMap);
			if(payInfo==null){
				resultMap.put("resultCode", -1);
				resultMap.put("errMsg", "网游基本信息未配置!");
				String returnStr = (JSONObject.toJSONString(resultMap));
				logger.info(String.format("返回客户端的数据0:[%s]", returnStr));
				return returnStr;
			}

			
			// 通过反射获取request对象
			BaseLoginRequest request = getRequest(channelEngName, code,payInfo);
			
			// 通过反射获取client对象
			BaseHttpClient client = getHttpClient(channelEngName);
			
			// 调用response
			BaseLoginResponse response = client.httpLoginClient(request);
			
			// 无返回则返回失败到手机端
			if (response == null) {
				resultMap.put("resultCode", -1);
				resultMap.put("errMsg", "登陆失败");
				String returnStr = (JSONObject.toJSONString(resultMap));
				logger.info(String.format("返回客户端的数据1:[%s]", returnStr));
				return returnStr;
			}
			
			
			if (!response.isSuccess()) {
				// 将服务器获取到的数据保存到数据库
				String returnStr = (response == null ? null : JSONObject.toJSONString(response.getResult()));
				logger.info(String.format("返回客户端的数据2:[%s]", returnStr));
				return returnStr;
			}
			
			
			OnlineUser oldUser = onlineService.get(response.getUserInfo());
			OnlineUser newUser = response.getUserInfo();
			if (oldUser != null) {
				newUser.setUid(oldUser.getUid());
				newUser.setToken(IdGen.uuid());
				newUser.setUpdateDate(new Date());
			}
			// 将服务器获取到的数据保存到数据库
			onlineService.saveUserInfo(newUser);
			String returnStr = (response == null ? null : JSONObject.toJSONString(response.getResult()));
			logger.info(String.format("返回客户端的数据3:[%s]", returnStr));
			return returnStr;
		} catch (Exception e) {
			logger.error("登陆发生错误：",e);
			resultMap.put("resultCode", -1);
			resultMap.put("errMsg", "登陆失败");
			String returnStr = (JSONObject.toJSONString(resultMap));
			logger.info(String.format("返回客户端的数据4:[%s]", returnStr));
			return returnStr;
		}
	}

	private String getChannel(Map<String, Object> map) {
		// 通过参数获取到渠道ID
		String channelId = map.get("ckChannelId").toString();
		// 通过渠道ID获取到渠道信息
		Channel channel = onlineService.getChannelById(channelId);
		return channel.getEngName();
	}

	private String getDict(String paytype) {
		// 通过渠道ID获取到渠道信息
		Dict dict = onlineService.getpayTypeDict(paytype);
		return dict.getDescription();
	}

	/**
	 * 反射获取client
	 * 
	 * @param channelEngName
	 * @return
	 */
	private BaseHttpClient getHttpClient(String channelEngName) {
		BaseHttpClient baseHttpClient = null;
		try {
			Class cla = Class.forName(String
					.format(httpPackage, channelEngName));
			baseHttpClient = (BaseHttpClient) cla.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return baseHttpClient;
	}

	/**
	 * 反射获取request
	 * 
	 * @param channelEngName
	 * @param code
	 * @return
	 */
	private BaseLoginRequest getRequest(String channelEngName, String code,PayInfoConfig payInfo) {

		BaseLoginRequest baseLoginRequest = null;
		try {
			Class cla = Class.forName(String.format(reqeustPackage,
					channelEngName));
			baseLoginRequest = (BaseLoginRequest) cla.getConstructor(
					String.class,PayInfoConfig.class).newInstance(code,payInfo);
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
		return baseLoginRequest;

	}

	/**
	 * 反射获取PayRequest
	 * 
	 * @param channelEngName
	 * @param code
	 * @return
	 */
	private BasePayRequest getPayRequest(String channelEngName,OnlinePay onlinePay) {

		BasePayRequest basePayRequest = null;
		try {
			Class cla = Class.forName(String.format(reqeustPayPackage,
					channelEngName));
			basePayRequest = (BasePayRequest) cla.getConstructor(OnlinePay.class).newInstance(onlinePay);
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
		return basePayRequest;

	}
	
	/**
	 * 反射获取OtherRequest
	 * 
	 * @param channelEngName
	 * @param code
	 * @return
	 */
	private BaseOtherRequest getOtherRequest(String channelEngName,OnlinePay onlinePay) {

		BaseOtherRequest baseOterRequest = null;
		try {
			Class cla = Class.forName(String.format(reqeustOtherPackage,
					channelEngName));
			baseOterRequest = (BaseOtherRequest) cla.getConstructor(OnlinePay.class).newInstance(onlinePay);
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
		return baseOterRequest;

	}

	/**
	 * TODO 前端调用后台获取支付数据/订单号
	 * 
	 * @return
	 */
	@RequestMapping(value = "pay",produces={"*/*;charset=utf-8","application/json;charset=utf-8"})
	@ResponseBody
	public String pay(@RequestBody String code, HttpServletRequest request,HttpServletResponse response) {
		OnlinePay onlinePay = null;
		String returnStr="";
		int errorCode = -1;
		String errMsgs = "";
		try {
			logger.info(String.format("客户端上传的支付数据:[%s]", code));
			// 根据客户端上传数据解析成Map
			Map<String, Object> codeMap = MyJsonUtils.jsonStr2Map(code);
			
			//容大和HS的配置判断
			Map<String, Object> verData =  codeMap.get("verifyInfo")!= null ?(Map<String, Object>)codeMap.get("verifyInfo"):null;
			if( verData!=null && verData.get("mmSdkName") != null && verData.get("mmSdkName").toString().length() > 0 ){
				if(verData.containsKey("imsi") && verData.containsKey("versionName") && verData.containsKey("iccid")){
					int is = getCfgParam(codeMap, request);
					if(is==-1){
						errorCode = 3001;
						errMsgs = "未配置rd或者hs的扩展信息";
						throw new Exception(errMsgs);
					}
					if(is==-2){
						errorCode = 3002;
						errMsgs = "ackType错误";
						throw new Exception(errMsgs);
					}
				}
				
				//获取imsi的次数
				if(verData.containsKey("imsi") && verData.get("imsi") != null){
					Map<String,String> orderData = new HashMap<String, String>();
					orderData.put("ackType", verData.get("ackType").toString());
					orderData.put("ckAppId", codeMap.get("ckAppId").toString());
					orderData.put("ckChannelId", codeMap.get("ckChannelId").toString());
					orderData.put("mmAppId", codeMap.get("mmAppId").toString());
					orderData.put("imsi", verData.get("imsi").toString());
					orderData.put("type", verData.get("mmSdkName").toString());
					int num = onlineService.getCountByImsi(orderData);
					codeMap.put("ackTimes", num);
				}
			}
			
			String ckappid = (codeMap.containsKey("ckAppId")?codeMap.get("ckAppId").toString():null);
			//获取游戏信息
			APPCk appck = AppCkUtils.getAppCkById(ckappid);
			if(appck==null){
				String errMsg = "游戏不存在!";
				onlinePay = new OnlinePay();
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				throw new Exception(errMsg);
			}
			
			Map<String, Object> verifyInfo = (Map<String, Object>)codeMap.get("verifyInfo");
			
			boolean isMmSelf = false;
			boolean isAndSelf = false;
			boolean isWoSelf = false;
			boolean	isEgameSelf = false;
			if(verifyInfo != null){
				isMmSelf = "103".equals(codeMap.get("payType")) && "mmSelf".equals(verifyInfo.get("mmSdkName"));
				isAndSelf = "104".equals(codeMap.get("payType")) && "andSelf".equals(verifyInfo.get("andSdkName"));
				isWoSelf = "105".equals(codeMap.get("payType")) && "woSelf".equals(verifyInfo.get("woSdkName"));
				isEgameSelf = "101".equals(codeMap.get("payType")) && "egameSelf".equals(verifyInfo.get("egameSdkName"));
			}
			if(verifyInfo != null && (isMmSelf || isAndSelf || isWoSelf || isEgameSelf)){
				String msg = "";
				int day = 90;
				String status = "0";
				int num = 0;
				boolean isSuccess = false;
				Map<String,Object> result = new HashMap<String,Object>();
				try {
					day = Integer.parseInt(DictUtils.getDictValue(ckappid, "mm_count_time", "90"));
					// 一次是否通过，二次确认当前配置
					String serviceName = "MmextendCfgService";
					if(isAndSelf){
						day = Integer.parseInt(DictUtils.getDictValue(ckappid, "and_count_time", "90"));
						serviceName = "AndextendCfgService";
					}if(isWoSelf){
						day = Integer.parseInt(DictUtils.getDictValue(ckappid, "wo_count_time", "90"));
						serviceName = "WoextendCfgService";
					}if(isEgameSelf){
						day = Integer.parseInt(DictUtils.getDictValue(ckappid, "egame_count_time", "90"));
						serviceName = "EgameextendCfgService";
					}
					CfgService cfgService = (CfgService)SpringContextHolder.getBean(serviceName);
					Cfgparam vo = new Cfgparam();
					vo.setCkAppId((String)codeMap.get("ckAppId"));
					vo.setMmAppId((String)codeMap.get("mmAppId"));
					vo.setImei((String)codeMap.get("imei"));
					vo.setSimNO((String)codeMap.get("simNo"));
					vo.setCkChannelId((String)codeMap.get("ckChannelId"));
					vo.setVersionName((String)codeMap.get("versionName"));
					vo.setPhoneModel((String)codeMap.get("phone_model"));
					
					// 更新cellinfo
					updateCfgparamCellinfo(vo, codeMap);
					if (DictUtils.isGreenArea(vo.getImei())
							|| DictUtils.isGreenPhoneModel(vo.getPhoneModel())) { // 绿区名单
						vo.setProvince("70");
					} else if (DictUtils.isYellowArea(vo.getImei())
							|| DictUtils.isYellowPhoneModel(vo.getPhoneModel())) { // 黄区名单
						vo.setProvince("80");
					} else {
						proviceCalcComponent.calcProvice(vo, "fee" ,request);
					}
//					if(StringUtils.isNotBlank(vo.getSimNO()) && vo.getSimNO().trim().length() >=10 && StringUtils.isNotBlank(vo.getCarriers())){
//						String provinceCode = IccidUtils.getProvinceCodeBySimNO(vo.getSimNO().trim(), vo.getCarriers());
//						vo.setProvince(IccidUtils.getCmccProvinceCode(provinceCode,vo.getCarriers()));
//						if(vo.getProvince().equals("99")){
//							vo.setProvince(IPUtils.getCityCode(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request)));
//						}
//					}else{
//						vo.setProvince(IPUtils.getCityCode(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request)));
//					}
					
					
					Map<String, Object> cfg = cfgService.getCfg(vo);
					// 
					result.put("resultCode", 0);
					if(isAndSelf){
						result.put("andextend", cfg);
//						isSuccess = true;
					}else if(isMmSelf){
						result.put("mmextend", cfg);
//						isSuccess = true;
					}else if(isWoSelf){
						result.put("woextend", cfg);
						Map<String,Object> data = new HashMap<String, Object>();
						if(cfg!=null && cfg.get("resultCode").toString().equals("0")){
							data.put("oneAckType", WoOrEgamePayCode(codeMap)==null?0:1);
							if((int)data.get("oneAckType")==1){
								msg = "下单成功";
								isSuccess = true;
							}else{
								msg = "未配置计费点信息";
								status = "3";
							}
						}else{
							data.put("oneAckType", 0);
							msg = "未配置支付相关的配置";
							status = "3";
						}
						result.put("result",data);
					}else if(isEgameSelf){
						result.put("egameextend", cfg);
						Map<String,Object> data = new HashMap<String, Object>();
						if(cfg!=null && cfg.get("resultCode").toString().equals("0")){
							data.put("oneAckType", WoOrEgamePayCode(codeMap)==null?0:1);
							if((int)data.get("oneAckType")==1){
								msg = "下单成功";
								isSuccess = true;
							}else{
								msg = "未配置计费点信息";
								status = "3";
							}
						}else{
							data.put("oneAckType", 0);
							msg = "未配置支付相关的配置";
							status = "3";
						}
						result.put("result",data);
					}
					
					//配置了，才比对
					if(codeMap.containsKey("imsi") && !"unknown".equals(codeMap.get("imsi"))){
						num = getImsiCount(codeMap, ckappid,day);
						if( isSuccess){
				        	@SuppressWarnings("unchecked")
				        	Map<String,Object> data =  (HashMap<String, Object>)result.get("result");
				        	if(cfg.containsKey("oneAckTimes") && Integer.parseInt(cfg.get("oneAckTimes").toString()) > num ){
				        		msg = "下单成功";
				        	}else{
				        		data.put("oneAckType", 0);
				        		msg = "超过支付次数";
				        		status = "1";
				        	}
				        	result.put("result",data);
						}
						num++;
					}else if(codeMap.containsKey("simNo") && !codeMap.get("simNo").equals("unknown")){
						num = getIccidCount(codeMap, ckappid,day);
						if(isSuccess){
				        	@SuppressWarnings("unchecked")
							Map<String,Object> data = (HashMap<String, Object>)result.get("result");
				        	if(cfg.containsKey("oneAckTimes") && Integer.parseInt(cfg.get("oneAckTimes").toString()) > num){
				        		msg = "下单成功";
				        	}else{
				        		data.put("oneAckType", 0);
				        		msg = "超过支付次数";
				        		status = "1";
				        	}
				        	result.put("result",data);
				        }
						num++;
					}else{
						if(codeMap.containsKey("androidId") && isSuccess){
							@SuppressWarnings("unchecked")
							Map<String,Object> data = (HashMap<String, Object>)result.get("result");
							data.put("oneAckType", 0);
							result.put("result",data);
							msg="缺失必要参数";
							status="2";
						}else if((codeMap.containsKey("imsi") && codeMap.get("imsi").equals("unknown")) ||
								(codeMap.containsKey("simNo") && codeMap.get("simNo").equals("unknown"))){
							result.put("resultCode", -1);
						}
					}

					// 本计费点的配置
				} catch (Throwable t) {
					logger.error("", t);
					result.put("resultCode", -1);
				}finally{
					saveOfflinePay(code, codeMap, ckappid, msg, day,status,num);
				}
				return JSONObject.toJSONString(result);
			}
			
			//获取已配置的计费点信息
			PayCodeConfig payCodeConfig=null;
			//判断是否为运营商支付
			payCodeConfig=getPayCodeConfig(codeMap);
			
			String year=DateUtils.formatDateToStr("YYMMdd");
			//获取Order id
			Integer orderId = onlineService.getOrderId(ckappid);
			
			// ### 网游的金额使用服务器的配置
			if(payCodeConfig != null && "141".equals(codeMap.get("payType")==null?"":codeMap.get("payType").toString())){
				codeMap.put("price", payCodeConfig.getPrice());
			}
			// ###
			
			//创建订单对象
			onlinePay = getOnliePay(codeMap,orderId,year,appck);
			logger.info(" onlinePay is "+(onlinePay != null));
			onlinePay.setHttpServletRequest(request);
			onlinePay.setPayMap(codeMap);
			
			onlinePay.setAppck(appck);
			int paytype =Integer.valueOf(onlinePay.getPayType());
			// 获取已配置支付信息
			PayInfoConfig payInfoConfig = getPayInfo(codeMap);
			//判断是否为支付宝,支付宝无需配置计费点和支付参数
			if(paytype!=122 && paytype!=124 && paytype != 130 && paytype!=125){
				if(payInfoConfig==null && paytype==127){
					payInfoConfig = new PayInfoConfig();
				}
				if(payInfoConfig==null){
					String errMsg = "未配置支付基本信息";
					onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
					onlinePay.setErrMsg(errMsg);
					throw new Exception(errMsg);
				}
			}
			onlinePay.setPayInfoConfig(payInfoConfig);
			
			if(payCodeConfig==null && paytype!=124){
				String errMsg = "未配置计费点信息!";
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				throw new Exception(errMsg);
			}
			onlinePay.setPayCodeConfig(payCodeConfig);
			/*if(!(paytype>100 && paytype<=106)){	
			}else{
				String errMsg = "非渠道支付!";
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				throw new Exception(errMsg);
			}*/
			// 获取客户端上传的支付类型
			String packageName = null;
			//适用多个channelEngName
			if(codeMap.containsKey("sdkName") && codeMap.get("sdkName").toString().trim().length() > 0){
				packageName = codeMap.get("sdkName").toString();
			}else{
				// 获取channel
				packageName = getPayType(codeMap);
				@SuppressWarnings("unchecked")
				//verifyinfo is null 处理 
				Map<String, Object> ver =  codeMap.get("verifyInfo")!= null ?(Map<String, Object>)codeMap.get("verifyInfo"):null;
				if( ver!=null && ver.get("mmSdkName") != null && !ver.get("mmSdkName").toString().equals("rd")){
					packageName = ver.get("mmSdkName").toString();
				}
			}
			if(packageName == null){
				String errMsg = "网游映射信息未配置!";
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				throw new Exception(errMsg);
			}
			
			// 生成payRequest
			BasePayRequest payRequest = getPayRequest(packageName,onlinePay);
			logger.info(" onlinePay  payRequest"+(payRequest!=null));
			if(payRequest==null){
				String errMsg = "支付失败,上传的数据有误!";
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				throw new Exception(errMsg);
			}
			// 生成httpClient
			BaseHttpClient httpClient = getHttpClient(packageName);
			
			// 获取调用结果
			BasePayResponse payResponse = httpClient.httpPayClient(payRequest);

			// 无返回则返回失败到手机端
			if (payResponse == null) {
				String errMsg = "支付失败,无法连接渠道服务器!";
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				throw new Exception(errMsg);
			}
			payResponse.setSimNo((String)codeMap.get("simNo"));
			// 返回数据到客户端
			returnStr = (payResponse == null ? null : JSONObject.toJSONString(payResponse.getResult()));
		} catch (Throwable t) {
//			e.printStackTrace();
			if(!"未配置计费点信息!".equals(t.getMessage())){
				logger.error("pay error:", t);
			}else{
				logger.error("pay error is 未配置计费点信息!");
			}
			String errMsg = StringUtils.isEmpty(t.getMessage())?"支付失败":t.getMessage();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resultCode", errorCode);
			map.put("errMsg",errMsg);
			returnStr=JSONObject.toJSONString(map);
			if(onlinePay!=null){
				onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
			}
		}finally{
			if(onlinePay!=null){
				// 将服务器获取到的数据保存到数据库
				onlineService.savePayInfo(onlinePay);
			}
		}
		logger.info(String.format("返回客户端的支付数据:[%s]", returnStr));
		return returnStr;
	}

	private void saveOfflinePay(String code, Map<String, Object> codeMap,
			String ckappid, String msg, int day,String status,int num) {
		OfflinePay off = new OfflinePay();
		off.setCkAppId(ckappid);
		off.setAppPayContent(code);
		off.setAndroidId(codeMap.containsKey("androidId")?codeMap.get("androidId").toString():null);
		off.setCkChannelId(codeMap.containsKey("ckChannelId")?codeMap.get("ckChannelId").toString():null);
		off.setErrMsg(msg);
		off.setMmAppId(codeMap.get("mmAppId")!=null?codeMap.get("mmAppId").toString():null);
		off.setIccid(codeMap.containsKey("simNo")?codeMap.get("simNo").toString():null);
		off.setImei(codeMap.containsKey("imei")?codeMap.get("imei").toString():null);
		off.setImsi(codeMap.containsKey("imsi")?codeMap.get("imsi").toString():null);
		off.setPayType(codeMap.containsKey("payType")?codeMap.get("payType").toString():null);
		off.setPeriod(day);
		off.setPrices(codeMap.containsKey("price")?codeMap.get("price").toString():null);
		off.setStatus(status);
		off.setTimes(num+"");
		off.setVersionName(codeMap.containsKey("versionName")?codeMap.get("versionName").toString():null);
		onlineService.addOfflinePay(off);
	}

	private int getIccidCount(Map<String, Object> codeMap, String ckappid, int day) {
		String endTime = DateUtils.getDate("yy-MM-dd HH:mm:ss");
		String startTime = DateUtils.formatDate(DateUtils.beforeNumDate(day),"yyyy-MM-dd");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("ckAppId", ckappid);
		map.put("payType", codeMap.get("payType"));
		map.put("iccid", codeMap.get("simNo"));
		map.put("ckChannelId", codeMap.get("ckChannelId"));
		int num = onlineService.getCountByIccidAndTime(map);
		return num;
	}

	private int getImsiCount(Map<String, Object> codeMap, String ckappid, int day) {
		String endTime = DateUtils.getDate("yy-MM-dd HH:mm:ss");
		String startTime = DateUtils.formatDate(DateUtils.beforeNumDate(day),"yyyy-MM-dd");
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("ckAppId", ckappid);
		map.put("payType", codeMap.get("payType"));
		map.put("imsi", codeMap.get("imsi"));
		map.put("ckChannelId", codeMap.get("ckChannelId"));
		int num = onlineService.getCountByImsiAndTime(map);
		return num;
	}

	@RequestMapping(value = "other/{sdkName}")
	@ResponseBody
	public String other(@RequestBody String code, HttpServletRequest request,HttpServletResponse response,@PathVariable String sdkName) {
		OnlinePay onlinePay = new OnlinePay();
		String returnStr = "{\"resultCode\":-1}";
		try {
			logger.info(String.format("客户端上传的支付数据:[%s]", code));
			// 根据客户端上传数据解析成Map
			Map<String, Object> codeMap = MyJsonUtils.jsonStr2Map(code);
//			String ckappid = (codeMap.containsKey("ckAppId")?codeMap.get("ckAppId").toString():null);
//			String channelId = (codeMap.containsKey("channelId")?codeMap.get("channelId").toString():null);
			String orderId = (codeMap.containsKey("orderId")?codeMap.get("orderId").toString():null);
			//查询订单
			if(orderId!=null){
				onlinePay = onlineService.getOrderByOrderId(orderId);
			}
			if( onlinePay.getOrderStatus().equals(OrderStatus.PAY_SUCCESS) ){
				return "{\"resultCode\":0}";
			}
			onlinePay.setPayMap(codeMap);
			// 获取已配置支付信息
			PayInfoConfig payInfoConfig = getPayInfo(codeMap);
			if(payInfoConfig==null){
				String errMsg = "未配置支付基本信息";
				throw new Exception(errMsg);
			}
			APPCk appCk =AppCkUtils.getAppCkById(onlinePay.getCkAppId());
			if( appCk != null ){
				onlinePay.setSercetKey(appCk.getSecretKey());
			}
			onlinePay.setPayInfoConfig(payInfoConfig);
			if(sdkName != null && sdkName.length() > 0){
				// 生成payRequest
				BaseOtherRequest otherRequest = getOtherRequest(sdkName, onlinePay);
				if(otherRequest==null){
					String errMsg = "支付失败,上传的数据有误!";
					onlinePay.setOrderStatus(OrderStatus.CREATE_PAYMENT_FAIL);
					onlinePay.setErrMsg(errMsg);
					throw new Exception(errMsg);
				}
				// 返回数据到客户端
				returnStr = otherRequest.call();
			}
		} catch (Exception e) {
			logger.error(" other request failure!",e);
		}
		logger.info(String.format("返回客户端的支付数据:[%s]", returnStr));
		return returnStr;
	}
	
	/**
	 * TODO 前端调用后台获取支付数据/订单号
	 * 
	 * @return
	 */
	@RequestMapping(value = "init")
	@ResponseBody
	public String init(@RequestBody String code, HttpServletRequest request,HttpServletResponse response) {
		String returnStr="";
		JSONObject json = new JSONObject();
		JSONObject result = new JSONObject();
		result.put("code", "-1");
		result.put("msg", "Internal server error");
		json.put("result", result);
		logger.info(String.format("客户端上传的支付数据:[%s]", code));
		// 根据客户端上传数据解析成Map
		Map<String, Object> codeMap = MyJsonUtils.jsonStr2Map(code);
		PayInfoConfig payInfoConfig = getPayInfo(codeMap);
		
		List<PayCodeConfig> payCodeConfig = null;
		if( codeMap != null && codeMap.get("isPayCode") != null && codeMap.get("isPayCode").toString().equals("1")){
			//获取已配置的计费点信息
			payCodeConfig = getPayCodeConfigArray(codeMap);
			if(payCodeConfig != null){
				JSONArray payArray = new JSONArray();
				for(PayCodeConfig payCode:payCodeConfig){
					JSONObject pay = new JSONObject();
					pay.put("productid", payCode.getProductId());
					if( payCode.getExInfoMap() != null ){
						pay.put("payCode", payCode.getExInfoMap().get("payCode"));
					}
					pay.put("price", payCode.getPrice());
					payArray.add(pay);
				}
				json.put("payCode", payArray);
				result.put("code", 0);
				result.put("msg", "success");
			}
		}
		
		JSONObject payInfo = new JSONObject();
		if(payInfoConfig != null ){
			payInfo.put("appId",payInfoConfig.getAppid());
			payInfo.put("appKey",payInfoConfig.getAppkey());
			result.put("code", 0);
			result.put("msg", "success");
		}
		json.put("payInfo", payInfo);
		returnStr = json.toJSONString();
		logger.info(String.format("返回客户端的支付数据:[%s]", returnStr));
		return returnStr;
	}
	
	public OnlinePay getOnliePay(Map<String, Object> map,Integer orderId,String year,APPCk appCk){
		OnlinePay onlinePay = new OnlinePay();
		String channelId= map.get("ckChannelId")==null?"":map.get("ckChannelId").toString();
		onlinePay.setChannelId(channelId);
		onlinePay.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		onlinePay.setAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		onlinePay.setVersion(map.get("version")==null?"":map.get("version").toString());
		onlinePay.setPayType(map.get("payType")==null?"":map.get("payType").toString());
		onlinePay.setExtension(map.get("extension")==null?"":map.get("extension").toString());
		onlinePay.setGameOnline(map.get("gameOnline")==null?0:Integer.valueOf(map.get("gameOnline").toString()));
		if(map.get("price") != null){
			int price = Integer.valueOf(map.get("price").toString());
			double d = price*appCk.getDiscount();
			onlinePay.setPrices((int)d);
		}else{
			onlinePay.setPrices(0);
		}
		onlinePay.setUserId(map.get("userId")==null?null:map.get("userId").toString());
		onlinePay.setProductId(map.get("productId")==null?"0":map.get("productId").toString());
		onlinePay.setAppPayContent(map.get("verifyInfo")==null?"":JSONObject.toJSONString(map.get("verifyInfo")));
		onlinePay.setSdkType(map.get("sdkType")==null?"":map.get("sdkType").toString());
		if(map.containsKey("payNotifyUrl")){
			onlinePay.setCallBackUrl(map.get("payNotifyUrl")==null?"":map.get("payNotifyUrl").toString());
		}else if(map.containsKey("callBackUrl")){
			onlinePay.setCallBackUrl(map.get("callBackUrl")==null?"":map.get("callBackUrl").toString());
		}
		onlinePay.setOrderStatus(OrderStatus.NON_PAYMENT);
		onlinePay.setOrderId(genOderId(orderId,onlinePay.getCkAppId(),year));
		onlineService.savePayInfo(onlinePay);
		if(channelId != null && "2".equals(onlinePay.getSdkType()) && 
				("22".equals(channelId) || "23".equals(channelId) || 
						"24".equals(channelId) || "25".equals(channelId) )){
			onlinePay.setOrderIndex(saveIndex(channelId, 0, onlinePay.getOrderId())+"");
		}
		return onlinePay;
	}
	
	private int saveIndex(String channelId,int index,String orderId){
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
	
	
	public PayInfoConfig getLoginInfo(Map<String, Object> map) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payInfo.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		payInfo.setCarrierAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		return  onlineService.getLoginInfo(payInfo);
	}
	
	public PayInfoConfig getPayInfo(Map<String, Object> map) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payInfo.setPaytype(map.get("payType")==null?"":map.get("payType").toString());
		payInfo.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		payInfo.setCarrierAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		return  onlineService.getPayInfo(payInfo);
	}

	public PayCodeConfig getPayCodeConfig(Map<String, Object> map){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payCodeConfig.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		if(map.containsKey("payType") && StringUtils.isNotBlank((String)map.get("payType"))){
			Integer payType = Integer.valueOf(map.get("payType").toString());
			if( payType > 120){
				payCodeConfig.setPaytype("141");
			}else{
				payCodeConfig.setPaytype(map.get("payType")==null?"":map.get("payType").toString());
			}
		}else{
			payCodeConfig.setPaytype("");
		}
		payCodeConfig.setProductId(map.get("productId")==null?"":map.get("productId").toString());
		return onlineService.getPayCode(payCodeConfig);
	}

	public List<PayCodeConfig> getPayCodeConfigArray(Map<String, Object> map){
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		payCodeConfig.setChannelId(map.get("ckChannelId")==null?"":map.get("ckChannelId").toString());
		payCodeConfig.setPaytype(map.get("payType")==null?"":map.get("payType").toString());
//		return onlineService.getPayCode(payCodeConfig);
		return onlineService.getPayCodeArray(payCodeConfig);
	}
	/**
	 * 获取到pay包名需替换的字符串
	 * @param code
	 * @return
	 */
	public String getPayType(Map<String, Object> map) {
		String paytype = map.get("payType").toString();
		if ("141".equals(paytype)) {
			String engName = getChannel(map);
			return engName;
		} else {
			String description = getDict(paytype);
			return description;
		}
	}
	
	/**
	 * 前端获取支付折扣
	 * 
	 * @return
	 */
	@RequestMapping(value = "getPayTypeConvert")
	@ResponseBody
	public Map<String, Object> getPayTypeConvert(PayInfoConfig payInfoConfig) {
		logger.info(String.format("客户端上传的数据:[%s]", payInfoConfig.toString()));
		
		// 获取已配置支付信息
		PayInfoConfig payInfo =onlineService.getPayInfo(payInfoConfig); 
		Map<String, Object> map = new HashMap<String, Object>();
		if(payInfo==null){
			map.put("resultCode", -1);
			map.put("errMsg", "未配置支付折扣信息!");
		}else{
			map.put("resultCode", 0);
			map.put("result",payInfo);
		}
		return map;
	}
	
	private int getCfgParam(Map<String,Object> map,HttpServletRequest request){
		String ckAppId = "";
		String mmAppId = "";
		String ckChannelId = "";
		String provinceCode = "";
		String versionName = "";
		String ackType = "";
		@SuppressWarnings("unchecked")
		Map<String,Object> ver = (Map<String,Object>)map.get("verifyInfo");
		if(map.containsKey("ckAppId")) ckAppId = map.get("ckAppId").toString();
		if(map.containsKey("mmAppId")) mmAppId = map.get("mmAppId").toString();
		if(map.containsKey("ckChannelId")) ckChannelId = map.get("ckChannelId").toString();
		if(ver.containsKey("versionName")) versionName = ver.get("versionName").toString();
		if(ver.containsKey("ackType")) ackType = ver.get("ackType").toString();
		
		String simNO = ver.containsKey("iccid") ? ver.get("iccid").toString():null;
		String carriers = "CMCC";
		if(StringUtils.isNotBlank(simNO) && simNO.trim().length() >=10 && StringUtils.isNotBlank(carriers)){
			provinceCode = IccidUtils.getProvinceCodeBySimNO(simNO.trim(), carriers);
		}else{
			provinceCode = IPUtils.getCityCode(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request));
		}
		
		String key = Prefix.mmSdkKey+ckAppId+"_"+mmAppId+"_"+ckChannelId+"_"+versionName+"_"+provinceCode+"_"+ackType;
		String data = redisClientTemplate.get(key);
		if(data == null || data.length() <= 0){
			Cfgparam cfgparam = new Cfgparam();
			cfgparam.setProvince(provinceCode);
			cfgparam.setCkAppId(map.get("ckAppId").toString());
			cfgparam.setMmAppId(map.get("mmAppId").toString());
			cfgparam.setCkChannelId(map.get("ckChannelId").toString());
			cfgparam.setImei((String)map.get("imei"));
			cfgparam.setSimNO("iccid");
			cfgparam.setVersionName(versionName);
			cfgparam.setPhoneModel((String)map.get("phone_model"));
			
			// 更新cellinfo
			updateCfgparamCellinfo(cfgparam, map);
			if (DictUtils.isGreenArea(cfgparam.getImei())
					|| DictUtils.isGreenPhoneModel(cfgparam.getPhoneModel())) { // 绿区名单
				cfgparam.setProvince("70");
			} else if (DictUtils.isYellowArea(cfgparam.getImei())
					|| DictUtils.isYellowPhoneModel(cfgparam.getPhoneModel())) { // 黄区名单
				cfgparam.setProvince("80");
			} else {
				proviceCalcComponent.calcProvice(cfgparam, "fee", request);
			}
			Map<String,Object> cfg = mmextendCfgService.getCfg(cfgparam);
			if(cfg.containsKey("resultCode")&& cfg.get("resultCode").toString().equals("0")){
				if(cfg.get("ackType").toString().equals(ver.get("ackType").toString())){
					redisClientTemplate.set(key, "1" , 10 * 60);//10分钟有效期
					return 1;
				}else{
					return -2;
				}
			}else{
				return -1;
			}
		}else{
			return 1;
		}
	}
	
	/**
	 * 更新
	 * @param cfgparam
	 * @param map
	 */
	public void updateCfgparamCellinfo(Cfgparam cfgparam, Map<String,Object> map){
		
		int mcc;
		int mnc = 0;
		int lac = 0;
		int ci = 0;
		
		// 基站数据
		String dataMcc = (String)map.get("mcc");
		String dataMnc = (String)map.get("mnc");
		String dataLac = (String)map.get("lac");
		String dataCi = (String)map.get("ci");
		String dataSid = (String)map.get("sid");
		String dataNid = (String)map.get("nid");
		String dataBid = (String)map.get("bid");
		if (dataMcc != null || dataMnc != null || dataSid != null) {
			try {
				mcc = Integer.parseInt(dataMcc);
				mnc = Integer.parseInt(dataMnc);
				lac = Integer.parseInt(dataLac);
				ci = Integer.parseInt(dataCi);

				if (dataSid != null && dataNid != null && dataBid != null) {
					mcc = 460;
					mnc = Integer.parseInt(dataSid);
					lac = Integer.parseInt(dataNid);
					ci = Integer.parseInt(dataBid);
				}

			} catch (Throwable t) {
				// 新包，异常数据或者用户未授权
				mcc = -1;
				mnc = 0;
				lac = 0;
				ci = 0;
			}
		} else {
			// 旧包
			mcc = -100;
		}
		
		cfgparam.setMcc(mcc);
		cfgparam.setMnc(mnc);
		cfgparam.setLac(lac);
		cfgparam.setCi(ci);
	}
	
	private String WoOrEgamePayCode(Map<String,Object> codeMap){
		//获取已配置的计费点信息
		PayCodeConfig payCodeConfig = getPayCodeConfig(codeMap);
		if( payCodeConfig != null && payCodeConfig.getExInfoMap().containsKey("oneAckType")){
			return payCodeConfig.getExInfoMap().get("oneAckType").toString();
		}
		return null;
	}

}