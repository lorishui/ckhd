package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.util.anzhi.Des3Util;

import com.alibaba.fastjson.JSONObject;

/**
 * anzhi  回调地址sdkname需写成anzhi+ckappid形式   比如：anzhi1001
 * @author llbas
 *
 */
@Component("anzhi")
@Scope("prototype")
public class AnzhiHandle extends BaseHandle {

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		String data = request.getParameter("data");
		String dataJson = Des3Util.decrypt(data,onlinePay.getPayInfoConfig().getExInfoMap().get("AppSecret").toString());
		JSONObject json = (JSONObject) JSONObject.parse(dataJson);
		for (String key : json.keySet()) {
			respData.put(key, json.get(key));
		}
		if(json != null){
			onlinePay.setChannelOrderId(json.getString("orderId"));
			onlinePay.setOrderId(json.getString("cpInfo"));
			onlinePay.setActualAmount(json.getString("orderAmount"));
			onlinePay.setCallBackContent(code);
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String code = respData.getString("code");
		if(!"1".equals(code)){
			result.put("code", 4000);
			result.put("errMsg", "失败订单");
			getReturnSuccess();
		}else{
			String orderAmount = respData.getString("orderAmount");
			if(orderAmount.equals(onlinePay.getPrices()+"")){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4001);
				result.put("errMsg", "金额验证错误");
				return getReturnFailure();
			}
		}
		return getReturnFailure();
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "money_error";
	}

}
