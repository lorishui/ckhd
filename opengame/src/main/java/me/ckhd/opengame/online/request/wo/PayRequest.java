package me.ckhd.opengame.online.request.wo;

import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;

public class PayRequest extends BasePayRequest{

	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
		
		onlinePay.setActualAmount(onlinePay.getPayCodeConfig().getPrice());
		
	}
	
	@Override
	public Map<String, Object> getPayParam() {
		return null;
	}

	
}
