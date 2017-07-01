package me.ckhd.opengame.online.httpclient.wandoujia;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.wandoujia.LoginResponse;
import me.ckhd.opengame.online.response.wandoujia.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	static final String verifyUrl = "https://pay.wandoujia.com/api/uid/check";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String token = request.verifyInfo.get("token")!=null?request.verifyInfo.getString("token"):"";
		String uid = request.verifyInfo.get("uid")!=null?request.verifyInfo.getString("uid"):"";
		String appkey_id = request.payInfoConfig.getAppid();
		String data = "token="+token+"&uid="+uid+"&appkey_id="+appkey_id;
		String result = HttpClientUtils.get(verifyUrl, data, 2000, 2000);
		return new LoginResponse(getUser(result,request));
	}
	
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg("failure");
		user.setSid(request.verifyInfo.get("uid")!=null?request.verifyInfo.getString("uid"):"");
		user.setUserName(request.verifyInfo.get("userName")!=null?request.verifyInfo.getString("userName"):"");
		if(result != null && result.replace("\n", "").replace("\r", "").equals("true")){
			user.setErrMsg("success");
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
