package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpUtils;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 蓝海支付接口
 * @author wizard
 * @see https://doc.rightyoo.net/docs/payapi
 */
@Component("hailan")
@Scope("prototype")
public class hailanHandle extends BaseHandle{

	public static final String TRADE_TYPE_H5			= "H5";
	public static final String PAY_CHANNEL_WECHAT		= "wechat";
	public static final String PAY_CHANNEL_ALIPAY		= "alipay";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		return null;
	}
	
	/**
	 * 参数：ckAppId、ckChannelId、productId、subCkAppId、verifyInfo.appName、verifyInfo.packageName、verifyInfo.bundleId
	 */
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		String uid = getRandomString();
		Map<String, Object> ret = new HashMap<String, Object>();
		try {
			Result<JsonObject> result = pay0(uid, onlinePay, codeJson);
			ret.put("resultCode", result.getCode());
			ret.put("errMsg", result.getMessage());
			ret.put("result", result.getData());
			
			//转换成统一的属性
			result.getData().add("orderId", result.getData().get("out_trade_no"));
		}
		catch(Exception e) {
			log.error("[" + uid + "]" + e.getMessage(), e);
			
			ret.put("resultCode", -1);
			ret.put("errMsg", "[FAILURE]" + e.getMessage());
		}
		return new Gson().toJson(ret);
	}
	/**
	 * 使用H5方式支付
	 * @see https://doc.rightyoo.net/docs/payapi/unifiedorder
	 * @param uid
	 * @param onlinePay
	 * @param codeJson
	 * @return
	 * @throws EncoderException
	 * @throws UnsupportedEncodingException 
	 * @throws ConfigurationException 
	 */
	private Result<JsonObject> pay0(String uid, OnlinePay onlinePay, JSONObject codeJson) throws EncoderException, UnsupportedEncodingException, ConfigurationException {

		String payUrl = getConfig("hailan.pay_url", "https://api.rightyoo.net/v1/?service=Pay.Unifiedorder");
		
		int connTimeout = getConfig("hailan.connect_timeout", 1000 * 5);
		int readTimeout = getConfig("hailan.request_timeout", 1000 * 5);

		Assert.notNull(onlinePay.getPayInfoConfig(), "payInfoConfig can't be null.");
		Assert.notNull(onlinePay.getPayInfoConfig().getExInfoMap(), "payInfoConfig.extInfoMap can't be null.");
		
		String notifyUrl = isBlank(onlinePay.getNotifyUrl()) ? onlinePay.getPayInfoConfig().getNotifyUrl() : onlinePay.getNotifyUrl();
		Assert.hasText(notifyUrl, "notify url can't be null.");
		
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		Assert.notNull(verifyInfo, "verifyInfo can't be null.");
		
		String appName = verifyInfo.getString("appName");
		if( isBlank(appName) ) {
			appName = (String) onlinePay.getPayInfoConfig().getExInfoMap().get("appName");
		}
		Assert.hasText(appName, "appName can't be null.");
		
		String packageName = verifyInfo.getString("appPackage");
		if( isBlank(packageName) ) {
			packageName = (String) onlinePay.getPayInfoConfig().getExInfoMap().get("appPackage");
		}
		//Assert.hasText(packageName, "appPackage can't be null.");
		
		String bundleId = verifyInfo.getString("bundleId");
		if( isBlank(bundleId) ) {
			bundleId = (String) onlinePay.getPayInfoConfig().getExInfoMap().get("bundleId");
		}
		if( isBlank(bundleId) ) {
			bundleId = packageName;
		}
		//Assert.hasText(bundleId, "bundleId can't be null.");
		Assert.isTrue(!StringUtils.isBlank(packageName) || !StringUtils.isBlank(bundleId), "appPackage or bundleId can't be null.");
		
		String sceneInfo = String.format("app_name=%s&package_name=%s&bundle_id=%s", appName, packageName, bundleId);
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("app_id", onlinePay.getPayInfoConfig().getAppid());							//应用id
		map.put("mch_id", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));		//商户号
		map.put("nonce_str", uid);															//随机字符串，不长于32位
		map.put("pay_channel", PAY_CHANNEL_WECHAT);										//范围：wechat/alipay
		map.put("out_trade_no",  onlinePay.getOrderId());									//商户系统内部的订单号,32个字符内、可包含字母,
		map.put("body", onlinePay.getPayCodeConfig().getProductName());						//商品或支付单简要描述
		map.put("total_fee",onlinePay.getPrices());											//订单总金额，单位为分
		map.put("spbill_create_ip", onlinePay.getClientIp());								//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
		map.put("notify_url", notifyUrl);													//接收微信支付异步通知回调地址
		map.put("trade_type", TRADE_TYPE_H5);												//交易类型，范围：JSAPI/APP/NATIVE/H5
		map.put("scene_info", sceneInfo);													//自定义字段,H5支付的scene_info字段为必传,
		map.put("product_id", onlinePay.getProductId());									//商品ID,此参数为二维码中包含的商品ID
		map.put("sign",getSign(map, onlinePay.getPayInfoConfig().getAppkey()));				//签名
		
		String request = HttpUtils.toHttpParameter(map);

		String response = HttpUtils.post(payUrl, request, "application/x-www-form-urlencoded", connTimeout, readTimeout);

		JsonObject json = isBlank(response) ? new JsonObject() : new JsonParser().parse(response).getAsJsonObject();
		
		Result<JsonObject> result = new Result<JsonObject>();
		
		int code = json.get("ret") == null ? -1 : json.get("ret").getAsInt();
		if( code == 200 ) {
			result.setCode(0);
			result.setMessage("SUCCESS");
			result.setData(json.get("data") == null ? new JsonObject() : json.get("data").getAsJsonObject());
		}
		else {
			result.setCode(code == 0 ? -1 : code);
			result.setMessage("[FAILURE]" + (isBlank(json.get("msg")) ? "" : json.get("msg").getAsString()));
			result.setData(json.get("data") == null ? new JsonObject() : json.get("data").getAsJsonObject());
		}
		return result;
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		log.info("parseParamter:{}", code);
		respData = JSON.parseObject(code);
		if( !respData.isEmpty() ){
			onlinePay.setOrderId( respData.getString("out_trade_no") );
			onlinePay.setActualAmount( respData.containsKey("total_fee")?respData.getString("total_fee"):"0" );
			onlinePay.setChannelOrderId( respData.getString("transaction_id") );
			onlinePay.setCallBackContent( code );
		}
	}
	
	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String signNew = CoderUtils.md5(SignContext.getSignContext(respData)+"&key="+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
		String sign = respData.getString("sign");
		if(respData.containsKey("sign") && signNew.equals(sign.toLowerCase())){
			result.put("code", 2000);
			return getReturnSuccess();
		}
		else{
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
			return getReturnFailure();
		}
	}

	/**
	 * @see https://doc.rightyoo.net/docs/payapi/7
	 */
	@Override
	public String getReturnSuccess() { return "SUCCESS"; }

	/**
	 * @see https://doc.rightyoo.net/docs/payapi/7
	 */
	@Override
	public String getReturnFailure() { return "FAIL"; }
	
	@Override
	public Result<Void> queryOrderState(OnlinePay onlinePay,JSONObject codeJson) {
		String uid = getRandomString();
		try {
			return queryOrderState0(uid, onlinePay, codeJson);
		}
		catch(Exception e) {
			log.error("[" + uid + "]" + e.getMessage(), e);
			
			Result<Void> result = new Result<Void>();
			result.setCode(-1);
			result.setMessage("Internal Server Error." + e.getMessage());
			return result;
		}
	}
	private Result<Void> queryOrderState0(String uid, OnlinePay onlinePay,JSONObject codeJson) throws UnsupportedEncodingException, ConfigurationException {

		String url = getConfig("hailan.query_url", "https://api.rightyoo.net/v1/?service=Pay.OrderQuery");

		int connTimeout = getConfig("hailan.connect_timeout", 1000 * 5);
		int readTimeout = getConfig("hailan.request_timeout", 1000 * 5);

		Assert.notNull(onlinePay.getPayInfoConfig(), "payInfoConfig can't be null.");
		Assert.notNull(onlinePay.getPayInfoConfig().getExInfoMap(), "payInfoConfig.extInfoMap can't be null.");
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("app_id", onlinePay.getPayInfoConfig().getAppid());							//应用id
		map.put("mch_id", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));		//商户号
		map.put("nonce_str", uid);															//随机字符串，不长于32位
		map.put("pay_channel", PAY_CHANNEL_WECHAT);										//范围：wechat/alipay
		map.put("out_trade_no",  onlinePay.getOrderId());									//商户系统内部的订单号,32个字符内、可包含字母,
		map.put("sign",getSign(map, onlinePay.getPayInfoConfig().getAppkey()));				//签名
		
		String request = HttpUtils.toHttpParameter(map);

		String response = HttpUtils.post(url, request, "application/x-www-form-urlencoded", connTimeout, readTimeout);

		JsonObject json = isBlank(response) ? new JsonObject() : new JsonParser().parse(response).getAsJsonObject();
		
		Result<Void> result = new Result<Void>();

		int code = json.get("ret") == null ? -1 : json.get("ret").getAsInt();
		if( code == 200 ) {
			
			JsonObject data = json.get("data") == null ? new JsonObject() : json.get("data").getAsJsonObject();
			
			String tradeState = data.get("trade_state").getAsString();
			
			if( "SUCCESS".equalsIgnoreCase(tradeState) ) {
				result.setCode(0);
				result.setMessage(tradeState);
			}
			else {
				result.setCode(-1);
				result.setMessage(tradeState);
			}
		}
		else {
			result.setCode(code == 0 ? -1 : code);
			result.setMessage("query failure." + (isBlank(json.get("msg")) ? "" : json.get("msg").getAsString()));
		}
		
		return result;
	}
	

	private String getRandomString(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	private String getSign(Map<String, Object> params, String appKey) {
		List<String> keys = new ArrayList<String>();
		for(Map.Entry<String, Object> entry : params.entrySet()) {
			if( !isBlank(entry.getValue()) ) {
				keys.add(entry.getKey());
			}
		}
		Collections.sort(keys);
		
		List<String> strs = new ArrayList<String>();
		for(String key : keys) {
			strs.add(key + "=" + params.get(key));
		}
		strs.add("key=" + appKey);
		return DigestUtils.md5Hex(StringUtils.join(strs, "&")).toUpperCase();
	}
	
	private boolean isBlank(Object str){ return str == null || StringUtils.isBlank(str.toString()); }
	
	
	private static PropertiesConfiguration config = null;
	private synchronized PropertiesConfiguration getConfig() throws ConfigurationException {
		if( config == null ) {
			config = new PropertiesConfiguration(getClass().getResource("hailanHandle.properties"));
			config.setReloadingStrategy(new FileChangedReloadingStrategy());	
		}
		return config;
	}
	private String getConfig(String key, String defVal) throws ConfigurationException {
		return getConfig().getString(key, defVal);
	}
	private Integer getConfig(String key, Integer defVal) throws ConfigurationException {
		String val = getConfig(key, (String) null);
		return StringUtils.isNumeric(val) ? Integer.valueOf(val) : defVal;
	}
	
	
	
	public static void main(String argc[]) {
		
		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setNotifyUrl("http://i16785s665.iok.la/netpay/callback/hailan/1.1.0");
		onlinePay.setPayInfoConfig(new PayInfoConfig());
		onlinePay.getPayInfoConfig().setAppid("W1571260");
		onlinePay.getPayInfoConfig().setAppkey("86744848512621299762214803775184");
		onlinePay.getPayInfoConfig().setExInfo("{mch_id:100100112}");
		onlinePay.setOrderId("ya1706290005c014");
		onlinePay.setPayCodeConfig(new PayCodeConfig());
		onlinePay.getPayCodeConfig().setProductName("1");
		onlinePay.setPrices(1);
		onlinePay.setClientIp("192.168.2.122");
		onlinePay.setProductId("1");
		
		JSONObject codeJson = new JSONObject();
		codeJson.put("act", 1002);
		codeJson.put("userId", "unknow");
		codeJson.put("extension", "gameData");
		codeJson.put("notifyUrl", "http://i16785s665.iok.la/netpay/callback/hailan/1.1.0");
		codeJson.put("subCkChannelId", "1");
		codeJson.put("androidId", "aaf6bf16b7f9616a");
		codeJson.put("roleId", "1");
		codeJson.put("ckAppId", "2000");
		codeJson.put("imei", "862964037987934");
		codeJson.put("zoneId", "1");
		codeJson.put("osVersion", "Android22");
		codeJson.put("gameOnline", "1");
		codeJson.put("imsi", "460022800484146");
		codeJson.put("ckChannelId", "103");
		codeJson.put("payNotifyUrl", "www.cp.server");
		codeJson.put("osVersionName", "5.1");
		codeJson.put("subCkAppId", "1");
		codeJson.put("payType", "131");
		codeJson.put("price", "1");
		codeJson.put("mmAppId", "300009127531");
		codeJson.put("serverId", "1");
		codeJson.put("phone_model", "OPPO A37m");
		codeJson.put("productId", "1");
		codeJson.put("simNo", "898600c81316f1033046");
		codeJson.put("isTest", "0");
		
		//System.out.println( new hailanHandle().pay(onlinePay, codeJson) );
		System.out.println( new Gson().toJson(new hailanHandle().queryOrderState(onlinePay, codeJson)) );
	}
}
