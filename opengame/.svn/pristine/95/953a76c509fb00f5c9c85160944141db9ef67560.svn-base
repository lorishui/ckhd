package me.ckhd.opengame.online.response.lenovo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.request.lenovo.sign.SignHelper;
import me.ckhd.opengame.online.response.BaseLoginResponse;


public class LoginResponse extends BaseLoginResponse {

	Map<String, Object> resultMap;
	Map<String, Object> transdataMap;
	String errMsg="";
	public LoginResponse(OnlineUser user) {
		super(user);
		if(isSuccess()){
			if(StringUtils.isBlank(user.getLoginType()) || "1".equals(user.getLoginType())){
				transdataMap = MyJsonUtils.jsonStr2Map(resultMap.get("transdata").toString());
				user.setSid(transdataMap.get("userid").toString());
				user.setUserName(transdataMap.get("loginname")==null?"":transdataMap.get("loginname").toString());
			}
		}
		
	}
	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", user.getUid());
		if(StringUtils.isBlank(user.getLoginType()) || "1".equals(user.getLoginType())){
			if(!"10001".equals(user.getVersion())){
				map.put("token",  user.getToken());
			}
		}else if("2".equals(user.getLoginType())){
			map.put("token",  user.getToken());
		}
		map.put("userName", user.getUserName());
		map.put("expansion","");
		result.put("result", map);
		return result;
	}
	
	public boolean verify(){
		try {
			String transdata = resultMap.get("transdata").toString(); // "{\"loginname\":\"18701637882\",\"userid\":\"14382295\"}";
			String sign="";
			if(resultMap.containsKey("sign")){
				sign = resultMap.get("sign").toString(); // 
			}else{
				return false;
			}
			/*
			 * 调用验签接口
			 * 
			 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
			 */
			if (SignHelper.verify(transdata, sign, user.getLoginParam().get("PLATP_KEY").toString())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isSuccess() {
		boolean flag = false;
		try {
			if(StringUtils.isBlank(user.getLoginType()) || "1".equals(user.getLoginType())){
				if(user.getChannelUserContent()!=null){
					resultMap=SignUtils.getParmters(user.getChannelUserContent());
					if(!verify()){
						String transdata=resultMap.get("transdata").toString();
						Map<String, Object> map = MyJsonUtils.jsonStr2Map(transdata);
						result.put("resultCode", -1);
						result.put("errMsg", map.get("errmsg")==null?"验证失败":map.get("errmsg").toString());
						flag= false;
					}else{
						result.put("resultCode", 0);
						result.put("errMsg", "");
						flag = true;
					}
				}else{
					result.put("resultCode", -1);
					result.put("errMsg", "未获取到用户信息");
					flag = false;
				}
			}else if("2".equals(user.getLoginType())){
				if((user.getErrMsg()==null || "".equals(user.getErrMsg()))){
					result.put("resultCode", 0);
					result.put("errMsg", "");
					flag = true;
				}else{
					result.put("resultCode", -1);
					result.put("errMsg",user.getErrMsg());
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("resultCode", -1);
			result.put("errMsg", "未获取到用户信息");
			flag = false;
		}
		return flag;
	}
	
	@Override
	public OnlineUser getUserInfo() {
		return user;
	}
}
