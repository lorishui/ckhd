package me.ckhd.opengame.online.handle.common.gionee;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifyUtils {
	static Logger logger = LoggerFactory.getLogger(VerifyUtils.class);

	// verify 方法封装了 验证方法，调用此方法即可完成帐号安全验证
	public static Map<String, String> verify(String amigoToken,String goLoginUrl,String host,String port,
			String secretKey,String urlStr,String apiKey) {
		HttpsURLConnection httpURLConnection = null;
		OutputStream out;
		Map<String, String> map = new HashMap<String, String>();
		String errMsg;
		TrustManager[] tm = {new MyX509TrustManager()};
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL sendUrl = new URL(goLoginUrl);
			httpURLConnection = (HttpsURLConnection) sendUrl.openConnection();
			httpURLConnection.setSSLSocketFactory(ssf);
			httpURLConnection.setDoInput(true); // true表示允许获得输入流,读取服务器响应的数据,该属性默认值为true
			httpURLConnection.setDoOutput(true); // true表示允许获得输出流,向远程服务器发送数据,该属性默认值为false
			httpURLConnection.setUseCaches(false); // 禁止缓存
			int timeout = 30000;
			httpURLConnection.setReadTimeout(timeout); // 30秒读取超时
			httpURLConnection.setConnectTimeout(timeout); // 30秒连接超时
			String method = "POST";
			httpURLConnection.setRequestMethod(method);
			httpURLConnection.setRequestProperty("Content-Type", "application/json");
			String reqData = builderAuthorization(host, port, secretKey, method, urlStr, apiKey);
			logger.info("登陆时发送给渠道的数据为:"+reqData);
			httpURLConnection.setRequestProperty("Authorization",reqData);
			out = httpURLConnection.getOutputStream();
			out.write(amigoToken.getBytes());
			out.flush();
			out.close();
			InputStream in = httpURLConnection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = in.read(buff)) != -1) {
				buffer.write(buff, 0, len);
			}
			map.put("resultCode", "0");
			map.put("result", new String(buffer.toByteArray(),"utf-8"));
			return map;
		} catch (KeyManagementException e) {
			e.printStackTrace();
			errMsg=e.getMessage();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			errMsg=e.getMessage();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			errMsg=e.getMessage();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			errMsg=e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			errMsg=e.getMessage();
		}
		map.put("resultCode", "-1");
		map.put("result", errMsg);
		return map;
	}
		
	private static String builderAuthorization(String host,String port,
			String secretKey,String method,String urlStr,String apiKey) {
		Long ts = System.currentTimeMillis() / 1000;
		String nonce = CamelUtility.uuidToString(UUID.randomUUID()).substring(0, 8);
		String mac = CryptoUtility.macSig(host, port, secretKey, ts.toString(), nonce, method, urlStr);
		mac = mac.replace("\n", "");
		StringBuilder authStr = new StringBuilder();
		authStr.append("MAC ");
		authStr.append(String.format("id=\"%s\"", apiKey));
		authStr.append(String.format(",ts=\"%s\"", ts));
		authStr.append(String.format(",nonce=\"%s\"",nonce));
		authStr.append(String.format(",mac=\"%s\"",mac));
		return authStr.toString();
	}
}
