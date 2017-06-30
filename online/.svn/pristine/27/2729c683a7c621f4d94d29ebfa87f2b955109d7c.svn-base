package me.ckhd.opengame.online.response.unionpay;

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
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("tn", onlinePay.getPrepayid());
		result.put("result", resultMap);
		return result;
	}
	
	@Override
	public boolean isSuccess() {
		boolean flag=true;
		if(onlinePay!=null && onlinePay.getPrepayid() != null && onlinePay.getPrepayid().trim().length() > 0){
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
