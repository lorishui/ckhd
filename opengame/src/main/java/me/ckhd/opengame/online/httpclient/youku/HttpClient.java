package me.ckhd.opengame.online.httpclient.youku;

import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.youku.HmacMD5;
import me.ckhd.opengame.online.response.youku.LoginResponse;
import me.ckhd.opengame.online.response.youku.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	final static String verfityUrl = "http://sdk.api.gamex.mobile.youku.com/game/user/infomation";
	private String errMsg = "";
	static String apiKey = ""; // 替换成商户申请获取的APIKey
	static String secretKey = ""; // 替换成商户申请获取的SecretKey
	String uid = "";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String loginType = request.loginType;
		if("2".equals(loginType)){
			return new LoginResponse(getUser("", request));
		}else{
			String sessionid = request.getParam().get("sessionId")!=null?request.getParam().get("sessionId").toString():"";
			String appkey = request.payInfoConfig.getAppid();
			String signContent = "appkey="+appkey+"&sessionid="+sessionid;
			String sign = HmacMD5.hmac(signContent, request.payInfoConfig.getAppkey());
			String data = signContent+"&sign="+sign;
			String result = HttpClientUtils.post(verfityUrl, data, 2000, 2000);
			if( result != null ){
				JSONObject json = JSONObject.parseObject(result);
				if(json.containsKey("status") && json.getString("status").equals("success")){
					uid = json.getString("uid");
				}
			}
		}
		return new LoginResponse(getUser("", request));
	}

	@Override
	public OnlineUser getUser(String result, BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		if (!"2".equals(request.loginType)) {
			user.setSid(uid);
			user.setUserName(uid);
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

	// -----------------------TODO---------------支付-------------------------------------------------------
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		Map<String, Object> params = request.getPayParam();
		if("2".equals(params.get("sdkType").toString())){
			return new PayResponse(getPay("", request));
		}
		return new PayResponse(getPay("", request));
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
}
