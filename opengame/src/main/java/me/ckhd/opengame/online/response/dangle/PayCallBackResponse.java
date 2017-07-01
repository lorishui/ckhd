package me.ckhd.opengame.online.response.dangle;

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
		//order=xxxx&money=xxxx&mid=xxxx&time=xxxx&result=x&ext=xxx&key=xxxx
		String context = "order="+onlinePay.getCallBackMap().get("order")+
				"&money="+onlinePay.getCallBackMap().get("money")+
				"&mid="+onlinePay.getCallBackMap().get("mid")+
				"&time="+onlinePay.getCallBackMap().get("time")+
				"&result="+onlinePay.getCallBackMap().get("result")+
				"&ext="+onlinePay.getCallBackMap().get("ext")+
				"&key="+onlinePay.getPayInfoConfig().getExInfoMap().get("PAYMENT_KEY");
		String sign = onlinePay.getCallBackMap().get("signature").toString();
		String signNew = MD5Util.string2MD5(context);
		log.info("dangle sign ="+sign);
		log.info("dangle sign new ="+signNew);
		if(sign.equals(signNew) && "1".equals(onlinePay.getCallBackMap().get("result").toString())){
			isSuccess= true;
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
