package me.ckhd.opengame.online.response.hs;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse {
	
	boolean isSuccess = false;
	public PayResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		if( onlinePay.getChannelPayContent() != null ){
			JSONObject json = JSONObject.parseObject(onlinePay.getChannelPayContent());
			if(json.get("resultCode") != null && json.getString("resultCode").equals("0000")){
				isSuccess = true;
			}
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			result.put("resultCode", -1);
			result.put("errMsg", "下单失败");
			return result;
		}
		result.put("resultCode", 0);
		result.put("errMsg", "");
		HashMap<String, Object> data = MyJsonUtils.jsonStr2Map(onlinePay.getChannelPayContent());
		data.put("ackTimes", onlinePay.getPayMap().get("ackTimes"));
		result.put("result", data);
		return result;
	}
}
