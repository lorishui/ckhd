package me.ckhd.opengame.online.httpclient.wft;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.httpclient.BaseHttpClient;
import me.ckhd.opengame.online.request.BaseLoginRequest;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.response.BaseLoginResponse;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.response.wft.PayResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClient extends BaseHttpClient {
	Logger logger = LoggerFactory.getLogger(HttpClient.class);
	static final String payUrl = "https://paya.swiftpass.cn/pay/gateway";
	
	@Override
	public BaseLoginResponse httpLoginClient(BaseLoginRequest request) {
		return null;
	}
	
	@Override
	public OnlineUser getUser(String result,BaseLoginRequest request) {
		return null;
	}

	@Override
	public BasePayResponse httpPayClient(BasePayRequest request) {
		String key = Global.getConfig("WFT_KEY");
		String appid = Global.getConfig("WFT_APPID");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("service","unified.trade.pay");//接口类型
		map.put("version", "1.0");//版本号
		map.put("sign_type", "MD5");
		map.put("charset", "UTF-8");
		map.put("mch_id", appid);//商户号，由威富通分配
		map.put("out_trade_no", request.onlinePay.getOrderId());//商户系统内部的订单号
		map.put("body", request.onlinePay.getPayCodeConfig().getProductName());//商品描述
		/*try {
			map.put("body", URLEncoder.encode(request.onlinePay.getPayCodeConfig().getProductName(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		map.put("total_fee", request.onlinePay.getPrices());//总金额
		map.put("mch_create_ip", StringUtils.getRemoteAddr(request.onlinePay.getHttpServletRequest()));//订单生成的机器 IP
		map.put("notify_url", Global.getConfig("WFT_NOTIFY_URL")+request.onlinePay.getCkAppId()+"/"+request.onlinePay.getChannelId());//回掉地址
		map.put("nonce_str",getRandomStr());
		map.put("sign_type", "MD5");
		log.info("加密串："+SignContext.getSignContext(map)+"&key="+key);
		map.put("sign",CoderUtils.md5(SignContext.getSignContext(map)+"&key="+key,"utf-8"));
		log.info("加密密文："+map.get("sign"));
		return new PayResponse(getPay(postPayUrl(map), request));
	}
	@Override
	public BasePayResponse httpPayValidateClient(BasePayRequest request) {
		return null;
	}
	
	private String getRandomStr(){
		String charStr = "012356789abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<16;i++){
			sb.append(charStr.charAt(RandomUtils.nextInt(61)));
		}
		return sb.toString();
	}
	
	private String postPayUrl(Map<String,Object> map){
		StringBuffer data = new StringBuffer("<xml>");
		for(String key:map.keySet()){
				data.append("<").append(key).append(">")
					.append(map.get(key))
					.append("</").append(key).append(">");
		}
		data.append("</xml>");
		String responseStr = HttpUtils.post(payUrl, data.toString(), "utf-8");
		return responseStr;
	}
	
}
