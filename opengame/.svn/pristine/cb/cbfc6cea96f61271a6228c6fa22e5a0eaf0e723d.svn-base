package me.ckhd.opengame.online.request.i4;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		for(Object key : httpRequest.getParameterMap().keySet()){
			map.put(key.toString(), httpServletRequest.getParameter(key+""));
		}
	}
	
	@Override
	public String getOrderId() {
		String billno = map.get("billno")==null?null:map.get("billno").toString();
		return billno;
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("amount")==null?0:(int)(Double.parseDouble(map.get("amount").toString())*100));
	}

	@Override
	public String getChannelOrderId() {
		return map.get("order_id")==null?"":map.get("order_id").toString();
	}
	
}
