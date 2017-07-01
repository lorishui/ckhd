package me.ckhd.opengame.online.response.unionpay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.response.BaseLoginResponse;

import com.alibaba.fastjson.JSONObject;


public class LoginResponse extends BaseLoginResponse {

	
	Map<String, Object> ply;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if(!"2".equals(user.getLoginType())){
			if(isSuccess()){
				JSONObject json = JSONObject.parseObject(user.getChannelUserContent());
				String tem = json.get("ply")==null?"":json.get("ply").toString();
				List<Map<String, Object>> briefUser=MyJsonUtils.jsonArrayStr2List(tem);
				if(briefUser!=null && briefUser.size()>0){
					ply = briefUser.get(0);
				}
				if(ply!=null){
					user.setSid(ply.get("pid")==null?"":ply.get("pid").toString());
					user.setUserName(ply.get("na")==null?"":ply.get("na").toString());
				}
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
	
	public boolean isSuccess(){
		if(user!=null && user.getChannelUserContent()!=null){
			loginContent=MyJsonUtils.jsonStr2Map(user.getChannelUserContent());
		}
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
	
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
