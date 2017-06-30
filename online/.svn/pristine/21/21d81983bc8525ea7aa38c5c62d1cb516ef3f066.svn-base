package me.ckhd.opengame.online.request.googleplay;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map= MyJsonUtils.jsonStr2Map(_code);
	}

	@Override
	public String getOrderId() {
		return map.get("orderId")!=null?map.get("orderId").toString():"";
	}

	@Override
	public String getActualAmount() {
		return null;
	}

	@Override
	public String getChannelOrderId() {
		return null;
	}
}
