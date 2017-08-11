package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("fb")
@Scope("prototype")
public class facebookHandle extends BaseHandle{

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(verifyInfo.getString("userId"));
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject returnData = new JSONObject();
		onlineUser.setUid("fb#"+onlineUser.getSid());
		// FB = 3
		onlineUser.setSdkLoginType(3);
		returnData.put("uid", onlineUser.getUid() );
		returnData.put("token",getRandomStr(16));
		result.put("result", returnData);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		return null;
	}

	@Override
	public String getReturnSuccess() {
		return null;
	}

	@Override
	public String getReturnFailure() {
		return null;
	}
	
}
