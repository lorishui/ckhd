package me.ckhd.opengame.online.util.anzhi;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;



/**
 * httpClient工具类
 * @author zguowei add 2012-08-27
 * 
 */
public class HttpClientUtil {

	private  HttpClient httpclient;

	public void setHttpclient(HttpClient httpclient) {
		this.httpclient = httpclient;
	}

	public HttpClient getHttpclient() {
		return httpclient;
	}



	public HttpClientUtil() {

	}

	/**
	 * 以get方式发送http请求
	 * 
	 * @param url
	 * @return
	 */
	public String sendRequest(String url) {
		GetMethod getMethod = new GetMethod(url);
		try {
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			httpclient.executeMethod(getMethod);
			return getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
		finally{
			getMethod.releaseConnection();
		}
	}
	
	/**
	 * 以get方式发送http请求
	 * 
	 * @param url
	 * @return
	 */
	public boolean isActive(String url) {
		boolean flag = false;
		GetMethod getMethod = new GetMethod(url);
		try {
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			int statusCode = httpclient.executeMethod(getMethod);
			if(statusCode==200){
				flag = true;
			}
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return flag;
		}
		finally{
			getMethod.releaseConnection();
		}
	}
	
	/**
	 * 以post方式发送http请求
	 * 
	 * @param url
	 * @return
	 */
	public  int sendRequestAsPost(String url) {
		PostMethod postMethod = new PostMethod(url);
		try {
			postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(1000);
			postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,1000);

			int statusCode = httpclient.executeMethod(postMethod);
			return statusCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
		finally{
			postMethod.releaseConnection();
		}
	}



	/**
	 * 获取本机IP地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddr() {
		try {
			Enumeration<?> netInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			String ipAddr = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
				ip = (InetAddress) ni.getInetAddresses().nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					ipAddr = ip.getHostAddress();
					break;
				}
			}
			return ipAddr;
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 判断某回调地址是否是指定的网关地址
	 * 
	 * @param notifyUrl
	 * @param getwayList
	 * @return true 是网关 false不是网关地址
	 */
	public static boolean isLocalNotifyUrl(String notifyUrl, List<?> getwayList) {
		boolean flag = false;
		for (Object object : getwayList) {
			String getway = (String) object;
			if (notifyUrl.toLowerCase().contains(getway)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	
	/**
	 * 回调(如果超时，在请求一次接口)
	 * 
	 * @param api
	 * @param method
	 * @param params
	 * @return 文本内容
	 * @throws SNSException
	 */
	public String callBackTwice(String api,
			NameValuePair... params) {
		String result=null;
		try {
			result = callBack(api,params);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
			try {
				result = callBack(api,  params);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(null==result){
			return "fail";
		}
		return result;
	}
	
//	public static  HttpClientUtil getClient(){
//		return (HttpClientUtil)ApplicationContextHolder.getBean("httpClientUtil");
//	}
	
	/**
	 * 回调
	 * 
	 * @param api
	 * @param method
	 * @param params
	 * @return 文本内容
	 * @throws IOException
	 * @throws IOException
	 * @throws
	 * @throws HttpException
	 */
	public String callBack(String api,NameValuePair... params) throws IOException {
		String uri = getUrl(api, params);
		if(null==uri){
			return null;
		}
		//GetMethod method = new GetMethod(uri);
		PostMethod method=new PostMethod(api);
		method.addParameters(params);
		try {
			int status = httpclient.executeMethod(method);
			if (200 == status) {
				//byte[] responseBody = method.getResponseBody();
				//return new String(responseBody);
				InputStream is=method.getResponseBodyAsStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("utf-8")));  
		        StringBuffer resBuffer = new StringBuffer();  
		        String resTemp = "";  
		        while((resTemp = br.readLine()) != null){  
		            resBuffer.append(resTemp);  
		        }  
		        return resBuffer.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			method.releaseConnection();
		}
		return "failed";
	}
	
	public String callBackByUTF8(String api,NameValuePair... params) throws IOException {
		String uri = getUrl(api, params);
		if(null==uri){
			return null;
		}
		//GetMethod method = new GetMethod(uri);
		PostMethod method=new PostMethod(api);
		method.addParameters(params);
		try {
			int status = httpclient.executeMethod(method);
			if (200 == status) {
				InputStream is=method.getResponseBodyAsStream();
				//byte[] responseBody = method.getResponseBody();
				//return new String(responseBody, Charset.forName("utf-8"));
				BufferedReader br = new BufferedReader(new InputStreamReader(is,Charset.forName("utf-8")));  
		        StringBuffer resBuffer = new StringBuffer();  
		        String resTemp = "";  
		        while((resTemp = br.readLine()) != null){  
		            resBuffer.append(resTemp);  
		        }  
		        return resBuffer.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			method.releaseConnection();
		}
		return "failed";
	}
	
	
	/**
	 * 拼写URL
	 * @param api
	 * @param nvs
	 * @return String
	 * @throws IOException
	 */
	public String getUrl(String api,NameValuePair... nvs) throws IOException {
		if(null==nvs||null==api){
			return api;
		}
		StringBuffer paramline = new StringBuffer();
		for (NameValuePair param : nvs) {
			if(param.getName()!=null&&param.getValue()!=null){
			paramline.append(param.getName()).append("=")
					.append(URLEncoder.encode(param.getValue(), "UTF-8"))
					.append("&");
		}
		}
		String uri = "";
		if (paramline.length() > 1) {
			paramline.setLength(paramline.length() - 1);
			if (api.indexOf("?") != -1) {
				uri = api + "&" + paramline.toString();
			} else {
				uri = api + "?" + paramline.toString();
			}
		} else {
			uri = api;
		}
		return uri;
	}
	
	
	public  String sendMsg(String msg,String url){
		String resultStr = null;
		try {
			if (!isNullOrNothing(msg)) {
				PostMethod post = new PostMethod(url);
				InputStream respStr = null;
				StringRequestEntity requestEntity = new StringRequestEntity(URLEncoder.encode(msg, "utf-8"), "text/xml", "utf-8");

				post.setRequestEntity(requestEntity);
				post.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
				httpclient.getHostConfiguration().setHost(url);
				int result = 0;
				result = httpclient.executeMethod(post);
				
				if (result == HttpStatus.SC_OK) {
					respStr = post.getResponseBodyAsStream();
					if (respStr != null){
						resultStr = stream2String(respStr);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
		return resultStr;
	}
	
	public static boolean isNullOrNothing(String obj){
		return (obj==null || "".equals(obj))?true:false;
	}
	
	//转换数据流为字符串
		public static String stream2String(InputStream is) throws IOException {   
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();   
	        int i = -1;   
	        while ((i = is.read()) != -1) {   
	            baos.write(i);   
	        }   
	        return baos.toString();   
	    } 
	
	public static void main(String[] arg){
	/*	HttpClient httpclient = new HttpClient();
		String url = "http://www.163.com";
		GetMethod method = new GetMethod(url);
		try {
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			int statusCode = httpclient.executeMethod(method);
			System.out.println(statusCode);
			byte[] responseBody = method.getResponseBody();
			System.out.println(new String(responseBody));
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			method.releaseConnection();
		}*/
	}
}

