package me.ckhd.opengame.online.handle;

import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("37376")
@Scope("prototype")
public class m37376Handle extends BaseHandle {
	
	private static final String LOGIN_URL = "https://api.37376.com/Game/TokenCheckV2";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.containsKey("verifyInfo") ? codeJson.getJSONObject("verifyInfo") : null;
		if (verifyInfo != null) {
			String data = getData(verifyInfo,payInfo);
			
			String resultStr = HttpClientUtils.get(LOGIN_URL, data, 2000, 2000);
			if (StringUtils.isNotBlank(resultStr)) {
				 JSONObject resp = JSONObject.parseObject(resultStr);
				if (result != null && resp.getInteger("ResultCode") == 0) {
					setOnlineUser(onlineUser, codeJson);
					onlineUser.setSid(verifyInfo.getString("userId"));
					onlineUser.setUserName(verifyInfo.getString("userName"));
					returnLoginSuccess(result, onlineUser);
				} else {
					result.put("resultCode", 2013);
					result.put("errMsg", "37376token验证失败!");
				}
			}
		} else {
			result.put("resultCode", 2012);
			result.put("errMsg", "缺少参数verifyInfo!");
		}
		return result.toJSONString();
	}
	
	

	private String getData(JSONObject verifyInfo, PayInfoConfig payInfo) {
		String UserID = verifyInfo.getString("userId");
		String GameID = payInfo.getAppid();
		String UserToken = verifyInfo.getString("token");
		String SignatureStamp = System.currentTimeMillis()/1000+"";
		String preData = "GameID="+GameID+"&SignatureStamp="+SignatureStamp+"&UserID="+UserID+"&UserToken="+UserToken;
		String SignatureMD5 = CoderUtils.md5(preData.toLowerCase()+payInfo.getAppkey(),"utf-8").toUpperCase();
		return preData+"&SignatureMD5="+SignatureMD5;
	}



	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Set<?> keySet = request.getParameterMap().keySet();
		for (Object key : keySet) {
			respData.put((String)key, request.getParameter((String)key));
		}
		onlinePay.setOrderId(respData.getString("GameOrderNo"));
		onlinePay.setChannelId(respData.getString("TradeNo"));
		onlinePay.setActualAmount(respData.containsKey("TotalFee")?respData.getDouble("TotalFee")*100+"":"");
		onlinePay.setCallBackContent(respData.toJSONString());
		log.info(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String serverSign = CoderUtils.md5(getSign().toLowerCase()+onlinePay.getPayInfoConfig().getAppkey(),"utf-8").toUpperCase();
		String SignatureMD5 = respData.getString("SignatureMD5");
		if (serverSign.equals(SignatureMD5)) {
			result.put("code", 2000);
			return getReturnSuccess();
		} else {
			result.put("code", 4001);
			result.put("errMsg", "37376验证失败");
			return getReturnFailure();
		}
	}
	
	private String getSign(){
		Set<String> keySet = respData.keySet();
		String[] array =  keySet.toArray(new String[keySet.size()]);
		Arrays.sort(array);
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < array.length ; i++) {
			String key = array[i];
			if(!"SignatureMD5".equals(key)){
				sb.append(key).append("=").append(respData.getString(key)).append("&");
			}
		}
		return sb.substring(0, sb.length()-1);
	}

	@Override
	public String getReturnSuccess() {
		return "{\"ResultCode\":\"0\",\"ResultMessage\":\"success\"}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"ResultCode\":\"1\",\"ResultMessage\":\"fail\"}";
	}

}
