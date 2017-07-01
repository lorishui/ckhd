package me.ckhd.opengame.online.response.huawei;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{

	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		onlinePay.getCallBackMap().remove("ckappid");
		onlinePay.getCallBackMap().remove("channelid");
		//获取待签名字符串
        String content = RSA.getSignData(onlinePay.getCallBackMap());
        if(onlinePay.getCallBackMap().get("result") != null && onlinePay.getCallBackMap().get("result").toString().equals("0")){
        	//验签
            isSuccess = RSA.doCheck(content, onlinePay.getCallBackMap().get("sign").toString(), onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey").toString());
        }
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}
	
	@Override
	public String getCallBackResult() {
		if(isSuccess()){
			JSONObject json = new JSONObject();
			json.put("result", 0);
			return json.toJSONString();
		}else{
			JSONObject json = new JSONObject();
			json.put("result", 1);
			return json.toJSONString();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject json = new JSONObject();
		json.put("result", 0);
		return json.toJSONString();
	}
}
