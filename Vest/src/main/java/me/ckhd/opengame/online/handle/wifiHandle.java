package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.wifi.RSA;
import me.ckhd.opengame.util.HttpClientUtils;
import me.ckhd.opengame.util.HttpUtils;

import com.alibaba.fastjson.JSONObject;

@Component("wifi")
@Scope("prototype")
public class wifiHandle extends BaseHandle {
	
	private static final String LOGIN_URL = "https://oauth.51y5.net/open-sso/fa.scmd";
//	private static final String LOGIN_URL_TEST = "https:/wifi30.51y5.net/open-sso/fa.scmd";
	
	private static final String REFRESH_TOKEN_URL = "https://oauth.51y5.net/open-sso/fa.scmd";
//	private static final String REFRESH_TOKEN_URL_TEST = "https:/wifi30.51y5.net/open-sso/fa.scmd";
	
	
//	private static final String QUERY_USER_URL = "https://oauth.51y5.net/open-sso/fa.scmd";
//	private static final String QUERY_USER_URL_TEST = "https:/wifi30.51y5.net/open-sso/fa.scmd";	

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("appId", payInfo.getAppid());
		String URL = LOGIN_URL;
		if(verifyInfo.containsKey("refreshToken")){
			URL = REFRESH_TOKEN_URL;
			map.put("pid", "refreshToken");
			map.put("refreshToken", verifyInfo.getString("refreshToken"));
		}else{
			map.put("pid", "getAccessToken");
			map.put("code", verifyInfo.getString("code"));
		}
		map.put("st", "m");
		String sign = getSign(map,payInfo.getAppkey());
		map.put("sign", sign);
		String data = "";
		try {
			data = HttpUtils.toHttpParameter(map);
		} catch (UnsupportedEncodingException e) {
			log.error("wifi万能钥匙登录参数转换错误："+ e.getMessage());
		}
		String respData = HttpClientUtils.post(URL,data,5000,5000,"application/x-www-form-urlencoded; ");
		log.info("wifi万能钥匙 login result="+respData);
		if( StringUtils.isNotBlank(respData) ){
			JSONObject resp = JSONObject.parseObject(respData);
			if(resp.containsKey("retCd") && "0".equals(resp.getString("retCd"))){
//				String oid = resp.getString("oid");
//				String accessToken = resp.getString("accessToken");
				onlineUser.setSid(resp.get("oid")+"");
				onlineUser.setUserName(verifyInfo.getString("accessToken") == null ? "": resp.get("username").toString());
				returnLoginSuccess(result, onlineUser);
			}else{
				onlineUser.setErrMsg(resp.getString("retCd").toString()+" : "+resp.getString("retMsg"));
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}
	
	private String getSign(HashMap<String, Object> map, String salt) {
		Set<String> keySet = map.keySet();
		String[] array = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(array);
		StringBuilder sb = new StringBuilder();
		for (String key : array) {
			sb.append(map.get(key));
		}
		sb.append(salt);
		String sign = CoderUtils.md5(sb.toString(), "utf-8");
		return sign;
	}


	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("callback", codeJson.getString("notifyUrl"));
		
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("0.00");
		 sb.append(verifyInfo.getString("openId")).append("&")
		 	.append(onlinePay.getPayInfoConfig().getExInfoMap().get("mMerchantNo")+"").append("&")
		 	.append(onlinePay.getOrderId()).append("&")
		 	.append(df.format(Integer.parseInt(onlinePay.getPayCodeConfig().getPrice())*0.01)).append("&")
		 	.append(codeJson.getString("notifyUrl")).append("&")
		 	.append(verifyInfo.getString("productName")).append("&")
		 	.append(onlinePay.getPayInfoConfig().getAppid()).append("&")
		 	.append(verifyInfo.getString("wifiAppName"));
		String key = onlinePay.getPayInfoConfig().getExInfoMap().get("payKey")+"";
		String sign = RSA.sign(sb.toString(),key,"utf-8");
		data.put("mMerchantNo", onlinePay.getPayInfoConfig().getExInfoMap().get("mMerchantNo")+"");
		data.put("sign", URLEncode(sign));
		result.put("result", data);
		return result.toJSONString();
	}
	
	

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
	    for(Object key:request.getParameterMap().keySet()){
            respData.put(key+"", request.getParameter(key+""));
        }
		onlinePay.setOrderId(respData.getString("merchantOrderNo"));
		onlinePay.setChannelOrderId(respData.getString("orderNo"));
		onlinePay.setActualAmount(respData.getString("payAmount"));
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String serverSign = getVerifySign(onlinePay.getPayInfoConfig().getAppkey());
		String sign = respData.getString("md5Sign");
		if(serverSign.equals(sign)){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4001);
			result.put("errMsg", "wifi万能钥匙验证失败");
			return getReturnFailure();
		}
	}

	private String getVerifySign(String appkey) {
		Set<String> keySet = respData.keySet();
		String[] array = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(array);
		StringBuilder sb = new StringBuilder();
		for (String key : array) {
			if(!"md5Sign".equals(key)){
				sb.append(key).append("=").append(respData.getString(key)).append("&");
			}
		}
		sb.append("key=").append(appkey);
		return CoderUtils.md5(sb.toString(), "utf-8").toUpperCase();
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}

	@Override
	public String getReturnFailure() {
		return "FAIL";
	}

}
