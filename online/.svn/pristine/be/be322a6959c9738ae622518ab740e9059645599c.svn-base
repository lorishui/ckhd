package me.ckhd.opengame.online.response.lenovo2;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse {
	
	Map<String, Object> resultMap;
	
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderId",onlinePay.getOrderId());
		resultMap.put("payCode",onlinePay.getPayCodeConfig().getExInfoMap().get("payCode"));
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		result.put("resultCode", 0);
		result.put("errMsg", "");
		return true;
	}
	
}
