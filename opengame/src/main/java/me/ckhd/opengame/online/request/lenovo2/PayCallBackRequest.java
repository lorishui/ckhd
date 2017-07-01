package me.ckhd.opengame.online.request.lenovo2;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
	    String transdata = httpRequest.getParameter("transdata");
		map= MyJsonUtils.jsonStr2Map(transdata);
		map.put("sign", httpRequest.getParameter("sign"));
		map.put("transdata", httpRequest.getParameter("transdata"));
		log.info(" lenovo2 paramters="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		return map.get("exorderno")==null?"":map.get("exorderno").toString();
	}

	@Override
	public String getActualAmount() {
		return map.get("money")==null?"":Integer.valueOf(map.get("money").toString()).toString();
	}

	@Override
	public String getChannelOrderId() {
		return String.valueOf(map.get("transid"));
	}
}
