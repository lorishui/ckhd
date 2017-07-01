package me.ckhd.opengame.online.response.appstore;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{
	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		log.info("syc="+_onlinePay.getCallBackMap().containsKey("syc")+",value="+_onlinePay.getCallBackMap().get("syc"));
		if(_onlinePay.getCallBackMap().containsKey("syc") && _onlinePay.getCallBackMap().get("syc").equals("1")){
			_onlinePay.setIsTest(1);
		}
	}

	@Override
	public boolean isSuccess() {
		if( onlinePay.getCallBackMap().get("status") != null && onlinePay.getCallBackMap().get("status").toString().equals("0")){
			if(onlinePay.getProductId() != null && onlinePay.getPayCodeConfig().getExInfoMap().get("payCode").toString().equals(onlinePay.getCallBackMap().get("productId").toString())){
				return true;
			}else {
				return false;
			}
		}else{
			if(onlinePay.getCallBackMap().get("status").toString().equals("-3")){
				errMsg="计费点错误";
			}else if(onlinePay.getCallBackMap().get("status").toString().equals("-2")){
				errMsg="校验的数据超过当前3天";
			}else if(onlinePay.getCallBackMap().get("status").toString().equals("1")){
				errMsg="苹果返回的code非零";
			}else if(onlinePay.getCallBackMap().get("status").toString().equals("2")){
				errMsg="凭证校验失败或超时";
			}
			onlinePay.setErrMsg(errMsg);
			return false;
		}
	}

	@Override
	public String getCallBackResult() {	
		JSONObject json = new JSONObject();
		if(isSuccess()){
			json.put("resultCode", 0);
			json.put("resultMsg", "");
		}else{
			json.put("resultCode", -1);
			json.put("resultMsg", "validate fail");
		}
		return json.toJSONString();
	}

	@Override
	public String getReturnSuccess() {
		JSONObject json = new JSONObject();
		json.put("resultCode", 0);
		json.put("resultMsg", "");
		return json.toJSONString();
	}
	
	public String getReturnFailure() {
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		json.put("resultMsg", "orderId is failure!");
		return json.toJSONString();
	}
}
