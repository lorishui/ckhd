package me.ckhd.opengame.online.response.lenovo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger logger = LoggerFactory.getLogger(PayCallBackResponse.class);
	
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		if(Integer.valueOf(onlinePay.getCallBackMap().get("result")==null?"1":onlinePay.getCallBackMap().get("result").toString())==0){
			return true;
		}else{
			onlinePay.setErrMsg("支付失败");
			return false;
		}
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "SUCCESS";
		}else{
			return "FAILURE";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}
}
