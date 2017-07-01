package me.ckhd.opengame.online.request.rd;

import java.util.Map;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

import com.alibaba.fastjson.JSONObject;

public class LoginRequest extends BaseLoginRequest {

	JSONObject token =null;
	
	public LoginRequest(String code, PayInfoConfig _payInfoConfig) {
		super(code, _payInfoConfig);
		token=verifyInfo.getJSONObject("token");
	}

	@Override
	public Map<String, Object> getParam() {
		return null;
	}
}
