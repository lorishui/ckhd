package me.ckhd.opengame.online.request.guopan;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		@SuppressWarnings("unchecked")
		Set<String> keys = httpRequest.getParameterMap().keySet();
		for(String name:keys){
			map.put(name, httpRequest.getParameter(name));
		}
		log.info("guopan map="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		if(map != null && map.containsKey("serialNumber")){
			String orderId = map.get("serialNumber").toString();
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
		if(map != null && map.containsKey("trade_no")){
			String channelOrderId = map.get("trade_no").toString();
			return channelOrderId;
		}
		return null;
	}
}
