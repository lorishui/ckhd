package me.ckhd.opengame.online.response.coolpad;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.request.lenovo.sign.SignHelper;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.util.OrderStatus;

public class PayResponse extends BasePayResponse {
	
	Map<String, Object> resultMap;
	
	public PayResponse(OnlinePay onlinePay) {
		super(onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("transid",payContent==null?null:payContent.get("transid"));
		result.put("result", resultMap);
		return result;
	}

	public boolean verify(){
		try {
			String transdata = resultMap.get("transdata").toString(); // "{\"loginname\":\"18701637882\",\"userid\":\"14382295\"}";
			String sign="";
			if(resultMap.containsKey("sign")){
				sign = resultMap.get("sign").toString(); // 
			}else{
				return false;
			}
			
			/*
			 * 调用验签接口
			 * 
			 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
			 */
//	        System.out.println("PLATP_KEY====="+ onlinePay.getPayInfoConfig().getExInfoMap().get("PLATP_KEY").toString());
			if (SignHelper.verify(transdata, sign,onlinePay.getPayInfoConfig().getExInfoMap().get("PLATP_KEY").toString())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean isSuccess() {
		try {
			if(onlinePay.getChannelPayContent()==null){
				result.put("resultCode", -1);
				result.put("errMsg", "下单失败");
				onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
				onlinePay.setErrMsg("渠道无数据返回!");
				return false;
			}
			resultMap=SignUtils.getParmters(onlinePay.getChannelPayContent());
			payContent = MyJsonUtils.jsonStr2Map(resultMap.get("transdata").toString());
			if(!verify()){
				String errMsg =payContent.get("errmsg")==null?"下单失败":payContent.get("errmsg").toString();
				result.put("resultCode", -1);
				result.put("errMsg", errMsg);
				onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
				onlinePay.setErrMsg(errMsg);
				return false;
			}else{
				result.put("resultCode", 0);
				result.put("errMsg", "");
				onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_SUCCESS);
				onlinePay.setPrepayid(payContent.get("transid")==null?"":payContent.get("transid").toString());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("resultCode", -1);
			result.put("errMsg", "下单失败");
			onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
			onlinePay.setErrMsg("下单失败");
			return false;
		}
	}
	
}
