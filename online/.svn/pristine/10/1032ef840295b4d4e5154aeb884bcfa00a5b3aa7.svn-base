package me.ckhd.opengame.online.response.baidu;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{

	String errMsg="";
	int resultCode=1;
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if("2".equals(onlinePay.getSdkType())){
			resultMap.put("outOrderNo",onlinePay.getOrderId());
			resultMap.put("orderIndex", onlinePay.getOrderIndex());
			resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
			resultMap.put("payCode",onlinePay.getPayCodeConfig().getExInfoMap().get("payCode"));
		}else{
			resultMap.put("orderId",onlinePay.getOrderId());
			resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
		}
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		if("2".equals(onlinePay.getSdkType())){
			result.put("resultCode", 0);
			result.put("errMsg", "");
			return true;
		} else {
			result.put("resultCode", 0);
			result.put("errMsg", "");
			return true;
		}
	}
	
}
