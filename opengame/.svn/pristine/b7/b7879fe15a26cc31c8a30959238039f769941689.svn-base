package me.ckhd.opengame.online.httpclient.xiaomi;

import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.xiaomi.LoginResponse;
import me.ckhd.opengame.online.response.xiaomi.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);

	private static final String GOURL="http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";//接口地址
	String errMsg="";
	private static final String GOPARAM = "appId=%s&session=%s&uid=%s&signature=%s";
	Map<String, Object> reqParam=null;
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		reqParam = request.getParam();
		String appid = reqParam.get("appId")==null?"":reqParam.get("appId").toString();
		String session = reqParam.get("session")==null?"":reqParam.get("session").toString();
		String uid = reqParam.get("uid")==null?"":reqParam.get("uid").toString();
		String signature = reqParam.get("signature")==null?"":reqParam.get("signature").toString();
		String params = String.format(GOPARAM,appid,session,uid,signature );
		logger.info("登陆时发送给渠道的数据为:"+params);
		String result =HttpClientUtils.doGet(GOURL,params,null);
		if(result==null){
			errMsg="未获取到相关用户信息";
		}else{
			Map<String, Object> map = MyJsonUtils.jsonStr2Map(result);
			if (map==null || !map.containsKey("errcode") || !"200".equals(map.get("errcode").toString())) {
				errMsg=map.get("errcode")==null?"验证失败":map.get("errcode").toString();
			}
		}
		return new LoginResponse(getUser(result,request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setSid(reqParam.get("uid")==null?"":reqParam.get("uid").toString());
		user.setUserName(reqParam.get("uid")==null?"":reqParam.get("uid").toString());
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
