package me.ckhd.opengame.online.request.uc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	public String sid="";
	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
		JSONObject jsonCode = JSONObject.parseObject(code);
		JSONObject sidData = JSONObject.parseObject(jsonCode.getString("verifyInfo"));
		sid = sidData.getString("sid");
	}
	
	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		if(StringUtils.isBlank(loginType) || "1".equals(loginType) ){
			map.put("sid", sid);
		}else if("2".equals(loginType)){
			map.put("userId", verifyInfo.get("userId")==null?"":verifyInfo.get("userId").toString());
			map.put("userName", verifyInfo.get("userName")==null?"":verifyInfo.get("userName").toString());
		}
		return map;
	}
}
