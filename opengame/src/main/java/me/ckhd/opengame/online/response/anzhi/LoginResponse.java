package me.ckhd.opengame.online.response.anzhi;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.util.anzhi.Base64;
import me.ckhd.opengame.online.util.anzhi.JacksonMapper;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> briefUser;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if(isSuccess()){
			Map map = JacksonMapper.readMapValue(user.getChannelUserContent().toString(), Object.class);
			String tem = Base64.decode((String)map.get("msg"),"UTF-8");
			briefUser=MyJsonUtils.jsonStr2Map(tem);
			user.setSid(briefUser.get("uid").toString());
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
	
	public boolean isSuccess(){
		if(user!=null && user.getChannelUserContent()!=null){
			loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
		}
		boolean isSuccess=true;
		if((user.getErrMsg()==null || "".equals(user.getErrMsg())) && loginContent.size()>0){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		}else{
			result.put("resultCode", -1);
			result.put("errMsg",user.getErrMsg());
			isSuccess=false;
		}
		return isSuccess;
	}
	
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
