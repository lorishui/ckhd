package me.ckhd.opengame.online.request.tencent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

public class LoginRequest extends BaseLoginRequest {

	public String ip="";
	public String openId="";
	public String accessToken="";
	public Integer tencentLoginType = 0;
	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
		JSONObject jsonCode = JSONObject.parseObject(code);
		JSONObject sidData = JSONObject.parseObject(jsonCode.getString("verifyInfo"));
		openId = sidData.getString("openId");
		accessToken = sidData.getString("accessToken");
		ip = sidData.getString("ip");
		tencentLoginType = sidData.getInteger("tencentLoginType");
	}
	
	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		if(StringUtils.isBlank(loginType) || "1".equals(loginType) ){
			map.put("openId", openId);
			map.put("accessToken", accessToken);
			map.put("ip", ip);
			map.put("tencentLoginType", tencentLoginType);
		}else if("2".equals(loginType)){
			
		}
		return map;
	}
}
