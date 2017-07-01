package me.ckhd.opengame.online.response.dianyou;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{

	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			result.put("resultCode", -1);
			result.put("errMsg", "未获取到支付信息");
			return result;
		}
		result.put("resultCode", 0);
		result.put("errMsg", "");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderId",onlinePay.getOrderId());
		resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		return true;
	}
	
}
