package me.ckhd.opengame.online.handle;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.online.dao.OnlinePayDao;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import com.alibaba.fastjson.JSONObject;

@Component("baidu")
@Scope("prototype")
public class BaiduHandle extends BaseHandle {
	
	@Autowired
	private OnlinePayDao onlinePayDao;

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderIndex());
		data.put("payCode", onlinePay.getPayCodeConfig().getExInfoMap().get("payCode"));
		result.put("result", data);
		return result.toJSONString();
	}
	

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Set<String> keySet = request.getParameterMap().keySet();
		for (String key : keySet) {
			respData.put(key, request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setChannelOrderId(respData.getString("orderid"));
			String unit = respData.getString("unit");
			String amount = respData.getString("amount");
			if("yuan".equals(unit)){
				onlinePay.setActualAmount((int)(Double.parseDouble(amount)*100)+"");
			}else{
				onlinePay.setActualAmount(amount);
			}
			onlinePay.setCallBackContent(respData.toJSONString());
			onlinePay.setOrderId(onlinePayDao.getOrderIdByIndex(Integer.parseInt(respData.getString("cpdefinepart"))));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String status = respData.getString("status");
		if("success".equals(status)){
			String sign = respData.getString("sign");
			String serverSign = MD5Util.string2MD5(respData.getString("appid")+respData.getString("orderid")
					+respData.getString("amount")+respData.getString("unit")+respData.getString("status")+
					respData.getString("paychannel")+onlinePay.getPayInfoConfig().getAppkey());
			if(sign.equals(serverSign)){
				result.put("code", 2000);
				getReturnSuccess();
			}else{
				result.put("code", 4001);
				result.put("errMsg", "验证失败");
				getReturnFailure();
			}
		}else{
			result.put("code", 4000);
			result.put("errMsg", "支付失败");
			getReturnSuccess();
		}
		return null;
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}

}
