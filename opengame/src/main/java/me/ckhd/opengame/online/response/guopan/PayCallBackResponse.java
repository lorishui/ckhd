package me.ckhd.opengame.online.response.guopan;

import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
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
		log.info("guopan sign ="+sign);
		String serialNumber = onlinePay.getCallBackMap().get("serialNumber").toString();
		String money = onlinePay.getCallBackMap().get("money").toString();
		String status = onlinePay.getCallBackMap().get("status").toString();
		String t = onlinePay.getCallBackMap().get("t").toString();
		String key = onlinePay.getPayInfoConfig().getExInfoMap().containsKey("server_secret_key")?onlinePay.getPayInfoConfig().getExInfoMap().get("server_secret_key").toString():"";
		if(StringUtils.isNoneBlank(status) && status.equals("1")){
			String signNew = MD5Util.string2MD5(serialNumber+money+status+t+key);
			if(sign.equals(signNew)){
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
