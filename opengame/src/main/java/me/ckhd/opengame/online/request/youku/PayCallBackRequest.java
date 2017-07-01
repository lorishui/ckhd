package me.ckhd.opengame.online.request.youku;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map.put("apporderID", httpServletRequest.getParameter("apporderID"));
		map.put("price",httpServletRequest.getParameter("price"));
		map.put("sign",httpServletRequest.getParameter("sign"));
		map.put("passthrough",httpServletRequest.getParameter("passthrough"));
	}
	
	@Override
	public String getOrderId() {
		String orderId = map.get("apporderID")==null?null:map.get("apporderID").toString();
		return orderId;
	}

	@Override
	public String getActualAmount() {
		int price =Integer.valueOf(map.get("price")==null?"0":map.get("price").toString());
		return price+"";
	}

	@Override
	public String getChannelOrderId() {
		return null;
	}
}
