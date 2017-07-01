package me.ckhd.opengame.online.request.xiaomi;

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
		map.put("appId",payInfoConfig.getAppid());
		map.put("session",verifyInfo.getString("sessionId"));
		map.put("uid",verifyInfo.getString("userId"));
		try {
			String param = encryptData(map);
			String encryptKey  = payInfoConfig.getExInfoMap().get("encryptKey")==null?"":payInfoConfig.getExInfoMap().get("encryptKey").toString();
			String signature = HmacSHA1Encryption.HmacSHA1Encrypt(param,encryptKey);
			map.put("signature",signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	private String encryptData(Map<String, Object> map){
		StringBuffer parStr=new StringBuffer();
		parStr.append("appId="+map.get("appId"));
		parStr.append("&session="+map.get("session"));
		parStr.append("&uid="+map.get("uid"));
		return parStr.toString();
	}
}
