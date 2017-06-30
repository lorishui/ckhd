package me.ckhd.opengame.online.handle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.google.Security;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("googleplay")
@Scope("prototype")
public class googleplayHandle extends BaseHandle{

	public int resultCode=2015;
	public boolean isSuccess=false;
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("callBackUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		if( onlinePay.getPayCodeConfig().getExInfoMap() != null ){
			data.put("payCode", onlinePay.getPayCodeConfig().getExInfoMap().get("payCode"));
			result.put("result", data);
		}else{
			result.put("resultCode",5001);
			result.put("errMsg","googleplay计费点没配置");
		}
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		respData = JSONObject.parseObject(code);
		if(respData != null && respData.size()>0){
			JSONObject validationJson = JSONObject.parseObject(respData.getString("validation"));//respData.getJSONObject("validation");
			respData.put("validationJson", validationJson);
			onlinePay.setOrderId(validationJson.containsKey("developerPayload")?validationJson.getString("developerPayload").split(";")[0]:"");
//			onlinePay.setPrices(respData.getInteger("prices"));
			if(!validationJson.containsKey("orderId")){
				onlinePay.setIsTest(1);
			}
			onlinePay.setChannelOrderId(validationJson.containsKey("orderId")?validationJson.getString("orderId"):"");
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		onlinePay.setActualAmount(String.valueOf(onlinePay.getPayCodeConfig().getPrice()));
		isSuccess=Security.verify(onlinePay.getPayInfoConfig().getAppkey(), respData.getString("validation"), respData.getString("sign"));
		if(isSuccess){
			JSONObject validationJson = respData.getJSONObject("validationJson");
			if( validationJson.containsKey("purchaseState") && 0==validationJson.getInteger("purchaseState") ){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 2001);
				result.put("errMsg", "非购买状态");
				return getReturnSuccess();
			}
		}else{
			result.put("code", 2002);
			result.put("errMsg", "验证错误");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject json = new JSONObject();
		json.put("resultCode", 0);
		json.put("errMsg", "SUCCESS");
		return json.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject json = new JSONObject();
		json.put("resultCode", resultCode);
		json.put("errMsg", "验证失败！");
		return json.toJSONString();
	}
}
