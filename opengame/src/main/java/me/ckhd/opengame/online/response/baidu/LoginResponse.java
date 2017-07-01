package me.ckhd.opengame.online.response.baidu;

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
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("uid", user.getUid());
		resultMap.put("token",  user.getToken());
		resultMap.put("userName", user.getUserName());
		resultMap.put("expansion","");
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
		boolean isSuccess=true;
		if((user.getErrMsg()==null || "".equals(user.getErrMsg()))){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		}else{
			result.put("resultCode", -1);
			result.put("errMsg",user.getErrMsg());
			isSuccess=false;
		}
		return isSuccess;
	}
}
