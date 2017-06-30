package me.ckhd.opengame.online.httpclient.oppo;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.oppo.LoginResponse;
import me.ckhd.opengame.online.response.oppo.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.nearme.oauth.model.AccessToken;
import com.nearme.oauth.open.AccountAgent;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	String errMsg="";
	private static final String GOURL="http://i.open.game.oppomobile.com/gameopen/user/fileIdInfo";//接口地址

	
//------------------------------TODO-----------------------登陆-----------------------------------------
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		
		Map<String, Object> map = request.getParam();
		String userInfo="";
		if("10001".equals(request.version)){
			try {
				userInfo = AccountAgent.getInstance().getGCUserInfo(new AccessToken(map.get("oauth_token").toString(),map.get("oauth_token_secret").toString()));
			}catch(Exception e){
				e.printStackTrace();
				errMsg=e.getMessage();
			}
			return new LoginResponse(getUser(userInfo,request));
		}else if("10002".equals(request.version)){
			logger.info(String.format("发送渠道的数据:params[%s],headers[%s]", JSONObject.toJSONString((HashMap<String, Object>)map.get("params")),JSONObject.toJSONString((HashMap<String, Object>)map.get("headers"))));
			userInfo=HttpClientUtils.doGetHeader(GOURL,(HashMap<String, Object>)map.get("params"),(HashMap<String, String>)map.get("headers"));
			logger.info("userInfo===="+userInfo);
			if(userInfo==null){
				errMsg="未获取到相关用户信息";
			}else{
				Map<String, Object> user = MyJsonUtils.jsonStr2Map(userInfo);
				if (user==null || !user.containsKey("resultCode") || !"200".equals(user.get("resultCode").toString())) {
					errMsg=user.get("resultMsg")==null?"验证失败":user.get("resultMsg").toString();
				}
			}
			return new LoginResponse(getUser(userInfo,request));
		}
		return new LoginResponse(getUser("",request));
	}

	@Override
	public OnlineUser getUser(String userInfo,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setChannelUserContent(userInfo);
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
