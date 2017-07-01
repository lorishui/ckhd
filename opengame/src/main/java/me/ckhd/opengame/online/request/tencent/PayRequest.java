package me.ckhd.opengame.online.request.tencent;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.BasePayRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayRequest extends BasePayRequest {
	Logger log = LoggerFactory.getLogger(this.getClass());

	public PayRequest(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public Map<String, Object> getPayParam() {
		Map<String, Object> map = new HashMap<String, Object>();
		if( StringUtils.isNotBlank(onlinePay.getSdkType()) && "1".equals(onlinePay.getSdkType())){
			
		}else{
			
		}
		return map;
	}

}
