package me.ckhd.opengame.online.response.youlong;

import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		//flag	MD5(orderId+userName+amount+extra+PKEY)
		String signContent = onlinePay.getCallBackMap().get("orderId").toString()+onlinePay.getCallBackMap().get("userName")+onlinePay.getCallBackMap().get("amount")
				+onlinePay.getCallBackMap().get("extra")+onlinePay.getPayInfoConfig().getAppkey();
		log.info("youlong sign context ="+signContent);
		String signNew = MD5Util.string2MD5(signContent).toUpperCase();
		String sign = _onlinePay.getCallBackMap().get("flag")!=null?onlinePay.getCallBackMap().get("flag").toString():"";
		log.info("youlong sign ="+sign);
		log.info("youlong sign new ="+signNew);
		if(sign.equals(signNew)){
			isSuccess = true;
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess){
			return "OK";
		}
		return "failure";
	}

	@Override
	public String getReturnSuccess() {
		return "OK";
	}
}
