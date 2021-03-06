package me.ckhd.opengame.online.handle;

import java.net.URLEncoder;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("sogou")
@Scope("prototype")
public class sougoHandle extends BaseHandle{
	
	private static final String login_test_url = "http://dev.sdk.g.sogou.com/api/v2/login/verify";
	private static final String login_url = "http://gamesdk.sogou.com/api/v2/login/verify";
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("rate", onlinePay.getPayInfoConfig().getExInfoMap().get("rate")+"");
		data.put("callBackUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", data);
		return result.toJSONString();
	}
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		String url = login_url;
		if( codeJson.containsKey("isTest") && 1 == codeJson.getInteger("isTest") ){
			url = login_test_url;
		}
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String gid = payInfo.getAppid();
		String sessionKey = verifyInfo.getString("accessToken");
		String userId = verifyInfo.getString("userId");
		StringBuilder sb = new StringBuilder();
		try {
    		sb.append("gid=").append(URLEncoder.encode(gid, "utf-8"))
    			.append("&sessionKey=").append(URLEncoder.encode(sessionKey, "utf-8")).
    			append("&userId=").append(URLEncoder.encode(userId, "utf-8"));
		} catch (Throwable e) {
		    log.error("sogou is urlencode error!", e);
		}
		String data = sb.toString()+"&"+payInfo.getExInfoMap().get("SecretKey");
		String auth = MD5Util.string2MD5(data);
		sb.append("&auth=").append(auth);
		String responseData = HttpClientUtils.post(url, sb.toString(), 10000, 10000,"application/x-www-form-urlencoded; ");
		if(StringUtils.isNotBlank(responseData)){
			JSONObject jo = JSONObject.parseObject(responseData);
			Integer code = jo.getInteger("code");
			if(code == 0){
				onlineUser.setSid(userId);
				returnLoginSuccess(result, onlineUser);
			}else if(code == 1){
				result.put("errMsg", "参数错误");
			}else if(code == 5){
				result.put("errMsg", "签名错误");
			}else{
				result.put("errMsg", "登录态错误");
			}
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		for(Object key:request.getParameterMap().keySet()){
			respData.put(key+"", request.getParameter(key+""));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("appdata"));
			onlinePay.setActualAmount(respData.containsKey("amount1")?respData.getDouble("amount1")*100+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("oid"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String sign = respData.getString("auth");
		String singContent = getSignContent()+onlinePay.getPayInfoConfig().getExInfoMap().get("payKey");
		String signNew = CoderUtils.md5(singContent, "utf-8");
		if( sign.equals(signNew) ){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "OK";
	}

	@Override
	public String getReturnFailure() {
		return "ERR_200";
	}
	
	private String getSignContent(){
		Object[] obj = respData.keySet().toArray();
		Arrays.sort(obj);
		StringBuffer sb = new StringBuffer();
		for(Object key:obj){
			if(!"auth".equals(key)){
				sb.append(key).append("=").append(respData.get(key)).append("&");
			}
		}
		return sb.toString();
	}

}
