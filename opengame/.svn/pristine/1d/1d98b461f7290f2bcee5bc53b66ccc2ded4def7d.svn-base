package me.ckhd.opengame.online.response.xiaomi;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean check = false;
		if(onlinePay.getCallBackMap().containsKey("orderStatus") && "TRADE_SUCCESS".equals(onlinePay.getCallBackMap().get("orderStatus"))){
			check=true;
		}
		return check;
	}

	@Override
	public String getCallBackResult() {
		Map<String,Object> map = new HashMap<String, Object>();
		if(isSuccess()){
			map.put("errcode", 200);
		}else{
			map.put("errcode", 1515);
		}
		return JSONObject.toJSONString(map);
	}
	
	
	@Override
	public String getReturnSuccess() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("errcode", 200);
		return JSONObject.toJSONString(map);
	}

}
