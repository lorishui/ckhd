package me.ckhd.opengame.online.request.alipay;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		//获取支付宝POST过来反馈信息
		Map requestParams = httpRequest.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			map.put(name, valueStr);
		}
	}

	@Override
	public String getOrderId() {
		return map.get("out_trade_no")==null?"":map.get("out_trade_no").toString();
	}

	@Override
	public String getActualAmount() {
		Double total_fee = map.get("total_fee")==null?0:Double.parseDouble(map.get("total_fee").toString());
		return String.valueOf((int)(total_fee*100));
	}

	@Override
	public String getChannelOrderId() {
		return map.get("trade_no")==null?"":map.get("trade_no").toString();
	}

	
}
