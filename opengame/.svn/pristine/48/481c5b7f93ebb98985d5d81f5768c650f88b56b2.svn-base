package me.ckhd.opengame.online.response.anzhi;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

public class PayCallBackResponse extends BasePayCallBackResponse{

	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean check = false;
		if("1".equals(onlinePay.getCallBackMap().get("code")==null?"0":onlinePay.getCallBackMap().get("code").toString())){
			int price = onlinePay.getPrices();
			int orderAmount = Integer.parseInt(onlinePay.getCallBackMap().containsKey("orderAmount")?onlinePay.getCallBackMap().get("orderAmount").toString():"0");
			int redBagMoney = Integer.parseInt(onlinePay.getCallBackMap().containsKey("redBagMoney")?onlinePay.getCallBackMap().get("redBagMoney").toString():"0");
			if(price == orderAmount + redBagMoney){
				check=true;
			}
		}else{
			onlinePay.setErrMsg(onlinePay.getCallBackMap().get("msg")==null?"":onlinePay.getCallBackMap().get("msg").toString());
		}
		return check;
	}

	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "success";
		}else{
			return "order_error";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}
}
