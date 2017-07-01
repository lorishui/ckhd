package me.ckhd.opengame.online.response.i4;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;

/** */
/**
 * RSA安全编码组件
 * 
 * @version 1.0
 * @since 1.0
 */
public class RSADecrypt extends Coder {

	// 默认公钥(openssl)
	public static final String DEFAULT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1hZW86F5AYXlRATheoBmpXdNDhefLlALnFTFezmuTHvK5n2KxMva618zv5Zmr4G1Rs5KfXKPiZ7SCDl8o+KwRiXVxM/ppc/vIMf0JIn0txYogzukHI+f9iePAKX0Sq2uVJOrNJzHHYbBmaJMfB8KoHSAeNBn12ubSsvZQPFyyUwIDAQAB";
	public static final String KEY_ALGORITHM = "RSA";
	/** *//**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

	/** */
	/**
	 * 解密<br>
	 * 用公钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key)
			throws Exception {
		byte[] keyBytes = new BASE64Decoder().decodeBuffer(key);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
	}

	public static void main(String[] args) {
		// 文档测试数据
//		String testDataStr = "em%2FGgIVAwcAh1TpkmandTsgq05tYqeylyRUp6lTdYh0r3RIwodf1%2FgJWf8MtUSxX%2FTIVwjDkaHno%0AAMPwGGDXUcZFNq196t0Tvur1aP4RpTdvm07mMZByQGaPk1ROw%2BpB74yQAbKtGgZTWAB%2BeJOGhvKc%0A7D%2BhK6jYaceRpcjGHEo%3D%0A";
		String testDataStr = "em/GgIVAwcAh1TpkmandTsgq05tYqeylyRUp6lTdYh0r3RIwodf1/gJWf8MtUSxX/TIVwjDkaHnoAMPwGGDXUcZFNq196t0Tvur1aP4RpTdvm07mMZByQGaPk1ROw+pB74yQAbKtGgZTWAB+eJOGhvKc7D+hK6jYaceRpcjGHEo=";

		try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] dcDataStr = base64Decoder.decodeBuffer(testDataStr);
            byte[] plainData = RSADecrypt.decryptByPublicKey(dcDataStr, RSADecrypt.DEFAULT_PUBLIC_KEY);  
            System.out.println("文档测试数据明文长度:" + plainData.length);  
            System.out.println(new String(plainData));
            
        } catch (Exception e) {  
            System.err.println(e.getMessage());  
        }  
	}

}