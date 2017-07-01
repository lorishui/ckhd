package me.ckhd.opengame.online.response.gionee;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.util.OrderStatus;

public class PayResponse extends BasePayResponse{

	// 成功响应状态码
	private static final String CREATE_SUCCESS_RESPONSE_CODE = "200010000";
	Map<String, Object> map;

	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if("2".equals(onlinePay.getSdkType())){
			resultMap.put("outOrderNo",onlinePay.getOrderId());
			resultMap.put("callBackUrl",onlinePay.getPayInfoConfig().getNotifyUrl());
		}else{
			resultMap.put("outOrderNo",map.get("out_order_no"));
			resultMap.put("submitTime",map.get("submit_time"));
		}
		result.put("result", resultMap);
		return result;
	}

	@Override
	public boolean isSuccess() {
		if("2".equals(onlinePay.getSdkType())){
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
		} else {
			if(onlinePay.getChannelPayContent()==null){
				result.put("resultCode", -1);
				result.put("errMsg", "下单失败");
				onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
				onlinePay.setErrMsg("渠道无数据返回!");
				return false;
			}
			map=MyJsonUtils.jsonStr2Map(onlinePay.getChannelPayContent());
			if (CREATE_SUCCESS_RESPONSE_CODE.equals(map.get("status"))) {
				String orderNo = (String) map.get("order_no");
				if (StringUtils.isBlank(orderNo)) {
					result.put("resultCode", -1);
					result.put("errMsg", "下单失败");
					onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
					onlinePay.setErrMsg("无orderNo返回!");
					return false;
				}
				onlinePay.setChannelOrderId(orderNo);
			}else{
				result.put("resultCode", -1);
				result.put("errMsg", "下单失败");
				onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
				onlinePay.setErrMsg("无orderNo返回!");
				return false;
			}
			result.put("resultCode", 0);
			result.put("errMsg", "");
			return true;
		}
		
	}
			
			
}
