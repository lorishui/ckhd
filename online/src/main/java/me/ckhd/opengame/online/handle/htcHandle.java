package me.ckhd.opengame.online.handle;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.htc.RSASignature;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("htc")
@Scope("prototype")
public class htcHandle extends BaseHandle {
	
	public String encoding = "";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject info = codeJson.getJSONObject("verifyInfo");
		onlineUser.setSid(info.getString("userId"));
		onlineUser.setUserName(info.getString("userName"));
		returnLoginSuccess(result, onlineUser);
		return result.toJSONString();
	}
	
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",0);
		result.put("errMsg","SUCCESS");
		JSONObject data = new JSONObject();
		data.put("orderId", onlinePay.getOrderId());
		data.put("callBackUrl", StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
		data.put("price", onlinePay.getPrices());
		data.put("gameId", onlinePay.getPayInfoConfig().getAppid());
		data.put("productName", onlinePay.getPayCodeConfig().getProductName());
		data.put("productId", onlinePay.getProductId());
		result.put("result", data);
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		try{
			Map<String, String> paramters = changeToParamters(code);
//			String signType = paramters.get("sign_type");//签名类型，一般是RSA
			
			String sign = java.net.URLDecoder.decode(paramters.get("sign"),"utf-8");// 签名
			String order = paramters.get("order");
			String orderDecoderToJson = java.net.URLDecoder.decode(order, "utf-8");// urlDecoder
			respData.put("sign", sign);
			respData.put("order", orderDecoderToJson);
			if(paramters.size() > 0 && paramters.containsKey("order")){ //orderJson
				ObjectMapper mapper = new ObjectMapper();
				Map<?, ?> jsonObject = mapper.readValue(orderDecoderToJson, Map.class);
				respData.put("result_code", jsonObject.get("result_code").toString());
				onlinePay.setActualAmount(jsonObject.get("real_amount")!=null?jsonObject.get("real_amount").toString():"0");
				onlinePay.setOrderId(jsonObject.get("game_order_id").toString());
				onlinePay.setChannelOrderId(jsonObject.get("jolo_order_id").toString());
				onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:jsonObject.toString());
			}
		}catch(Throwable e){
			log.error("HTC callback 解析数据失败!", e);
		}
			
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		try{
			if( respData!=null && respData.get("result_code")!=null && "1".equals(respData.get("result_code").toString()) ){
				String sign = respData.getString("sign");// 签名
				String order = respData.getString("order");
				boolean isOk = RSASignature.doCheck(order, sign, onlinePay.getPayInfoConfig().getAppkey());
				if(isOk){
					result.put("code", 2000);
					return getReturnSuccess();
				}else{
					result.put("code", 4006);
					result.put("errMsg", "验证错误！");
					return getReturnFailure();
				}
			}else{
				result.put("code", 4007);
				result.put("errMsg", "返回码错误！");
				return getReturnFailure();
			}
		}catch(Throwable e){
			log.error("HTC callback 解析数据失败!", e);
			result.put("code", 4008);
			result.put("errMsg", "urldecode错误！");
		}
		return getReturnFailure();
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "success";
	}
	
	private Map<String, String> changeToParamters(String payContent) {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtils.isNotBlank(payContent)) {
			String[] paramertes = payContent.split("&");
			for (String parameter : paramertes) {
				String[] p = parameter.split("=");
				map.put(p[0], p[1].replaceAll("\"", ""));
			}
		}
		return map;
	}
	
	@Test
	public void test(){
		String account = "{\"result_code\":1,\"gmt_create\":\"2017-03-21 10:14:27\",\"real_amount\":100,\"result_msg\":\"支付成功\",\"game_code\":\"game10001\",\"game_order_id\":\"1490062272127\",\"jolo_order_id\":\"ZF-0a54c15dd74f48fdb27c4e342683024e\",\"gmt_payment\":\"2017-03-21 10:15:58\"}";
		String accountSign = "A0Z2ClUKL0TxAucLj9wRC/HCcSyHmKs7V4O5A9Z1MuyO85yr7nTKES5lfl3fTgE5apPCWlCCdgupcrjPueE3QaCXS+KbOWD+B0hTVZ5aDSpx+b71u9JnYcLD2cBb8b+t1/taEVGQ+f8oPjcxA5l302ac5mjzBBqMmMaLjHLoKBc=";
		String rsa = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbMoWvxnZ6jjlJ6PgtPTVjmjmJZxGIr1L4qL+g8SncK0Hq2reV/1Q5h2/fjvR5Vth2w6wN+laIL2dUi6OskZ3p8HmOquxT6uNo7BWL666c7PSO88L5PFH4kGI5XNV1UUAro5KVrGRi4RHWclAFT23/48xnDWWcyIwT7Xttxd4V4QIDAQAB";
		boolean isOk = RSASignature.doCheck(account, accountSign, rsa);
		System.out.println(isOk);
	}

}
