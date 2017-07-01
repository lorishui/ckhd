package me.ckhd.opengame.online.httpclient.m4399;

import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.m4399.LoginResponse;
import me.ckhd.opengame.online.response.m4399.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

public class HttpClient extends BaseHttpClient {
	private static final String GOURL="http://m.4399api.com/openapi/oauth-check.html";//接口地址
	String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String result =HttpClientUtils.doGet(GOURL,request.getParam());
		if(result==null||"".equals(result)){
			errMsg="未获取到相关信息";
			return new LoginResponse(getUser(result,request));
		}
		Map<String, Object> map = MyJsonUtils.jsonStr2Map(result);
		if(!isSuccess(map)){
			errMsg=map.get("message").toString();
		}
		return new LoginResponse(getUser(result,request));
	}
	
	private boolean isSuccess(Map<String, Object> map) {
		if(Integer.valueOf(map.get("code").toString())==100){
			return true;
		}else{
			return false;
		}
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

	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		return new PayResponse(getPay("", request));
	}


	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
}
