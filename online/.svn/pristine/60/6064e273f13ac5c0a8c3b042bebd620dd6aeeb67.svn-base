package me.ckhd.opengame.online.httpclient.jifeng;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.jifeng.LoginResponse;
import me.ckhd.opengame.online.response.jifeng.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	static final String verifyUrl = "http://api.gfan.com/uc1/common/verify_token";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String token = request.verifyInfo.get("token")!=null?request.verifyInfo.getString("token"):"";
		String data = "token="+token;
		String result = HttpClientUtils.get(verifyUrl, data, 2000, 2000);
		return new LoginResponse(getUser(result,request));
	}
	
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		JSONObject json = null;
		if(StringUtils.isNotBlank(result)){
			json = JSONObject.parseObject(result);
		}
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg("failure");
		if(json != null && json.getInteger("resultCode") == 1){
			user.setErrMsg("success");
			user.setSid(json.getString("uid")!=null?json.getString("uid"):"");
			user.setUserName(json.getString("uid")!=null?json.getString("uid"):"");
		}else{
			user.setErrMsg("failure");
		}
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		return user;
	}

	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		return new PayResponse(getPay("", request));
	}
	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
}
