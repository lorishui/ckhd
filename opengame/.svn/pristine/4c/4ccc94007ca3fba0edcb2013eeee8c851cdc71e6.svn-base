package me.ckhd.opengame.online.httpclient.xy;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.xy.LoginResponse;
import me.ckhd.opengame.online.response.xy.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

public class HttpClient extends BaseHttpClient {
	private static final String verifyUrl="http://passport.xyzs.com/checkLogin.php";
	
	public String uid = "";
	public String username = "";
	
	String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String result="";
		String uid = request.verifyInfo.get("userId")==null?"":request.verifyInfo.getString("userId");
		String appid = request.payInfoConfig.getAppid();
		String token = request.verifyInfo.get("token")==null?"":request.verifyInfo.getString("token");
		String data = "uid="+uid+"&appid="+appid+"&token="+token;
		result = HttpClientUtils.post(verifyUrl, data, 2000, 2000,"application/x-www-form-urlencoded;");
		return new LoginResponse(getUser(result,request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setSid(request.verifyInfo.get("userId") == null ? "": request.verifyInfo.getString("userId"));
		user.setUserName(request.verifyInfo.get("userName") == null ? "": request.verifyInfo.getString("userName"));
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
