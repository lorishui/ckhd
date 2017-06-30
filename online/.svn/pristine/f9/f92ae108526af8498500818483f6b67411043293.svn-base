package me.ckhd.opengame.online.request.letv;

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
		map.put("client_id",payInfoConfig.getAppid());
		map.put("uid", verifyInfo.get("uid"));
		map.put("access_token", verifyInfo.get("accessToken"));
		return map;
	}
}
