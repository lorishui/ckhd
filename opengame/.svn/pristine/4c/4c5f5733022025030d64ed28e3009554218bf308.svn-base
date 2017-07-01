package me.ckhd.opengame.online.request.gionee;

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
		if(!"2".equals(onlinePay.getSdkType())){
			map.put("api_key", onlinePay.getPayInfoConfig().getAppkey());
			map.put("private_key", onlinePay.getPayInfoConfig().getExInfoMap().get("privateKey"));
			map.put("notifyURL", onlinePay.getPayInfoConfig().getNotifyUrl());
			map.put("orderId",  onlinePay.getOrderId());//商户系统内部的订单号,32个字符内、可包含字母,
			map.put("appuserid", onlinePay.getUserId());//userid
			map.put("waresname", onlinePay.getPayCodeConfig().getProductName());//商品名称
			map.put("price",onlinePay.getPrices());
			map.put("sdkType",onlinePay.getSdkType());
		}else{
			map.put("sdkType",onlinePay.getSdkType());
		}
		return map;
	}

}
