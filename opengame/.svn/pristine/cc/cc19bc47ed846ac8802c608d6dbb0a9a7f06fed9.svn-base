package me.ckhd.opengame.online.request.m4399;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
	}

	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("state",  verifyInfo.getString("state"));
		map.put("uid",  verifyInfo.getString("uid"));
		return map;
	}
	
	
}
