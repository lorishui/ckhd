package me.ckhd.opengame.online.response.xy;

import java.util.Map;

import me.ckhd.opengame.common.utils.Encodes;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCallBackResponse extends BasePayCallBackResponse{

	Logger logger = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	Integer dataType = null;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		Map<String,Object> map = _onlinePay.getCallBackMap();
		dataType = Integer.parseInt(map.get("dataType").toString());
		String signContext = SignContext.getSignContext(map, _onlinePay.getPayInfoConfig().getExInfoMap().get("PayKey").toString(), "&");
		logger.info("xy sig context:"+signContext);
		logger.info("xy sig:"+map.get("sign"));
		if(Encodes.string2MD5(signContext).equals(map.get("sig"))){
			isSuccess = true;
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			if(dataType == 1){//json
				return "success";
			}else{//string
				return "{\"ret\":0,\"msg\":\"\u53d1\u8d27\u6210\u529f\"}";
			}
		}else{
			if(dataType == 1){//String
				return "fail";
			}else{//string
				return "{\"ret\":999,\"msg\":\"\u6821\u9a8c\u5931\u8d25\"}";
			}
		}
	}

	@Override
	public String getReturnSuccess() {
		if(dataType == 1){//json
			return "success";
		}else{//string
			return "{\"ret\":0,\"msg\":\"\u53d1\u8d27\u6210\u529f\"}";
		}
	}
}
