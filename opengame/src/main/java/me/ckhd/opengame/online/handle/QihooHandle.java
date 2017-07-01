package me.ckhd.opengame.online.handle;

import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("qihoo")
@Scope("prototype")
public class QihooHandle extends BaseHandle {

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
		data.put("orderId", onlinePay.getOrderId());
		data.put("callBackUrl", codeJson.get("notifyUrl"));
		result.put("result", data);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Set<String> keySet = request.getParameterMap().keySet();
		for (String key : keySet) {
			respData.put(key, request.getParameter(key));
		}
		onlinePay.setChannelId(respData.getString("order_id"));
		onlinePay.setOrderId(respData.getString("app_order_id"));
		onlinePay.setActualAmount(respData.getIntValue("amount")+"");
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String sign = respData.getString("sign");
		String serverSign = getServerSign(respData,onlinePay.getPayInfoConfig().getExInfoMap().get("appSecret")+"");
		if(sign.equals(serverSign)){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4000);
			result.put("errMsg", "验证失败");
			return getReturnFailure();
		}
	}

	private String getServerSign(JSONObject data,String key) {
		Set<String> keySet = data.keySet();
		Object[] array = keySet.toArray();
		Arrays.sort(array);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			String k = array[i].toString();
			Object val = data.get(k);
			if(val != null && !k.equals("sign") && !k.equals("sign_return")){
				sb.append(val).append("#");
			}
		}
		sb.append(key);
		String sign = MD5Util.string2MD5(sb.toString());
		return sign.toLowerCase();
	}

	@Override
	public String getReturnSuccess() {
		JSONObject json = new JSONObject();
		json.put("status", "ok");
		json.put("delivery", "success");
		json.put("msg", null);
		return json.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject json = new JSONObject();
		json.put("status", "ok");
		json.put("delivery", "mismatch");
		json.put("msg", null);
		return json.toJSONString();
	}

}
