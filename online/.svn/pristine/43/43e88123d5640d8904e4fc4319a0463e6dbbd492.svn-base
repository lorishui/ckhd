package me.ckhd.opengame.online.request.letv;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		for(Object key:httpServletRequest.getParameterMap().keySet()){
			map.put(key+"", httpServletRequest.getParameter(key+""));
		}
	}
	@Override
	public String getOrderId() {
		return map.containsKey("cooperator_order_no")?map.get("cooperator_order_no").toString():"";
	}

	@Override
	public String getActualAmount() {
		double price = Double.valueOf(map.get("price")==null?"0":map.get("price").toString())*100;
		return ((int)price)+"";
	}

	@Override
	public String getChannelOrderId() {
		return map.containsKey("out_trade_no")?map.get("out_trade_no").toString():"";
	}
}
