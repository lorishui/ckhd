package me.ckhd.opengame.online.request.dianyou;

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
		log.info("dianyou map="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		if(map != null && map.containsKey("ssid")){
			String orderId = map.get("ssid").toString();
			return orderId;
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		if(map != null && map.containsKey("fee")){
			String money = ((int)(Double.parseDouble(map.get("fee").toString())*100))+"";
			return money;
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		if(map != null && map.containsKey("tcd")){
			String channelOrderId = map.get("tcd").toString();
			return channelOrderId;
		}
		return null;
	}
	
}
