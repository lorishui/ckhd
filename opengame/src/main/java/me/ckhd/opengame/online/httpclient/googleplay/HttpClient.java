package me.ckhd.opengame.online.httpclient.googleplay;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.googleplay.LoginResponse;
import me.ckhd.opengame.online.response.googleplay.PayResponse;

public class HttpClient extends BaseHttpClient {
	
	String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		return new LoginResponse(getUser("",request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setSid(request.getParam().get("userId") == null ? "": request.getParam().get("userId").toString());
		user.setUserName(request.getParam().get("userName") == null ? "": request.getParam().get("userName").toString());
		user.setChannelUserContent(result);
		user.setVersion(request.version);
		user.setLoginType(request.loginType);
		user.setAppVerifyInfo(request.verifyInfo==null?"":request.verifyInfo.toJSONString());
		return user;
	}
	
	
//-----------------------TODO---------------支付-------------------------------------------------------
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		return new PayResponse(getPay("", request));
	}


	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
}
