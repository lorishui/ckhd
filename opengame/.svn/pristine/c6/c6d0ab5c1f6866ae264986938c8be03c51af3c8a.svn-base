package me.ckhd.opengame.online.request.weixin;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;
import me.ckhd.opengame.online.request.weixin.net.sourceforge.simcpux.MD5;

public class PayRequest extends BasePayRequest {
	
	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public Map<String, Object> getPayParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", onlinePay.getPayInfoConfig().getAppid());//公众账号ID
		map.put("body", onlinePay.getPayCodeConfig().getProductName());//商品或支付单简要描述
		map.put("mch_id", onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));//商户号
		map.put("nonce_str", genNonceStr());//随机字符串，不长于32位
		map.put("notify_url", onlinePay.getPayInfoConfig().getNotifyUrl());//接收微信支付异步通知回调地址
		map.put("out_trade_no",  onlinePay.getOrderId());//商户系统内部的订单号,32个字符内、可包含字母,
		map.put("spbill_create_ip", onlinePay.getClientIp());//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
		map.put("total_fee",onlinePay.getPrices());//订单总金额，单位为分
		map.put("trade_type", "APP");//取值如下：JSAPI，NATIVE，APP
		map.put("sign",genPackageSign(map,onlinePay.getPayInfoConfig().getAppkey()));//签名
		return map;
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	/**
	 生成签名
	 */

	private String genPackageSign(Map<String, Object> params,String apiKey) {
		StringBuilder sb = new StringBuilder();
		String[] obj = new String[params.size()];
		params.keySet().toArray(obj);
		Arrays.sort(obj);
		for (String key:obj) {
			sb.append(key);
			sb.append('=');
			sb.append(params.get(key));
			sb.append('&');
		}
		sb.append("key=");
		sb.append(apiKey);
		String packageSign = null;
		try {
			packageSign = MD5.getMessageDigest(sb.toString().getBytes("utf-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return packageSign;
	}
	
}
