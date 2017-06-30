package me.ckhd.opengame.online.response.rd;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse {
	public PayResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean flag=true;
		if(onlinePay!=null){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		} else {
			result.put("resultCode", -1);
			result.put("errMsg", "未获取到支付信息");
			flag = false;
		}
		return flag;
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderId","00000000"+onlinePay.getOrderId());
		resultMap.put("payCode", (String)(onlinePay.getPayCodeConfig().getExInfoMap().get("payCode")));
		result.put("result", resultMap);
		return result;
	}
}
