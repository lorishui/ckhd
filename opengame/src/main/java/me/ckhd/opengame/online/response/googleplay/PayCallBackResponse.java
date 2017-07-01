package me.ckhd.opengame.online.response.googleplay;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class PayCallBackResponse extends BasePayCallBackResponse{
	String validateUrl = "https://www.googleapis.com/androidpublisher/v2/applications/%s/purchases/products/%s/tokens/%s";

	Logger log = LoggerFactory.getLogger(PayCallBackResponse.class);
	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String packageName = onlinePay.getCallBackMap().get("packageName")==null?"":onlinePay.getCallBackMap().get("packageName").toString();
		String productId = onlinePay.getCallBackMap().get("productId")==null?"":onlinePay.getCallBackMap().get("productId").toString();
		String token = onlinePay.getCallBackMap().get("token")==null?"":onlinePay.getCallBackMap().get("token").toString();
		String url = String.format(validateUrl, packageName,productId,token);
		String result = HttpClientUtils.get(url);
		if(result != null){
			
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String getCallBackResult() {
		JSONObject json = new JSONObject();
		if(isSuccess()){
			json.put("code", 200);
			json.put("message", "success");
			return json.toJSONString();
		}
		json.put("code", 900000);
		json.put("message", "未知异常");
		return json.toJSONString();
	}

	@Override
	public String getReturnSuccess() {
		return "{\"code\":200,\"message\":\"success\"}";
	}

}
