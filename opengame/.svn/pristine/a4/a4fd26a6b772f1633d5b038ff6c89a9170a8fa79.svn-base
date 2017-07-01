package me.ckhd.opengame.common.utils;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class RSACoder {
	public static final String KEY_ALGORITHM = "RSA";  
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    
    /** 
     * 校验数字签名 
     *  
     * @param data 
     *            加密数据 
     * @param publicKey 
     *            公钥 
     * @param sign 
     *            数字签名 
     *  
     * @return 校验成功返回true 失败返回false 
     * @throws Exception 
     *  
     */  
    public static boolean verify(byte[] data, String publicKey, String sign)  
            throws Exception {  
  
        // 解密由base64编码的公钥  
    	//return (new BASE64Decoder()).decodeBuffer(key);
        byte[] keyBytes = Base64.decodeBase64(publicKey);
  
        // 构造X509EncodedKeySpec对象  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
  
        // KEY_ALGORITHM 指定的加密算法  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
  
        // 取公钥匙对象  
        PublicKey pubKey = keyFactory.generatePublic(keySpec);  
  
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);  
        signature.initVerify(pubKey);  
        signature.update(data);  
  
        // 验证签名是否正常  
        return signature.verify(Base64.decodeBase64(sign));  
    }
    
    /** 
     * 校验数字签名 
     *  
     * @param data 
     *            加密数据 
     * @param publicKey 
     *            公钥 
     * @param sign 
     *            数字签名 
     *  
     * @return 校验成功返回true 失败返回false 
     * @throws Exception 
     *  
     */  
    public static boolean verifySHA1(byte[] data, String publicKey, String sign)  
            throws Exception {  
  
        // 解密由base64编码的公钥  
    	//return (new BASE64Decoder()).decodeBuffer(key);
        byte[] keyBytes = Base64.decodeBase64(publicKey);
  
        // 构造X509EncodedKeySpec对象  
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);  
  
        // KEY_ALGORITHM 指定的加密算法  
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);  
  
        // 取公钥匙对象  
        PublicKey pubKey = keyFactory.generatePublic(keySpec);  
  
        Signature signature = Signature.getInstance(SIGN_ALGORITHMS);  
        signature.initVerify(pubKey);  
        signature.update(data);  
  
        // 验证签名是否正常  
        return signature.verify(Base64.decodeBase64(sign));  
    }
    /** 
     * 校验数字签名 
     *  
     * @param data 
     *            加密数据 
     * @param publicKey 
     *            公钥 
     * @param sign 
     *            数字签名 
     *  
     * @return 校验成功返回true 失败返回false 
     * @throws Exception 
     *  
     */  
    public static boolean verify(String data, String publicKey, String sign)  
            throws Exception {  
    	return verify(data.getBytes("utf-8"), publicKey, sign);
    }
    
    /** 
     * 校验数字签名 
     *  
     * @param data 
     *            加密数据 
     * @param publicKey 
     *            公钥 
     * @param sign 
     *            数字签名 
     *  
     * @return 校验成功返回true 失败返回false 
     * @throws Exception 
     *  
     */  
    public static boolean verifySHA1(String data, String publicKey, String sign)  
            throws Exception {  
    	return verifySHA1(data.getBytes("utf-8"), publicKey, sign);
    }
    
    public static void main(String[] args) {
		String sign = "WdwM+zn/9yZhA80cpms+0scVUG6gZb3sUdwcryhCNe2xv/MdNMnbFKc5VDMIIwFZwbgHrSyeFjbPuCZ+DxCFtqhTQgdtJ99qIdov1LnqdG9AHD+qroV5WhokOqxp6cZSiUqSpH458qOj4SGEvEGPSCwp3D/O6evV9vAY8SaIIgM=";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
		String data= "body=测试&buyer_email=15013169981&buyer_id=2088022690990148&discount=0.00&gmt_create=2016-09-05 18:25:02&gmt_payment=2016-09-05 18:25:03&is_total_fee_adjust=N&notify_id=d03bc157d326e9bc013a11769155a1ch2y&notify_time=2016-09-05 18:49:40&notify_type=trade_status_sync&out_trade_no=4516090500001484&payment_type=1&price=0.01&quantity=1&seller_email=szckhd@163.com&seller_id=2088601048948103&subject=测试&total_fee=0.01&trade_no=2016090521001004140212665833&trade_status=TRADE_SUCCESS&use_coupon=N";
		try {
//			sign =  URLEncoder.encode(sign,"utf-8");
			System.out.println(sign);
			System.out.println(verifySHA1(data, publicKey, sign));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
