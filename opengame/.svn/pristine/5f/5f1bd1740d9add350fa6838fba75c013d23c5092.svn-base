package me.ckhd.opengame.online.response.letv;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean flag = false;
		if(LetvVerify.verify(onlinePay.getHttpServletRequest(), onlinePay.getPayInfoConfig().getExInfoMap().get("secretKey").toString())){
			flag=true;
		}
		//兼容新版本2.0
		if( !flag && LetvVerify.verifyV2(onlinePay.getHttpServletRequest(), onlinePay.getPayInfoConfig().getExInfoMap().get("secretKey").toString())){
			flag=true;
		}
		return flag;
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
