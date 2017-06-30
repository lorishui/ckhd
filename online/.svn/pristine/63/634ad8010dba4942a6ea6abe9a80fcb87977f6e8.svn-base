package me.ckhd.opengame.online.handle.common.coolpad;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSA 
{
	private static final String  SIGN_ALGORITHMS = "MD5WithRSA";
	
	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key  爱贝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	public static String str;
	public static boolean verify(String content, String sign, String iapp_pub_key, String input_charset)
	{
		
		try 
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        byte[] encodedKey = Base64.decode(iapp_pub_key);
	        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		
			java.security.Signature signature = java.security.Signature
			.getInstance(SIGN_ALGORITHMS);
		
			signature.initVerify(pubKey);
			signature.update( content.getBytes(input_charset) );
		
			return signature.verify( Base64.decode(sign) );
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String input_charset)
	{
        try 
        {
        	PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( Base64.decode(privateKey) ); 
        	KeyFactory          keyf        = KeyFactory.getInstance("RSA");
        	PrivateKey          priKey 		= keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update( content.getBytes(input_charset) );
            byte[] signed = signature.sign();
            return Base64.encode(signed);
          
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        
        return null;
    }
	
	
	public static String md5s(String plainText) {
		String buff = null;
		  try {
		   MessageDigest md = MessageDigest.getInstance("MD5");
		   md.update(plainText.getBytes());
		   byte b[] = md.digest();
		   int i;

		   StringBuffer buf = new StringBuffer("");
		   for (int offset = 0; offset < b.length; offset++) {
		    i = b[offset];
		    if (i < 0)
		     i += 256;
		    if (i < 16)
		     buf.append("0");
		    buf.append(Integer.toHexString(i));
		   }
		   buff =buf.toString();
		    Base64.encode(buff.getBytes());
		    System.out.println("base64:"+ Base64.encode(buff.getBytes()));
		   str = buf.toString();
		   System.out.println("result: " + buf.toString());// 32位的加密
		   System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密
		  } catch (NoSuchAlgorithmException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();

		  }
		return Base64.encode(buff.getBytes());
	
	
	
	
  }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		//transdata={"transid":"32021702201143059060"}&sign=e9n2ZGWQA+dEWXmc9RYGvcpb+NgoDPnV80MMnRH4rLgaT+BNgFBqE9udSd0Y8UByvy2DLyzb3bmeWXOD6MiRj6VHoHbCiZmBWbKR7eGzWmdvgqeGRz8Z6I1/h7gQtd9HvDmqb3d6A0dSrO9rUTvod9OpHaWTO2xbdUqypUfaqq8=&signtype=RSA
		String content = "{\"transid\":\"32021702201143059060\"}";
		String sign = "e9n2ZGWQA+dEWXmc9RYGvcpb+NgoDPnV80MMnRH4rLgaT+BNgFBqE9udSd0Y8UByvy2DLyzb3bmeWXOD6MiRj6VHoHbCiZmBWbKR7eGzWmdvgqeGRz8Z6I1/h7gQtd9HvDmqb3d6A0dSrO9rUTvod9OpHaWTO2xbdUqypUfaqq8=";
		String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDXYIVqImGhAWrzqFemx/eDbMQAwn7BLgvOd4aWlXGXHz/0CL6h12HofzkdoZ3P4ztTniEbmeqqo2japRKm/MxMYHp0Qz+7WFDup6GE65+K7hEE4A/w6CbkGo6SbOdPl6IuZrBFaWDfOuejY9Cl+7O5NHYsnD5l9M54gAqoAlBQjQIDAQAB";
		System.out.println(verify(content, sign, key, "utf-8"));
	}
}
