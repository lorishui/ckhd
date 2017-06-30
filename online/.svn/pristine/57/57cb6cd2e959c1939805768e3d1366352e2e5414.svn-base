package me.ckhd.opengame.online.request.oppo;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map.put("notifyId", httpServletRequest.getParameter("notifyId"));
		map.put("partnerOrder",httpServletRequest.getParameter("partnerOrder"));
		map.put("productName",httpServletRequest.getParameter("productName"));
		map.put("productDesc",httpServletRequest.getParameter("productDesc"));
		map.put("price",Integer.parseInt(httpServletRequest.getParameter("price")));
		map.put("count",Integer.parseInt(httpServletRequest.getParameter("count")));
		map.put("attach",httpServletRequest.getParameter("attach"));
		map.put("sign",httpServletRequest.getParameter("sign"));
	}

	@Override
	public String getOrderId() {
		return map.get("partnerOrder")==null?"":map.get("partnerOrder").toString();
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("price")==null?0:map.get("price"));
	}

	@Override
	public String getChannelOrderId() {
		return map.get("notifyId")==null?"":map.get("notifyId").toString();
	}

	
}
