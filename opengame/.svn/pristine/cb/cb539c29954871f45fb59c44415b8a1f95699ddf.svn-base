package me.ckhd.opengame.online.response.iqiyi;

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
		// sign=md5(serialNumber +money+status+t+SERVER_KEY)
		String sign = onlinePay.getCallBackMap().get("sign").toString();
		log.info("iqiyi sign ="+sign);
		String user_id = onlinePay.getCallBackMap().get("user_id").toString();
		String role_id = onlinePay.getCallBackMap().get("role_id").toString();
		String money = onlinePay.getCallBackMap().get("money").toString();
		String order_id = onlinePay.getCallBackMap().get("order_id").toString();
		String time = onlinePay.getCallBackMap().get("time").toString();
		String key = onlinePay.getPayInfoConfig().getExInfoMap().containsKey("key")?onlinePay.getPayInfoConfig().getExInfoMap().get("key").toString():"";
		//密后的签名，sign= MD5($user_id.$role_id.$order_id.$money.$time.$key)
		String signNew = MD5Util.string2MD5(user_id+role_id+order_id+money+time+key);
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
			return "{\"result\":0,\"message\":\"success\"}";
		}
		return "{\"result\":-1,\"message\":\"Sign error\"}";
	}

	@Override
	public String getReturnSuccess() {
		return "{\"result\":0,\"message\":\"success\"}";
	}
}
