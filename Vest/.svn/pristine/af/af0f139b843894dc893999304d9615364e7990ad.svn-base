package me.ckhd.opengame.online.handle;

import java.util.Arrays;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.online.handle.common.jrtt.RSA;
import me.ckhd.opengame.util.HttpClientUtils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("jrtt")
@Scope("prototype")
public class jrttHandle extends BaseHandle {
	
	private static final String LOGIN_URL = "https://open.snssdk.com/partner_sdk/check_user/";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject verifyInfo = codeJson.getJSONObject("verifyInfo");
		String client_key = payInfo.getAppid();
		String uid = verifyInfo.getString("uid");
		String access_token = verifyInfo.getString("accessToken");
		String param = "client_key="+client_key+"&uid="+uid+"&access_token="+access_token;
		String respStr = HttpClientUtils.post(LOGIN_URL, param, 2000, 2000);
		if ( StringUtils.isNotBlank(respStr) ) {
			JSONObject resp = JSONObject.parseObject(respStr);
			if ("success".equals(resp.getString("message"))) {
				JSONObject data = resp.getJSONObject("data");
				if("1".equals(data.getString("verify_result"))){
					onlineUser.setSid(uid);
					onlineUser.setUserName(uid);
					returnLoginSuccess(result, onlineUser);
				}else{
					log.info("jrtt login failure!");
					result.put("errMsg", "jrtt 验证失败");
				}
			}else{
				log.info("jrtt login failure!");
				result.put("errMsg", "jrtt 请求响应不正常！");
			}
		}else{
			result.put("errMsg", "jrtt 数据为空或者请求失败！");
		}
		return result.toJSONString();
	}

	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		Set<?> keySet = request.getParameterMap().keySet();
		for (Object key : keySet) {
			respData.put((String)key, request.getParameter((String)key));
		}
		onlinePay.setOrderId(respData.getString("out_trade_no"));
		onlinePay.setChannelId(respData.getString("trade_no"));
		onlinePay.setActualAmount(respData.getString("total_fee"));
		onlinePay.setCallBackContent(respData.toJSONString());
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		if("0".equals(respData.getString("trade_status")) || "3".equals(respData.getString("trade_status"))){
			String param = getParam();
			String tt_sign = respData.getString("tt_sign");
			String public_key = onlinePay.getPayInfoConfig().getAppkey();
			boolean b = RSA.verify(param, tt_sign, public_key);
			if(b){
				result.put("code", 2000);
				return getReturnSuccess();
			}else{
				result.put("code", 4006);
				result.put("errMsg", "验证错误！");
				return getReturnFailure();
			}
		}
		result.put("code", 4007);
		result.put("errMsg", "支付未成功！");
		return getReturnFailure();
	}

	private String getParam() {
		Set<String> keySet = respData.keySet();
		String[] array = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(array);
		StringBuilder sb = new StringBuilder();
		for (String key : array) {
			if(!"tt_sign".equals(key) && !"tt_sign_type".equals(key)){
				sb.append(key+"="+respData.getString(key)+"&");
			}
		}
		
		return sb.substring(0,sb.length()-1);
	}

	@Override
	public String getReturnSuccess() {
		return "success";
	}

	@Override
	public String getReturnFailure() {
		return "fail";
	}
	
	 public static void main(String argv[]) {
	        String toutiao_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDOZZ7iAkS3oN970+yDONe5TPhPrLHoNOZOjJjackEtgbptdy4PYGBGdeAUAz75TO7YUGESCM+JbyOz1YzkMfKl2HwYdoePEe8qzfk5CPq6VAhYJjDFA/M+BAZ6gppWTjKnwMcHVK4l2qiepKmsw6bwf/kkLTV9l13r6Iq5U+vrmwIDAQAB";
	        String content = "buyer_id=18253265571&client_id=96f988d461913403&notify_id=ea8fbc6985da2a1a4a3129afa51b83185m&notify_time=2015-07-02 18:13:48&notify_type=trade_status_sync&out_trade_no=20150702-100-688-0000389565&pay_time=2015-07-02 17:49:57&total_fee=100&trade_no=2015070200001000650056705706&trade_status=0&way=2";
	        String sign = "CqE8e8sHOEiU4cAVdiVXVjpWuPBg6l9lwVw2H8tOe9c7s8XZOzh7jlGyfZFdelGiILZSKdzFyWhQWWmbFQAapJ+wwxPw66qjcJghXwcqJuZCADzP+VcJeV57T/y+AzfsAQQvSGHppNWvVEHJ8HG9El7FKZZq0F+qC2Sgi2yTTpY=";
	        boolean verify_result = RSA.verify(content, sign, toutiao_public_key);
	        System.out.println("verify_result: " + verify_result);
	    }

}
