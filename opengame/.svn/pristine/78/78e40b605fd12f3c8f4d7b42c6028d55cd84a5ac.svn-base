package me.ckhd.opengame.online.request.lenovo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	String loginToken="";
	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
		loginToken=verifyInfo.getString("token");
	}

	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		if(StringUtils.isBlank(loginType) || "1".equals(loginType)){
			map.put("loginToken", loginToken);
			map.put("appid",payInfoConfig.getAppid());
			map.put("APPV_KEY", payInfoConfig.getAppkey());
			map.put("PLATP_KEY", payInfoConfig.getExInfoMap().get("PLATP_KEY"));
		}else if("2".equals(loginType)){
			map.put("userId", verifyInfo.get("userId").toString());
			map.put("userName", verifyInfo.get("userName").toString());
		}
		return map;
	}
}
