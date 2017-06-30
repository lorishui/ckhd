package me.ckhd.opengame.online.handle;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("easypay")
@Scope("prototype")
public class easypayHandle extends BaseHandle{

	public static final String pay_url="http://api.easypay-tech.com/prepay";//支付预付接口地址
	public static final String validate_url = "http://api.easypay-tech.com/query";//支付验证接口
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		try{
			String appId = onlinePay.getPayInfoConfig().getAppid();//
			String appSecret = onlinePay.getPayInfoConfig().getAppkey();//
			String orderName = URLEncoder.encode(onlinePay.getPayCodeConfig().getProductName(),"utf-8");
			String orderNo = onlinePay.getOrderId();//
			int orderAmt = onlinePay.getPrices();//
			String orderDetail =  URLEncoder.encode(onlinePay.getPayCodeConfig().getProductName(),"utf-8");//
			String reqData = "appId="+appId+"&appSecret="+appSecret+"&orderName="+orderName+"&orderNo="+orderNo+
					"&orderAmt="+orderAmt+"&orderDetail="+orderDetail;
			log.info("支付时发送给渠道的数据为:"+reqData);
			String content = HttpClientUtils.get(pay_url+"?"+reqData,10000,10000);
			if( StringUtils.isNoneBlank(content) ){
				JSONObject pay = JSONObject.parseObject(content);
				if ( pay.containsKey("retcode") && 0 == pay.getInteger("retcode") ) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("orderId",onlinePay.getOrderId());
					resultMap.put("prepayid", pay.getJSONObject("data").getString("prepayid"));
					onlinePay.setChannelOrderId(pay.getJSONObject("data").getString("prepayid"));
					resultMap.put("prices", onlinePay.getPrices());
					result.put("result", resultMap);
					resultMap.put("callBackUrl",StringUtils.isNotBlank(onlinePay.getNotifyUrl())?onlinePay.getNotifyUrl():onlinePay.getPayInfoConfig().getNotifyUrl());
					result.put("resultCode", 0);
					result.put("errMsg", "SUCCESS");				
				}else{
					result.put("errMsg", pay.get("return_msg"));
				}
			}
		}catch(Throwable e){
			log.error("easypay 下单出现异常!", e);
		}
		return result.toJSONString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		respData = JSONObject.parseObject(code);
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("orderId"));
			onlinePay.setActualAmount(respData.containsKey("prices")?respData.getString("prices"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("prepayid"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String appId = onlinePay.getPayInfoConfig().getAppid();//
		String appSecret = onlinePay.getPayInfoConfig().getAppkey();//
		String orderId = onlinePay.getOrderId();
		String reqData = "appId="+appId+"&appSecret="+appSecret+"&orderNo="+orderId;
		String content = HttpClientUtils.get(validate_url,reqData,10000,10000);
		if( StringUtils.isNotBlank(content) ){
			JSONObject json = JSONObject.parseObject(content);
			if( json.containsKey("rtn") && json.getInteger("rtn") == 0 ){
				if(json.getJSONObject("data").containsKey("orderStatus") && json.getJSONObject("data").getString("orderStatus").equals("A001") ){
					if( json.getJSONObject("data").getInteger("orderAmt") == onlinePay.getPrices() ){
						result.put("code", 2000);
						return getReturnSuccess();
					}else{
						result.put("code", 4017);
						result.put("errMsg", "金额异常！");
						return getReturnFailure();
					}
				}else{
					result.put("code", 4006);
					result.put("errMsg", "验证错误！");
					return getReturnFailure();
				}
			}else{
				result.put("code", 4015);
				result.put("errMsg", "请求数据有误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4016);
			result.put("errMsg", "验证请求异常！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		JSONObject json = new JSONObject();
		json.put("resultCode", 0);
		return json.toJSONString();
	}

	@Override
	public String getReturnFailure() {
		JSONObject json = new JSONObject();
		json.put("resultCode", -1);
		return json.toJSONString();
	}

}
