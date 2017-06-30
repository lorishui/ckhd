package me.ckhd.opengame.online.response;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;

public abstract class BaseLoginResponse {

	protected Map<String, Object> result = new HashMap<String, Object>();
	protected Map<String, Object> loginContent = new HashMap<String, Object>();
	protected OnlineUser user;
	public BaseLoginResponse(OnlineUser _user) {
		user=_user;
	}
    
	public boolean isSuccess(){
		if(user!=null && user.getChannelUserContent()!=null){
			loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
		}
		boolean isSuccess=true;
		if((user.getErrMsg()==null || "".equals(user.getErrMsg())) && loginContent!=null && loginContent.size()>0){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		}else{
			result.put("resultCode", -1);
			result.put("errMsg",user.getErrMsg());
			isSuccess=false;
		}
		return isSuccess;
	}
	
    public abstract Map<String, Object> getResult();
    
    public  OnlineUser getUserInfo(){
    	return user;
    }
    
    public void refreshUser(OnlineUser _user){
    	user=_user;
    }
}
