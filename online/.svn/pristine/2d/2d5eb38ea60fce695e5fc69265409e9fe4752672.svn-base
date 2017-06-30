package me.ckhd.opengame.online.response.uc;

import java.util.Map;

import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{

	Logger logger = LoggerFactory.getLogger(PayCallBackResponse.class);
	
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		boolean flag = false;
		String sign = "";
		Map<String, Object> resultMap = onlinePay.getCallBackMap();
		if(StringUtils.isBlank(onlinePay.getSdkType()) || "1".equals(onlinePay.getSdkType())){
			if (resultMap.containsKey("sign") && StringUtils.isNotBlank(resultMap.get("sign").toString()) ) {
				logger.info("uc online sign resultMap="+resultMap);
				if(resultMap.containsKey("data")){
					@SuppressWarnings("unchecked")
					Map<String, Object> dataMap = (Map<String, Object>)resultMap.get("data");
					if(dataMap.containsKey("orderStatus") && "S".equals(dataMap.get("orderStatus"))){
						String apiKey = onlinePay.getPayInfoConfig().getAppkey();
						String md5Str = SignContext.getSignContext(dataMap,apiKey);
						logger.info("uc online sign context="+md5Str);
						logger.info("uc online sign ="+resultMap.get("sign"));
						sign = Encodes.string2MD5(md5Str).toLowerCase();
						String returnSign= resultMap.get("sign").toString();
						int money = (int)(Double.parseDouble(dataMap.get("amount").toString())*100);
						if(sign.equals(returnSign.toLowerCase()) && onlinePay.getPrices() == money){
							flag=true;
						}
					}
				}
			}
		}else{
			if (resultMap.containsKey("sign") && StringUtils.isNotBlank(resultMap.get("sign").toString()) ) {
				if(resultMap.containsKey("data")){
					@SuppressWarnings("unchecked")
					Map<String, Object> dataMap = (Map<String, Object>)resultMap.get("data");
					if(dataMap.containsKey("orderStatus") && "S".equals(dataMap.get("orderStatus"))){
						String apiKey = onlinePay.getPayInfoConfig().getAppkey();
						String md5Str = getDataStr(dataMap)+apiKey;
						sign = Encodes.string2MD5(md5Str).toLowerCase();
						String returnSign= resultMap.get("sign").toString();
						int money = (int)(Double.parseDouble(dataMap.get("amount").toString())*100);
						if(sign.equals(returnSign.toLowerCase()) && onlinePay.getPrices() == money){
							flag=true;
						}
					}
				}
			}else{
				if(resultMap.containsKey("data")){
					@SuppressWarnings("unchecked")
					Map<String, Object> dataMap = (Map<String, Object>)resultMap.get("data");
					if(dataMap.containsKey("orderStatus") && "S".equals(dataMap.get("orderStatus"))){
						flag=true;
					}
				}
			}
		}
		return flag;
	}
	
	public String getDataStr(Map<String, Object> dataMap){
		StringBuffer strBuff = new StringBuffer();
		if(dataMap.containsKey("amount")){
			strBuff.append("amount="+dataMap.get("amount"));
		}
		if(dataMap.containsKey("attachInfo")){
			strBuff.append("attachInfo="+dataMap.get("attachInfo"));
		}
		if(dataMap.containsKey("failedDesc")){
			strBuff.append("failedDesc="+dataMap.get("failedDesc"));
		}
		if(dataMap.containsKey("gameId")){
			strBuff.append("gameId="+dataMap.get("gameId"));
		}
		if(dataMap.containsKey("orderId")){
			strBuff.append("orderId="+dataMap.get("orderId"));
		}
		if(dataMap.containsKey("orderStatus")){
			strBuff.append("orderStatus="+dataMap.get("orderStatus"));
		}
		if(dataMap.containsKey("payType")){
			strBuff.append("payType="+dataMap.get("payType"));
		}
		if(dataMap.containsKey("tradeId")){
			strBuff.append("tradeId="+dataMap.get("tradeId"));
		}
		if(dataMap.containsKey("tradeTime")){
			strBuff.append("tradeTime="+dataMap.get("tradeTime"));
		}
		return strBuff.toString();
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			return "SUCCESS";
		}else{
			return "FAILURE";
		}
	}

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}
}
