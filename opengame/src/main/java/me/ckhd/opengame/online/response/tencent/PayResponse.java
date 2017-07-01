package me.ckhd.opengame.online.response.tencent;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.lang3.StringUtils;

public class PayResponse extends BasePayResponse{

	String errMsg="";
	int resultCode=1;
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String rate = DictUtils.getDictValue(onlinePay.getCkAppId(), "tencent_rate", "10");
		if(StringUtils.isNotBlank(onlinePay.getSdkType()) && "2".equals(onlinePay.getSdkType())){
			resultMap.put("orderId",onlinePay.getOrderId());
			resultMap.put("notifyUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
			resultMap.put("rate", rate);
		}else{
			resultMap.put("orderId",onlinePay.getOrderId());
			resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
			resultMap.put("rate", rate);
		}
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		boolean flag = false;
		if( StringUtils.isNoneBlank(onlinePay.getSdkType()) && "2".equals(onlinePay.getSdkType())){
			if(onlinePay!=null){
				result.put("resultCode", 0);
				result.put("errMsg", "");
				flag = true;
			} else {
				result.put("resultCode", -1);
				result.put("errMsg", "未获取到支付信息");
			}
			return flag;
		} else {
			if( onlinePay != null ){
				result.put("resultCode", 0);
				result.put("errMsg", "");
			}else{
				result.put("resultCode", -1);
				result.put("errMsg", "未获取到支付信息");
			}
			return true;
		}
	}
}
