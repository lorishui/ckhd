package me.ckhd.opengame.online.request.oppo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.online.request.BaseLoginRequest;

import com.alibaba.fastjson.JSONObject;

public class LoginRequest extends BaseLoginRequest {

	JSONObject token =null;
	
	public LoginRequest(String code,PayInfoConfig _payInfoConfig) {
		super(code,_payInfoConfig);
		token=verifyInfo.getJSONObject("token");
	}

	protected String getOauth_token_secret(){
		return token.getString("oauth_token_secret");
	}
	
	protected String getOautjh_token(){
		return token.getString("oauth_token");
	}

	@Override
	public Map<String, Object> getParam() {
		Map<String, Object> map= new HashMap<String, Object>();
		if("10001".equals(this.version)){
			map.put("oauth_token", getOautjh_token());
			map.put("oauth_token_secret", getOauth_token_secret());
		}else if("10002".equals(this.version)){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("token",token.getString("token"));
			params.put("fileId", token.getString("ssoid"));
			map.put("params", params);
			
			
			Map<String, String> headers = new HashMap<String, String>();
			String baseStr = generateBaseString(System.currentTimeMillis()+"", (new Random().nextInt())+"", payInfoConfig.getAppkey());
			headers.put("param", baseStr);
			String appSecret = (payInfoConfig.getExInfoMap().get("appSecret")==null?"":payInfoConfig.getExInfoMap().get("appSecret").toString());
//			System.out.println(String.format("baseStr[%s],appSecret[%s]", baseStr,appSecret));
			headers.put("oauthSignature", generateSign(baseStr, appSecret));
			map.put("headers", headers);
		}
		return map;
	}
	
	public String generateBaseString(String timestamp, String nonce,String appKey) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("oauthConsumerKey").append("=")
					.append(URLEncoder.encode(appKey, "UTF-8"))
					
					.append("&").append("oauthToken").append("=")
					.append(URLEncoder.encode(token.getString("token"), "UTF-8"))
					
					.append("&").append("oauthSignatureMethod").append("=")
					.append(URLEncoder.encode("HMAC-SHA1", "UTF-8"))
					
					.append("&").append("oauthTimestamp").append("=")
					.append(URLEncoder.encode(timestamp, "UTF-8")).append("&")
					
					.append("oauthNonce").append("=")
					.append(URLEncoder.encode(nonce, "UTF-8")).append("&")
					
					.append("oauthVersion").append("=")
					.append(URLEncoder.encode("1.0", "UTF-8"))
					
					.append("&");
		} catch (UnsupportedEncodingException e) { // TODO Auto-generated catch
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String generateSign(String baseStr,String appSecret) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec spec = null;
			String oauthSignatureKey = appSecret + "&";
			spec = new SecretKeySpec(oauthSignatureKey.getBytes(), "HmacSHA1");
			mac.init(spec);
			byteHMAC = mac.doFinal(baseStr.getBytes());
			return URLEncoder.encode(String.valueOf(base64Encode(byteHMAC)),"UTF-8");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static char[] base64Encode(byte[] data) {
		final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
				.toCharArray();
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}
}
