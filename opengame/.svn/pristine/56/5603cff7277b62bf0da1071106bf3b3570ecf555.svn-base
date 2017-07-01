package me.ckhd.opengame.online.response.jifeng;

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
		String context = onlinePay.getPayInfoConfig().getExInfoMap().get("userId")+(onlinePay.getCallBackMap().get("time")!=null?onlinePay.getCallBackMap().get("time").toString():"");
		String sign = onlinePay.getCallBackMap().get("sign").toString();
		String signNew = MD5Util.string2MD5(context);
		log.info("jifeng sign ="+sign);
		log.info("jifeng sign new ="+signNew);
		if(sign.equals(signNew)){
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
			return "<response><ErrorCode>1</ErrorCode><ErrorDesc>Success</ErrorDesc></response>";
		}
		return "<response><ErrorCode>0</ErrorCode><ErrorDesc>check failure</ErrorDesc></response>";
	}

	@Override
	public String getReturnSuccess() {
		return "<response><ErrorCode>1</ErrorCode><ErrorDesc>Success</ErrorDesc></response>";
	}
}
