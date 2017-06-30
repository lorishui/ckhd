package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("jifeng")
@Scope("prototype")
public class jifengHandle extends BaseHandle{
	static final String verifyUrl = "http://api.gfan.com/uc1/common/verify_token";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject json = new JSONObject();
		JSONObject verifyInfo = codeJson.containsKey("verifyInfo")?codeJson.getJSONObject("verifyInfo"):null;
		if(verifyInfo != null ){
			String token = verifyInfo.containsKey("token")?verifyInfo.getString("token"):"";
			String data = "token="+token;
			String resultStr = HttpClientUtils.get(verifyUrl, data, 2000, 2000);
			JSONObject result = null;
			if(StringUtils.isNotBlank(resultStr)){
				result = JSONObject.parseObject(resultStr);
				if(result != null && result.getInteger("resultCode") == 1){
					setOnlineUser(onlineUser, codeJson);
					onlineUser.setSid(result.getString("uid")!=null?result.getString("uid"):"");
					onlineUser.setUserName(result.getString("uid")!=null?result.getString("uid"):"");
					onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
					json.put("resultCode", 0);
					json.put("errMsg", "SUCCESS");
					JSONObject child = new JSONObject();
					child.put("uid", onlineUser.getUid());
					child.put("token",  onlineUser.getToken());
					child.put("userName", onlineUser.getUserName());
					json.put("result", child);
				}else{
					json.put("resultCode", 2013);
					json.put("errMsg", "向平台验证失败!");
				}
			}
		}else{
			json.put("resultCode", 2012);
			json.put("errMsg", "缺少参数verifyInfo!");
		}
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String verifyData(OnlinePay onlinePay,JSONObject result,HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturnSuccess() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturnFailure() {
		// TODO Auto-generated method stub
		return null;
	}

}
