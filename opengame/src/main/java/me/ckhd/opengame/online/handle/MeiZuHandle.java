package me.ckhd.opengame.online.handle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("meizu")
@Scope("prototype")
public class MeiZuHandle extends BaseHandle {
	
	private Map<String,Object> resquestMap = new HashMap<String, Object>();
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		return null;
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		result.put("result", data);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
			resquestMap.put(key, request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("cp_order_id"));
			onlinePay.setActualAmount(respData.containsKey("total_price")?((int)(respData.getDouble("total_price")*100))+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("order_id"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String signContent = SignContext.getMeizuPaySignContext(resquestMap);
		log.info("meizu callback sign content="+signContent);
		String signNew = CoderUtils.md5(signContent+":"+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
		if( respData.containsKey("sign") && respData.getString("sign").equals(signNew) ){
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
		return "{\"code\":\"200\",\"message\":\"success\"}";
	}

	@Override
	public String getReturnFailure() {
		return "{\"code\":\"200\",\"message\":\"未知异常 \"}";
	}

}
