package me.ckhd.opengame.online.httpclient.meizu;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.meizu.LoginResponse;
import me.ckhd.opengame.online.response.meizu.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	private static final String GOURL="https://api.game.meizu.com/game/security/checksession";//接口地址
	Logger log = LoggerFactory.getLogger(HttpClient.class);
	String errMsg="";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		String result = "";
		if( request.loginType == null || request.loginType.trim().length() == 0 || request.loginType.equals("1") ){
			String app_id = request.payInfoConfig.getAppid();
			String appKey = request.payInfoConfig.getAppkey();
			long ts = System.currentTimeMillis();
			String session_id = request.getParam().get("sessionId")!= null?request.getParam().get("sessionId").toString():"";
			String uid = request.getParam().get("uid")!= null?request.getParam().get("uid").toString():"";
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("app_id", app_id);
			param.put("ts", ts);
			param.put("session_id", session_id);
			param.put("uid", uid);
			String signContent = SignContext.getMeizuSignContext(param);
			log.info("meizu sign content="+signContent);
			String content = signContent + ":" +  appKey;
			String sign = CoderUtils.md5(content, "utf-8");
			log.info("meizu sign="+sign);
			param.put("sign", sign);
			param.put("sign_type", "md5");
			String data = signContent+"&sign="+sign+"&sign_type=md5";
			result =HttpClientUtils.postVivo(GOURL, data, 2000, 2000);
			if(result==null||"".equals(result)){
				errMsg="未获取到相关信息";
				return new LoginResponse(getUser(result,request));
			}
			Map<String, Object> map = MyJsonUtils.jsonStr2Map(result);
			if(!isSuccess(map)){
				errMsg=map.get("message").toString();
			}
		}
		return new LoginResponse(getUser(result,request));
	}
	
	private boolean isSuccess(Map<String, Object> map) {
		if( Integer.valueOf(map.get("code").toString())==200 ){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setErrMsg(errMsg);
		user.setChannelUserContent(result);
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		if (!"2".equals(request.loginType)) {
			user.setSid(request.getParam().get("uid") == null ? "": request.getParam().get("uid").toString());
			user.setUserName(request.getParam().get("uid") == null ? "": request.getParam().get("uid").toString());
		}else{
			user.setSid(request.verifyInfo.getString("userId") == null ? "": request.getParam().get("userId").toString());
			user.setUserName(request.verifyInfo.getString("userName") == null ? "": request.verifyInfo.getString("userName").toString());
		}
		return user;
	}

	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		Map<String,Object> map = new HashMap<String, Object>();
		if(request.onlinePay.getSdkType() == null || request.onlinePay.getSdkType().trim().length() == 0 || request.onlinePay.getSdkType().equals("1")){
			//网游需要加密
			map.put("uid", request.onlinePay.getPayMap().get("userId").toString().substring(0,request.onlinePay.getPayMap().get("userId").toString().lastIndexOf("&")));
			map.put("app_id", request.onlinePay.getPayInfoConfig().getAppid());
			map.put("cp_order_id", request.onlinePay.getOrderId());
			map.put("product_id", "0");
			map.put("product_subject",request.verifyInfo.get("productName"));
			map.put("product_body", "");
			map.put("product_unit","");
			map.put("buy_amount", 1);
			map.put("product_per_price",((float)request.onlinePay.getPrices())/100);
			map.put("total_price", ((float)request.onlinePay.getPrices())/100);
			map.put("create_time", System.currentTimeMillis());
			map.put("pay_type", "0");
			map.put("user_info", "");
			String signContent = SignContext.getMeizuPaySignContext(map);
			log.info("meizu pay sign content="+signContent);
			String sign = CoderUtils.md5(signContent+":"+request.onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
			map.put("sign", sign);
		}
		//单机不需要做任何操作
		return new PayResponse(getPay(JSONObject.toJSONString(map), request));
	}


	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
}
