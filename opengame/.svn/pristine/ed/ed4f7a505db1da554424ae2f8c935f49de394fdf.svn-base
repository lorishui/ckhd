package me.ckhd.opengame.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
		String str = "POST&%2F&AccessKeyId%3Dtestid%26Action%3DSingleSendSms%26Format%3DXML%26ParamString%3D%257B%2522name%2522%253A%2522d%2522%252C%2522name1%2522%253A%2522d%2522%257D%26RecNum%3D13098765432%26RegionId%3Dcn-hangzhou%26SignName%3D%25E6%25A0%2587%25E7%25AD%25BE%25E6%25B5%258B%25E8%25AF%2595%26SignatureMethod%3DHMAC-SHA1%26SignatureNonce%3D9e030f6b-03a2-40f0-a6ba-157d44532fd0%26SignatureVersion%3D1.0%26TemplateCode%3DSMS_1650053%26Timestamp%3D2016-10-20T05%253A37%253A52Z%26Version%3D2016-09-27";
		String key = "testsecret&";
		System.out.println(URLEncoder.encode(makeSig(str, key),"utf-8"));
	}
}
