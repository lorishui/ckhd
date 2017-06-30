package me.ckhd.opengame.online.request.anzhi;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;
import me.ckhd.opengame.online.util.anzhi.Des3Util;

public class PayCallBackRequest extends BasePayCallBackRequest {

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		setMap();
	}
	
	@Override
	public String getOrderId() {
		String cpInfo = map.get("cpInfo")==null?null:map.get("cpInfo").toString();
		return cpInfo;
	}

	@Override
	public String getActualAmount() {
		return String.valueOf(map.get("orderAmount")==null?0:map.get("orderAmount"));
	}

	@Override
	public String getChannelOrderId() {
		return map.get("orderId")==null?"":map.get("orderId").toString();
	}
	
	public void setMap(){
		if(payInfoConfig!=null){
			String data =Des3Util.decrypt(httpServletRequest.getParameter("data"),payInfoConfig.getExInfoMap().get("app_secret").toString());
			map = MyJsonUtils.jsonStr2Map(data);
		}
	}
}
