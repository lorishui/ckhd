package me.ckhd.opengame.online.response.yyh;

import me.ckhd.opengame.common.utils.RSACoder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String sign = onlinePay.getCallBackMap().get("sign").toString();
		String data = onlinePay.getCallBackMap().get("transdata").toString();
		log.info("yyh sign ="+sign);
		String publicKey = onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey").toString();
		try {
			if( RSACoder.verify(data, publicKey, sign) && "0".equals(onlinePay.getCallBackMap().get("result").toString())){
				isSuccess= true;
			}
		} catch (Exception e) {
			log.error("yyh rsa verify!", e);
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess){
			return "SUCCESS";
		}
		return "FAILURE";
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}
}
