package me.ckhd.opengame.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class CoderUtils {
	static Logger log = LoggerFactory.getLogger(CoderUtils.class);
	public static final String KEY_SHA = "SHA";  
	public static final String KEY_MD5 = "MD5";  
  
	/** 
	  * MAC算法可选以下多种算法 
	  *  
	  * <pre> 
	  * HmacMD5  
	  * HmacSHA1  
	  * HmacSHA256  
	  * HmacSHA384  
	  * HmacSHA512 
	  * </pre> 
	  */  
	 public static final String KEY_MAC_MD5 = "HmacMD5";
	 public static final String KEY_MAC_SHA1 = "HmacSHA1";
	 public static final String KEY_MAC_SHA256 = "HmacSHA256";
	 public static final String KEY_MAC_SHA384 = "HmacSHA384";
	 public static final String KEY_MAC_SHA512 = "HmacSHA512";
	 /** 
	  * BASE64解密 
	  *  
	  * @param key 
	  * @return 
	  * @throws Exception 
	  */  
	 public static byte[] decryptBASE64(String key) throws Exception {  
		 return (new BASE64Decoder()).decodeBuffer(key);  
	 }  
  
	 /** 
	  * BASE64加密 
	  *  
	  * @param key 
	  * @return 
	  * @throws Exception 
	  */  
	 public static String encryptBASE64(byte[] key) throws Exception {  
	    return (new BASE64Encoder()).encode(key);  
	 }  
  
	 /** 
	  * MD5加密 
	  *  
	  * @param data 
	  * @return 
	  * @throws Exception 
	  */  
	 public static byte[] encryptMD5(byte[] data) throws Exception {  
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);  
        md5.update(data);
        return md5.digest();
	 }  
  
    /** 
     * SHA加密 
     *  
     * @param data 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptSHA(byte[] data) throws Exception {  
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);  
        sha.update(data);  
        return sha.digest();
    }  
  
    /** 
     * 初始化HMAC密钥 
     *  
     * @return 
     * @throws Exception 
     */  
    public static String initMacKey() throws Exception {  
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC_SHA1);
        SecretKey secretKey = keyGenerator.generateKey();  
        return encryptBASE64(secretKey.getEncoded());  
    }  
  
    /** 
     * HMAC加密 
     *  
     * @param data 
     * @param key 
     * @return 
     * @throws Exception 
     */  
    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {  
        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC_SHA1);  
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
        mac.init(secretKey);  
        return mac.doFinal(data); 
    }

    /* 生成签名
    *
    * @param method HTTP请求方法 "get" / "post"
    * @param url_path CGI名字, eg: /v3/user/get_info
    * @param params URL请求参数
    * @param secret 密钥
    * @return 签名值
    * @throws OpensnsException 不支持指定编码以及不支持指定的加密方法时抛出异常。
    */
	public static String makeSig(String data, String secret){
		String sig = null;
		Mac mac;
		try {
			mac = Mac.getInstance(KEY_MAC_SHA1);
			SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("utf-8"), mac.getAlgorithm());
			mac.init(secretKey);         
			byte[] hash = mac.doFinal(data.getBytes("utf-8"));
			
			// base64
			sig = encryptBASE64(hash);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return sig;
   }
	
	
	
	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5","6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
    public static String md5(String data,String encode){
		try {
			return byteToString(encryptMD5(data.getBytes(encode)));
		} catch (UnsupportedEncodingException e) {
			log.info("md5加密error",e);
		} catch (Exception e) {
			log.info("md5加密error",e);
		}
		return "";
	}
	
	// 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
    
 // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

	
	public static void main(String[] args) throws UnsupportedEncodingException, Exception {
		String str = "9603561viDcDee9V9fIea8yOq-1141-2017052306ya17052306ef9934eyJCYW5rRGF0ZVRpbWUiOiIyMDE3LTA1LTIzIDE4OjA2OjUxIiwiRXh0SW5mbyI6ImNrc2RrIiwiTWVyY2hhbmRpc2VOYW1lIjoiMTk4MOWFg+WunSIsIk9yZGVyTW9uZXkiOiIxOTguMDAiLCJPcmRlclN0YXR1cyI6MSwiU3RhcnREYXRlVGltZSI6IjIwMTctMDUtMjMgMTg6MDY6NDQiLCJTdGF0dXNNc2ciOiLmiJDlip8iLCJVSUQiOiIxMDgzNzExOTg5IiwiVm91Y2hlck1vbmV5IjowfQ==fDxS9L86rhU9Wg9hAKgWTSifkcdfcF6w";
		System.out.println(md5(str, "utf-8"));
		//5B7755C81AF59BFBB373D7BC0BC9B12E
		String str2 = "1419879403111890866361149647340843a77386c5088d7ce8de40002858e39f";
		System.out.print(md5(str2, "utf-8"));
	}
}
