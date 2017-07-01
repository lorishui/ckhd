package me.ckhd.opengame.online.request.gionee;

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
		if("1".equals(loginType) || StringUtils.isBlank(loginType)){
			String amigoToken = verifyInfo.getString("amigoToken");
			String appkey = payInfoConfig.getAppkey();
			String secretKey = payInfoConfig.getExInfoMap().get("secretKey").toString();
			map.put("amigoToken", amigoToken);
			map.put("apiKey", appkey);
			map.put("secretKey",secretKey);
		}else if("2".equals(loginType)) {
			map.put("userId", verifyInfo.get("userId").toString());
			map.put("userName", verifyInfo.get("userName").toString());
		}
		return map;
	}
}
