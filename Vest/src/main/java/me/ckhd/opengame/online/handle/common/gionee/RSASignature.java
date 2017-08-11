package me.ckhd.opengame.online.handle.common.gionee;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import net.iharder.Base64;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;

/**
 * RSA签名验签类
 * 
 * @author tianxb
 * @date 2012-12-1 下午2:23:27
 * @since 2.0.0
 */
public class RSASignature {

	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @param encode
	 *            字符集编码
	 * @return 签名值
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws SignatureException
	 * @throws InvalidKeyException
	 */
	public static String sign(String content, String privateKey, String encode) throws IOException,
			NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {
		String charset = CharEncoding.UTF_8;
		if (!StringUtils.isBlank(encode)) {
			charset = encode;
		}
		PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
		KeyFactory keyf = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyf.generatePrivate(priPKCS8);

		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(priKey);
		signature.update(content.getBytes(charset));
		byte[] signed = signature.sign();
		return Base64.encodeBytes(signed);

	}

	/**
	 * <pre>
	 * <p>函数功能说明:RSA验签名检查</p>
	 * <p>修改者名字:guocl</p>
	 * <p>修改日期:2012-11-30</p>
	 * <p>修改内容:抛异常</p>
	 * </pre>
	 * 
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param publicKey
	 *            支付宝公钥
	 * @param encode
	 *            字符集编码
	 * @return 布尔值
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public static boolean doCheck(String content, String sign, String publicKey, String encode)
			throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException,
			SignatureException {
		String charset = CharEncoding.UTF_8;
		if (!StringUtils.isBlank(encode)) {
			charset = encode;
		}
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decode(publicKey);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

		signature.initVerify(pubKey);
		signature.update(content.getBytes(charset));

		boolean bverify = signature.verify(Base64.decode(sign));
		return bverify;

	}
	
	public static void main(String[] args) {
		String content="api_key=15CF694F62674AD58752B14839A9E2D1&close_time=20170103174347&create_time=20170103174312&deal_price=0.01&out_order_no=9017010300000a44&pay_channel=101&submit_time=20170103174312&user_id=null";
		String sign = "UHPC2lW0XG/YQbjelRBwwN25hQZqjBtcLuQYUZJc7WwzWLUtTeIRcNLdCGzFbcVZ9YLgZD0BbEhyHqG0piwzHLbtOCqn6wmSp6aSxfye2UlW0n6bX+tpc0ZKDkPo8mwGghsmc/+U4vgj5uoxc3wUyllB8afe2H1A9BuB9uB1N/s=";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9kCsPGNfAx7iM19X+NCniDEn9mCetwvYvf8dJ6GBMIm1+vQkgBv0FlCeYUWB+EMWEc0805qOtf/vlXfONdcZII4tNHQUwrlErbvYVAbzpVUSIy12emt7Fz3Nz49hm7L3qpL/LtVjSaKAaaO9Uz0Mxxu4m3UKuzzO9Q9lv9LHZQwIDAQAB";
		try {
			System.out.println(doCheck(content, sign, publicKey, "utf-8"));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
