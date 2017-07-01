package me.ckhd.opengame.online.httpclient.alipay;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.alipay.PayResponse;
import me.ckhd.opengame.online.response.gionee.LoginResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOPAYURL="";//支付预付接口地址
	
	private static final String GOLOGINURL="";//接口地址

	String errMsg="";//错误信息
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		if("2".equals(request.loginType)){
			return new LoginResponse(getUser("", request)); 
		}
		return null;
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		if (!"2".equals(request.loginType)) {
			
		}else{
			user.setSid(request.getParam().get("userId") == null ? "": request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? "": request.getParam().get("userName").toString());
		}
		user.setLoginParam(request.getParam());
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
