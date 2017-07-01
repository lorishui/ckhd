package me.ckhd.opengame.online.version;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.BaseHandle;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.OnlineService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.sys.utils.DictUtils;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("onlineVersion110")
public class OnlineVersion110 extends Version {

	@Autowired
	public OnlineService onlineService;
	@Autowired
	public RedisClientTemplate redisClientTemplate;

	// pay start
	@Override
	public String pay(JSONObject codeJson, HttpServletRequest request) {
		String engName = codeJson.getString("a");
		JSONObject result = new JSONObject();
		String resultStr = null;
		OnlinePay onlinePay = null;
		try {
			String ckAppid = codeJson.containsKey("ckAppId") ? codeJson.getString("ckAppId") : null;
			String ckChannelId = codeJson.containsKey("ckChannelId") ? codeJson.getString("ckChannelId") : null;
			String productId = codeJson.containsKey("productId") ? codeJson.getString("productId") : null;
			if (ckAppid == null || ckChannelId == null || productId == null) {
				result.put("resultCode", 3001);
				result.put("errMsg", "必要参数缺失");
				throw new Exception("3001");
			}
			// 获取游戏信息
			APPCk appck = AppCkUtils.getAppCkById(ckAppid);
			if (appck == null) {
				result.put("resultCode", 3002);
				result.put("errMsg", "游戏不存在");
				throw new Exception("3002");
			}
			// 获取已配置的计费点信息
			PayCodeConfig payCodeConfig = getPayCodeConfig(codeJson);
			if (payCodeConfig == null) {
				result.put("resultCode", 3003);
				result.put("errMsg", "未配置计费点信息");
				throw new Exception("3003");
			}

			String price = codeJson.containsKey("price") ? codeJson.getString("price") : null;
			// ### 网游的金额使用服务器的配置
			/*
			 * if( "141".equals(codeJson.get("payType")) ){ price =
			 * payCodeConfig.getPrice(); }
			 */
			// 白名单
			String imei = codeJson.containsKey("imei") ? codeJson.getString("imei") : "";
			if (StringUtils.isNotBlank(imei)) {
				String white_imei = DictUtils.getDictLabel(imei, "online_imei_white_list", "");
				if (!"1".equals(white_imei)) {
					price = payCodeConfig.getPrice();
				}
			} else {
				price = payCodeConfig.getPrice();
			}

			String year = DateUtils.formatDateToStr("YYMMdd");
			// 获取Order id
			Integer orderId = onlineService.getOrderId(ckAppid);
			// 创建订单对象
			onlinePay = getOnliePay(codeJson, orderId, year, appck, price);
			// 获取已配置的支付信息
			PayInfoConfig payInfoConfig = null;
			payInfoConfig = getPayInfoConfig(onlinePay);
			onlinePay.setPayCodeConfig(payCodeConfig);
			onlinePay.setPayInfoConfig(payInfoConfig);

			BaseHandle handle = getHandle(engName);
			if (handle != null) {
				onlinePay.setHttpServletRequest(request);
				onlinePay.setClientIp(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(onlinePay.getHttpServletRequest()));
				resultStr = handle.pay(onlinePay, codeJson);
			} else {
				result.put("resultCode", 2011);
				result.put("errMsg", "请求的内容不存在!");
			}
		} catch (Throwable e) {
			logger.error("下单失败!!!! ", e);
			if (result.size() == 0) {
				result.put("resultCode", 3004);
				result.put("errMsg", "内部错误");
			}
		} finally {
			if (onlinePay != null) {
				// 将服务器获取到的数据保存到数据库
				onlineService.savePayInfo(onlinePay);
			}
		}
		if (StringUtils.isBlank(resultStr)) {
			resultStr = result.toJSONString();
		}
		logger.info(String.format("支付返回客户端的数据:[%s]", resultStr));
		return resultStr;
	}

	public PayCodeConfig getPayCodeConfig(JSONObject condeJson) {
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(condeJson.containsKey("ckAppId") ? condeJson.getString("ckAppId") : "");
		payCodeConfig.setChannelId(condeJson.containsKey("ckChannelId") ? condeJson.getString("ckChannelId") : "");
		
		payCodeConfig.setPaytype("141");
		if ((condeJson.containsKey("productId")) && (StringUtils.isNotBlank(condeJson.getString("productId")))) {
			payCodeConfig.setProductId(condeJson.containsKey("productId") ? condeJson.getString("productId") : "");
			return this.onlineService.getPayCode(payCodeConfig);
		}
		return null;
	}

	public OnlinePay getOnliePay(JSONObject codeJson, Integer orderId, String year, APPCk appCk, String price) {
		OnlinePay onlinePay = new OnlinePay();
		String channelId = codeJson.getString("ckChannelId");
		onlinePay.setChannelId(codeJson.getString("ckChannelId"));
		onlinePay.setCkAppId(codeJson.getString("ckAppId"));
		onlinePay.setAppId(codeJson.containsKey("appId") ? codeJson.getString("appId") : "");
		onlinePay.setVersion(codeJson.containsKey("version") ? codeJson.getString("version") : "");
		onlinePay.setPayType(codeJson.containsKey("payType") ? codeJson.getString("payType") : "");
		onlinePay.setExtension(codeJson.containsKey("extension") ? codeJson.getString("extension") : "");
		onlinePay.setGameOnline(codeJson.containsKey("gameOnline") ? codeJson.getInteger("gameOnline").intValue() : 0);
		if (price != null) {
			int prices = Integer.valueOf(price).intValue();
			double d = prices * appCk.getDiscount();
			onlinePay.setPrices((int) d);
		} else {
			onlinePay.setPrices(0);
		}
		onlinePay.setUserId(codeJson.containsKey("userId") ? codeJson.getString("userId") : "");
		onlinePay.setProductId(codeJson.containsKey("productId") ? codeJson.getString("productId") : "");
		onlinePay.setAppPayContent(codeJson.toJSONString());
		onlinePay.setSdkType(codeJson.containsKey("sdkType") ? codeJson.getString("sdkType") : "");
		if (codeJson.containsKey("payNotifyUrl")) {
			onlinePay.setCallBackUrl(codeJson.getString("payNotifyUrl"));
		} else if (codeJson.containsKey("callBackUrl")) {
			onlinePay.setCallBackUrl(codeJson.getString("callBackUrl"));
		}
		onlinePay.setOrderStatus("0");
		onlinePay.setOrderId(getOderId(orderId.intValue(), onlinePay.getCkAppId(), year));
		this.onlineService.savePayInfo(onlinePay);
		if ((channelId != null) && (("22".equals(channelId)) || ("23".equals(channelId)) || ("24".equals(channelId)) || ("25".equals(channelId)))) {
			onlinePay.setOrderIndex(saveIndex(channelId, 0, onlinePay.getOrderId()) + "");
		}
		return onlinePay;
	}

	private int saveIndex(String channelId, int index, String orderId) {
		int orderIndex = 0;
		try {
			orderIndex = this.onlineService.saveOrderIndex(orderId);
		} catch (Exception e) {
			if (index < 3) {
				orderIndex = saveIndex(channelId, index++, orderId);
			} else {
				throw e;
			}
		}
		return orderIndex;
	}

	private String getOderId(int orderId, String ckAppId, String year) {
		StringBuffer strBuff = new StringBuffer();

		String appCode = null;
		if (ckAppId.startsWith("10")) {
			appCode = ckAppId.substring(ckAppId.length() - 2);
		} else {
			int y = Integer.parseInt(ckAppId);

			y = y - 1000 + 234;
			appCode = Integer.toString(y, 36);
		}
		strBuff.append(appCode);
		strBuff.append(year);

		String orderStr = "000000" + orderId;
		orderStr = orderStr.substring(orderStr.length() - 6);
		strBuff.append(orderStr);

		Random random = new Random();
		strBuff.append(String.valueOf(10 + random.nextInt(90)));
		return strBuff.toString();
	}

	public String login(JSONObject codeJson, HttpServletRequest request) {
		String engName = codeJson.getString("c");
		JSONObject json = new JSONObject();

		PayInfoConfig payInfo = getLoginInfo(codeJson);
		String resultStr = null;
		if (payInfo == null) {
			json.put("resultCode", 2010);
			json.put("errMsg", "单机基本信息未配置!");
		} else {
			OnlinePay onlinePay = new OnlinePay();
			onlinePay.setPayInfoConfig(payInfo);
			BaseHandle handle = getHandle(engName);
			if (handle != null) {
				OnlineUser user = new OnlineUser();
				resultStr = handle.login(user, codeJson, payInfo);
				JSONObject result = JSONObject.parseObject(resultStr);
				if (result.getInteger("resultCode").intValue() == 0) {
					OnlineUser oldUser = this.onlineService.get(user);
					if (oldUser != null) {
						user.setUid(oldUser.getUid());
						user.setUpdateDate(new Date());
					}
					this.onlineService.saveUserInfo(user);
				}
			} else {
				json.put("resultCode", 2011);
				json.put("errMsg", "请求的内容不存在!");
			}
		}
		if (StringUtils.isBlank(resultStr)) {
			resultStr = json.toJSONString();
		}
		this.logger.info(String.format("返回客户端的数据:[%s]", resultStr));
		return json.toJSONString();
	}

	public PayInfoConfig getLoginInfo(JSONObject codeJson) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(codeJson.containsKey("ckAppId") ? codeJson.getString("ckAppId") : "");
		payInfo.setChannelId(codeJson.containsKey("ckChannelId") ? codeJson.getString("ckChannelId") : "");
		payInfo.setCarrierAppId(codeJson.containsKey("sdkVersion") ? codeJson.getString("sdkVersion") : "");
		return this.onlineService.getLoginInfo(payInfo);
	}

	public String callback(String code, String engName, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		String returnStr = null;
		OnlinePay newPay = null;
		OnlinePay onlinePay = null;
		BaseHandle handle = null;
		try {
			//安智回调地址sdkname需写成anzhi+ckappid格式，例如anzhi1001
			onlinePay = new OnlinePay();
			if("anzhi".equals(engName.substring(0, 5))){
				OnlinePay ob = new OnlinePay();
				ob.setCkAppId(engName.substring(5,engName.length()));
				ob.setChannelId("9");
				ob.setPayType("141");
				PayInfoConfig payInfo = getPayInfoConfig(ob);
				onlinePay.setPayInfoConfig(payInfo);
				engName = "anzhi";
			}
			handle = (BaseHandle) SpringContextHolder.getBean(engName);
			handle.parseParamter(code, request, onlinePay);
			if (StringUtils.isNotBlank(onlinePay.getOrderId())) {
				newPay = this.onlineService.getOrderByOrderId(onlinePay.getOrderId());
				if (newPay == null) {
					onlinePay.setErrMsg("0");		//客户端生成的订单号  errMsg设为0
					onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
					this.onlineService.savePayInfo(onlinePay);
					this.logger.info(String.format("插入订单号为[%s]的订单!", onlinePay.getOrderId()));
				}
			} else if (StringUtils.isNotBlank(onlinePay.getChannelOrderId())) {
				newPay = this.onlineService.getOrderByChannelOrderIdOther(onlinePay.getChannelOrderId());
				if (newPay == null) {
					newPay = this.onlineService.getOrderByPrepayid(onlinePay.getChannelOrderId());
					if (newPay == null) {
						this.logger.info(String.format("插入渠道订单号为[%s]的订单!", onlinePay.getChannelOrderId()));
						String ckappid = onlinePay.getCkAppId();
						if (ckappid == null) {
							ckappid = "1000";			//客户端生成的订单号  ckappId设为1000
						}
						String year = DateUtils.formatDateToStr("YYMMdd");
						Integer odId = Integer.valueOf(this.onlineService.getOrderId(ckappid));
						String orderId = getOderId(odId.intValue(), onlinePay.getCkAppId(), year);
						onlinePay.setOrderId(orderId);
						onlinePay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						onlinePay.setErrMsg("0");		//客户端生成的订单号  errMsg设为0
						this.onlineService.savePayInfo(onlinePay);
					}
				}
			} else {
				json.put("resultCode", 4004);
				json.put("errMsg", "无法定位记录");
				throw new Exception();
			}
			if ("0".equals(onlinePay.getErrMsg())) { return handle.getReturnSuccess(); }
			if( newPay != null ){
				if ("3".equals(newPay.getOrderStatus())) {
					throw new Exception("重复发送");
				}
				PayCodeConfig payCodeConfig = getPayCodeConfig(newPay);
				newPay.setPayCodeConfig(payCodeConfig);
				PayInfoConfig payInfoConfig = getPayInfoConfig(newPay);
				newPay.setPayInfoConfig(payInfoConfig);
				JSONObject result = new JSONObject();
				returnStr = handle.verifyData(newPay, result, response);
				if (result.getInteger("code").intValue() == 2000) {
					APPCk appCk =AppCkUtils.getAppCkById(newPay.getCkAppId());
					if( StringUtils.isNotBlank(appCk.getPayCallbackUrl()) || StringUtils.isNotBlank(newPay.getCallBackUrl()) ){
						newPay.setSercetKey(appCk.getSecretKey());
						newPay.setIsTest(onlinePay.getIsTest());
					
						newPay.setProductName(payCodeConfig == null ? null : payCodeConfig.getProductName());
						newPay.setErrMsg("");
						newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						newPay.setSendStatus(OrderStatus.SEND_DOWN_ING);
						newPay.setCallBackContent(code);
						newPay.setActualAmount(onlinePay.getActualAmount());
						newPay.setChannelOrderId(onlinePay.getChannelOrderId());
						newPay.setCallBackContent(onlinePay.getCallBackContent());
						Map<String, Object> map = handle.getSendOrder(newPay);
						newPay.setSendNum(Integer.valueOf(map.get("sendNum")==null?"0":map.get("sendNum").toString()));
						newPay.setErrMsg("");
						newPay.setContent(map.get("content").toString());
						//加入发货队列
						OrderSenderBoot.getInstance().add(newPay);
					}else{
						newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
						newPay.setErrMsg("");
						newPay.setIsTest(onlinePay.getIsTest());
						newPay.setCallBackContent(onlinePay.getCallBackContent());
						if(StringUtils.isBlank(onlinePay.getActualAmount())){
							newPay.setActualAmount(onlinePay.getActualAmount());
						}
						newPay.setChannelOrderId(onlinePay.getChannelOrderId());
					}
				} else {
					newPay.setOrderStatus(OrderStatus.PAY_FAIL);
					newPay.setCallBackContent(code);
					throw new Exception();
				}
				onlineService.savePayInfo(newPay);
			}
		} catch (Exception e) {
			if (json.size() == 0) {
				json.put("resultCode", 4002);
				json.put("errMsg", "内部错误");
			}
			this.logger.info("支付异常提示：" + json.toJSONString());
			this.logger.error("支付发生错误：" + e.getMessage(), e);
		} finally {
			if( handle != null ){
				returnStr = handle.getReturnSuccess();
			}
		}
		if (returnStr == null) {
			returnStr = json.toJSONString();
		}
		this.logger.info(String.format("返回渠道的数据%s:[%s]", engName, returnStr));
		return returnStr;
	}

	public PayCodeConfig getPayCodeConfig(OnlinePay onlinePay) {
		PayCodeConfig payCodeConfig = new PayCodeConfig();
		payCodeConfig.setCkAppId(onlinePay.getCkAppId());
		payCodeConfig.setChannelId(onlinePay.getChannelId());
		payCodeConfig.setPaytype("141");
		payCodeConfig.setProductId(onlinePay.getProductId());
		return this.onlineService.getPayCode(payCodeConfig);
	}

	public PayInfoConfig getPayInfoConfig(OnlinePay onlinePay) {
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setCkAppId(onlinePay.getCkAppId());

		payInfo.setPaytype(onlinePay.getPayType());
		payInfo.setChannelId(onlinePay.getChannelId());
		return this.onlineService.getPayInfo(payInfo);
	}

	private BaseHandle getHandle(String engName) {
		BaseHandle BaseHandle = null;
		try {
			BaseHandle = (BaseHandle) SpringContextHolder.getBean(engName);
		} catch (Throwable e) {
			this.logger.error("spring获取bean出问题!!!!", e);
		}
		return BaseHandle;
	}
}
