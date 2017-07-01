package me.ckhd.opengame.online.response.youku;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{

	String msg = "%s?apporderID=%s&price=%s";
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean check = false;
		PayInfoConfig payInfoConfig = onlinePay.getPayInfoConfig();
		String notifyUrl = payInfoConfig.getNotifyUrl();
		String orderId = onlinePay.getOrderId();
		String price = onlinePay.getActualAmount();
		msg = String.format(msg, notifyUrl,orderId,price);
		String payKey = payInfoConfig.getExInfoMap().get("payKey").toString();
		String sign = HmacMD5.hmac(msg, payKey);
		String callBackSign  =(onlinePay.getCallBackMap().get("sign")==null?"":onlinePay.getCallBackMap().get("sign").toString());
		if(sign.equals(callBackSign)){
			check=true;
		}
		return check;
	}

	@Override
	public String getCallBackResult() {
		Map<String, String> map = new HashMap<String, String>();
		if(isSuccess()){
			map.put("status", "success");
			map.put("desc", "通知成功");
		}else{
			map.put("status", "failed");
			map.put("desc", "数字签名错误");
		}
		return JSONObject.toJSONString(map);
	}

	@Override
	public String getReturnSuccess() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "success");
		map.put("desc", "通知成功");
		return JSONObject.toJSONString(map);
	}
}
