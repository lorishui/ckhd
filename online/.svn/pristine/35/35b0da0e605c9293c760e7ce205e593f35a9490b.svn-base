package me.ckhd.opengame.online.response.unionpay;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{
	Logger log = LoggerFactory.getLogger(this.getClass());
	boolean success = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		int price = Integer.parseInt(_onlinePay.getCallBackMap().get("txnAmt")!=null?_onlinePay.getCallBackMap().get("txnAmt").toString():"0");
		if(_onlinePay.getCallBackMap().containsKey("statusCode") && _onlinePay.getCallBackMap().get("statusCode").equals("00")
				&&_onlinePay.getCallBackMap().containsKey("txnAmt") && price == _onlinePay.getPrices()){//比对价格
			success = true;
		}
	}

	@Override
	public boolean isSuccess() {
		if(success){
			return true;
		}else{
			errMsg="验签失败。";
			onlinePay.setErrMsg(errMsg);
			return false;
		}
	}

	@Override
	public String getCallBackResult() {	
		if(isSuccess()){
			return "ok";
		}
		return "fail";
	}

	@Override
	public String getReturnSuccess() {
		return "ok";
	}
}
