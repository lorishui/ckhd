package me.ckhd.opengame.online.httpclient.anzhi;

import java.util.Date;
import java.util.Map;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.anzhi.LoginResponse;
import me.ckhd.opengame.online.response.anzhi.PayResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOLOGINURL="http://user.anzhi.com/web/api/sdk/third/1/queryislogin";//接口地址
	private String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		Map<String,Object> msgTemp = request.getParam();
		logger.info("登陆时发送给渠道的数据为:"+msgTemp);
		org.apache.commons.httpclient.HttpClient httpclient = new org.apache.commons.httpclient.HttpClient();
		PostMethod post = new PostMethod(GOLOGINURL);
		String respStr = null;
		try{
			post.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
			NameValuePair[] data ={new NameValuePair("appkey",request.payInfoConfig.getAppkey()),new NameValuePair("account",msgTemp.get("loginname").toString()),new NameValuePair("sid",msgTemp.get("sid").toString()),new NameValuePair("sign",msgTemp.get("sign").toString()),new NameValuePair("time",DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS"))};
	        post.setQueryString(data);
			httpclient.getHostConfiguration().setHost(GOLOGINURL);
			int result = 0;
			result = httpclient.executeMethod(post);
			if (result == HttpStatus.SC_OK) {
				respStr = post.getResponseBodyAsString();
			}
		}catch (Exception e) {
			e.printStackTrace();
			errMsg=(StringUtils.isBlank(e.getMessage())?"连接失败":e.getMessage());
		}
		Map<String, Object> obj = MyJsonUtils.jsonStr2Map(respStr);
		if(!obj.containsKey("sc") || !"1".equals(obj.get("sc").toString())){
			errMsg = obj.get("st").toString();
		}
		return  new LoginResponse(getUser(respStr,request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setUserName(request.getParam().get("loginname")==null?"":request.getParam().get("loginname").toString());
		user.setErrMsg(errMsg);
		user.setChannelUserContent(result);
		user.setLoginParam(request.getParam());
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
