package me.ckhd.opengame.online.httpclient.huawei;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.huawei.LoginResponse;
import me.ckhd.opengame.online.response.huawei.PayResponse;
import me.ckhd.opengame.online.response.huawei.RSA;
import me.ckhd.opengame.util.HttpClientUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpClient extends BaseHttpClient {
	
	private static String verifyUrl = "https://api.vmall.com/rest.php";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
//		nsp_svc=OpenUP.User.getInfo&nsp_ts=1358237366&access_token=mF_9.B5f-4.1JqM
		StringBuffer sb = new StringBuffer("nsp_svc=OpenUP.User.getInfo&");
		sb.append("nsp_ts="+System.currentTimeMillis()/1000+"&");
		try {
			sb.append("access_token="+URLEncoder.encode(request.verifyInfo.get("accessToken").toString(),"utf-8").replace("+", "%2B"));
		} catch (UnsupportedEncodingException e) {
			log.error(" huawei login url encode failure!",e);
		}
		String result = HttpClientUtils.post(verifyUrl, sb.toString(), 2000, 2000);
		return new LoginResponse(getUser(result, request));
	}
	
	
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		Map<String,Object> map = request.verifyInfo;
		map.put("applicationID", request.onlinePay.getPayInfoConfig().getAppid());
		map.put("amount", getDoubleNumber(request.onlinePay.getPrices()*0.01));//金额单位转换
		map.put("requestId", request.onlinePay.getOrderId());
		//获取需要加密的串
		String signContent = RSA.getSignData(map);
		String sign = RSA.sign(signContent, request.onlinePay.getPayInfoConfig().getAppkey());
		return new PayResponse(getPay(sign, request));
	}
	
	@Override
	public OnlineUser getUser(String returnCode, BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setChannelUserContent(returnCode);
		user.setLoginParam(request.getParam());
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		if(returnCode != null && returnCode.length() >0){
			JSONObject json = JSONObject.parseObject(returnCode);
			user.setSid(json.getString("userID"));
			user.setUserName(json.getString("userName"));
		}else{
			user.setErrMsg("授权失败!");
		}
		return user;
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String getDoubleNumber(Double d){
		DecimalFormat df = new DecimalFormat("#.00");
        return  df.format(d);
	}

}
