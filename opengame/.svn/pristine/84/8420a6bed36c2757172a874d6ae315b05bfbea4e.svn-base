package me.ckhd.opengame.online.request.chongchong;

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
		String cpInfo = map.get("partnerTransactionNo")==null?null:map.get("partnerTransactionNo").toString();
		return cpInfo;
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("orderPrice")==null?0:(int)(Double.parseDouble(map.get("orderPrice").toString())*100));
	}

	@Override
	public String getChannelOrderId() {
		return map.get("transactionNo")==null?"":map.get("transactionNo").toString();
	}
	
}
