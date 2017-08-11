package me.ckhd.opengame.online.handle;

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

@Component("guopan")
@Scope("prototype")
public class guopanHandle extends BaseHandle {

	private static final String LOGIN_URL = "http://userapi.guopan.cn/gamesdk/verify/";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String game_uin = verifyInfo.getString("gameUin");
		String appid = payInfo.getAppid();
		String token = verifyInfo.getString("token");
		String userId = verifyInfo.getString("userId");
		String userName = verifyInfo.getString("userName");
		String t = System.currentTimeMillis()/1000 + "";
		String sign = CoderUtils.md5(game_uin+appid+t+payInfo.getExInfoMap().get("SERVER_KEY"),"utf-8");
		String data = "game_uin="+game_uin+"&appid="+appid+"&token="+token+"&t="+t
				+"&sign="+sign;
		String respStr = HttpClientUtils.get(LOGIN_URL, data, 2000, 2000);
		if ( StringUtils.isNotBlank(respStr) ) {
			if (respStr.contains("true")) {
				onlineUser.setSid(userId);
				onlineUser.setUserName(userName);
				returnLoginSuccess(result, onlineUser);
			}else{
				log.info("guopan login failure! return str="+respStr);
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("mRate", onlinePay.getPayInfoConfig().getExInfoMap().get("mRate")+"");
		data.put("orderId", onlinePay.getOrderId());
		data.put("callBackUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", data);
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		for(Object key:request.getParameterMap().keySet()){
			respData.put(key.toString(), request.getParameter(key.toString()));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("serialNumber"));
			onlinePay.setActualAmount(respData.containsKey("money")?respData.getDouble("money")*100+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("trade_no"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		if ( respData.containsKey("status") && "1".equals(respData.getString("status")) ){
			//Upper(MD5(partner_id+game_id+server_id+user_id+bill_no+price+trade_status+partner_key))
			String context = respData.getString("serialNumber")
							+respData.getString("money")
							+respData.getString("status")
							+respData.getString("t")
							+onlinePay.getPayInfoConfig().getExInfoMap().get("SERVER_KEY");
			String sign = respData.getString("sign");
			String signServer = CoderUtils.md5(context, "utf-8");
			log.info("guopan signServer ="+signServer);
			log.info("guopan sign ="+sign);
			if( sign.equals(signServer) ){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4007);
			result.put("errMsg", "支付失败！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}
	
}
