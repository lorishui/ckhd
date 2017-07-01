package me.ckhd.opengame.online.request.easypay;

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
		return map.get("orderId")==null?"":map.get("orderId").toString();
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("prices"));
	}

	@Override
	public String getChannelOrderId() {

		// return String.valueOf(map.get("prepayid"));
		return (map == null) ? null : map.get("prepayid").toString();
	}
}
