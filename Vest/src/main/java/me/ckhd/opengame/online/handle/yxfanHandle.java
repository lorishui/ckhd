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

@Component("yxfan")
@Scope("prototype")
public class yxfanHandle extends BaseHandle {

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String username = verifyInfo.getString("userName");
		String appkey = payInfo.getAppkey();
		String logintime = verifyInfo.getString("logintime");
		String sign = verifyInfo.getString("sign");
		String serverSign = CoderUtils.md5(
				"username="+username
				+"&appkey="+appkey
				+"&logintime="+logintime, "utf-8");
		if(serverSign.equals(sign)){
			onlineUser.setSid(verifyInfo.getString("userId"));
			onlineUser.setUserName(username);
			returnLoginSuccess(result, onlineUser);
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
	    for(Object key:request.getParameterMap().keySet()){
            respData.put(key+"", request.getParameter(key+""));
        }
		onlinePay.setOrderId(respData.getString("attach"));
		onlinePay.setChannelOrderId(respData.getString("orderid"));
		onlinePay.setActualAmount(respData.containsKey("amount")?respData.getInteger("amount")*100+"":"0");
		onlinePay.setCallBackContent(respData.toJSONString());
		
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String orderid = respData.getString("orderid");
		String username = respData.getString("username");
		String gameid = respData.getString("gameid");
		String roleid = respData.getString("roleid");
		String serverid = respData.getString("serverid");
		String paytype = respData.getString("paytype");
		String amount = respData.getString("amount");
		String paytime = respData.getString("paytime");
		String attach = respData.getString("attach");
		String appkey = onlinePay.getPayInfoConfig().getAppkey();
		String sign = respData.getString("sign");
		String serverSign = CoderUtils.md5(
				"orderid="+orderid
				+"&username="+username
				+"&gameid="+gameid
				+"&roleid="+roleid
				+"&serverid="+serverid
				+"&paytype="+paytype
				+"&amount="+amount
				+"&paytime="+paytime
				+"&attach="+attach
				+"&appkey="+appkey,"utf-8").toLowerCase();
		if(sign.equals(serverSign)){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4001);
			result.put("errMsg", "游戏fan验证失败");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "errorSign";
	}

}
