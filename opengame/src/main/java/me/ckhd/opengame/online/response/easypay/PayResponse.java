package me.ckhd.opengame.online.response.easypay;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse {
	
	boolean isSuccess = false;
	@SuppressWarnings("unchecked")
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
		result.put("resultCode", -1);
		if(onlinePay!=null && onlinePay.getChannelPayContent()!=null){
			payContent = MyJsonUtils.jsonStr2Map(onlinePay.getChannelPayContent());
			//判断获取到渠道返回是否成功
			if ( payContent.containsKey("retcode") && "0".equals(payContent.get("retcode").toString()) ) {
				isSuccess = true;
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("orderId",onlinePay.getOrderId());
				resultMap.put("prepayid", ((Map<String,Object>)payContent.get("data")).get("prepayid"));
				onlinePay.setChannelOrderId(((Map<String,Object>)payContent.get("data")).get("prepayid").toString());
				resultMap.put("prices", onlinePay.getPrices());
				result.put("result", resultMap);
				result.put("resultCode", 0);
				result.put("errMsg", "");				
			}else{
				result.put("errMsg", payContent.get("return_msg"));
			}
		}else{
			result.put("errMsg", "请求失败");
		}
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	
	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
}
