package me.ckhd.opengame.online.request.coolpad;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	
	@SuppressWarnings("deprecation")
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		try {
			callBackCode = URLDecoder.decode(callBackCode, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    String transdata = URLDecoder.decode(httpRequest.getParameter("transdata"));
		map= MyJsonUtils.jsonStr2Map(transdata);
		
	}
	
	@Override
	public String getOrderId() {
		return map.get("cporderid")==null?"":map.get("cporderid").toString();
	}

	@Override
	public String getActualAmount() {
		Float money = Float.valueOf(String.valueOf(map.get("money")));
		return String.valueOf((int)(money*100));
	}

	@Override
	public String getChannelOrderId() {
		return String.valueOf(map.get("transid"));
	}
}
