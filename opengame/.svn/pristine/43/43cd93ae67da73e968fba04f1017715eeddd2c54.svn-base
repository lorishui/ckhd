package me.ckhd.opengame.online.response.wft;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{

	String errMsg="";
	boolean isSuccess = false;
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
		payContent = XmlUtils.Str2Map(onlinePay.getChannelPayContent());
		if(payContent.containsKey("status") && payContent.get("status").equals("0")){
			isSuccess = true;
			result.put("resultCode", 0);
			result.put("errMsg", "");
		}else{
			result.put("resultCode", -1);
			result.put("errMsg", "下单失败");
		}
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderId",onlinePay.getOrderId());
		resultMap.put("token_id",payContent.get("token_id"));
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
}
