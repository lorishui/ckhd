package me.ckhd.opengame.online.httpclient.tencent;

import java.net.URLEncoder;
import java.util.Map;

import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.tencent.LoginResponse;
import me.ckhd.opengame.online.response.tencent.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
//	private String verifyUrl="http://ysdk.qq.com/auth/verify_login/?timestamp=%s&appid=%s&sig=%s&openid=%s&encode=1";//正式接口地址
	public String qqVerifyUrl="http://ysdktest.qq.com/auth/qq_check_token?timestamp=%s&appid=%s&sig=%s&openid=%s&openkey=%s";//测试接口地址
	public String wxVerifyUrl="http://ysdktest.qq.com/auth/wx_check_token?timestamp=%s&appid=%s&sig=%s&openid=%s&openkey=%s";//测试接口地址
	
	public String uid = "";
	public String username = "";
	
	String errMsg="";
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		JSONObject json = request.verifyInfo;
		if( json != null && "sandbox".equalsIgnoreCase(json.getString("environment"))){
		}else{
			qqVerifyUrl = qqVerifyUrl.replace("ysdktest", "ysdk");
			wxVerifyUrl = wxVerifyUrl.replace("ysdktest", "ysdk");
		}
		String result="";
		String code = null;
		if (StringUtils.isBlank(request.loginType) || "1".equals(request.loginType)) {
			Map<String,Object> map = request.getParam();
			String appid = request.payInfoConfig.getAppid();
			String appKey = request.payInfoConfig.getAppkey();
			if( null != map.get("tencentLoginType") && map.get("tencentLoginType").toString().equals("2")){
				Map<String,Object> exinfo = request.payInfoConfig.getExInfoMap();
				appid = exinfo.get("appId") != null?exinfo.get("appId").toString() : "";
				appKey = exinfo.get("appKey").toString() != null ? exinfo.get("appKey").toString() : "";
			}
			String openid = "";
			String openkey = "";
			String userip = "";
			if( map != null && map.size() > 0 ){
				openid = map.get("openId")==null?"":map.get("openId").toString();
				openkey = map.get("accessToken")==null?"":map.get("accessToken").toString();	
				userip = map.get("ip")==null?"":map.get("ip").toString();	
			}
			//数据格式  json
			JSONObject jsonData = new JSONObject();
			jsonData.put("appid", appid);
			jsonData.put("openid", openid);
			jsonData.put("accessToken", openkey);
			jsonData.put("openkey", openkey);
			jsonData.put("userip", userip);
			String time = System.currentTimeMillis()+"";
			String sig = Encodes.string2MD5(appKey+time);
			String verifyUrl = String.format(qqVerifyUrl,time,appid,sig,openid,URLEncoder.encode(openkey));
			if( null != map.get("tencentLoginType") && map.get("tencentLoginType").toString().equals("2")){
				verifyUrl = String.format(wxVerifyUrl, time,appid,sig,openid,URLEncoder.encode(openkey));
			}
			result = HttpClientUtils.get(verifyUrl, null, 2000, 2000);
			//响应数据格式 
			if(result==null || result.equals("")){
				errMsg="未获取到相关用户信息";
			}else{
				JSONObject resultData = JSONObject.parseObject(result);
				code = resultData.get("ret").toString();
				if( resultData.getInteger("ret") == 0 ){
					
				}else{
					errMsg = resultData.getString("msg");
				}
			}
		}else if("2".equals(request.loginType)){
			
		}
		return new LoginResponse(getUser(code,request));
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		if(StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)){
			user.setSid(request.getParam().get("openId") == null ? "": request.getParam().get("openId").toString());
			user.setUserName(request.getParam().get("openId") == null ? "": request.getParam().get("openId").toString());
		}else if( null == request.loginType || "1".equals(request.loginType)){
			user.setSid(request.getParam().get("openId") == null ? "": request.getParam().get("openId").toString());
			user.setUserName(request.getParam().get("openId") == null ? "": request.getParam().get("openId").toString());
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
