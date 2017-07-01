package me.ckhd.opengame.online.response.oppo;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.nearme.oauth.model.UserInfo;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> briefUser;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if ("10001".equals(user.getVersion())) {
			if(user!=null && user.getChannelUserContent()!=null){
				loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
			}
			if(isSuccess()){
				briefUser = (HashMap<String, Object>)loginContent.get("BriefUser");
				user.setSid(briefUser.get("id")==null?"":briefUser.get("id").toString());
				user.setUserName(briefUser.get("userName")==null?"":briefUser.get("userName").toString());
			}
		}else if("10002".equals(user.getVersion())){
			if(user!=null && user.getChannelUserContent()!=null){
				loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
			}
			if(isSuccess()){
				user.setSid(loginContent.get("ssoid")==null?"":loginContent.get("ssoid").toString());
				user.setUserName(loginContent.get("userName")==null?"":loginContent.get("userName").toString());
			}
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
		resultMap.put("expansion","");
		result.put("result", resultMap);
		return result;
	}
	
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
