package me.ckhd.opengame.online.response.appstore;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{
	Logger logger = LoggerFactory.getLogger(PayResponse.class);
	
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
		logger.info(" onlinePay is "+(onlinePay != null));
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", onlinePay.getPayCodeConfig().getPrice());//分
		resultMap.put("orderId", onlinePay.getOrderId());
		resultMap.put("payCode", onlinePay.getPayCodeConfig().getExInfoMap().get("payCode"));
		result.put("result", resultMap);
		return result;
	}
	
	@Override
	public boolean isSuccess() {
		logger.info(" onlinePay is "+(onlinePay != null));
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
}
