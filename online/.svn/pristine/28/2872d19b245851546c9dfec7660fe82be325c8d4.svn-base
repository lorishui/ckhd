package me.ckhd.opengame.online.httpclient.letv;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.letv.LoginResponse;
import me.ckhd.opengame.online.response.letv.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	private static final String GOURL="https://sso.letv.com/oauthopen/userbasic";//接口地址
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	String errMsg="";
	Map<String, Object> resultMap;
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String, Object> map = request.getParam();
		StringBuilder param = new StringBuilder();
		param.append("client_id=");
		param.append(map.get("client_id").toString());
		param.append("&uid=");
		param.append(map.get("uid").toString());
		param.append("&access_token=");
		param.append(map.get("access_token").toString());
		logger.info("登陆时发送给渠道的数据为:"+param.toString());
		String result =HttpClientUtils.get(GOURL, param.toString(),2000,2000);
		try {
			if(StringUtils.isNotBlank(result)){
				resultMap = MyJsonUtils.jsonStr2Map(result);
				if(!isSuccess(resultMap)){
					errMsg = resultMap.get("error").toString();
				}
			}else{
				errMsg="未获取到用户信息";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg="未获取到用户信息";
		}
		return new LoginResponse(getUser(result,request));
	}

	private boolean isSuccess(Map<String, Object> resultMap) throws NumberFormatException, UnsupportedEncodingException{
		if(Integer.parseInt(resultMap.get("status").toString())==1){
			return true;
		}else{
			return false;
		}
	}
	
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelUserContent(result);
		if(StringUtils.isBlank(errMsg)){
			Map<String, Object> res = (HashMap<String, Object>)resultMap.get("result");
			user.setSid(res.containsKey("letv_uid")?res.get("letv_uid").toString():"");
			user.setUserName(res.containsKey("nickname")?res.get("nickname").toString():res.get("letv_uid").toString());
		}
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
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
