package me.ckhd.opengame.online.request.wft;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map = XmlUtils.Str2Map(_code);
	}
	@Override
	public String getOrderId() {
		return map.get("out_trade_no")==null?null:map.get("out_trade_no").toString();
	}

	@Override
	public String getActualAmount() {
		return map.get("total_fee")==null?null:map.get("total_fee").toString()+"";
	}

	@Override
	public String getChannelOrderId() {
		return  map.get("transaction_id")==null?null:map.get("transaction_id").toString();
	}

}
