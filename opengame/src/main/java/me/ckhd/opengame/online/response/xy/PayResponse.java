package me.ckhd.opengame.online.response.xy;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

import org.apache.commons.lang3.StringUtils;

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
		if(StringUtils.isNotBlank(onlinePay.getSdkType()) && "2".equals(onlinePay.getSdkType())){
			resultMap.put("orderId",onlinePay.getOrderId());
			resultMap.put("notifyUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
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
		} else {
			result.put("resultCode", 0);
			result.put("errMsg", "");
			return true;
		}
	}
}