package me.ckhd.opengame.online.response.wft;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{

	Logger logger = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	Integer dataType = null;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String key=Global.getConfig("WFT_KEY");
		if(onlinePay.getCallBackMap().containsKey("status") && onlinePay.getCallBackMap().get("status").equals("0")){
			if(onlinePay.getCallBackMap().containsKey("result_code") && onlinePay.getCallBackMap().get("result_code").equals("0")){
				String signContent = SignContext.getSignContext(onlinePay.getCallBackMap())+"&key="+key;
				logger.info("wft signContent="+signContent);
				if(onlinePay.getCallBackMap().containsKey("sign") && onlinePay.getCallBackMap().get("sign").equals(MD5Util.string2MD5(signContent).toUpperCase())){
					int price = Integer.parseInt(onlinePay.getCallBackMap().containsKey("total_fee")?onlinePay.getCallBackMap().get("total_fee").toString():"0");//价格
					if(onlinePay.getCallBackMap().containsKey("total_fee") && price ==  onlinePay.getPrices()){
						isSuccess = true;
					}
				}
			}
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "success";
		}else{
			return "fail";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
}
