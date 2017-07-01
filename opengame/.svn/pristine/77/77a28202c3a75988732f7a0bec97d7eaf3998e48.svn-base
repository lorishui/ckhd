package me.ckhd.opengame.online.request.uc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {

	Map<String, Object> dataMap = new HashMap<String, Object>();
	
	@SuppressWarnings("unchecked")
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		map= MyJsonUtils.jsonStr2Map(callBackCode);
		if(map.containsKey("data")){
			dataMap=(HashMap<String, Object>)map.get("data");
		}
	}
	@Override
	public String getOrderId() {
		String orderId = null;
		if(dataMap!=null && dataMap.containsKey("orderId")){
			orderId = dataMap.get("orderId")==null?null:dataMap.get("orderId").toString();
		}
		if(dataMap!=null && dataMap.containsKey("cpOrderId")){
			orderId = dataMap.get("cpOrderId")==null?null:dataMap.get("cpOrderId").toString();
		}
		return orderId;
	}

	@Override
	public String getActualAmount() {
		if(dataMap!=null && dataMap.containsKey("amount")){
			return dataMap.get("amount")==null?null:(int)(Double.parseDouble(dataMap.get("amount").toString()))*100+"";
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		String channelOrderId = null;
		if(dataMap!=null && dataMap.containsKey("orderId")){
			channelOrderId = dataMap.get("orderId")==null?null:dataMap.get("orderId").toString();
		}
		if(dataMap!=null && dataMap.containsKey("tradeId")){
			channelOrderId =  dataMap.get("tradeId")==null?"":dataMap.get("tradeId").toString();
		}
		return channelOrderId;
	}

}
