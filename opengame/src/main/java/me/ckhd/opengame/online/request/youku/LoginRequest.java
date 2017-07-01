package me.ckhd.opengame.online.request.youku;

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
		if("2".equals(loginType)) {
			map.put("userId", verifyInfo.get("userId").toString());
			map.put("userName", verifyInfo.get("userName").toString());
		}else{
			map.put("sessionId", verifyInfo.get("sessionId"));
		}
		return map;
	}
}