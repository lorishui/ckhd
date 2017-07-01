package me.ckhd.opengame.online.response.egame;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;

/**
 * Egame服务器下发业务中间接口
 */
public class PayResponse extends BasePayResponse{

	public PayResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	public static void send(MmAppOrder mmAppOrder){
		
		OnlinePay onlinePay = new OnlinePay();
//		onlinePay.setAccessKey(accessKey);
//		onlinePay.setActualAmount(actualAmount);
//		onlinePay.setAppck(appck);
//		onlinePay.setAppId(mmAppOrder.getAppId());
////		onlinePay.setAppPayContent(appPayContent);
//		onlinePay.setCkAppId(mmAppOrder.getCkappId());
//
//		onlinePay.setChannelId(channelId);
//		onlinePay.setChannelOrderId(channelOrderId);
//		onlinePay.setChannelPayContent(channelPayContent);
//		onlinePay.setCkAppId(ckAppId);
//		onlinePay.setContent(content);
//		onlinePay.setCreateBy(createBy);
//		onlinePay.setCreateDate(createDate);
//		
//		
//		
//		onlinePay.setSqlMap(sqlMap);

		OrderSenderBoot.getInstance().add(onlinePay);
		
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

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderId",onlinePay.getOrderId());
		result.put("result", resultMap);
		return result;
	}
	
}
