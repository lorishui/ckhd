package me.ckhd.opengame.online.request.m4399;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		@SuppressWarnings("unchecked")
		Set<String> keySet = httpRequest.getParameterMap().keySet();
		for(String str : keySet){
			map.put(str, httpRequest.getParameter(str));
		}
		log.info("4399 callback data="+map.toString());
	}

	@Override
	public String getOrderId() {
		return map.get("mark")!=null?map.get("mark").toString():"";
	}

	@Override
	public String getActualAmount() {
		return map.get("money")!=null?(int)(Double.parseDouble(map.get("money").toString())*100)+"":"";
	}

	@Override
	public String getChannelOrderId() {
		return map.get("orderid")!=null?map.get("orderid").toString():"";
	}
}
