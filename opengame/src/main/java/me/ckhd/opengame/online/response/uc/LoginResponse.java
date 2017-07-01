package me.ckhd.opengame.online.response.uc;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	
	Map<String, Object> ply;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if(!"2".equals(user.getLoginType())){
			
		}
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
		//online
		resultMap.put("accountId", loginContent.get("accountId"));
		resultMap.put("creator", loginContent.get("creator"));
		resultMap.put("nickName", loginContent.get("nickName"));
		
		result.put("result", resultMap);
		return result;
	}
	
	public boolean isSuccess(){
		if(user!=null && user.getChannelUserContent()!=null){
			loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
		}
		boolean isSuccess=false;
		if( null == user.getLoginType() || user.getLoginType().equals("1")){
			if( loginContent.get("code").toString().equals("1") ){
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
