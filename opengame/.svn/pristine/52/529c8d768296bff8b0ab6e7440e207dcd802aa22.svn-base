package me.ckhd.opengame.online.httpclient.hs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.hs.LoginResponse;
import me.ckhd.opengame.online.response.hs.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient extends BaseHttpClient {
	final static String payUrl = "http://pay.sdk.new.5isy.com/center/getCommand.ashx";
	Logger logger = LoggerFactory.getLogger(HttpClient.class);

	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		if("2".equals(request.loginType)){
			return new LoginResponse(getUser("", request)); 
		}
		return null;
	}
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		OnlineUser user = new OnlineUser();
		user.setChannelId(request.channelId);
		user.setCkAppId(request.ckAppId);
		user.setChannelUserContent(result);
		user.setLoginParam(request.getParam());
		user.setVersion(request.version);
		user.setAppVerifyInfo(request.verifyInfo.toJSONString());
		user.setLoginType(request.loginType);
		if(StringUtils.isNotBlank(request.loginType) && "2".equals(request.loginType)){
			user.setSid(request.getParam().get("userId") == null ? "": request.getParam().get("userId").toString());
			user.setUserName(request.getParam().get("userName") == null ? "": request.getParam().get("userName").toString());
		}
		return user;
	}
	
	
//--------------------------------------支付-------------------------------------------------------
	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		//设置实际金额
		request.onlinePay.setActualAmount(String.valueOf(request.onlinePay.getPrices()));
		
		String partnerId = request.onlinePay.getPayInfoConfig().getAppkey();
		String appId = request.onlinePay.getPayInfoConfig().getAppid();
		String channelId = request.onlinePay.getPayInfoConfig().getExInfoMap().get("channelId").toString();
		String appFeeId = request.onlinePay.getPayCodeConfig().getExInfoMap().get("hsPayCode").toString();
		int fee = request.onlinePay.getPrices();
		String imei = request.verifyInfo.get("imei")!=null?request.verifyInfo.get("imei").toString():"";
		String imsi = request.verifyInfo.get("imsi")!=null?request.verifyInfo.get("imsi").toString():"";
		String os_info = request.verifyInfo.get("osInfo")!=null?request.verifyInfo.get("osInfo").toString():"";
		String os_model = request.verifyInfo.get("osModel")!=null?request.verifyInfo.get("osModel").toString():"";
		String net_info = request.verifyInfo.get("netInfo")!=null?request.verifyInfo.get("netInfo").toString():"";
		String extra = request.onlinePay.getOrderId();
		long timestamp = System.currentTimeMillis();
		String client_ip = request.onlinePay.getClientIp();
//		String client_ip = "59.173.11.90";
		String province = request.verifyInfo.get("provinceName")!=null?request.verifyInfo.get("provinceName").toString():"";
//		String province = "";
		
		StringBuffer content = new StringBuffer();
		try {
			content.append("partnerId="+partnerId+"&appId="+appId+"&channelId="+channelId+"&appFeeId="+appFeeId)
				.append("&fee="+fee+"&imei="+imei+"&imsi="+imsi+"&os_info="+os_info+"&os_model="+os_model)
				.append("&net_info="+net_info+"&timestamp="+timestamp+"&client_ip="+client_ip+"&extra="+extra+"&province="+URLEncoder.encode(province,"utf-8"));
		} catch (UnsupportedEncodingException e) {
			log.error("hs pay error!", e);
		}
		log.info("hs http data="+content.toString());
		String result = HttpClientUtils.get(payUrl, content.toString(), 2000, 2000);
		return  new PayResponse(getPay(result, request));
	}

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}	
}
