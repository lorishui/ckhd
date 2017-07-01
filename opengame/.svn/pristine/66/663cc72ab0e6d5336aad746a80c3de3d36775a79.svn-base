package me.ckhd.opengame.online.response.vivo;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.util.OrderStatus;

public class PayResponse extends BasePayResponse{
	Map<String, Object> resultMap;
	String errMsg = "" ;
	public PayResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("transNo",onlinePay.getPrepayid());
		resultMap.put("accessKey",onlinePay.getAccessKey());
		result.put("result", resultMap);
		return result;
	}
	
	@Override
	public boolean isSuccess() {
		try {
			Map<String, Object> resultMap = MyJsonUtils.jsonStr2Map(onlinePay.getChannelPayContent());
			String respCode = resultMap.get("respCode")==null?"":resultMap.get("respCode").toString();
			if("2".equals(onlinePay.getSdkType())){
				if((onlinePay.getErrMsg()==null || "".equals(onlinePay.getErrMsg()))){
					result.put("resultCode", 0);
					result.put("errMsg", "");
					return true;
				}else{
					result.put("resultCode", -1);
					result.put("errMsg",onlinePay.getErrMsg());
					return false;
				}
			} else {
				if(respCode.equals("200")){
					result.put("resultCode", 0);
					result.put("errMsg", "");
					return true;
				}else{
					result.put("resultCode", -1);
					result.put("errMsg", onlinePay.getErrMsg());
					onlinePay.setOrderStatus(OrderStatus.ADVANCE_PAYMENT_FAIL);
					return false;
				}
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
