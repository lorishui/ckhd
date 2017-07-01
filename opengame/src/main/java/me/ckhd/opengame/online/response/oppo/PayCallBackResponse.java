package me.ckhd.opengame.online.response.oppo;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.common.config.Global;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayCallBackResponse;

import org.apache.commons.codec.binary.Base64;

public class PayCallBackResponse extends BasePayCallBackResponse{

	private static final String RESULT_STR = "result=%s&resultMsg=%s";
	public PayCallBackResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	@Override
	public boolean isSuccess() {
		String baseString = getBaseString();
		boolean check = false;
		try{
			check = doCheck(baseString, onlinePay.getCallBackMap().get("sign")==null?"":onlinePay.getCallBackMap().get("sign").toString(), Global.getConfig("OPPO_PUBLIC_KEY"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return check;
	}

	@Override
	public String getCallBackResult() {
		Map<String,String> map = new HashMap<String, String>();
		if(isSuccess()){
			map.put("return_code", "OK");
			map.put("return_msg", "成功");
		}else{
			map.put("return_code", "FAIL");
			map.put("return_msg", "解析失败");
		}
		return String.format(RESULT_STR, map.get("return_code"),map.get("return_msg"));
	}
	
	private String getBaseString() {
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(onlinePay.getCallBackMap().get("notifyId")==null?"":onlinePay.getCallBackMap().get("notifyId").toString());
		sb.append("&partnerOrder=").append(onlinePay.getCallBackMap().get("partnerOrder")==null?"":onlinePay.getCallBackMap().get("partnerOrder").toString());
		sb.append("&productName=").append(onlinePay.getCallBackMap().get("productName")==null?"":onlinePay.getCallBackMap().get("productName").toString());
		sb.append("&productDesc=").append(onlinePay.getCallBackMap().get("productDesc")==null?"":onlinePay.getCallBackMap().get("productDesc").toString());
		sb.append("&price=").append(onlinePay.getCallBackMap().get("price")==null?"":onlinePay.getCallBackMap().get("price").toString());
		sb.append("&count=").append(onlinePay.getCallBackMap().get("count")==null?"":onlinePay.getCallBackMap().get("count").toString());
		sb.append("&attach=").append(onlinePay.getCallBackMap().get("attach")==null?"":onlinePay.getCallBackMap().get("attach").toString());
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

	@Override
	public String getReturnSuccess() {
		return String.format(RESULT_STR, "OK","成功");
	}

}
