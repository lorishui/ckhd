package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.DecimalFormatUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.wft.HttpUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("easypayNew")
@Scope("prototype")
public class easypayNewHandle extends BaseHandle{

//	private static String old_pay_url = "https://gdnybank.chejingji.com/aps/cloudplatform/api/trade.html";
	private static String pay_url = "https://api.onepaypass.com/aps/cloudplatform/api/trade.html";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		JSONObject json = new JSONObject();
		json.put("tradeType", "cs.pay.submit");
		json.put("version", "1.3");;
		//微信：wxApp 支付宝：alipayApp
		String channel = "wxApp";
		JSONObject verify = codeJson.getJSONObject("verifyInfo");
		if( verify != null && verify.containsKey("easyPayType") && 1 == verify.getInteger("easyPayType") ){
			channel = "alipayApp";
		}
		json.put("channel", channel);
		json.put("mchId", onlinePay.getPayInfoConfig().getAppid());
		json.put("body", onlinePay.getPayCodeConfig().getProductName());
		json.put("outTradeNo", onlinePay.getOrderId());
		json.put("amount", DecimalFormatUtils.getDoubleFormat(onlinePay.getPrices()/100d));
		json.put("subject", onlinePay.getPayCodeConfig().getProductName());
		json.put("mobileAppId", onlinePay.getPayInfoConfig().getExInfoMap().get("appid"));
		json.put("notifyUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		String signContent = SignContext.getSignContext(json)+"&key="+onlinePay.getPayInfoConfig().getAppkey();
		log.info("easy new pay sign content:"+signContent);
		JSONObject extra = new JSONObject();
		extra.put("mobileAppId", onlinePay.getPayInfoConfig().getExInfoMap().get("appid"));
		extra.put("notifyUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		json.put("extra", extra);
		json.remove("mobileAppId");
		json.remove("notifyUrl");
		String sign = CoderUtils.md5(signContent, "utf-8").toUpperCase();
		json.put("sign", sign);
		String respStr = HttpUtils.post(pay_url, json.toJSONString(), "utf-8");
		log.info("easyPayNew pay_url:"+pay_url);
		log.info("easyPayNew data:"+json.toJSONString());
		log.info("easyPayNew response:"+respStr);
		if( StringUtils.isNotBlank(respStr) ){
			JSONObject respJson = JSONObject.parseObject(respStr);
			if( respJson.containsKey("returnCode") && 0 == respJson.getInteger("returnCode") ){
				if( respJson.containsKey("resultCode") && 0 == respJson.getInteger("resultCode") ){
					String signRContent = SignContext.getSignContext(respJson)+"&key="+onlinePay.getPayInfoConfig().getAppkey();
					log.info("easy new pay response sign response content:"+signRContent);
					String rsign = CoderUtils.md5(signRContent, "utf-8").toUpperCase();
					if( respJson.containsKey("sign") && rsign.equals(respJson.getString("sign")) ){
						result.put("resultCode", 0);
						result.put("errMsg","SUCCESS");
						onlinePay.setPrepayid(respJson.getString("outChannelNo"));
						JSONObject data = new JSONObject();
						data.put("payCode", respJson.getString("payCode"));
						data.put("appId", onlinePay.getPayInfoConfig().getExInfoMap().get("appid"));
						data.put("wxMchId",onlinePay.getPayInfoConfig().getExInfoMap().get("wx_mch_id"));
						result.put("result", data);
					}else{
						result.put("errMsg","支付加密校验失败！");
					}
				}else{
					result.put("errMsg","easy服务端下单失败！");
				}
			}else{
				result.put("errMsg","easy服务端通讯失败！");
			}
		}else{
			result.put("errMsg","http请求easy服务端失败！");
		}
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		if(StringUtils.isNotBlank(code)){
			respData = JSONObject.parseObject(code);
		}else{
			for( Object key : request.getParameterMap().keySet() ){
				respData.put((String) key, request.getParameter((String)key));
			}
		}
		if( respData != null && respData.size() >= 0){
			onlinePay.setOrderId(respData.getString("outTradeNo"));
			onlinePay.setActualAmount(respData.containsKey("amount")?(respData.getDouble("amount")*100)+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("outChannelNo"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("returnCode") && 0 == respData.getInteger("returnCode") ){
			if( respData.containsKey("resultCode") && 0 == respData.getInteger("resultCode") ){
				if( respData.containsKey("status") && "02".equals(respData.getString("status")) ){
					String signContent = SignContext.getSignContext(respData)+"&key="+onlinePay.getPayInfoConfig().getAppkey();
					log.info("easy new pay response sign content:"+signContent);
					String sign = CoderUtils.md5(signContent, "utf-8").toUpperCase();
					if( respData.containsKey("sign") && sign.equals(respData.getString("sign")) ){
						result.put("code", 2000);
						return getReturnSuccess();
					}else{
						result.put("code", 4006);
						result.put("errMsg", "easy callback支付验证错误！");
						return getReturnFailure();
					}
				}else{
					result.put("code", 4007);
					result.put("errMsg", "easy callback失败订单！");
					return getReturnFailure();
				}
			}else{
				result.put("code", 4008);
				result.put("errMsg", "easy callback失败订单！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4009);
			result.put("errMsg", "easy callback错误数据！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}

	@Override
	public String getReturnFailure() {
		return "FAILURE";
	}

}
