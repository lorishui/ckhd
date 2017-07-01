package me.ckhd.opengame.online.request.huawei;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);

	int resultCode = 1;
	String resultMsg = "";
	String appid="";
	String cooperatorOrderSerial="";
	String content="";
	String sign="";
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		for(Object key:httpServletRequest.getParameterMap().keySet()){
			map.put(key+"", httpServletRequest.getParameter(key+""));
		}
		log.info(" huawei callbakc data="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		if(map.containsKey("requestId")){
			return map.get("requestId")==null?"":map.get("requestId").toString();
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		if(map.containsKey("amount")){
			return map.get("amount")==null?"":((int)(Double.parseDouble(map.get("amount").toString())*100))+"";
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		if(map.containsKey("orderId")){
			return map.get("orderId")==null?"":map.get("orderId").toString();
		}
		return null;
	}

}
