package me.ckhd.opengame.online.response.lenovo2;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class Rsa {
	 public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	  
	 public static String sign(String content, String privateKey, String input_charset){
		 try{
			 PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			 KeyFactory keyf = KeyFactory.getInstance("RSA");
			 PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			 Signature signature = Signature.getInstance("SHA1WithRSA");
			 signature.initSign(priKey);
			 signature.update(content.getBytes(input_charset));
			 byte[] signed = signature.sign();
			 return Base64.encode(signed);
	    }catch (Exception e){
	      e.printStackTrace();
	    }
	    return null;
	  }
	  
}
