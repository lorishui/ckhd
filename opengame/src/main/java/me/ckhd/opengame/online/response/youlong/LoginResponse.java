package me.ckhd.opengame.online.response.youlong;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> content;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
	}
	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			result.put("resultCode", -1);
			result.put("errMsg",user.getErrMsg());
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		result.put("resultCode", 0);
		result.put("errMsg", "");
		resultMap.put("uid", user.getUid());
		resultMap.put("token",  user.getToken());
		resultMap.put("userName", user.getUserName());
		result.put("result", resultMap);
		return result;
	}
	
	
	@Override
	public OnlineUser getUserInfo() {
		if (isSuccess()) {
			user.setSid(user.getSid());
		}
		return user;
	}
	
	@Override
	public boolean isSuccess(){
		boolean isSuccess=false;
		if(user.getErrMsg()!=null && "success".equals(user.getErrMsg())){
			isSuccess=true;
		}
		return isSuccess;
	}
}
