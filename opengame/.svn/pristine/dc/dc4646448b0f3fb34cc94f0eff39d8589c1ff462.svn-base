package me.ckhd.opengame.online.request.meizu;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.online.request.BasePayCallBackRequest;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);
	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		if(_code !=null && _code.length() >0){
			String[] str = _code.split("&");
			for(String s:str ){
				String[] name = s.split("=");
				if(name.length > 1){
					try {
						map.put(name[0], URLDecoder.decode(name[1],"utf-8"));
					} catch (UnsupportedEncodingException e) {
						log.error(" meizu callback get map failure! ",e);
					}
				}else if(name.length>0){
					map.put(name[0], "");
				}
			}
		}
	}

	@Override
	public String getOrderId() {
		return map.get("cp_order_id")!=null?map.get("cp_order_id").toString():"";
	}

	@Override
	public String getActualAmount() {
		return map.get("total_price")!=null?(int)(Double.parseDouble(map.get("total_price").toString())*100)+"":"";
	}

	@Override
	public String getChannelOrderId() {
		return map.get("order_id")!=null?map.get("order_id").toString():"";
	}
	
}
