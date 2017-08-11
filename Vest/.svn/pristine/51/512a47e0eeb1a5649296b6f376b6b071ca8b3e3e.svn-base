package me.ckhd.opengame.online.handle;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.DecimalFormatUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.gionee.HttpUtils;
import me.ckhd.opengame.online.handle.common.gionee.RSASignature;
import me.ckhd.opengame.online.handle.common.gionee.VerifyUtils;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Component("gionee")
@Scope("prototype")
public class gioneeHandle extends BaseHandle{

	private static final String login_url = "https://id.gionee.com/account/verify.do";// 登陆接口地址
	private static final String pay_url = "https://pay.gionee.com/amigo/create/order";// 登陆接口地址
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		String apiKey = payInfo.getAppkey();
		String secretKey = (String)payInfo.getExInfoMap().get("secretKey");
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String amigoToken = verifyInfo.getString("amigoToken");
		if (StringUtils.isBlank(apiKey) && StringUtils.isBlank(secretKey) ) {
			result.put("errMsg","apiKey or secretKey is empty!");
		}else{
			Map<String, String> data = VerifyUtils.verify(
					amigoToken, login_url, "id.gionee.com", "443",
					secretKey, "/account/verify.do", apiKey);
			if( data != null ){
				if ( data.containsKey("resultCode") && "0".equals(data.get("resultCode"))) {
					String respnoseData = (String)data.get("result");
					JSONObject rJson = JSONObject.parseObject(respnoseData);
					JSONArray temData  = rJson.containsKey("ply")?rJson.getJSONArray("ply"):null;
					if( temData != null && temData.size()>0 ){
						onlineUser.setSid(temData.getJSONObject(0).getString("pid"));
						onlineUser.setUserName(temData.getJSONObject(0).getString("na"));
						returnLoginSuccess(result, onlineUser);
					}else{
						result.put("errMsg", "沒有用戶信息!");
					}
				} else {
					result.put("errMsg", "登录验证不正确!");
				}
			}else{
				result.put("errMsg", "数据为空或者请求失败！");
			}
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		@SuppressWarnings("unchecked")
		Set<String> set = request.getParameterMap().keySet();
		for( String key : set ){
			respData.put(key, request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("out_order_no"));
			onlinePay.setActualAmount(respData.containsKey("deal_price")?(respData.getDouble("deal_price")*100)+"":"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setAppId(respData.getString("api_key"));//appid
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay,JSONObject result,HttpServletResponse response) {
		String content = getSignContext();
		boolean isSign = false;
		try {
			isSign = RSASignature.doCheck(content, respData.getString("sign"), (String)onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey"), "utf-8");
		} catch (Throwable e) {
			log.error("gionee RSA docheck ERROR!", e);
		}
		if(isSign){
			result.put("code", 2000);
			return getReturnSuccess();
		}else{
			result.put("code", 4006);
			result.put("errMsg", "验证错误！");
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
	
	private String getSignContext(){
		Object[] obj = respData.keySet().toArray();
		Arrays.sort(obj);
		StringBuffer sb = new StringBuffer();
		for(Object key:obj){
			if( !"sign".equals(key)&& !"msg".equals(key) ){
				sb.append(key).append("=").append(respData.get(key)).append("&");
			}
		}
		sb.setLength(sb.length()>0?sb.length()-1:sb.length());
		return sb.toString();
	}
	
	public String pay(OnlinePay onlinePay,JSONObject codeJson){
		JSONObject result = new JSONObject();
		result.put("resultCode",-1);
		JSONObject param = new JSONObject();
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		param.put("user_id",verifyInfo.getString("userId") );
		param.put("out_order_no", onlinePay.getOrderId());
		param.put("subject", onlinePay.getPayCodeConfig().getProductName());
		param.put("submit_time", DateUtils.getDate("yyyyMMddHHmmss"));
		param.put("total_fee", DecimalFormatUtils.getDoubleFormat(onlinePay.getPrices()*0.01d));
		param.put("deal_price",DecimalFormatUtils.getDoubleFormat(onlinePay.getPrices()*0.01d));
		param.put("deliver_type", "1");
		param.put("notify_url", codeJson.getString("notifyUrl"));
		param.put("api_key", onlinePay.getPayInfoConfig().getAppkey());
		String privateKey = (String) onlinePay.getPayInfoConfig().getExInfoMap().get("privateKey");
		
		try {
			getBody(param, privateKey);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		String response = null;
		try {
			response = HttpUtils.post(pay_url, param.toJSONString());
			if( StringUtils.isNotBlank(response) ){
				JSONObject responseJson = JSONObject.parseObject(response);
				if( responseJson.containsKey("status") && "200010000".equals(responseJson.getString("status")) ){
					onlinePay.setPrepayid(responseJson.getString("order_no"));
					result.put("resultCode",0);
					result.put("errMsg","SUCCESS");
					JSONObject data = new JSONObject();
					data.put("outOrderNo", onlinePay.getOrderId());
					data.put("submitTime", responseJson.getString("submit_time"));
					result.put("result", data);
				}else{
					result.put("errMsg", "到金立下订单失败");
				}
			}else{
				result.put("errMsg", "http请求失败");
			}
		} catch (Exception e) {
			log.error("gionee pay error!", e);
			result.put("errMsg", "创建订单失败");
		}
		return result.toJSONString();
	}
	
	private void getBody(JSONObject json,String privateKey) throws java.security.InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, IOException{
		StringBuffer sb = new StringBuffer();
		Object[] objArr = json.keySet().toArray();
		Arrays.sort(objArr);
		for( int i=0;i<objArr.length;i++ ){
			sb.append(json.get(objArr[i]));
		}
		String sign = RSASignature.sign(sb.toString().toString(), privateKey, CharEncoding.UTF_8);
		json.put("sign", sign);
	}
}
