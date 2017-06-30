package me.ckhd.opengame.online.request.wandoujia;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackRequest extends BasePayCallBackRequest {
	
	JSONObject json = null;
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		for(Object key : httpRequest.getParameterMap().keySet()){
			map.put(key.toString(), httpServletRequest.getParameter(key+""));
		}
		if(map.get("content") != null){
			json = JSONObject.parseObject(map.get("content").toString());
		}
	}
	
	@Override
	public String getOrderId() {
		if(json != null && json.get("out_trade_no") != null){
			String orderId = json.getString("out_trade_no");
			return orderId;
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		if(json != null && json.get("money") != null){
			String money = json.getString("money");
			return money;
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		if(json != null && json.get("orderId") != null){
			String orderId = json.getString("orderId");
			return orderId;
		}
		return null;
	}
	
}
