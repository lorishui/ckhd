package me.ckhd.opengame.online.response.weixin;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;
import me.ckhd.opengame.online.util.XmlUtils;

public class PayCallBackResponse extends BasePayCallBackResponse{

	boolean isSuccess = false;
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
		String signNew = CoderUtils.md5(SignContext.getSignContext(onlinePay.getCallBackMap())+"&key="+onlinePay.getPayInfoConfig().getAppkey(),"utf-8");
		String sign = ((String)onlinePay.getCallBackMap().get("sign"));
		if(onlinePay.getCallBackMap().containsKey("sign") && signNew.equalsIgnoreCase(sign) ){
			isSuccess = true;
		}
	}

	@Override
	public boolean isSuccess() {
		return isSuccess;
	}

	@Override
	public String getCallBackResult() {
		Map<String,Object> map = new HashMap<String, Object>();
		if(isSuccess()){
			map.put("return_code", "SUCCESS");
		}else{
			map.put("return_code", "FAIL");
			map.put("return_msg", "订单不存在!");
		}
		return XmlUtils.toXml(map);
	}

	@Override
	public String getReturnSuccess() {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("return_code", "SUCCESS");
		return XmlUtils.toXml(map);
	}
}
