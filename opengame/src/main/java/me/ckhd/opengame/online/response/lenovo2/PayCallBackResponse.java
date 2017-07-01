package me.ckhd.opengame.online.response.lenovo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger logger = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		if( onlinePay.getCallBackMap().size()>0 ){
			isSuccess = CpTransSyncSignValid.validSign(onlinePay.getCallBackMap().get("transdata").toString(), onlinePay.getCallBackMap().get("sign").toString(), onlinePay.getPayInfoConfig().getAppkey());
			logger.info("lenovo result="+isSuccess);
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
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
