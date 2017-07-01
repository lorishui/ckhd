package me.ckhd.opengame.online.response;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;

import com.alibaba.fastjson.JSONObject;

public abstract class BasePayCallBackResponse {

	protected OnlinePay onlinePay;
	protected String errMsg;
	public BasePayCallBackResponse(OnlinePay _onlinePay) {
		this.onlinePay=_onlinePay;
	}
	
	//判断回调是否成功
	public abstract boolean isSuccess();
	
	//返回回调信息
	public abstract String getCallBackResult();
	
	//返回正确信息
	public abstract String getReturnSuccess();
	
	//返回正确信息
	public String getReturnFailure(){
		return null;
	}
	
	public Map<String, Object> getSendOrder(){
		Map<String, Object> map = new HashMap<String, Object>();
		String content = JSONObject.toJSONString(onlinePay.getSenderMap());
		map.put("sendNum",0);
		map.put("content",content);
		map.put("sendStatus",1);
		return map;
	}
}
