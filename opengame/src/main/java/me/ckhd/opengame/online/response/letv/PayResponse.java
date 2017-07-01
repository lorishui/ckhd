package me.ckhd.opengame.online.response.letv;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{

	String errMsg="";
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
		resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		if(StringUtils.isNotBlank(onlinePay.getErrMsg())){
			result.put("resultCode", -1);
			result.put("errMsg", onlinePay.getErrMsg());
			return false;
		}else{
			result.put("resultCode", 0);
			result.put("errMsg", "");
			return true;
		}
	}
}
