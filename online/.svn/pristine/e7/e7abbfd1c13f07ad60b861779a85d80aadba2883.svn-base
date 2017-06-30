package me.ckhd.opengame.online.request.qihoo;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map.put("order_id", httpServletRequest.getParameter("order_id"));
		map.put("app_key",httpServletRequest.getParameter("app_key"));
		map.put("product_id",httpServletRequest.getParameter("product_id"));
		map.put("amount",httpServletRequest.getParameter("amount"));
		map.put("app_uid",httpServletRequest.getParameter("app_uid"));
		map.put("app_ext1",httpServletRequest.getParameter("app_ext1"));
		map.put("app_order_id",httpServletRequest.getParameter("app_order_id"));
		map.put("user_id",httpServletRequest.getParameter("user_id"));
		map.put("sign_type",httpServletRequest.getParameter("sign_type"));
		map.put("gateway_flag",httpServletRequest.getParameter("gateway_flag"));
		map.put("sign",httpServletRequest.getParameter("sign"));
		map.put("sign_return",httpServletRequest.getParameter("sign_return"));
	}

	@Override
	public String getOrderId() {
		return map.get("app_order_id")==null?"":map.get("app_order_id").toString();
	}

	@Override
	public String getActualAmount() {
		return map.get("amount")==null?"":map.get("amount").toString();
	}

	@Override
	public String getChannelOrderId() {
		return map.get("order_id")==null?"":map.get("order_id").toString();
	}
}
