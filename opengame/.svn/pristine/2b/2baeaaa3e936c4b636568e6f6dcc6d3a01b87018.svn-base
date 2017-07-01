package me.ckhd.opengame.online.request.yyh;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.request.BasePayCallBackRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackRequest extends BasePayCallBackRequest {
	Logger log = LoggerFactory.getLogger(PayCallBackRequest.class);

	public PayCallBackRequest(String _code,HttpServletRequest httpRequest) {
		super(_code,httpRequest);
		try {
			_code = URLDecoder.decode(_code, "utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("yyh urlDecode error!",e);
		}
		getMap(_code);
		log.info("yyh map="+map.toString());
	}
	
	@Override
	public String getOrderId() {
		if(map != null && map.containsKey("cporderid")){
			String orderId = map.get("cporderid").toString();
			return orderId;
		}
		return null;
	}

	@Override
	public String getActualAmount() {
		if(map != null && map.containsKey("money")){
			String money = ((int)(Double.parseDouble(map.get("money").toString())*100))+"";
			return money;
		}
		return null;
	}

	@Override
	public String getChannelOrderId() {
		if(map != null && map.containsKey("transid")){
			String channelOrderId = map.get("transid").toString();
			return channelOrderId;
		}
		return null;
	}
	
	private void getMap(String code){
		/*transdata={"transtype":0,"cporderid":"1","transid":"2","appuserid":"test","appid":"3","ware
		sid":31,"feetype":4,"money":5.00,"currency":"RMB","result":0,"transtime":"2012-­‐12-­‐12
		12:11:10","cpprivate":"test","paytype":1}&sign=xxxxxx&signtype=RSA*/
		String[] str = code.split("&");
		for(String value:str){
			String[] strArr = value.split("=");
			if(strArr.length >1){
				map.put(strArr[0], strArr[1]);
				if(strArr[0].equals("transdata")){
					Map<String,Object> data = MyJsonUtils.jsonStr2Map(strArr[1]);
					map.putAll(data);
				}
			}
		}
	}
}
