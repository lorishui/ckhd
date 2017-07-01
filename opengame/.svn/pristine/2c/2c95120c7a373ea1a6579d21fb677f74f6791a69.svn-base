package me.ckhd.opengame.online.response.wandoujia;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		if(onlinePay.getCallBackMap().get("content") != null && onlinePay.getCallBackMap().get("sign") != null){
			boolean check = WandouRsa.doCheck(onlinePay.getCallBackMap().get("content").toString(), onlinePay.getCallBackMap().get("sign").toString());
			log.info("youlong sign new ="+check);
			if(check){
				isSuccess = true;
			}
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess){
			return "success";
		}
		return "failure";
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
}
