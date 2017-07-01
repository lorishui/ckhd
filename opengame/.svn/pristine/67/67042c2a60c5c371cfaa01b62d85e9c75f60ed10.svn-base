package me.ckhd.opengame.online.httpclient.uc;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.uc.LoginResponse;
import me.ckhd.opengame.online.response.uc.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	private static final String verifyUrl="http://sdk.g.uc.cn/cp/account.verifySession";//正式接口地址
//	private static final String verifyUrl="http://sdk.test4.g.uc.cn/cp/account.verifySession";//测试接口地址
	
	public String uid = "";
	public String username = "";
	
	String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String result="";
		JSONObject user = new JSONObject();
		if (StringUtils.isBlank(request.loginType) || "1".equals(request.loginType)) {
			Map<String,Object> map = request.getParam();
			String sid = "";
			if( map != null && map.size() > 0 ){
				sid = map.get("sid")==null?"":map.get("sid").toString();	
			}
			PayInfoConfig payInfoConfig = request.payInfoConfig;
			String gameid = payInfoConfig.getAppid();
			//加密规则 ：MD5(sid=...+apiKey)
			//数据格式  {key:value,key;{},key;{}}
			JSONObject jsonData = new JSONObject();
			JSONObject sidJ = new JSONObject();
			sidJ.put("sid", sid);
			JSONObject gameidJ = new JSONObject();
			gameidJ.put("gameId", gameid);
			jsonData.put("id", System.currentTimeMillis()/1000L);
			jsonData.put("data", sidJ);
			jsonData.put("game", gameidJ);
			jsonData.put("sign", MD5Util.string2MD5("sid="+sid+payInfoConfig.getAppkey()));
			result = HttpClientUtils.post(verifyUrl, jsonData.toJSONString(), 2000, 2000);
			//响应数据格式 
			if(result==null || result.equals("")){
				errMsg="未获取到相关用户信息";
			}else{
				try {
					result = new String(result.getBytes("gbk"),"utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				JSONObject resultData = JSONObject.parseObject(result);
				JSONObject state = JSONObject.parseObject(resultData.getString("state"));
				user.put("code", state.get("code"));
				if(state.getInteger("code") == 1){
					JSONObject data = JSONObject.parseObject(resultData.getString("data"));
					uid = data.getString("accountId");
					username = data.getString("nickName");
					user.put("accountId", data.getString("accountId"));
					user.put("nickName", data.getString("nickName"));
					
				}else{
					errMsg = state.get("msg").toString();
				}
			}
		}else if("2".equals(request.loginType)){
			
		}
		return new LoginResponse(getUser(user.toJSONString(),request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		if(StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)){
			user.setSid(request.getParam().get("userId") == null ? "": request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? "": request.getParam().get("userName").toString());
		}else if( null == request.loginType || "1".equals(request.loginType)){
			user.setSid(uid);
			user.setUserName(username);
		}
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
