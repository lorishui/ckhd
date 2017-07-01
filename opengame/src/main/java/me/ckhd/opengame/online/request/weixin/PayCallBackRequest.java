package me.ckhd.opengame.online.request.weixin;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;
import me.ckhd.opengame.online.util.XmlUtils;

public class PayCallBackRequest extends BasePayCallBackRequest {
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map= XmlUtils.decodeXml(callBackCode);
	}

	@Override
	public String getOrderId() {
		return map.get("out_trade_no")==null?"":map.get("out_trade_no").toString();
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("cash_fee"));
	}

	@Override
	public String getChannelOrderId() {
		return String.valueOf(map.get("transaction_id"));
	}
}
