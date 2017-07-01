package me.ckhd.opengame.online.httpclient.weixin;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.httpclient.wft.HttpUtils;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.weixin.PayResponse;
import me.ckhd.opengame.online.util.XmlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOPAYURL="https://api.mch.weixin.qq.com/pay/unifiedorder";//支付预付接口地址
	
//	private static final String GOLOGINURL="";//接口地址

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
		String reqdate = XmlUtils.toXml(request.getPayParam());
		logger.info("支付时发送给渠道的数据为:"+reqdate);
		String content = HttpUtils.post(GOPAYURL,reqdate, "utf-8");
		BasePayResponse response = new PayResponse(getPay(content, request));
		return response;
	}
	
	
	

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}	
}
