package me.ckhd.opengame.online.httpclient.guopan;

import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.guopan.LoginResponse;
import me.ckhd.opengame.online.response.guopan.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	static final String verifyUrl = "http://userapi.guopan.cn/gamesdk/verify/";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String game_uin = request.verifyInfo.containsKey("gameUin")?request.verifyInfo.getString("gameUin"):"";
		String appid = request.payInfoConfig.getAppid();
		String token = request.verifyInfo.containsKey("token")?request.verifyInfo.getString("token"):"";
		long t = System.currentTimeMillis();
		String sign = MD5Util.string2MD5(game_uin+appid+t+request.payInfoConfig.getExInfoMap().get("server_secret_key"));
		String data = "game_uin="+game_uin+"&appid="+appid+"&token="+token+"&t="+t+"&sign="+sign;
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
