package me.ckhd.opengame.online.request.baidu;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;
import me.ckhd.opengame.online.response.baidu.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);

	int resultCode = 1;
	String resultMsg = "";
	String appid="";
	String cooperatorOrderSerial="";
	String content="";
	String sign="";
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		for(Object key:httpServletRequest.getParameterMap().keySet()){
			map.put(key+"", httpServletRequest.getParameter(key+""));
		}
		log.info(" baidu callbakc data="+map.toString());
	}
	@Override
	public String getOrderId() {
		if(map.containsKey("CooperatorOrderSerial")){
			return map.get("CooperatorOrderSerial")==null?"":map.get("CooperatorOrderSerial").toString();
		}else if(map.containsKey("cpdefinepart")){
			return map.get("cpdefinepart")==null?"":map.get("cpdefinepart").toString();
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		double amount=0.00;
		if(map.containsKey("orderSerial")){
			JSONObject json = JSONObject.parseObject(Base64.decode(map.get("Content").toString()));
			return json.get("OrderMoney")==null?"0":json.getString("OrderMoney");
		}else if(map.containsKey("amount")){
			if("yuan".equals(map.get("unit")==null?"0":map.get("unit").toString())){
				 amount = map.get("amount")==null?0:Double.parseDouble(map.get("amount").toString())*100;
			}else{
				amount = map.get("amount")==null?0:Double.parseDouble(map.get("amount").toString());
			}
			return (int)amount+"";
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		if(map.containsKey("orderid")){
			return map.get("orderid")==null?"":map.get("orderid").toString();
		}else{
			return map.get("orderSerial")==null?"":map.get("orderSerial").toString();
		}
	}

}
