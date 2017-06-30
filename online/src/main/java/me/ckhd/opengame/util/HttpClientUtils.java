/*
 * 
 */
package me.ckhd.opengame.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 */
public class HttpClientUtils {

	private final static Logger LOG = LoggerFactory
			.getLogger(HttpClientUtils.class);

	/**
	 * @param serverUrl
	 * @param data
	 * @param conTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String get(String serverUrl, String data,int conTimeout, int readTimeout) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		URL url;
		try {
			if(serverUrl.indexOf("?") >= 0){
				serverUrl += "&" + data;
			}else{
				serverUrl += "?" + data;
			}
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}
	
	/**
	 * @param serverUrl
	 * @param data
	 * @param conTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String getLenovo(String serverUrl, String data,int conTimeout, int readTimeout) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		URL url;
		try {
			if(serverUrl.indexOf("?") >= 0){
				serverUrl += "&" + data;
			}else{
				serverUrl += "?" + data;
			}
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "*/*; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}
	
	/**
	 * @param serverUrl
	 * @param data
	 * @param conTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String get(String serverUrl,int conTimeout, int readTimeout) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		URL url;
		try {
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}
	
	public static String post(String serverUrl, String data,int conTimeout, int readTimeout) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		URL url;
		try {
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(data);
			wr.flush();

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}

	public static String post(String serverUrl, String data,int conTimeout, int readTimeout,String contentType) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		URL url;
		try {
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("Content-Type",  contentType+"charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			
			wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(data);
			wr.flush();

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}
	
	public static String postVivo(String serverUrl, String data,int conTimeout, int readTimeout) {
		BufferedReader reader = null;
		OutputStreamWriter wr = null;
		ByteArrayOutputStream outStream = null;
		InputStream in = null;
		URL url;
		try {
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); 
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);

			wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(data);
			wr.flush();

			/*reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}*/
			in = conn.getInputStream();
			outStream = new ByteArrayOutputStream();  
	        byte[] respData = new byte[512]; 
	        int count = -1;
	        while((count = in.read(respData,0,512)) != -1)  
	            outStream.write(respData, 0, count);    
	        data = null;  
	        return new String(outStream.toByteArray(),"utf-8");
//			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
			if(outStream != null){
				try {
					outStream.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}
	
	public static String doGet(String url,Map<String, Object> param){
		
     	String paramStr="";
     	for(String key:param.keySet()){
     		if(!"".equals(paramStr)){
     			paramStr+="&";
     		}
     		try {
				paramStr+=key+"="+URLEncoder.encode(param.get(key).toString(),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
     	}
     	return doGet(url, paramStr,null);
	}
	
	/**
	 * 通过参数和头数据调用接口
	 * @param url
	 * @param param
	 * @param headers
	 * @return
	 */
	public static String doGetHeader(String url,Map<String, Object> param,Map<String, String> headers){
		String paramStr="";
     	for(String key:param.keySet()){
     		if(!"".equals(paramStr)){
     			paramStr+="&";
     		}
     		try {
				paramStr+=key+"="+URLEncoder.encode(param.get(key).toString(),"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
     	}
		return doGet(url, paramStr,headers);
	}
	
	 public static String doGet(String url,String paramStr,Map<String, String> headers)
     {
		 
		String result= "";
		if(StringUtils.hasText(paramStr)){
			url = url+"?"+paramStr;
		}
     	HttpGet httpRequst = new HttpGet(url);
     	if(headers!=null && headers.size()>0){
     		for(String key : headers.keySet()){
     			httpRequst.addHeader(key, headers.get(key));
     		}
     	}
     	
     		try {
	 			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);//其中HttpGet是HttpUriRequst的子类
	 		    if(httpResponse.getStatusLine().getStatusCode() == 200)
	 		    {
	 		    	HttpEntity httpEntity = httpResponse.getEntity();
	 		    	result = EntityUtils.toString(httpEntity);//取出应答字符串
	 		    // 一般来说都要删除多余的字符 
	 		    	result.replaceAll("\r", "");//去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格  
	 		    }else{ 
	 		    	httpRequst.abort();
	 		    }
     		} catch (ClientProtocolException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			result = e.getMessage().toString();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			result = e.getMessage().toString();
 		}
 		return result;
     }
	 
	 
	 /**
	 * @param serverUrl
	 * @param data
	 * @param conTimeout
	 * @param readTimeout
	 * @return
	 */
	public static String get(String serverUrl, String data,int conTimeout, int readTimeout,Map<String,Object> map) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;
	
		URL url;
		try {
			if(serverUrl.indexOf("?") >= 0){
				serverUrl += "&" + data;
			}else{
				serverUrl += "?" + data;
			}
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			List<String> cookies = conn.getHeaderFields().get("Set-Cookie");
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			LOG.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
	
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LOG.error("close error", e);
				}
			}
		}
		return null;
	}
}
