package me.ckhd.opengame.online.request.xy;

import java.util.Map;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	public String uid="";
	public String appid="";
	public String token="";
	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
	}
	
	@Override
	public Map<String, Object> getParam() {
		return null;
	}
}
