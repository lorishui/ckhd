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

@Scope("prototype")
@Component("xiantu")
public class xiantuHandle extends BaseHandle {

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(verifyInfo.getString("accountNo"));
		returnLoginSuccess(result, onlineUser);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
	    for(Object key:request.getParameterMap().keySet()){
            respData.put(key+"", request.getParameter(key+""));
        }
		onlinePay.setOrderId(respData.getString("extend"));
		onlinePay.setActualAmount(respData.containsKey("price")?(int)(respData.getDouble("price")*100)+"":"");
		onlinePay.setChannelOrderId(respData.getString("out_trade_no"));
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		if(respData.containsKey("pay_status") && "1".equals(respData.getString("pay_status"))){
			String out_trade_no = respData.getString("out_trade_no");
			String price = respData.getString("price");
			String pay_status = respData.getString("pay_status");
			String extend = respData.getString("extend");
			String key = onlinePay.getPayInfoConfig().getAppkey();
			String serverSign = CoderUtils.md5(out_trade_no+price+pay_status+extend+key, "utf-8");
			if(serverSign.equals(respData.getString("sign"))){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4001);
				result.put("errMsg", "xiantu验证失败");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4002);
			result.put("errMsg", "xiantu支付失败");
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
