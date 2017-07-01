package me.ckhd.opengame.online.request.jifeng;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.XmlUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map = XmlUtils.Str2Map(_code);
		map.put("sign", httpRequest.getParameter("sign"));
		map.put("time", httpRequest.getParameter("time"));
		log.info("jifeng map="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		if(map != null && map.containsKey("order_id")){
			String orderId = map.get("order_id").toString();
			return orderId;
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		if(map != null && map.containsKey("cost")){
			String money = ((int)(Double.parseDouble(map.get("cost").toString())*10))+"";
			return money;
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		return null;
	}
	
}
