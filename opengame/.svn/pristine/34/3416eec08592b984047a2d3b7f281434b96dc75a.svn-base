package me.ckhd.opengame.online.response.qihoo;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if (isSuccess()) {
			user.setSid(loginContent.get("id").toString());
			user.setUserName(loginContent.get("name")==null?null:loginContent.get("name").toString());
		}
	}
	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("uid", user.getUid());
		resultMap.put("token", user.getToken());
		resultMap.put("userName", user.getUserName());
		resultMap.put("expansion","");
		result.put("result", resultMap);
		return result;
	}
	
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
