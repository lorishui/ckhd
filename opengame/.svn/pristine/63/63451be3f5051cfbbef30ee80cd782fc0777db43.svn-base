package me.ckhd.opengame.online.response.youku;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.util.OrderStatus;

public class PayResponse extends BasePayResponse{

	// 成功响应状态码
	private static final String CREATE_SUCCESS_RESPONSE_CODE = "200010000";
	Map<String, Object> map;

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
			resultMap.put("orderId",onlinePay.getOrderId());
			resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
		}else{
			
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
