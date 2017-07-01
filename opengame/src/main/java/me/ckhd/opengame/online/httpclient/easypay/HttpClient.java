package me.ckhd.opengame.online.httpclient.easypay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.easypay.PayResponse;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	private static final String GOPAYURL="http://api.easypay-tech.com/prepay";//支付预付接口地址
	
//	private static final String GOLOGINURL="";//接口地址
//	private static final String wx_appId="1480667098072516";
//	private static final String wx_appSecret="uWwtxl2SWIdptLggpBuywhIgxUb4FVa2";

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
		BasePayResponse response = null;
		try {
			String appId = request.onlinePay.getPayInfoConfig().getAppid();//
			String appSecret = request.onlinePay.getPayInfoConfig().getAppkey();//
			String orderName = URLEncoder.encode(request.onlinePay.getPayCodeConfig().getProductName(),"utf-8");
			String orderNo = request.onlinePay.getOrderId();//
			int orderAmt = request.onlinePay.getPrices();//
			String orderDetail =  URLEncoder.encode(request.onlinePay.getPayCodeConfig().getProductName(),"utf-8");//
			String reqData = "appId="+appId+"&appSecret="+appSecret+"&orderName="+orderName+"&orderNo="+orderNo+
					"&orderAmt="+orderAmt+"&orderDetail="+orderDetail;
			logger.info("支付时发送给渠道的数据为:"+reqData);
			String content = HttpClientUtils.get(GOPAYURL+"?"+reqData,10000,10000);
			response = new PayResponse(getPay(content, request));
		} catch (UnsupportedEncodingException e) {
			logger.error("urlencode error!", e);
		}//
		return response;
	}
	

	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}	
}