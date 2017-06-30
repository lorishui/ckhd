package me.ckhd.opengame.online.httpclient.lenovo2;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.lenovo2.LoginResponse;
import me.ckhd.opengame.online.response.lenovo2.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOLOGINURL="http://passport.lenovo.com/interserver/authen/1.2/getaccountid";//接口地址

	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String,Object> map = new HashMap<String, Object>();
		String data="lpsust="+request.verifyInfo.getString("accessToken")+"&realm="+request.payInfoConfig.getAppid();
		String respData = HttpClientUtils.get(GOLOGINURL, data, 2000, 2000);
		log.info(" lenovo2 login result="+respData);
		if(respData != null && respData.length() > 0 ) map = XmlUtils.Str2Map(respData);
		return  new LoginResponse(getUser(JSONObject.toJSONString(map),request));
	}
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		JSONObject json = JSONObject.parseObject(result);
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setChannelUserContent(result);
		user.setLoginParam(request.getParam());
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		if(json.containsKey("AccountID")){
			user.setSid(json.get("AccountID") == null ? "": json.get("AccountID").toString());
			user.setUserName(json.get("Username") == null ? "": json.get("Username").toString());
		}else{
			user.setErrMsg(json.getString("Code"));
		}
		return user;
	}
	
	
//-----------------------TODO---------------支付-------------------------------------------------------
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		return  new PayResponse(getPay("", request));
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}	
}
