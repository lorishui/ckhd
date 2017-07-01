package me.ckhd.opengame.online.httpclient.baidu;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.baidu.Base64;
import me.ckhd.opengame.online.response.baidu.LoginResponse;
import me.ckhd.opengame.online.response.baidu.PayResponse;
import me.ckhd.opengame.online.util.baidu.Sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	private static final String GOURL="http://querysdkapi.91.com/CpLoginStateQuery.ashx";//接口地址 
	private static final String GOURL2="http://querysdkapi.baidu.com/query/cploginstatequery";
	private static final String version = "3.6.0";
	
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	
	String errMsg="";
	String sign=""; 
	String uid = "";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String, Object> map = request.getParam();
		String loginType = request.loginType;
		if("2".equals(loginType)){
			return new LoginResponse(getUser("", request)); 
		}
		Sdk sdk = new Sdk();
		sign = Sdk.md5(request.payInfoConfig.getAppid() + map.get("accessToken") + request.payInfoConfig.getExInfoMap().get("SECRETKEY"));//签名
		StringBuilder param = new StringBuilder();
		param.append("AppID=");
		param.append(request.payInfoConfig.getAppid());
		param.append("&AccessToken=");
		param.append(map.get("accessToken").toString());
		param.append("&Sign=");
		param.append(sign.toLowerCase());
		logger.info("登陆时发送给渠道的数据为:"+param.toString());
		String result = null;
		Object versionNew = map.get("version");
		if( versionNew == null ){
			result = sdk.sendPost(GOURL, param.toString());
		}else{
			if(getLower(versionNew.toString())){
				result = sdk.sendPost(GOURL, param.toString());
			}else{
				result = sdk.sendPost(GOURL2, param.toString());
			}
		}
		try {
			if(result!=null){
				Map<String, Object> resultMap = MyJsonUtils.jsonStr2Map(result);
				if(!isSuccess(resultMap, sdk)){
					errMsg = resultMap.get("ResultMsg").toString();
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

	private boolean isSuccess(Map<String, Object> resultMap,Sdk sdk) throws NumberFormatException, UnsupportedEncodingException{
		if(Integer.parseInt(resultMap.get("ResultCode").toString())==1 ){			JSONObject json = JSONObject.parseObject(Base64.decode(resultMap.get("Content").toString()));
			uid = json.getString("UID");
			return true;
		}else{
			return false;
		}
	}
	
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		if (!"2".equals(request.loginType)) {
			user.setChannelUserContent(result);
			user.setSid(uid);
			user.setUserName(uid);
		}else{
			user.setSid(request.getParam().get("userId") == null ? ""
					: request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? ""
					: request.getParam().get("userName").toString());
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

	private boolean getLower(String verionNew){
		String[] obj = new String[2];
		obj[0] = verionNew;
		obj[1] = version;
		Arrays.sort(obj);
		if(verionNew.equals(obj[0])){
			return true;
		}
		return false;
	}
}
