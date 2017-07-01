package me.ckhd.opengame.online.response.easypay;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.util.XmlUtils;
import me.ckhd.opengame.util.HttpClientUtils;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{
	public static String url = "http://api.easypay-tech.com/query";
//	private static final String wx_appId="1480667098072516";
//	private static final String wx_appSecret="uWwtxl2SWIdptLggpBuywhIgxUb4FVa2";

	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String appId = _onlinePay.getPayInfoConfig().getAppid();//
		String appSecret = _onlinePay.getPayInfoConfig().getAppkey();//
		String orderId = onlinePay.getOrderId();
		String reqData = "appId="+appId+"&appSecret="+appSecret+"&orderNo="+orderId;
		String content = HttpClientUtils.get(url,reqData,10000,10000);
		JSONObject json = JSONObject.parseObject(content);
		if( json != null ){
			if( json.containsKey("rtn") && json.getInteger("rtn") == 0 ){
				if(json.getJSONObject("data").containsKey("orderStatus") && json.getJSONObject("data").getString("orderStatus").equals("A001") ){
					if( json.getJSONObject("data").getInteger("orderAmt") == onlinePay.getPrices() ){
						isSuccess = true;
					}
				}
			}
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String getCallBackResult() {
		Map<String,Object> map = new HashMap<String, Object>();
		if(isSuccess()){
			map.put("resultCode", 0);
		}else{
			map.put("resultCode", -1);
		}
		return JSONObject.toJSONString(map);
	}

	@Override
	public String getReturnSuccess() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("resultCode", 0);
		return XmlUtils.toXml(map);
	}
}