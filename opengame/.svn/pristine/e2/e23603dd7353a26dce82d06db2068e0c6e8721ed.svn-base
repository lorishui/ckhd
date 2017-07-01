package me.ckhd.opengame.online.handle;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component("oppo")
@Scope("prototype")
public class OppoHandle extends BaseHandle {
	
	private static final String RESULT_STR = "result=%s&resultMsg=%s";

	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
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
	public void parseParamter(String code, HttpServletRequest request,
			OnlinePay onlinePay) {
		HashMap<String, String> map = new HashMap<String, String>();
		Set<String> keySet = request.getParameterMap().keySet();
		for (String key : keySet) {
			map.put(key, request.getParameter(key));
		}

		String baseString = getBaseString(map);
		onlinePay.setChannelOrderId(map.get("notifyId"));
		onlinePay.setOrderId(map.get("partnerOrder"));
		onlinePay.setProductName(map.get("productName"));
		onlinePay.setPrices(Integer.parseInt(map.get("price")));
		
		respData.put("baseString", baseString);
		respData.put("sign", map.get("sign"));
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		String baseString = respData.getString("baseString");
		String sign = respData.getString("sign");
		boolean check = false;
		try{
			String key = onlinePay.getPayInfoConfig().getExInfoMap().get("publicKey").toString();
			check = doCheck(baseString,sign,key);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		if(check){
			result.put("code", 2000);
			return getReturnSuccess();
	    }else{ 
	    	result.put("code", 4000);
			result.put("errMsg", "验证失败！");
			return getReturnSuccess();
	    }
	
	}

	@Override
	public String getReturnSuccess() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("return_code", "OK");
		map.put("return_msg", "成功");
		return String.format(RESULT_STR, map.get("return_code"),map.get("return_msg"));
	}

	@Override
	public String getReturnFailure() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("return_code", "FAIL");
		map.put("return_msg", "解析失败");
		return String.format(RESULT_STR, map.get("return_code"),map.get("return_msg"));
	}
	
	
	private String getBaseString(Map<String, String> data) {
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(data.get("notifyId"));
		sb.append("&partnerOrder=").append(data.get("partnerOrder"));
		sb.append("&productName=").append(data.get("productName"));
		sb.append("&productDesc=").append(data.get("productDesc"));
		sb.append("&price=").append(data.get("price"));
		sb.append("&count=").append(data.get("count"));
		sb.append("&attach=").append(data.get("attach"));
		return sb.toString();
	}
	
	public boolean doCheck(String content, String sign, String publicKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decodeBase64(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

		signature.initVerify(pubKey);
		signature.update(content.getBytes("UTF-8"));
		boolean bverify = signature.verify(Base64.decodeBase64(sign.getBytes()));
		return bverify;
	}

}
