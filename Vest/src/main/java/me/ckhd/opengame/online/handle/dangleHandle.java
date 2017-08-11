package me.ckhd.opengame.online.handle;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("dangle")
@Scope("prototype")
public class dangleHandle extends BaseHandle{
	
	public static final String verifyUrl = "http://ctmaster.d.cn/api/cp/checkToken";
	public static final String ZBverifyUrl = "http://ctslave.d.cn/api/cp/checkToken";
	public static final String trdVerifyUrl = "http://ctslave.downjoy.com/api/cp/checkToken";
	public static final String pay_url = "http://paysrv.d.cn/api/cp/pay.do";
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String token = verifyInfo.containsKey("accessToken")?verifyInfo.getString("accessToken"):"";
		String loginUrl = verifyUrl;
		if(token.startsWith("ZB_")){
			loginUrl = ZBverifyUrl;
		}
		String umid = verifyInfo.containsKey("userId")?verifyInfo.getString("userId"):"";
		onlineUser.setSid(umid);
		//MD5(appId|appKey|token|umid)字符串
		String sig = MD5Util.string2MD5(payInfo.getAppid()+"|"+payInfo.getAppkey()+"|"+token+"|"+umid);
		String data = "token="+token+"&appid="+payInfo.getAppid()+"&umid="+umid+"&sig="+sig;
		String respStr = HttpClientUtils.get(loginUrl, data, 1500, 1500);
		if(StringUtils.isBlank(respStr)){
			loginUrl = trdVerifyUrl;
			respStr = HttpClientUtils.get(loginUrl, data, 1500, 1500);
		}
		if( StringUtils.isNotBlank(respStr) ){
			JSONObject json = JSONObject.parseObject(respStr);
			if( json.containsKey("msg_code") && json.containsKey("valid") &&
				2000 == json.getInteger("msg_code") && 1 == json.getInteger("valid") ){
				result.put("resultCode",0);
				result.put("errMsg","SUCCESS");
				result.put("isAdult", json.getInteger("is_adult"));	//0 未成年 ，1 成年
				result.put("isCertified", json.getInteger("is_certified"));	//0 未实名认证 ，1 已认证
				JSONObject returnData = new JSONObject();
				onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
				returnData.put("uid", onlineUser.getUid() );
				returnData.put("token",token);
				result.put("result", returnData);
			}else{
				log.info("dangle login failure! return str="+respStr);
				result.put("errMsg", "用户验证失败！");
			}
		}else{
			result.put("errMsg", "数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		JSONObject result = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		result.put("resultCode",-1);
		result.put("errMsg","下单失败!");
		Map<String,Object> dataMap = new HashMap<String, Object>();
		try {
			dataMap.put("appId", onlinePay.getPayInfoConfig().getAppid());
			dataMap.put("timestamp", System.currentTimeMillis());
			dataMap.put("umid", codeJson.getString("userId").split("&")[0]);
			dataMap.put("token", verifyInfo.getString("accessToken"));
			dataMap.put("seqNum", "1");
			dataMap.put("cpOrder", onlinePay.getOrderId());
			dataMap.put("zoneId", "1");
			dataMap.put("roleId", "1");
//			dataMap.put("productName", URLEncoder.encode(onlinePay.getPayCodeConfig().getProductName(), "utf-8"));
			dataMap.put("productName", onlinePay.getPayCodeConfig().getProductName());
			dataMap.put("productPrice", getDoubleFmt(onlinePay.getPrices()));
			dataMap.put("ext", "");
			String signContent = getSignContent(dataMap)+onlinePay.getPayInfoConfig().getExInfoMap().get("PAYMENT_KEY");
			dataMap.put("zoneName", "001");
			dataMap.put("roleName", "001");
			log.info("dangle pay signcontent:"+signContent);
			dataMap.put("paySig", CoderUtils.md5(signContent, "utf-8"));
//			dataMap.put("productName", URLEncoder.encode(onlinePay.getPayCodeConfig().getProductName(), "utf-8"));
//			String requestStr = getSignContent(dataMap);
			String respStr = HttpClientUtils.doGet(pay_url, dataMap);
			if( StringUtils.isNotBlank(respStr) ){
				JSONObject json = JSONObject.parseObject(respStr);
				if( json.containsKey("msg_code") && 2000 == json.getInteger("msg_code") ){
					result.put("resultCode",0);
					result.put("errMsg","SUCCESS");
					onlinePay.setPrepayid(json.getString("orderNo"));
					JSONObject data = new JSONObject();
					data.put("transid", json.getString("orderNo"));
					data.put("orderId", onlinePay.getOrderId());
					result.put("result", data);
				}else{
					log.info("dangle pay failure:"+respStr);
					result.put("resultCode",411);
					result.put("errMsg","下单失败");
				}
			}else{
				result.put("resultCode", 412);
				result.put("errMsg", "请求失败or响应异常！");
				
			}
		} catch (Throwable e) {
			log.error("dangle login 数据组合异常！", e);
			result.put("resultCode",411);
			result.put("errMsg","数据组合异常");
		}
		return result.toJSONString();
	}
	
	private String getDoubleFmt(int n){
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(n/100d);
	}
	
	private String getSignContent(Map<String,Object> map){
		Object[] obj = map.keySet().toArray();
		StringBuffer sb = new StringBuffer();
		Arrays.sort(obj);
		for(Object key:obj){
			sb.append(key).append("=").append(map.get(key)).append("&");
		}
		sb.setLength(sb.length()>0?sb.length()-1:sb.length());
		return sb.toString();
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		for(Object key:request.getParameterMap().keySet()){
			respData.put(key.toString(), request.getParameter(key.toString()));
		}
		log.info("dangle callback content:"+respData.toJSONString());
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("cpOrder"));
			onlinePay.setActualAmount(respData.containsKey("money")?respData.getDouble("money")*100+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("order"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		if( respData.containsKey("result") && 1 == respData.getInteger("result") ){
			//order=xxxx&money=xxxx&mid=xxxx&time=xxxx&result=x&ext=xxx&key=xxxx
			String context = "order="+respData.get("order")+
					"&money="+respData.get("money")+
					"&mid="+respData.get("mid")+
					"&time="+respData.get("time")+
					"&result="+respData.get("result")+
					"&cpOrder="+respData.get("cpOrder")+
					"&ext="+respData.get("ext")+
					"&key="+onlinePay.getPayInfoConfig().getExInfoMap().get("PAYMENT_KEY");
			String sign = respData.getString("signature");
			String signNew = MD5Util.string2MD5(context);
			log.info("dangle sign ="+sign);
			log.info("dangle sign new ="+signNew);
			if( sign.equals(signNew) ){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}else{
			result.put("code", 4007);
			result.put("errMsg", "数据为空！");
			return getReturnFailure();
		}
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "failure";
	}

}
