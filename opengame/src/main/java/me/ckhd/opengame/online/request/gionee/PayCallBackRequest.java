package me.ckhd.opengame.online.request.gionee;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map.put("api_key", httpServletRequest.getParameter("api_key"));
		map.put("close_time",httpServletRequest.getParameter("close_time"));
		map.put("create_time",httpServletRequest.getParameter("create_time"));
		map.put("deal_price",httpServletRequest.getParameter("deal_price"));
		map.put("out_order_no",httpServletRequest.getParameter("out_order_no"));
		map.put("pay_channel",httpServletRequest.getParameter("pay_channel"));
		map.put("submit_time",httpServletRequest.getParameter("submit_time"));
		map.put("user_id",httpServletRequest.getParameter("user_id"));
		map.put("sign",httpServletRequest.getParameter("sign"));
		
		/*************************************** 组装重排序参数 *********************************************/
		Enumeration<String> attributeNames = httpServletRequest.getParameterNames();
		Map<String, String> attriMap= new HashMap<String, String>();
		while (attributeNames.hasMoreElements()) {
			String name = attributeNames.nextElement();
			if(!"ckappid".equals(name) && !"channelid".equals(name)){
				attriMap.put(name, httpServletRequest.getParameter(name));
			}
		}

		StringBuilder contentBuffer = new StringBuilder();
		Object[] signParamArray = attriMap.keySet().toArray();
		Arrays.sort(signParamArray);
		for (Object key : signParamArray) {
			String value = attriMap.get(key);
			if (!"sign".equals(key) && !"msg".equals(key)) {// sign和msg不参与签名
				contentBuffer.append(key + "=" + value + "&");
			}
		}

		String content = StringUtils.removeEnd(contentBuffer.toString(), "&");
		map.put("content", content);
	}
	
	@Override
	public String getOrderId() {
		String orderId = map.get("out_order_no")==null?null:map.get("out_order_no").toString();
		return orderId;
	}

	@Override
	public String getActualAmount() {
		double price = Double.valueOf(map.get("deal_price")==null?"0":map.get("deal_price").toString())*100;
		return (int)price+"";
	}

	@Override
	public String getChannelOrderId() {
		return null;
	}
}
