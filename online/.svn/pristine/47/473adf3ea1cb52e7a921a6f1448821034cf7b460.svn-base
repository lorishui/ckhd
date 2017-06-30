package me.ckhd.opengame.online.httpclient.dianyou;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.dianyou.LoginResponse;
import me.ckhd.opengame.online.response.dianyou.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	static final String verifyUrl = "http://42.51.172.15/cpa_center/user/check.do";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String token = request.verifyInfo.get("token")!=null?request.verifyInfo.getString("token"):"";
		String data = "userCertificate="+token+"&userid="+request.verifyInfo.get("userId");
		String result = HttpClientUtils.get(verifyUrl, data, 2000, 2000);
		return new LoginResponse(getUser(result,request));
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
		if(StringUtils.isNotBlank(result) && result.replace("\n", "").replace("\r", "").equals("true")){
			user.setErrMsg("success");
		}
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
