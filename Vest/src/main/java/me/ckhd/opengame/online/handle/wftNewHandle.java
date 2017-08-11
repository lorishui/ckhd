package me.ckhd.opengame.online.handle;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.SignContext;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.util.XmlUtils;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("wftNew")
@Scope("prototype")
public class wftNewHandle extends BaseHandle{

	private final static String pay_url = "https://pay.swiftpass.cn/pay/gateway";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("service","unified.trade.pay");
		map.put("mch_id",onlinePay.getPayInfoConfig().getExInfoMap().get("mch_id"));
		map.put("out_trade_no",onlinePay.getOrderId());
//		map.put("body",onlinePay.getPayCodeConfig().getProductName());
		try{
			map.put("body", URLEncoder.encode(onlinePay.getPayCodeConfig().getProductName(), "utf-8"));
		}catch(Throwable e){
			log.error("wft new pay urlencode fail!", e);
		}
		map.put("total_fee",onlinePay.getPrices());
		map.put("mch_create_ip", StringUtils.getRemoteAddr(onlinePay.getHttpServletRequest()));
		map.put("notify_url",StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
//		map.put("time_start", DateUtils.getDate("yyyyMMddHHmmss"));
		map.put("nonce_str", getRandomStr());
		String key = onlinePay.getPayInfoConfig().getAppkey();
		String content = SignContext.getSignContext(map);
		log.info("wft new pay sign content="+content);
		log.info("wft new pay sign key="+key);
		String sign = CoderUtils.md5(content+"&key="+key, "utf-8").toUpperCase();
		log.info("wft new pay sign="+sign);
		map.put("sign", sign);
		try{
			map.put("body", URLEncoder.encode(onlinePay.getPayCodeConfig().getProductName(), "utf-8"));
		}catch(Throwable e){
			log.error("wft new pay urlencode fail!", e);
		}
		String reqStr = XmlUtils.toXml(map);
		String respStr = HttpClientUtils.post(pay_url, reqStr, 10000, 10000 ,"text/html;");
		if(StringUtils.isNotBlank(respStr)){
			Map<String, Object> respMap = XmlUtils.decodeXml(respStr);
//			JSONObject json = (JSONObject) JSONObject.toJSON(respMap);
			if( respMap.containsKey("status") && "0".equals(respMap.get("status")) ){
				result.put("resultCode",0);
				result.put("errMsg","SUCCESS");
				JSONObject data = new JSONObject();
				data.put("orderId", onlinePay.getOrderId());
				data.put("callBackUrl", onlinePay.getPayInfoConfig().getNotifyUrl());
				data.put("tokenId", respMap.get("token_id"));
				data.put("services", respMap.get("services"));
				data.put("appId", onlinePay.getPayInfoConfig().getAppid());
//				data.put("sign", map.get("sign"));
				result.put("result", data);
			}else{
				result.put("resultCode", 4002);
				result.put("errMsg", "wft下单失败！");
			}
		}else{
			result.put("resultCode", 4001);
			result.put("errMsg", "请求失败，请重新请求");
		}
		return result.toJSONString();
	}
	
	private String getRandomStr(){
		String str = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWSYZ1234567890";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i=0;i<16;i++){
			sb.append(str.charAt(random.nextInt(61)));
		}
		return sb.toString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		log.info("wft new callback content:"+code);
		Map<String,Object> map = XmlUtils.decodeXml(code);
		respData = (JSONObject) JSONObject.toJSON(map);
		if( respData != null && respData.size() > 0){
			onlinePay.setOrderId(respData.containsKey("out_trade_no")?respData.getString("out_trade_no"):"");
			onlinePay.setActualAmount(respData.containsKey("total_fee")?respData.getString("total_fee"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.containsKey("transaction_id")?respData.getString("transaction_id"):"");
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("status") && 0==respData.getInteger("status") ){
			if( respData.containsKey("result_code") && 0==respData.getInteger("result_code") ){
				String signNew = CoderUtils.md5(SignContext.getSignContext(respData)+"&key="+onlinePay.getPayInfoConfig().getAppkey(),"utf-8").toUpperCase();
				String sign = respData.getString("sign");
				if(respData.containsKey("sign") && signNew.equals(sign)){
					result.put("code", 2000);
					return getReturnSuccess();
				}else{
					result.put("code", 4006);
					result.put("errMsg", "验证错误！");
					return getReturnFailure();
				}
			}else{
				result.put("code", respData.getInteger("err_code"));
				result.put("errMsg", respData.getString("err_msg"));
				return getReturnFailure();
			}
		}else{
			result.put("code", 4007);
			result.put("errMsg", "无效数据！");
			return getReturnFailure();
		}
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
