package me.ckhd.opengame.online.request.tencent;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map = MyJsonUtils.jsonStr2Map(_code);
	}
	
	@Override
	public String getOrderId() {
		return map.get("billno")==null?null:map.get("billno").toString();
	}

	@Override
	public String getActualAmount() {
		return map.get("amt")==null?null:map.get("amt").toString()+"";
	}

	@Override
	public String getChannelOrderId() {
		return  map.get("billno")==null?null:map.get("billno").toString();
	}

}
