package me.ckhd.opengame.online.httpclient.unionpay;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.unionpay.PayResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);

	String errMsg="";//错误信息
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		return null;
	}

	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		return null;
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
