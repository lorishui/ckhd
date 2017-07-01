package me.ckhd.opengame.online.httpclient.qihoo;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.qihoo.PayResponse;
import me.ckhd.opengame.online.response.qihoo.LoginResponse;
import me.ckhd.opengame.util.HttpClientUtils;

public class HttpClient extends BaseHttpClient {
	private static final String GOURL="https://openapi.360.cn/user/me.json";//接口地址
	String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String result =HttpClientUtils.doGet(GOURL,request.getParam());
		if(result==null){
			errMsg="未获取到相关用户信息";
		}
		return new LoginResponse(getUser(result,request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setChannelUserContent(result);
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
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
