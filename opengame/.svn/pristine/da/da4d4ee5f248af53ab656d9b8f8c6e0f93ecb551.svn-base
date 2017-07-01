package me.ckhd.opengame.online.response.googleplay;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import me.ckhd.opengame.online.request.appstore.BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nearme.oauth.net.TrustAnyTrustManager;

public class HttpClientUtils {
	static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);
	
	public static String get(String url){
	   log.info("googleplay url:"+url);
	   InputStream in = null;
	   ByteArrayOutputStream outStream = null;
	   try{  
	       SSLContext sc = SSLContext.getInstance("SSL");  
	       sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
	       URL console = new URL(url);  
	       HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();  
	       conn.setSSLSocketFactory(sc.getSocketFactory());  
	       conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
	       conn.setRequestMethod("GET");  
	       conn.setRequestProperty("content-type", "text/json");  
	       conn.setRequestProperty("Proxy-Connection", "Keep-Alive");  
	       conn.setDoInput(true);  
	       conn.setDoOutput(true);
	       
	       in = conn.getInputStream();
	       outStream = new ByteArrayOutputStream();  
	       byte[] respData = new byte[512]; 
	       int count = -1;
	       while((count = in.read(respData,0,512)) != -1)  
	           outStream.write(respData, 0, count);     
	       return new String(outStream.toByteArray(),"utf-8");
	   }catch(Exception ex){  
		   log.error("查询googleplay商品数据失败!", ex);
	   }finally{
		   if(in != null){
			   try {
				in.close();
			   } catch (IOException e) {
					log.error("查询googleplay商品数据 inputstream.close failure!", e);
			   }
		   }
		   if(outStream != null){
			   try {
				outStream.close();
			   } catch (IOException e) {
				log.error("查询googleplay商品数据 inputstream.close failure!", e);
			   }			   
		   }
	   } 
	   return null;  
	}
}
