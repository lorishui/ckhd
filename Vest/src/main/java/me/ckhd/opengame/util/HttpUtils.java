/*
 * 
 */
package me.ckhd.opengame.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 提供普通的HTTP请求
 * 2017/06/27
 * @author wizard
 */
public class HttpUtils {

	private final static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	public final static String METHOD_POST	= "POST";
	public final static String METHOD_GET	= "GET";

	public final static Integer defaultConnectTimeout	= 1000 * 10;
	public final static Integer defaultReadTimeout		= 1000 * 10;

	public static String get(String serverUrl, String data) {
		return get(serverUrl, data, null);
	}
	public static String get(String serverUrl, String data, String contentType) {
		return get(serverUrl, data, contentType, defaultConnectTimeout, defaultReadTimeout);
	}
	public static String get(String serverUrl, String data, String contentType, int connectTimeout, int readTimeout) {
		
		Map<String, String> header = new HashMap<String, String>();
		if( !StringUtils.isBlank(contentType) ) {
			header.put("Content-Type", contentType);
		}
		
		return request(serverUrl, METHOD_GET, data, header, connectTimeout, readTimeout);
	}

	

	public static String post(String serverUrl, String data) {
		return post(serverUrl, data, null);
	}
	public static String post(String serverUrl, String data, String contentType) {
		return post(serverUrl, data, contentType, defaultConnectTimeout, defaultReadTimeout);
	}
	public static String post(String serverUrl, String data, String contentType, int connectTimeout, int readTimeout) {
		
		Map<String, String> header = new HashMap<String, String>();
		if( !StringUtils.isBlank(contentType) ) {
			header.put("Content-Type", contentType);
		}
		
		return request(serverUrl, METHOD_POST, data, header, connectTimeout, readTimeout);
	}

	public static String request(String serverUrl, String method, String data, Map<String, String> header, int connectTimeout, int readTimeout) {
		String response = null;
		long timemillis = System.currentTimeMillis();
		try {
			logger.info("[{}]request:{},method:{},data:{},header:{}", new Object[]{timemillis, serverUrl, method, data, header});
			response = request0(serverUrl, method, data, header, connectTimeout, readTimeout);
		}
		catch(Exception e) {
			logger.error("[{" + timemillis + "}]" + e.getMessage(), e);
		}
		finally {
			logger.info("[{}]response:{},elapse:{}", new Object[]{timemillis, response, System.currentTimeMillis() - timemillis});
		}
		return response;
	}
	public static String request0(String serverUrl, String method, String data, Map<String, String> header, int connectTimeout, int readTimeout) throws IOException {
		BufferedReader reader = null;
		OutputStreamWriter writer = null;
		try {
			if( METHOD_GET.equalsIgnoreCase(method) ) {
				serverUrl = buildURL(serverUrl, data);
			}
			
			URL url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestMethod(method); 
			
			if( header != null ) {
				for( Map.Entry<String, String> entry : header.entrySet() ) {
					conn.setRequestProperty(entry.getKey(),  entry.getValue());
				}
			}

			if( METHOD_POST.equalsIgnoreCase(method) ) {
				conn.setDoOutput(true);
				
				writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write(data);
				writer.flush();
			}

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			List<String> outs = new ArrayList<String>();
			while (true) {
				String line = reader.readLine();
				if( line == null ) {
					break;
				}
				outs.add(line);
			}
			return StringUtils.join(outs, "\n");
		}
		finally {
			close(writer);
			close(reader);
		}
	}

	public static String toHttpParameter(Map<String, Object> params) throws UnsupportedEncodingException {
		List<String> rets = new ArrayList<String>();
		if( params != null ) {
			for(Map.Entry<String, Object> entry : params.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue() == null ? "" : entry.getValue().toString();
				rets.add(key + "=" + URLEncoder.encode(val, "utf-8"));
			}
		}
		return StringUtils.join(rets, "&");
	}

	public static String buildURL(String url, Map<String, Object> params) throws UnsupportedEncodingException {
		return buildURL(url, toHttpParameter(params));
	}
	public static String buildURL(String url, String param) {

		Assert.hasText(url);
		
		if( url.indexOf("?") == -1 ) {
			return url + "?" + param;
		}
		else {
			return url + (url.endsWith("&") ? "" : "&") + param; 
		}
	}
	
	
	private static void close(Closeable closeable) {
		try {
			if( closeable != null ) {
				closeable.close();
			}
		}
		catch(Exception e) {}
	}
}
