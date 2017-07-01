package me.ckhd.opengame.online.response.hs;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.util.OrderStatus;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		return OrderStatus.PAY_SUCCESS.equals(onlinePay.getOrderStatus()) ? true
				: false;
	}

	@Override
	public String getCallBackResult() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(isSuccess()){
			resultMap.put("success","通知成功");
		}else{
			resultMap.put("order_error", "订单信息解析失败");
		}
		return JSONObject.toJSONString(resultMap);
	}

	@Override
	public String getReturnSuccess() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success","通知成功");
		return JSONObject.toJSONString(resultMap);
	}
}
