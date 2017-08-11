package me.ckhd.opengame.online.handle.common.gionee;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import me.ckhd.opengame.common.utils.StringUtils;
public class CryptoUtility {
	private static final String MAC_NAME = "HmacSHA1";

	public static String macSig(String host, String port, String macKey, String timestamp, String nonce, String method, String uri) {
		// 1. build mac string
		// 2. hmac-sha1
		// 3. base64-encoded

		StringBuffer buffer = new StringBuffer();
		buffer.append(timestamp).append("\n");
		buffer.append(nonce).append("\n");
		buffer.append(method.toUpperCase()).append("\n");
		buffer.append(uri).append("\n");
		buffer.append(host.toLowerCase()).append("\n");
		buffer.append(port).append("\n");
		buffer.append("\n");
		String text = buffer.toString();

		byte[] ciphertext = null;
		try {
			ciphertext = hmacSHA1Encrypt(macKey, text);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}

		String sigString = Base64.encodeToString(ciphertext,Base64.DEFAULT);
		return sigString;
	}

	public static byte[] hmacSHA1Encrypt(String encryptKey, String encryptText) throws InvalidKeyException, NoSuchAlgorithmException {
		Mac mac = Mac.getInstance(MAC_NAME);
		mac.init(new SecretKeySpec(StringUtils.getBytes(encryptKey), MAC_NAME));
		return mac.doFinal(StringUtils.getBytes(encryptText));
	}
}
