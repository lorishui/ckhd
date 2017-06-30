package me.ckhd.opengame.online.response.lenovo2;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> resultMap;
	Map<String, Object> transdataMap;
	String errMsg="";
	boolean isSuccess = false;
	
	public LoginResponse(OnlineUser user) {
		super(user);
		if(user.getErrMsg() == null || user.getErrMsg().length() <= 0 ){
			isSuccess = true;
			result.put("resultCode", 0);
			result.put("errMsg", "");
		}
	}
	
	@Override
	public Map<String, Object> getResult() {
		if ( !isSuccess ) {
			result.put("resultCode", -1);
			result.put("errMsg", "failure");
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", user.getUid());
		map.put("userName", user.getUserName());
		map.put("expansion","");
		result.put("result", map);
		return result;
	}
		
	public boolean isSuccess() {
		result.put("resultCode", 0);
		result.put("errMsg", "");
		return isSuccess;
	}
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
