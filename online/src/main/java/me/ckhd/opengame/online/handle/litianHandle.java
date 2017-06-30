package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("litian")
@Scope("prototype")
public class litianHandle extends BaseHandle {

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		
		onlineUser.setUserName(verifyInfo.getString("nickname"));
		onlineUser.setToken(verifyInfo.getString("token"));
		onlineUser.setSid(verifyInfo.getString("id"));
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject returnData = new JSONObject();
		onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
		returnData.put("uid", onlineUser.getUid() );
		returnData.put("token",onlineUser.getToken());
		result.put("result", returnData);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		String sign = request.getParameter("sign");
		respData.put("sign", sign);
		String data = request.getParameter("data");
		respData.put("data", data);
		JSONObject dataJson = JSONObject.parseObject(data);
		onlinePay.setActualAmount(dataJson.containsKey("money")?Double.parseDouble(dataJson.getString("money"))*100+"":"0");
		onlinePay.setOrderId(dataJson.containsKey("cp_order_sn")?dataJson.getString("cp_order_sn"):"");
		onlinePay.setChannelOrderId(dataJson.containsKey("order_sn")?dataJson.getString("order_sn"):"");
		onlinePay.setCallBackContent(respData.toJSONString());
		respData.put("notifyUrl", request.getRequestURL());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String notifyUrl = respData.getString("notifyUrl");
		String data = respData.getString("data");
		String cp_key = onlinePay.getPayInfoConfig().getAppkey();
		String sign = respData.getString("sign");
		String serverSign = CoderUtils.md5(notifyUrl+data+cp_key,"utf-8");
		if(sign.equals(serverSign)){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4000);
			result.put("errMsg", "fail");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject jo = new JSONObject();
		jo.put("status", "SUCCESS");
		return jo.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject jo = new JSONObject();
		jo.put("status", "FAIL");
		return jo.toJSONString();
	}

}
