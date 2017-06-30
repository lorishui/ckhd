package me.ckhd.opengame.online.request.vivo;

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
		if(map.containsKey("cpOrderNumber")){
			return map.get("cpOrderNumber")==null?"":map.get("cpOrderNumber").toString();
		}else if(map.containsKey("storeOrder")){
			return map.get("storeOrder")==null?"":map.get("storeOrder").toString();
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		Object orderAmount = map.get("orderAmount");
		String amount="";
		if (orderAmount instanceof String) {
			String temp = orderAmount.toString();
			if(temp.indexOf(".")>0){
				amount=(int)(Double.parseDouble(temp)*100)+"";
			}else{
				amount = temp+"";
			}
		}else{
			amount=orderAmount+"";
		}
		return amount;
	}

	@Override
	public String getChannelOrderId() {
		if(map.containsKey("orderNumber")){
			return map.get("orderNumber")==null?"":map.get("orderNumber").toString();
		}else if(map.containsKey("vivoOrder")){
			return map.get("vivoOrder")==null?"":map.get("vivoOrder").toString();
		}
		return null;
	}

	
}
