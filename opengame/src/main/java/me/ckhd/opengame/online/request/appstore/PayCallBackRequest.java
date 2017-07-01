package me.ckhd.opengame.online.request.appstore;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(BasePayCallBackRequest.class);
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map = MyJsonUtils.jsonStr2Map(_code);
		map.put("ckappid", httpRequest.getParameter("ckappid"));
		log.info("appstore start");
		AppStoreVerify.getIosResponseData(map);
		log.info("appstore end");
	}

	@Override
	public String getOrderId() {
		return map.get("orderId")==null?"":map.get("orderId").toString();
	}

	@Override
	public String getActualAmount() {
		return null;
	}

	@Override
	public String getChannelOrderId() {
		return map.get("transaction_id")==null?"":map.get("transaction_id").toString();
	}
}
