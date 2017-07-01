package me.ckhd.opengame.online.request.vivo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	public LoginRequest(String code, PayInfoConfig _payInfoConfig) {
		super(code, _payInfoConfig);
	}

	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(this.loginType) && "2".equals(this.loginType)){
			map.put("userId", verifyInfo.get("userId").toString());
			map.put("userName", verifyInfo.get("userName").toString());
		}else if(StringUtils.isBlank(this.loginType) || "1".equals(this.loginType)){
			map.put("access_token", verifyInfo.getString("authtoken"));
		}
		return map;
	}
}
