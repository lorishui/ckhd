package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.MyCardOrder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.mycard.HTTPSClientUtils;
import me.ckhd.opengame.online.service.MyCardOrderService;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Component("mycard")
@Scope("prototype")
public class myCardHandle extends BaseHandle {
	// 预订单测试
	private static final String PAY_URL_TEST = "https://test.b2b.mycard520.com.tw/MyBillingPay/api/AuthGlobal";
	// 预订单正式
	private static final String PAY_URL ="https://b2b.mycard520.com.tw/MyBillingPay/api/AuthGlobal";
	
	// 订单验证  测试
	private static final String VERIFY_URL_TEST = "https://test.b2b.mycard520.com.tw/MyBillingPay/api/TradeQuery";
	// 订单验证  正式
	private static final String VERIFY_URL = "https://b2b.mycard520.com.tw/MyBillingPay/api/TradeQuery";
	
	@Autowired
	private MyCardOrderService service;
	
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		result.put("resultCode", 0);
		result.put("errMsg", "SUCCESS");
		JSONObject data = new JSONObject();
		try {
			String response = prePay(onlinePay, codeJson);
			JSONObject resp = new JSONObject();
			if(StringUtils.isNotBlank(response)){
				resp = JSONObject.parseObject(response);
			}
			if("1".equals(resp.getString("ReturnCode"))){
//				data.put("orderId", onlinePay.getOrderId());
				JSONObject info = codeJson.getJSONObject("verifyInfo");
				MyCardOrder mco = new MyCardOrder();
				mco.setOrderId(onlinePay.getOrderId());
				mco.setChannelOrderId(resp.getString("TradeSeq"));
				mco.setAuthCode(resp.getString("AuthCode"));
				mco.setStatus("0");		//未成功
				mco.setPaymentType(info.getString("paymentType"));
				mco.setServerId(info.getString("serverId"));
				mco.setCustomerId(info.getString("customerId"));
				mco.setAmount(onlinePay.getPayCodeConfig().getPrice());
				mco.setCurrency(info.getString("currency"));
				mco.setIsTest(codeJson.getString("isTest"));
				mco.setServerId(info.getString("serverId"));
				mco.setCustomerId(info.getString("userId"));
//				mco.setPaymentType(paymentType);
				mco.setCurrency(info.getString("Currency"));
				service.save(mco);
				
				data.put("SandBoxMode", "1".equals(codeJson.getString("isTest")) ? "true" : "false");
				data.put("AuthCode", resp.getString("AuthCode"));
				data.put("TradeSeq", resp.getString("TradeSeq"));
				data.put("orderId", onlinePay.getOrderId());
			}else{
				result.put("resultCode", -1);
				result.put("errMsg", "FAIL");
				data.put("ReturnMsg", resp.getString("ReturnMsg"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		result.put("result", data);
		return result.toJSONString();
	}

	private String prePay(OnlinePay onlinePay, JSONObject codeJson) throws UnsupportedEncodingException {
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
//		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		String FacServiceId = onlinePay.getPayInfoConfig().getExInfoMap().get("FacServiceId")+""; // 厂商服务代码
		String FacTradeSeq = onlinePay.getOrderId();
		String TradeType = verifyInfo.getString("TradeType"); // 交易模式 1sdk2web
		String ServerId = verifyInfo.getString("serverId");
		String CustomerId = verifyInfo.getString("userId");
		String ProductName = onlinePay.getPayCodeConfig().getProductName();
		String Amount = Integer.parseInt(onlinePay.getPayCodeConfig().getPrice())*0.01+"";
		String Currency = verifyInfo.getString("Currency"); // 币别 TWD/HKD/USD
		String SandBoxMode = "1".equals(codeJson.getString("isTest")) ? "true" : "false"; // 是否为测试环境
		
		String URL = PAY_URL;
		if("true".equals(SandBoxMode)){
			URL = PAY_URL_TEST;
		}
		
		map.put("FacServiceId", FacServiceId);
		map.put("FacTradeSeq", FacTradeSeq);
		map.put("TradeType", TradeType);
		map.put("ServerId", ServerId);
		map.put("CustomerId", CustomerId);
		map.put("ProductName", ProductName);
		map.put("Amount", Amount);
		map.put("Currency", Currency);
		map.put("SandBoxMode", SandBoxMode);

		String EncodeHashValue = URLEncoder.encode(FacServiceId + FacTradeSeq + TradeType + ServerId + CustomerId, "utf-8")
				+ URLEncoder.encode(ProductName, "utf-8").toLowerCase() 
				+ URLEncoder.encode(Amount + Currency + SandBoxMode + onlinePay.getPayInfoConfig().getAppkey(), "utf-8");
		
		String hash = sha256(EncodeHashValue);
		map.put("hash", hash);
//		String data = HttpUtils.toHttpParameter(map);
//		String response = HttpUtils.post(URL, data, "application/x-www-form-urlencoded", 5000, 5000);
		String response = HTTPSClientUtils.doPost(URL, map, "utf-8");
		return response;
	}


	private String sha256(String encodeHashValue) {
		MessageDigest messageDigest = null;
		byte[] hash = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			hash = messageDigest.digest(encodeHashValue.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String encodeStr = Hex.encodeHexString(hash);
		return encodeStr;
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Set<?> keySet = request.getParameterMap().keySet();
		for (Object key : keySet) {
			respData.put((String)key, request.getParameter((String)key));
		}
		onlinePay.setOrderId(respData.getString("FacTradeSeq"));
		onlinePay.setChannelId(respData.getString("MyCardTradeNo"));
		onlinePay.setActualAmount(StringUtils.isNotBlank(respData.getString("Amount"))?respData.getDouble("Amount")*100+"":"0");
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		if("1".equals(respData.getString("ReturnCode")) && "3".equals(respData.getString("PayResult"))){
			String ReturnCode = respData.getString("ReturnCode");
			String ReturnMsg = respData.getString("ReturnMsg");
			String PayResult = respData.getString("PayResult");
			String FacTradeSeq = respData.getString("FacTradeSeq");
			String PaymentType = respData.getString("PaymentType");
			String Amount = respData.getString("Amount");
			String Currency = respData.getString("Currency");
			String MyCardTradeNo = respData.getString("MyCardTradeNo");
			String MyCardType = respData.getString("MyCardType");
			String PromoCode = respData.getString("PromoCode");
			String Hash = respData.getString("Hash");
			String preHashValue = ReturnCode + PayResult + FacTradeSeq + PaymentType + Amount + Currency
					+ MyCardTradeNo + MyCardType + PromoCode + onlinePay.getPayInfoConfig().getAppkey();
			String encodeHashValue = "";
			try {
				encodeHashValue = URLEncoder.encode(preHashValue,"utf-8");
			} catch (UnsupportedEncodingException e) {
				log.error("myCard url编码错误："+e.getMessage());
			}
			String ServerHash = sha256(encodeHashValue).toLowerCase();
			if(ServerHash.equals(Hash)){		//验证通过
				MyCardOrder mco = new MyCardOrder();
				mco.setOrderId(FacTradeSeq);
				MyCardOrder order = service.get(mco);
				if(order != null && "0".equals(order.getStatus())){
					String URL = VERIFY_URL;
					if("1".equals(order.getIsTest())){
						URL = VERIFY_URL_TEST;
					}
					JSONObject resp = query(order.getAuthCode(),URL);
					//查询订单状态
					if("1".equals(resp.getString("ReturnCode")) && "3".equals(resp.getString("PayResult"))){
						order.setStatus("1");			//状态为1   将进行请款
						order.setQueryCode(ReturnCode);
						order.setQueryMsg(ReturnMsg);
						order.setMycardTradeNo(MyCardTradeNo);
						order.setSerialId(resp.getString("SerialId"));
						order.setActualAmount(resp.getInteger("Amount")*100+"");
						order.setSuccessDate(new Date());
						service.save(order);		
						log.info("mycard回调订单[%s]更改状态为待1请款",FacTradeSeq);
						result.put("code", 2000);
						return getReturnSuccess();
					}else{
						result.put("code", 4004);
						result.put("errMsg", "订单未支付成功");
						return getReturnFailure();
					}
				}else{
					result.put("code", 4005);
					result.put("errMsg", "订单重复回调");
					return getReturnFailure();
				}
			}
			else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}
		result.put("code", 4007);
		result.put("errMsg", "支付失败");
		return getReturnFailure();
	}
	
	
	/**
	 * 手机端查询交易结果的接口
	 */
	@Override
	public Result<Void> queryOrderState(OnlinePay onlinePay, JSONObject codeJson) {
		Result<Void> result = new Result<Void>();
		String AuthCode = codeJson.getString("AuthCode");
		String URL = VERIFY_URL;
		if("1".equals(codeJson.getString("isTest"))){
			URL = VERIFY_URL_TEST;
		}
		JSONObject resp = query(AuthCode,URL);
		if("1".equals(resp.getString("ReturnCode")) && "3".equals(resp.getString("PayResult"))){
			result.setCode(0);
			result.setMessage(resp.getString("ReturnMsg"));
			MyCardOrder mco = new MyCardOrder();
			mco.setOrderId(codeJson.getString("orderId"));
			mco.setAuthCode(codeJson.getString("AuthCode"));
			MyCardOrder order = service.get(mco);
			if("0".equals(order.getStatus())){
				order.setStatus("1");
				order.setChannelOrderId(resp.getString("MyCardTradeNo"));
				order.setQueryCode(resp.getString("ReturnCode"));
				order.setQueryMsg(resp.getString("ReturnMsg"));
				order.setActualAmount(resp.getInteger("Amount")*100+"");
				order.setCurrency(resp.getString("Currency"));
				order.setSerialId(resp.getString("SerialId"));
				order.setSuccessDate(new Date());
				service.save(order);
			}
		}
		else {
			result.setCode(-1);
			result.setMessage(resp.getString("ReturnMsg"));
		}
		return result;
	}
	
	/**
	 * 请求mycard查询订单是否交易成功
	 * @param AuthCode
	 * @return
	 */
	private JSONObject query(String AuthCode,String URL){
		JSONObject result = new JSONObject();
		
//		String data = "AuthCode="+AuthCode;
//		String response = HttpClientUtils.post(URL, data, 5000, 5000);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("AuthCode", AuthCode);
		String response = HTTPSClientUtils.doPost(URL, map, "utf-8");
		
		JSONObject resp = new JSONObject();
		if(StringUtils.isNotBlank(response)){
			resp = JSONObject.parseObject(response);
			if("1".equals(resp.getString("ReturnCode")) && "3".equals(resp.getString("PayResult"))){
				result = resp;
			}else{
				log.info("查询AuthCode:"+AuthCode+"订单未成功支付");
			}
		}else{
			log.info("mycard订单查询返回值为空");
		}
		return result;
	}
	
	/**
	 * get需要補储的订单list
	 */
	@Override
	public List<OnlinePay> getOrders(String code, HttpServletRequest request) {
		ArrayList<OnlinePay> list = new ArrayList<OnlinePay>();
		String data = URLDecode(request.getParameter("DATA"));
		if(StringUtils.isNotBlank(data)){
			JSONObject json = JSONObject.parseObject(data);
			JSONArray orderIds = json.getJSONArray("FacTradeSeq");
			for (Object orderId : orderIds) {
				MyCardOrder mco = new MyCardOrder();
				mco.setOrderId(orderId.toString());
				MyCardOrder myCardOrder = service.get(mco);
				if(myCardOrder != null && "0".equals(myCardOrder.getStatus())){
					String URL = VERIFY_URL;
					if("1".equals(myCardOrder.getIsTest())){
						URL = VERIFY_URL_TEST;
					}
					JSONObject resp = query(myCardOrder.getAuthCode(),URL);
					if("1".equals(resp.getString("ReturnCode")) && "3".equals(resp.getString("PayResult"))){
						OnlinePay onlinePay = new OnlinePay();
						onlinePay.setOrderId(orderId.toString());
						onlinePay.setChannelOrderId(resp.getString("MyCardTradeNo"));
						onlinePay.setActualAmount(resp.containsKey("Amount")?resp.getInteger("Amount")*100+"":"0");
						onlinePay.setCallBackContent(resp.toJSONString());
						list.add(onlinePay);
						log.info("mycard補储订单[%s]验证通过",orderId.toString());
						
						myCardOrder.setStatus("1");
						myCardOrder.setChannelOrderId(resp.getString("MyCardTradeNo"));
						myCardOrder.setQueryCode(resp.getString("ReturnCode"));
						myCardOrder.setQueryMsg(resp.getString("ReturnMsg"));
						myCardOrder.setActualAmount(resp.getInteger("Amount")*100+"");
						myCardOrder.setCurrency(resp.getString("Currency"));
						myCardOrder.setSerialId(resp.getString("SerialId"));
						myCardOrder.setSuccessDate(new Date());
						service.save(myCardOrder);
						log.info("mycard補储订单[%s]更改状态为待请款",orderId.toString());
					}
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 提供给mycard成功订单查询的接口
	 * 1.多笔查询  param: StartDateTime  EndDateTime
	 * 2.单笔查询 param：MyCardTradeNo
	 */
	@Override
	public String queryOrder(Map<String, String> param) {
		String result = "";
		List<MyCardOrder> list = new ArrayList<MyCardOrder>();
		if(StringUtils.isNotBlank(param.get("MyCardTradeNo"))){
			MyCardOrder mco = new MyCardOrder();
			mco.setMycardTradeNo(param.get("MyCardTradeNo"));
			MyCardOrder myCardOrder = service.get(mco);
			list.add(myCardOrder);
			result = getqueryStr(list);
		}else if(StringUtils.isNotBlank(param.get("StartDateTime")) &&
				StringUtils.isNotBlank(param.get("EndDateTime"))){
			MyCardOrder mco = new MyCardOrder();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
			try {
				mco.setStartDateTime(sdf.parse(param.get("StartDateTime")));
				mco.setEndDateTime(sdf.parse(param.get("EndDateTime")));
				list = service.findList(mco);
				result = getqueryStr(list);
			} catch (ParseException e) {
				log.error("mycard查询成功订单日期转换错误：[%s][%s]",param.get("StartDateTime"),param.get("EndDateTime"));
			}
		}
		return result;
	}
	
	private String getqueryStr(List<MyCardOrder>  list){
		StringBuilder sb = new StringBuilder();
		for (MyCardOrder mco : list) {
			sb.append(mco.getPaymentType()).append(",")
				.append(mco.getChannelOrderId()).append(",")
				.append(mco.getMycardTradeNo()).append(",")
				.append(mco.getOrderId()).append(",")
				.append(mco.getCustomerId()).append(",")
				.append(mco.getActualAmount()).append(",")
				.append(mco.getCurrency()).append(",")
				.append(mco.getSuccessDate()).append("<BR>");
		}
		return sb.toString();
	}
	

	@Override
	public String getReturnSuccess() {
		return "SUCCESS";
	}

	@Override
	public String getReturnFailure() {
		return "FAIL";
	}
	
	public static void main(String[] args) throws ParseException {
		String time = "2015-01-01T18:18:19";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss");
		System.out.println(sdf.parse(time));
	}
	
}
