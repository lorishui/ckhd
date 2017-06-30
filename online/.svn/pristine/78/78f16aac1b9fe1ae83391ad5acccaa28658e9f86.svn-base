package me.ckhd.opengame.online.response.tencent;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {
	
	Map<String, Object> ply;
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
		result.put("result", resultMap);
		return result;
	}
	
	public boolean isSuccess(){
		String ret = "";
		if(user!=null && user.getChannelUserContent()!=null){
			ret = user.getChannelUserContent();
		}
		boolean isSuccess=false;
		if( null == user.getLoginType() || user.getLoginType().equals("1")){
			if( ret.equals("0") ){
				result.put("resultCode", 0);
				result.put("errMsg", "");
				isSuccess=true;
			}else{
				result.put("resultCode", -1);
				result.put("errMsg",user.getErrMsg());
			}
		}else if(user.getLoginType().equals("2")){
			if((user.getErrMsg()==null || "".equals(user.getErrMsg())) ){
				result.put("resultCode", 0);
				result.put("errMsg", "");
				isSuccess=true;
			}else{
				result.put("resultCode", -1);
				result.put("errMsg",user.getErrMsg());
			}
		}
		return isSuccess;
	}
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
