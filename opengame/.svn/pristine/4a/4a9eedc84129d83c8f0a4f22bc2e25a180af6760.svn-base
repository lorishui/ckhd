package me.ckhd.opengame.online.request.coolpad;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;

public class PayRequest extends BasePayRequest {
	
	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public Map<String, Object> getPayParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appid", onlinePay.getPayInfoConfig().getAppid());//公众账号ID
		map.put("waresid", onlinePay.getPayCodeConfig().getExInfoMap().get("waresid"));//商品编号
		map.put("waresname", onlinePay.getPayCodeConfig().getProductName());//商品名称
		map.put("price",onlinePay.getPrices()*0.01);
		map.put("cporderid",  onlinePay.getOrderId());//商户系统内部的订单号,32个字符内、可包含字母,
		map.put("currency","RMB");//货币类型
		map.put("appuserid", onlinePay.getUserId());//userid
		map.put("APPV_KEY", onlinePay.getPayInfoConfig().getExInfoMap().get("PRIVATE_KEY"));
		map.put("notifyurl", onlinePay.getPayInfoConfig().getNotifyUrl());
		return map;
	}
}

