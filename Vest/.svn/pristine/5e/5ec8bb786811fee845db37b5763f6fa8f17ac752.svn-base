package me.ckhd.opengame.online.handle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.common.Result;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.OnlineUser;
import me.ckhd.opengame.util.HttpUtils;

/**
 * 小7手游
 * @author wizard
 */
@Component("x7sy")
@Scope("prototype")
public class x7syHandle extends BaseHandle{
	
	@Override
	public String login(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) {
		try {
			return login0(onlineUser, codeJson, payInfo);
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);

			JSONObject result = new JSONObject();
			result.put("resultCode", -1);
			result.put("errMsg", "Internal server error.");
			return result.toJSONString();
		}
	}
	public String login0(OnlineUser onlineUser, JSONObject codeJson, PayInfoConfig payInfo) throws Exception {
		
		JSONObject verifyInfo = JSONObject.parseObject(codeJson.getString("verifyInfo"));
		Assert.notNull(verifyInfo, "verifyInfo can't be null.");
		
		String appKey = payInfo.getAppkey();
		
		String tokenKey = verifyInfo.getString("tokenkey");
		
		Result<JSONObject> ret = checkLogin(appKey, tokenKey);
		
		JSONObject result = new JSONObject();
		if( ret.getCode() == 0 ) {
			result.put("resultCode", 0);
			result.put("errMsg","SUCCESS");
			
			//see BaseHandle.returnLoginSuccess
			onlineUser.setSid(ret.getData().getString("guid"));
			onlineUser.setUid(onlineUser.getSid()+"&"+onlineUser.getChannelId());
			onlineUser.setUserName(ret.getData().getString("username"));
			
			JSONObject returnData = new JSONObject();
			returnData.put("uid", onlineUser.getUid() );
			returnData.put("token",getRandomStr(16));
			returnData.putAll( ret.getData() );
			
			result.put("result", returnData);
		}
		else {
			result.put("resultCode", ret.getCode());
			result.put("result", ret.getData());
		}
		return result.toJSONString();
	}

	/**
	 * 网游支付，直接返回订单号
	 * @param onlinePay
	 * @param codeJson
	 * @return
	 */
	@Override
	public String pay(OnlinePay onlinePay, JSONObject codeJson) {
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("orderId", onlinePay.getOrderId());
		
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("resultCode", 0);
		ret.put("errMsg", "SUCCESS");
		ret.put("result", data);
		
		return JSON.toJSONString(ret);
	}
	
	@Override
	public void parseParamter(String code, HttpServletRequest request, OnlinePay onlinePay) {
		log.info("parseParamter:{}", code);
		
		respData = parseURLParam(code);
		
		onlinePay.setOrderId( respData.getString("game_orderid") );
		onlinePay.setChannelOrderId( respData.getString("xiao7_goid") );
		onlinePay.setCallBackContent( code );
	}
	/**
	 * 计费回调验证
	 * 开发商接收到来自小 7 的支付通告后，需要做下面处理： 
	 * 1）验证数据的完整性； 2）解密 encryp_data 得到关键数据； 3）核对解密后的数据； 4）返回通告结果。 
	 */
	@Override
	public String verifyData(OnlinePay onlinePay, JSONObject result, HttpServletResponse response) {
		String ret = verifyData0(onlinePay, result);
		
		if( !"2000".equals(result.getString("code")) ) {
			log.info(result.toJSONString());
		}
		
		return ret;
	}
	private String verifyData0(OnlinePay onlinePay, JSONObject result) {
		
		Assert.notNull(respData, "response data can't be null.");
		
		String SIGN_ALGORITHMS = getConfig("x7sy.sign_algorithms", "SHA1WithRSA");
		
		String PUBLIC_KEY = (String) onlinePay.getPayInfoConfig().getExInfoMap().get("public_key");
		Assert.hasText(PUBLIC_KEY, "public_key can't be null.");
		
		PublicKey publicKey = loadPublicKey(PUBLIC_KEY);
		
		String guid = respData.getString("guid");
		String subject = respData.getString("subject");
		String xiao7Goid = respData.getString("xiao7_goid");
		String gameOrderId = respData.getString("game_orderid");
		String encrypData = respData.getString("encryp_data");
		String signData = respData.getString("sign_data");

		Map<String, String> map = new TreeMap<String, String>();
		map.put("game_orderid",	gameOrderId);
		map.put("encryp_data",	encrypData);		map.put("xiao7_goid",	xiao7Goid);
		map.put("subject",		subject);
		map.put("guid",			guid);

		if( !checkSign(buildHttpQuery(map), signData, SIGN_ALGORITHMS, publicKey) ) {
			result.put("code", -10);
			result.put("errMsg", "签名验证失败.");
			return getReturnFailure();
		}
		
		byte[] decryptByte = decrypt(publicKey, Base64.decodeBase64(encrypData.getBytes()));
		Map<String, String> decryptMap = decodeHttpQuery(new String(decryptByte));
		
		//比较解密后的订单号与POST参数的订单号是否一致
		if( !gameOrderId.equals(decryptMap.get("game_orderid")) ) {
			result.put("code", -11);
			result.put("errMsg", "订单号不一致.");
			return getReturnFailure();
		}
		
		//TODO 如果解密后比较订单号一致的话，需要通过当前订单号查找数据库，判断这个订单号对应的订单金额和用户guid是否与解密后或者传递过来的内容一致
		
		Double fee = NumberUtils.isNumber(decryptMap.get("pay")) ? new Double(decryptMap.get("pay")) * 100 : 0.0d;	//单位转换：元 => 分
		
		respData.put("fee", fee.intValue());
		
		onlinePay.setActualAmount( respData.getString("fee") );
		
		String payflag = decryptMap.get("payflag");
		
		if( "1".equals(payflag) ) {
			result.put("code", 2000);
			return getReturnSuccess();
		}
		else {
			result.put("code", NumberUtils.isNumber(payflag) && !"2000".equals(payflag) ? Integer.valueOf(payflag) : -1);
			result.put("errMsg", "扣费不成功." + payflag);
			if( "-1".equals(payflag) ) {
				result.put("errMsg", "支付失败");
			}
			else if( "-2".equals(payflag) ) {
				result.put("errMsg", "超时失败");
			}
			return getReturnFailure();
		}
	}
	
	@Override
	public String getReturnSuccess() { return "success"; }

	@Override
	public String getReturnFailure() { return "failed"; }
	
	/**
	 * （4） 【游戏服务器】将 tokenKey 原数据不做任何处理及应用签名 sign 发送给【小 7 手游平台服务器】验证接口；   
	 * @param appKey	SDK 提交到开发商服务器端的 tokenkey 
	 * @param tokenKey	是 appkey 与 tokenkey 的 MD5 值，appkey 在前，tokenkey 在后。 
	 * @return
	 * errorno： 0=成功  1=参数错误  2= tokenkey 无效  3=Sign 无效 （很多时候是因为超时所以需要再重新走一次上面流 程） 100=未知错误
	 * errormsg： 标识码的含义
	 * data： 当errorno为0的时候有效，是一个JSON数据包含下面两个个参数：guid 用户的唯一标识  username 用户名 
	 * @throws Exception
	 */
	private Result<JSONObject> checkLogin(String appKey, String tokenKey) throws Exception {
		
		String url = getConfig("x7sy.url_check_login", "http://developer.x7sy.com/member/check_login");

		Assert.hasText(appKey, "app_key can't be null.");
		Assert.hasText(tokenKey, "tokenkey can't be null.");
		
		String sign = DigestUtils.md5Hex(appKey + tokenKey);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tokenkey", tokenKey);
		params.put("sign", sign);

		String response = HttpUtils.get(url, HttpUtils.toHttpParameter(params));
		
		Result<JSONObject> ret = new Result<JSONObject>();

		JSONObject data = isBlank(response) ? new JSONObject() : JSONObject.parseObject(response);
		
		String errorno = data.getString("errorno");
		
		if( "0".equals(errorno) ) {
			ret.setCode(0);
			ret.setMessage("SUCCESS");
			ret.setData( data.getJSONObject("data") );
		}
		else {
			ret.setCode(NumberUtils.isNumber(errorno) ? Integer.valueOf(errorno) : -1);
			ret.setMessage(isBlank(data.get("errormsg")) ? "request failure." : data.getString("errormsg"));
			ret.setData(data);
		}
		return ret;
	}
	
	/**
	 * RSA验签名检查
	 * @param content
	 * @param sign
	 * @param SIGN_ALGORITHMS
	 * @param publicKey
	 * @return
	 */
	private boolean checkSign(String content, String sign, String SIGN_ALGORITHMS, PublicKey publicKey) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(publicKey);
			signature.update(content.getBytes());
			return signature.verify( Base64.decodeBase64(sign.getBytes()) );
		}
		catch (Exception e) {
			log.warn("RSA验签名检查出错." + e.getMessage(), e);
		}
		return false;
	}
	/**
	 * 公钥解密
	 * @param publicKey
	 * @param cipherData
	 * @return
	 * @throws Exception
	 */
	private byte[] decrypt(PublicKey publicKey, byte[] cipherData) {
		
		Assert.notNull(publicKey, "解密公钥为空, 请检查.");
		
	    //RSA最大解密密文大小
	    int maxDecryptBlock = getConfig("x7sy.max_decrypt_block", 128);
	    
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			
			int inputLen = cipherData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > maxDecryptBlock) {
					cache = cipher.doFinal(cipherData, offSet, maxDecryptBlock);
				}
				else {
					cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * maxDecryptBlock;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return decryptedData;                
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("无此解密算法." + e.getMessage(), e);
		}
		catch (NoSuchPaddingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		catch (InvalidKeyException e) {
			throw new RuntimeException("解密公钥非法." + e.getMessage(), e);
		}
		catch (IllegalBlockSizeException e) {
			throw new RuntimeException("密文长度非法." + e.getMessage(), e);
		}
		catch (BadPaddingException e) {
			throw new RuntimeException("密文数据已损坏." + e.getMessage(), e);
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    }
    
    /**
     * 从字符串加载公钥
     * @return
     * @throws Exception
     */
	private PublicKey loadPublicKey(String PUBLIC_KEY) {
		try {
			String publicKeyStr = "";
			int count = 0;
			for (int i = 0; i < PUBLIC_KEY.length(); ++i) {
				if (count < 64) {
					publicKeyStr += PUBLIC_KEY.charAt(i);
					count++;
				}
				else {
					publicKeyStr += PUBLIC_KEY.charAt(i) + "\r\n";
					count = 0;
				}
			}
			byte[] buffer = Base64.decodeBase64(publicKeyStr.getBytes());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("无此算法." + e.getMessage(), e);
		}
		catch (InvalidKeySpecException e) {
			throw new RuntimeException("公钥非法." + e.getMessage(), e);
		}
		catch (NullPointerException e) {
			throw new RuntimeException("公钥数据为空." + e.getMessage(), e);
		}
    }

	private String buildHttpQuery(Map<String, String> data) {
		String builder = new String();
		for (Entry<String, String> pair : data.entrySet()) {
			builder += URLEncode(pair.getKey()) + "=" + URLEncode(pair.getValue()) + "&";
		}
		return builder.isEmpty() ? builder.toString() : builder.substring(0, builder.length() - 1);
	}
	private Map<String, String> decodeHttpQuery(String httpQuery) {
		Map<String, String> map = new TreeMap<String, String>();
		for(String s: httpQuery.split("&")) {
			String pair[] = s.split("=");
			map.put(URLDecode(pair[0]), URLDecode(pair[1]));
		}
		return map;
	}

	
	private boolean isBlank(Object str){ return str == null || StringUtils.isBlank(str.toString()); }
	
	
	private static PropertiesConfiguration config = null;
	private synchronized PropertiesConfiguration getConfig() throws ConfigurationException {
		if( config == null ) {
			URL url = getClass().getResource( getClass().getSimpleName() + ".properties");
			if( url != null ) {
				config = new PropertiesConfiguration(url);
				config.setReloadingStrategy(new FileChangedReloadingStrategy());	
			}
		}
		return config;
	}
	private String getConfig(String key, String defVal) {
		try {
			PropertiesConfiguration config = getConfig();
			return config == null ? defVal : config.getString(key, defVal);
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new java.lang.RuntimeException("get config error." + e.getMessage(), e);
		}
	}
	private Integer getConfig(String key, Integer defVal) {
		try {
			String ret = getConfig(key, (String) null);
			return NumberUtils.isNumber(ret) ? Integer.valueOf(ret) : defVal;
		}
		catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new java.lang.RuntimeException("get config error." + e.getMessage(), e);
		}
	}

	public static void main(String argc[]) {
		
		JSONObject map = new JSONObject();
		map.put("encryp_data", "d1mURDEvyzY7ftfFBf1Y/4DfShLV+Nh9HEJP5Ld2WYmZBd2iosrFRJD3YerhChfqg4Ng6P9g94IJOkJ13KFQwXdn2yKSk1DiFEEOzD/ZltIsmAzf1lfqinfKwGgSio1lnveMMBGp4g/L1N+/7mgBBjJxVU6BzIN3ctUf5cCZ7Qs=");
		map.put("game_orderid", "987654321");
		map.put("guid", "112");
		map.put("xiao7_goid", "50");
		map.put("subject", "极品武器");
		map.put("sign_data", "cSz/2dsYql+Na+7WylCwtM2V5LDGk1zvBtz9EgplaLVKzKWE0csZ/gTsuKRL4IlcjtSz46Y7hZvtRKEpKrgOgVzBei98UBTKxGFdM0rMEwcDk+arsI9oKC5MKHHg7tIVJnak+G+GwjL/jFh7jmQe1FdyW2kM7joNT8fXjOd85wE=");

		OnlinePay onlinePay = new OnlinePay();
		onlinePay.setPayInfoConfig(new PayInfoConfig());
		onlinePay.getPayInfoConfig().setExInfo("{public_key:'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+zCNgOlIjhbsEhrGN7De2uYcfpwNmmbS6HYYI5KljuYNua4v7ZsQx5gTnJCZ+aaBqAIRxM+5glXeBHIwJTKLRvCxC6aD5Mz5cbbvIOrEghyozjNbM6G718DvyxD5+vQ5c0df6IbJHIZ+AezHPdiOJJjC+tfMF3HdX+Ng/VT80LwIDAQAB'}");

		JSONObject result = new JSONObject();

		x7syHandle x7sy = new x7syHandle();
		x7sy.respData = map;
		x7sy.verifyData0(onlinePay, result);

		System.out.println(result.toJSONString());
	}
}
