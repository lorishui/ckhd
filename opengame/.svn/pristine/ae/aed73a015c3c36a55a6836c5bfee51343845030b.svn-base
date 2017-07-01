package me.ckhd.opengame.online.response.xy;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {
	
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
		result.put("resultCode", 0);
		result.put("errMsg", "");
		resultMap.put("uid", user.getUid());
		resultMap.put("token",  user.getToken());
		resultMap.put("userName", user.getUserName());
		result.put("result", resultMap);
		return result;
	}
	
	public boolean isSuccess(){
		if(user != null && user.getChannelUserContent() != null){
			loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
		}
		boolean isSuccess=false;
		if( loginContent.get("ret").toString().equals("0") ){
			result.put("resultCode", 0);
			result.put("errMsg", "");
			isSuccess=true;
		}else{
			result.put("resultCode", -1);
			result.put("errMsg",loginContent.get("error"));
		}
		return isSuccess;
	}
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
