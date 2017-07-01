package me.ckhd.opengame.online.request;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;

public abstract class BasePayRequest {

	public Map<String,Object> verifyInfo = new HashMap<String, Object>();
	public OnlinePay onlinePay;
	public BasePayRequest(OnlinePay _onlinePay) {
		onlinePay=_onlinePay;
		if(StringUtils.isNotBlank(onlinePay.getAppPayContent())){
			verifyInfo=MyJsonUtils.jsonStr2Map(onlinePay.getAppPayContent());
		}
		onlinePay.setClientIp(StringUtils.getRemoteAddr(onlinePay.getHttpServletRequest()));
	}

	
	public Map<String, Object> getPayParam(){
		return null;
	}
}
