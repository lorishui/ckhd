package me.ckhd.opengame.online.request.xy;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;

public class PayRequest extends BasePayRequest {

	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public Map<String, Object> getPayParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!"2".equals(onlinePay.getSdkType())){
			
		}else{
			map.put("sdkType",onlinePay.getSdkType());
		}
		return map;
	}

}
