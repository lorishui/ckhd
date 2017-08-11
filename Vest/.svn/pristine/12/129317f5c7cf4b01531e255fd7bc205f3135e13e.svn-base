package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("iqiyi")
@Scope("prototype")
public class iqiyiHandle extends BaseHandle{

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(verifyInfo.getString("uid"));
		onlineUser.setUserName(verifyInfo.getString("uid"));
		returnLoginSuccess(result, onlineUser);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		for(Object key:request.getParameterMap().keySet()){
			respData.put(key+"", request.getParameter(key+""));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("userData"));
			onlinePay.setActualAmount(respData.containsKey("money")?respData.getDouble("money")*100+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("order_id"));
		}
	}
	
	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String sign = respData.getString("sign");
		log.info("iqiyi sign ="+sign);
		String user_id = respData.getString("user_id");
		String role_id = respData.getString("role_id");
		String money = respData.getString("money");
		String order_id = respData.getString("order_id");
		String time = respData.getString("time");
		String key = onlinePay.getPayInfoConfig().getExInfoMap().containsKey("key")?onlinePay.getPayInfoConfig().getExInfoMap().get("key").toString():"";
		//密后的签名，sign= MD5($user_id.$role_id.$order_id.$money.$time.$key)
		String signNew = CoderUtils.md5(user_id+role_id+order_id+money+time+key,"utf-8");
		if( sign.equals(signNew) && ((int)(Double.parseDouble(money)*100))==onlinePay.getPrices() ){
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
		return "{\"result\":0,\"message\":\"success\"}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"result\":-1,\"message\":\"Sign error\"}";
	}

}
