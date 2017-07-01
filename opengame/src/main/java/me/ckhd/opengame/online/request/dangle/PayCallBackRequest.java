package me.ckhd.opengame.online.request.dangle;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		for(Object key:httpRequest.getParameterMap().keySet()){
			map.put(key.toString(), httpRequest.getParameter(key.toString()));
		}
		log.info("dangle map="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		if(map != null && map.containsKey("ext")){
			String orderId = map.get("ext").toString();
			return orderId;
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		if(map != null && map.containsKey("money")){
			String money = ((int)(Double.parseDouble(map.get("money").toString())*100))+"";
			return money;
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		if(map != null && map.containsKey("order")){
			String channelOrderId = map.get("order").toString();
			return channelOrderId;
		}
		return null;
	}
	
}
