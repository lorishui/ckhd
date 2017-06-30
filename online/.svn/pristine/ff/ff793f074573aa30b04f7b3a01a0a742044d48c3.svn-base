package me.ckhd.opengame.online.request.anzhi;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.util.anzhi.Base64;

public class LoginRequest extends BaseLoginRequest {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
	}

	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		String account = verifyInfo.getString("loginName");
		String appkey = payInfoConfig.getAppkey();
		String appsecret = payInfoConfig.getExInfoMap().get("app_secret").toString();
		String sid = verifyInfo.getString("sid");
		map.put("uid", verifyInfo.getString("uid"));
		map.put("sid", sid);
		map.put("loginname", account);
		map.put("version", verifyInfo.getString("version"));
		map.put("sign",  Base64.encodeToString(appkey+account+sid+appsecret));
		return map;
	}
}
