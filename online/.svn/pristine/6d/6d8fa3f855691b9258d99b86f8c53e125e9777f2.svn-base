package me.ckhd.opengame.online.request.xiaomi;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger logger = LoggerFactory.getLogger(PayCallBackRequest.class);
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map.put("appId", httpServletRequest.getParameter("appId"));
		map.put("cpOrderId",httpServletRequest.getParameter("cpOrderId"));
		map.put("orderConsumeType",httpServletRequest.getParameter("orderConsumeType"));
		map.put("orderId",httpServletRequest.getParameter("orderId"));
		map.put("orderStatus",httpServletRequest.getParameter("orderStatus"));
		map.put("payFee",Integer.parseInt(httpServletRequest.getParameter("payFee")));
		map.put("payTime",httpServletRequest.getParameter("payTime"));
		map.put("productCode",httpServletRequest.getParameter("productCode"));
		map.put("productCount",httpServletRequest.getParameter("productCount"));
		map.put("productName",httpServletRequest.getParameter("productName"));
		map.put("uid",httpServletRequest.getParameter("uid"));
		map.put("signature",httpServletRequest.getParameter("signature"));
	}

	@Override
	public String getOrderId() {
		return map.get("cpOrderId")==null?"":map.get("cpOrderId").toString();
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("payFee")==null?0:map.get("payFee"));
	}

	@Override
	public String getChannelOrderId() {
		return map.get("orderId")==null?"":map.get("orderId").toString();
	}

	@Override
	public String redirect(String redirectUrl) {
		String queryString = httpServletRequest.getQueryString();
		logger.info(String.format("转发到旧回调地址数据为[%s]", queryString));
		return HttpClientUtils.get(redirectUrl, queryString, 2000, 2000);
	}
	
	
}
