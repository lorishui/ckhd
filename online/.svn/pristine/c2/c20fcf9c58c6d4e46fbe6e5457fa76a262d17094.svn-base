package me.ckhd.opengame.online.httpclient.dangle;

import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.dangle.LoginResponse;
import me.ckhd.opengame.online.response.dangle.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	static final String verifyUrl = "http://ngsdk.d.cn/api/cp/checkToken";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String token = request.verifyInfo.get("accessToken")!=null?request.verifyInfo.getString("accessToken"):"";
		String uid = request.verifyInfo.get("userId")!=null?request.verifyInfo.getString("userId"):"";
		//MD5(appId|appKey|token|umid)字符串
		String sig = MD5Util.string2MD5(request.payInfoConfig.getAppid()+"|"+request.payInfoConfig.getAppkey()+"|"+token+"|"+uid);
		String data = "token="+token+"&appid="+request.payInfoConfig.getAppid()+"&umid="+uid+"&sig="+sig;
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
		user.setSid(request.verifyInfo.get("userId")!=null?request.verifyInfo.getString("userId"):"");
		user.setUserName(request.verifyInfo.get("userName")!=null?request.verifyInfo.getString("userName"):"");
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		user.setErrMsg("failure");
		if(json != null && json.getInteger("msg_code") == 2000 && json.getInteger("valid") == 1){
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
