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

@Component("youlong")
@Scope("prototype")
public class youlongHandle extends BaseHandle {
	
	private static final String LOGIN_URL = "http://api.yljh.19196.com/Api/checkToken";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String data = "token="+verifyInfo.getString("accessToken")
					+"&pid="+payInfo.getAppid();
		String respData = HttpClientUtils.post(LOGIN_URL,data,10000,10000,"application/x-www-form-urlencoded; ");
		log.info("youlong login result="+respData);
		if( StringUtils.isNotBlank(respData) ){
			JSONObject resp = JSONObject.parseObject(respData);
			if(resp.containsKey("state") && "1".equals(resp.getString("state"))){
				onlineUser.setSid(resp.get("username")+"");
				onlineUser.setUserName(verifyInfo.getString("userName") == null ? "": resp.get("username").toString());
				returnLoginSuccess(result, onlineUser);
			}else{
				onlineUser.setErrMsg(resp.getString("errMsg").toString()+resp.getString("errcMsg"));
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
	    for(Object key:request.getParameterMap().keySet()){
            respData.put(key+"", request.getParameter(key+""));
        }
		onlinePay.setOrderId(respData.getString("orderId"));
		onlinePay.setActualAmount(Double.parseDouble(respData.getString("amount"))*100+"");
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String orderId = respData.getString("orderId");
		String userName = respData.getString("userName");
		String amount = respData.getString("amount");
		String extra = respData.getString("extra");
		String PKEY = onlinePay.getPayInfoConfig().getAppkey();
		String serverSign = CoderUtils.md5(orderId+userName+amount+extra+PKEY,"utf-8").toUpperCase();
		
		if(serverSign.equals(respData.getString("flag"))){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4001);
			result.put("errMsg", "验证失败");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "OK";
	}

	@Override
	public String getReturnFailure() {
		return "SORRY";
	}

}
