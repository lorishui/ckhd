package me.ckhd.opengame.online.response.qihoo;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		if("success".equals(onlinePay.getCallBackMap().get("gateway_flag")==null?"":onlinePay.getCallBackMap().get("gateway_flag").toString()))
		{
			return true;
		}
		return false;
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "ok";
		}else{
			return "fail";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "ok";
	}
}
