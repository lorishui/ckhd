package me.ckhd.opengame.online.response.googleplay;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> resultMap = new HashMap<String, Object>();
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
	}
	
	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("uid", user.getUid());
		resultMap.put("token", user.getId());
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
