package me.ckhd.opengame.online.request.xy;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		if( _code.indexOf("{")>= 0){
			map= MyJsonUtils.jsonStr2Map(callBackCode);
			map.put("dataType", 0);
		}else{
			String[] str = _code.split("&");
			for(String data:str){
				String[] key = data.split("=");
				if(key.length>1){
					map.put(key[0], key[1]);
				}else{
					map.put(key[0], "");
				}
			}
			map.put("dataType", 1);
		}
	}
	@Override
	public String getOrderId() {
		return map.get("extra")==null?null:map.get("extra").toString();
	}

	@Override
	public String getActualAmount() {
		return map.get("amount")==null?null:(int)Double.parseDouble(map.get("amount").toString())*100+"";
	}

	@Override
	public String getChannelOrderId() {
		return  map.get("orderid")==null?null:map.get("orderid").toString();
	}

}
