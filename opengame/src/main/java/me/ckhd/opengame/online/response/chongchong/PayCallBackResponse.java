package me.ckhd.opengame.online.response.chongchong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{
	
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String signContent = SignContext.getSignContextNotNull(_onlinePay.getCallBackMap());
		log.info("chongchong sign context ="+signContent);
		String signNew = MD5Util.string2MD5(signContent+"&"+onlinePay.getPayInfoConfig().getAppkey());
		String sign = _onlinePay.getCallBackMap().get("sign")!=null?onlinePay.getCallBackMap().get("sign").toString():"";
		log.info("chongchong sign ="+sign);
		log.info("chongchong sign new ="+signNew);
		if(sign.equals(signNew) && "0000".equals(onlinePay.getCallBackMap().get("statusCode").toString())){
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
			return "success";
		}
		return "failure";
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
}
