package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.util.egame.openapi.common.RequestParasUtil;

/**
 * 中国电信爱游戏统一支付
 * @author wizard
 */
@Component("egame")
@Scope("prototype")
public class egameHandle extends BaseHandle{
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		try {
			return login0(onlineUser, codeJson, payInfo);
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);

			JSONObject result = new JSONObject();
			result.put("resultCode", -1);
			result.put("errMsg", "Internal server error.");
			return result.toJSONString();
		}
	}
	public String login0(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) throws Exception {
		
		JSONObject verifyInfo = JSONObject.parseObject(codeJson.getString("verifyInfo"));
		Assert.notNull(verifyInfo, "verifyInfo can't be null.");
		
		String clientId = (String) payInfo.getExInfoMap().get("client_id");
		String clientSecret = (String) payInfo.getExInfoMap().get("client_secret");
		
		String grantType = codeJson.getString("grant_type");
		String scope = codeJson.getString("scope");
		String state = codeJson.getString("state");
		String code = verifyInfo.getString("code");
		
		Result<JSONObject> ret = oauthToken(clientId, clientSecret, code, grantType, scope, state);
		
		JSONObject result = new JSONObject();
		if( ret.getCode() == 0 ) {
			result.put("resultCode", 0);
			result.put("errMsg","SUCCESS");
			
			//see BaseHandle.returnLoginSuccess
			onlineUser.setSid(ret.getData().getString("user_id"));
			onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
			
			JSONObject returnData = new JSONObject();
			returnData.put("uid", onlineUser.getUid() );
			returnData.put("token",getRandomStr(16));
			returnData.putAll( ret.getData() );
			
			result.put("result", returnData);
		}
		else {
			result.put("resultCode", ret.getCode());
			result.put("result", ret.getData());
		}
		return result.toJSONString();
	}

	/**
	 * 网游支付，直接返回订单号
	 * @see 
	 * 	http://180.96.63.72/Documents/PaySDK.html#Title3_2
	 * 	http://180.96.63.72/Documents/PaySDK.html#Title2_4
	 * @param uid
	 * @param onlinePay
	 * @param codeJson
	 * @return
	 * @throws EncoderException
	 * @throws UnsupportedEncodingException 
	 * @throws ConfigurationException 
	 */
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("orderId", onlinePay.getOrderId());
		
		boolean isSignPayParam = "true".equals(getConfig("egame.is_sign_pay_parameter", "true"));
		if( isSignPayParam && onlinePay.getPrices() != 0 ){
			data.put("cp_info_md5", CoderUtils.md5(onlinePay.getOrderId() + onlinePay.getPrices() + onlinePay.getPayInfoConfig().getAppkey(),"utf-8"));
		}
		
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("resultCode", 0);
		ret.put("errMsg", "SUCCESS");
		ret.put("result", data);
		
		return JSON.toJSONString(ret);
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		log.info("parseParamter:{}", code);
		
		respData = parseURLParam(code);
		
		if( !respData.isEmpty() ){
			onlinePay.setOrderId( respData.getString("cp_order_id") );
			onlinePay.setActualAmount( respData.containsKey("fee") ? new Double(respData.getDouble("fee") * 100).intValue() + "" : "0" ); //单位转换：元 => 分
			onlinePay.setChannelOrderId( respData.getString("correlator") );
			onlinePay.setCallBackContent( code );
		}
	}
	/**
	 * 计费回调验证
	 * @throws ConfigurationException 
	 * @see http://180.96.63.72/Documents/PaySDK.html#Title3_4
	 */
	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		
		Assert.notNull(respData, "response data can't be null.");
		
		boolean isValidateIP = "true".equals(getConfig("egame.is_validate_callback_ip", "false"));
		if( isValidateIP ) {
			Assert.hasText(onlinePay.getClientIp(), "can't get client ip.");
			Assert.isTrue(onlinePay.getClientIp().startsWith("202.102.39"), "illegal ip." + onlinePay.getClientIp());
		}
		
		String appSecret = onlinePay.getPayInfoConfig().getAppkey();
		
		String interfaceId = respData.getString("method");			//固定值“callback”
		String orderId = respData.getString("cp_order_id");			//CP业务流水号（32位以内不含特殊字符）
        String serialNo = respData.getString("correlator");			//爱游戏平台流水号（32位以内）
        String resultCode = respData.getString("result_code");		//00为扣费成功，其他状态码均为扣费不成功
        String resultMsg = respData.getString("result_desc");		//
        String payFee = respData.getString("fee");					//计费金额，单位：元，服务器端请务必自行校验订购金额和计费金额是否一致
        String payType= respData.getString("pay_type");				//计费类型，smsPay：短代；alipay：支付宝；ipay：爱贝
        String sign = respData.getString("sign");					//MD5(cp_order_id+correlator+result_code+fee+paytype+method+appKey)
        
        Assert.isTrue("callback".equals(interfaceId), "method not identiy." + interfaceId);
        
		String resign = CoderUtils.md5(orderId + serialNo + resultCode + payFee + payType + interfaceId + appSecret,"utf-8");
		
		log.info("sign:{}, resign:{}", sign, resign);
		
		if( !resign.equals(sign) ){
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
			return getReturnFailure();
		}
		
		if( "00".equals(resultCode) ) {
			result.put("code", 2000);
			return getReturnSuccess();
		}
		else {
			result.put("code", "2000".equals(resultCode) ? -1 : resultCode);
			result.put("errMsg", isBlank(resultMsg) ? "扣费不成功" : resultMsg);
			return getReturnFailure();
		}
	}
	
	/**
	 * @see http://180.96.63.72/Documents/PaySDK.html#Title3_4
	 */
	@Override
	public String getReturnSuccess() {
		return "<cp_notify_resp><h_ret>0</h_ret><cp_order_id>" + this.respData.getString("cp_order_id") + "</cp_order_id></cp_notify_resp>";
	}

	/**
	 * @see http://180.96.63.72/Documents/PaySDK.html#Title3_4
	 */
	@Override
	public String getReturnFailure() {
		return "<cp_notify_resp><h_ret>-1</h_ret><cp_order_id>" + this.respData.getString("cp_order_id") + "</cp_order_id></cp_notify_resp>";
	}
	
	/**
	 * 服务端接口 - 授权码兑换令牌
	 * @see 用户SDK接入文档_v2.4.3_20160607_Eclipse.pdf 4.3.1 
	 * @param clientId			对应于应用注册时分配的 client_id 
	 * @param clientSecret		对应于应用注册时分配的 client_secret 
	 * @param code				用于兑换令牌的授权码 
	 * @param grantType			此值固定为 authorization_code 
	 * @param scope				访问请求的作用域 
	 * @param state				维护请求和响应的状态，传入值与返回值 保持一致 
	 * @return
	 * 成功响应： {     "scope": "all",     "re_expires_in": 15552000,     "user_id": 956877,     "token_type": "Bearer",     "expires_in": 5184000,     "refresh_token": "2c639e8c1cbfeee5fb07e968163d0343",     "access_token": "2cd0a6f9c8ce81ada335f1989413ca08" } 
	 * 失败响应： { "error_uri":null, "error":"invalid_request", "state":null, "error_description":"Missing grant_type parameter value" } 
	 * @throws Exception
	 */
	private Result<JSONObject> oauthToken(String clientId, String clientSecret, String code, String grantType, String scope, String state) throws Exception {
		
		long uid = System.currentTimeMillis();
		
		String url = getConfig("egame.url_oauth_token", "https://open.play.cn/oauth/token ");

		Assert.hasText(clientId, "client_id can't be null.");
		Assert.hasText(clientSecret, "client_secret can't be null.");
		Assert.hasText(code, "code can't be null.");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("code", code);
		params.put("grant_type", isBlank(grantType) ? "authorization_code" : grantType);
		//params.put("scope", isBlank(scope) ? "" : scope);
		//params.put("state", isBlank(state) ? "" : state);

        // 进行数字签名，并把签名相关字段放入请求参数MAP
        RequestParasUtil.signature("2", clientId, clientSecret, "MD5", "v1.0", params);

		log.info("[{}]request:{},{}", new Object[]{uid, url, params});
        
		String response = RequestParasUtil.sendPostRequest(url, params);

		log.info("[{}]response:{}", uid, response);
		
		Result<JSONObject> ret = new Result<JSONObject>();

		JSONObject data = isBlank(response) ? new JSONObject() : JSONObject.parseObject(response);
		
		if( isBlank(response) || !isBlank(data.get("error")) ) {
			ret.setCode(-1);
			ret.setMessage(isBlank(data.get("error")) ? "request failure." : data.getString("error"));
			ret.setData(data);
		}
		else {
			ret.setCode(0);
			ret.setMessage("SUCCESS");
			ret.setData(data);
		}
		return ret;
	}
	
	private boolean isBlank(Object str){ return str == null || StringUtils.isBlank(str.toString()); }
	
	
	private static PropertiesConfiguration config = null;
	private synchronized PropertiesConfiguration getConfig() throws ConfigurationException {
		if( config == null ) {
			URL url = getClass().getResource( getClass().getSimpleName() + ".properties");
			if( url != null ) {
				config = new PropertiesConfiguration(url);
				config.setReloadingStrategy(new FileChangedReloadingStrategy());	
			}
		}
		return config;
	}
	private String getConfig(String key, String defVal) {
		try {
			PropertiesConfiguration config = getConfig();
			return config == null ? defVal : config.getString(key, defVal);
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new java.lang.RuntimeException("get config error." + e.getMessage(), e);
		}
	}

	public static void main(String argc[]) throws UnsupportedEncodingException {
		JSONObject verifyInfo = new JSONObject();
		verifyInfo.put("code", "e94e0f9bea59c526f84e4c5c3a572500");
		
		JSONObject codeJson = new JSONObject();
		codeJson.put("client_id", "55708777");
		codeJson.put("client_secret", "4fb106fb00714395aefe5875aef26986");
		codeJson.put("grant_type", "authorization_code");
		codeJson.put("verifyInfo", verifyInfo);
		
		PayInfoConfig payInfo = new PayInfoConfig();
		payInfo.setAppid("55708777");
		payInfo.setAppkey("4fb106fb00714395aefe5875aef26986");
		
		System.out.println( new Gson().toJson(new egameHandle().login(new OnlineUser(), codeJson, payInfo)) );
	}
}
