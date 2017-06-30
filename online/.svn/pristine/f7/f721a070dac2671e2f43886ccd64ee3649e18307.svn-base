package me.ckhd.opengame.online.handle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpClientUtils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;


@Component("oppo")
@Scope("prototype")
public class oppoHandle extends BaseHandle{

	public static String verify_url="http://i.open.game.oppomobile.com/gameopen/user/fileIdInfo";//接口地址
	JSONObject token =null;
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson,
			PayInfoConfig payInfo) {
		JSONObject result = new JSONObject();
		result.put("resultCode", -1);
		JSONObject json = codeJson.getJSONObject("verifyInfo");
		token = json.getJSONObject("token");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token",token.getString("token"));
		params.put("fileId", token.getString("ssoid"));
		onlineUser.setSid(token.getString("ssoid"));
		Map<String, String> headers = new HashMap<String, String>();
		String baseStr = generateBaseString(System.currentTimeMillis()+"", (new Random().nextInt())+"", payInfo.getAppkey());
		headers.put("param", baseStr);
		String appSecret = (payInfo.getExInfoMap().get("appSecret")==null?"":payInfo.getExInfoMap().get("appSecret").toString());
		headers.put("oauthSignature", generateSign(baseStr, appSecret));
		System.out.println();
		log.info(String.format("发送到oppo渠道的数据:params[%s],headers[%s]",JSONObject.toJSONString(params),JSONObject.toJSONString(headers)));
		String responseData = HttpClientUtils.doGetHeader(verify_url,params,headers);
		log.info("oppo response data===="+responseData);
		if( responseData==null ){
			//"未获取到相关用户信息";
			log.info("oppo request failure!");
		}else{
			JSONObject user = JSONObject.parseObject(responseData);
			if ( user != null && user.containsKey("resultCode") && 200 == user.getInteger("resultCode")) {
				result.put("resultCode", 0);
				returnLoginSuccess(result, onlineUser);
			}else{
				log.info("oppo response data, resultCode is not 200 !");
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
//			resquestMap.put(key, (String)request.getParameter(key));
		}
		if(respData.size() > 0){
			onlinePay.setOrderId(respData.getString("partnerOrder"));
			onlinePay.setActualAmount(respData.containsKey("price")?respData.getString("price"):"0");
			onlinePay.setCallBackContent(StringUtils.isNotBlank(code)?code:respData.toJSONString());
			onlinePay.setChannelOrderId(respData.getString("notifyId"));
		}
	}

	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result,
			HttpServletResponse response) {
		boolean isSign = false;
		try {
			isSign = doCheck(getBaseString(), respData.getString("sign"), onlinePay.getPayInfoConfig().getExInfoMap().get("PUBLIC_KEY").toString() );
		} catch (Exception e) {
			log.error("oppo SHA1WithRSA docheck ERROR!", e);
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
		return "result=OK&resultMsg=成功";
	}

	@Override
	public String getReturnFailure() {
		return "result=FAIL&resultMsg=解析失败";
	}

	public String generateBaseString(String timestamp, String nonce,String appKey) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("oauthConsumerKey").append("=").append(URLEncoder.encode(appKey, "UTF-8"))
			.append("&").append("oauthToken").append("=").append(URLEncoder.encode(token.getString("token"), "UTF-8"))
			.append("&").append("oauthSignatureMethod").append("=").append(URLEncoder.encode("HMAC-SHA1", "UTF-8"))
			.append("&").append("oauthTimestamp").append("=").append(URLEncoder.encode(timestamp, "UTF-8"))
			.append("&").append("oauthNonce").append("=").append(URLEncoder.encode(nonce, "UTF-8"))
			.append("&").append("oauthVersion").append("=").append(URLEncoder.encode("1.0", "UTF-8"))
			.append("&");
		} catch (UnsupportedEncodingException e) { 
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
	
	private String getBaseString() {
		StringBuilder sb = new StringBuilder();
		sb.append("notifyId=").append(respData.containsKey("notifyId")?respData.getString("notifyId"):"");
		sb.append("&partnerOrder=").append(respData.containsKey("partnerOrder")?respData.getString("partnerOrder"):"");
		sb.append("&productName=").append(respData.containsKey("productName")?respData.getString("productName"):"");
		sb.append("&productDesc=").append(respData.containsKey("productDesc")?respData.getString("productDesc"):"");
		sb.append("&price=").append(respData.containsKey("price")?respData.getString("price"):"");
		sb.append("&count=").append(respData.containsKey("count")?respData.getString("count"):"");
		sb.append("&attach=").append(respData.containsKey("attach")?respData.getString("attach"):"");
		return sb.toString();
	}
}
