package me.ckhd.opengame.online.response.meizu;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;

public class PayResponse extends BasePayResponse{

	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
		if( StringUtils.isBlank(onlinePay.getSdkType()) || onlinePay.getSdkType().equals("1")){
			payContent = MyJsonUtils.jsonStr2Map(onlinePay.getChannelPayContent());
		}
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderId",onlinePay.getOrderId());
//		resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
		resultMap.put("sign", payContent.get("sign"));
		resultMap.put("createTime", payContent.get("create_time"));
		resultMap.put("productSubject",payContent.get("product_subject"));
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		boolean flag=true;
		if(onlinePay!=null){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		} else {
			result.put("resultCode", -1);
			result.put("errMsg", "未获取到支付信息");
			flag = false;
		}
		return flag;
	}
}
