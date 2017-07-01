package me.ckhd.opengame.online.request.baidu;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
	}
	
	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		if(!"2".equals(loginType) && StringUtils.isBlank(loginType)){
			map.put("accessToken", verifyInfo.getString("accessToken"));
			map.put("version", verifyInfo.getString("version"));
		}else if("2".equals(loginType)){
			map.put("userId", verifyInfo.get("userId")==null?"":verifyInfo.get("userId").toString());
			map.put("userName", verifyInfo.get("userName")==null?"":verifyInfo.get("userName").toString());
		}
		return map;
	}
}
