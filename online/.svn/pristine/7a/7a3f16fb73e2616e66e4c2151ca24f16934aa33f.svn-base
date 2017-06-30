package me.ckhd.opengame.online.response.coolpad;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> briefUser;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if(isSuccess()){
			user.setSid(loginContent.get("openid")==null?"":loginContent.get("openid").toString());
		}
		
	}
	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("uid", user.getUid());
		resultMap.put("access_token",  loginContent.containsKey("access_token")?loginContent.get("access_token").toString():"");
		resultMap.put("refresh_token",  loginContent.containsKey("refresh_token")?loginContent.get("refresh_token").toString():"");
		resultMap.put("expires_in",  loginContent.containsKey("expires_in")?loginContent.get("expires_in").toString():"");
		result.put("result", resultMap);
    	return result;
	}
	
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
