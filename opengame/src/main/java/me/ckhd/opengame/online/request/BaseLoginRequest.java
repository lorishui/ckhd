package me.ckhd.opengame.online.request;

import java.util.Map;

import me.ckhd.opengame.app.entity.PayInfoConfig;

import com.alibaba.fastjson.JSONObject;


public abstract class BaseLoginRequest {

	public  String ckAppId = "";
	public  String channelId = "";
	public  String version = "";
	public  String act = "";
	public String loginType = "";
	public JSONObject verifyInfo =null;
	public PayInfoConfig payInfoConfig;
	public BaseLoginRequest(String code,PayInfoConfig _payInfoConfig) {
		JSONObject jsonCode = JSONObject.parseObject(code);
		ckAppId=jsonCode.getString("ckAppId");
		channelId = jsonCode.getString("ckChannelId");
		version = jsonCode.getString("version");
		act = jsonCode.getString("act");
		verifyInfo = jsonCode.getJSONObject("verifyInfo");
		payInfoConfig = _payInfoConfig;
		loginType = jsonCode.getString("loginType");
	}
	
	public Map<String, Object> getParam(){
		return null;
	}
}
