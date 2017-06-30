package me.ckhd.opengame.online.httpclient.yyh;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.yyh.LoginResponse;
import me.ckhd.opengame.online.response.yyh.PayResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		return new LoginResponse(getUser("",request));
	}
	
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setSid(request.verifyInfo.get("userId")!=null?request.verifyInfo.getString("userId"):"");
		user.setUserName(request.verifyInfo.get("userName")!=null?request.verifyInfo.getString("userName"):"");
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		user.setErrMsg("failure");
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
