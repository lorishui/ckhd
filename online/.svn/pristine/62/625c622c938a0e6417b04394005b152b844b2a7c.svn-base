package me.ckhd.opengame.online.handle.common.appstore;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.handle.common.appstore.BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.nearme.oauth.net.TrustAnyTrustManager;

public class AppStoreVerify {
	static Logger log = LoggerFactory.getLogger(AppStoreVerify.class);
	private static final String url_sandbox="https://sandbox.itunes.apple.com/verifyReceipt"; //沙盒测试
	private static final String url_verify="https://buy.itunes.apple.com/verifyReceipt";//正式地址
	 
	public static void getIosResponseData(Map<String,Object> map){
		String url = url_verify;
		//苹果客户端传上来的数据,是最原始的加密数据
		String encData = map.get("receipt")!= null?map.get("receipt").toString():"";
		int n = 0;
		log.info("appstore encData="+encData);
		String data = postThree(encData, url,n);
		log.info("appstore responseData="+data);
		if(data==null){  
             //苹果服务器没有返回验证结果  
			map.put("status", 2);//请求验证异常
        }else{
        	//跟苹果验证有返回结果------------------  
            JSONObject json = JSONObject.parseObject(data);
            String states=json.getString("status"); 
            if(states.equals("0")){//验证成功  
            	JSONObject receiptJson = JSONObject.parseObject(json.getString("receipt"));   	
            	String transaction_id = receiptJson.getString("transaction_id");
            	String product_id = receiptJson.getString("product_id");
            	String item_id = receiptJson.getString("item_id");
            	if(!product_id.equals(map.get("productId").toString())){
            		map.put("status", -3);//计费点不对应
            	}else{
//            		long time = Long.parseLong(receiptJson.get("original_purchase_date_ms").toString());
            		map.put("status", 0);
            		/*Date date1 = DateUtils.beforeNumDate(3);
            		if(time >= date1.getTime()){
            			map.put("status", 0);
            		}else{
            			map.put("status", -2);//超过验证时间
            		}*/
            	}
            	map.put("transaction_id", transaction_id.toString());
            	map.put("item_id", item_id);
            }else if(states.equals("21007")){//切换到沙盒
            	url = url_sandbox;
            	map.put("syc", "1");
            	encData = map.get("receipt")!= null?map.get("receipt").toString():"";
            	int m = 0;
        		log.info("appstore encData="+encData);
        		data = postThree(encData, url,m);
        		log.info("appstore responseData="+data);
        		//跟苹果验证有返回结果------------------  
                json = JSONObject.parseObject(data);
                states=json.getString("status"); 
                if(states.equals("0")){
                	JSONObject receiptJson = JSONObject.parseObject(json.getString("receipt"));   	
                	String transaction_id = receiptJson.getString("transaction_id");
                	String product_id = receiptJson.getString("product_id");
                	String item_id = receiptJson.getString("item_id");
                	String purchase_date_ms = receiptJson.getString("purchase_date_ms");
                	if(!product_id.equals(map.get("productId").toString())){
                		map.put("status", -3);
                	}else{
                		map.put("status", 0);
                		/*long time = receiptJson.getLong("original_purchase_date_ms");
                		Date date1 = DateUtils.beforeNumDate(3);
                		log.info("3天时间前，time="+date1.getTime());
                		if(time >= date1.getTime()){
                			map.put("status", 0);
                		}else{
                			map.put("status", -2);//超过验证时间
                		}*/
                	}
                	map.put("transaction_id", transaction_id.toString());
                	map.put("purchase_date_ms", purchase_date_ms);
                	map.put("item_id", item_id);
                }else{
                	map.put("status", 1);//苹果code不为零
                }
            }else{
            	map.put("status", 1);//苹果code不为零
            }
            map.put("receipt-data", data);
        } 
	}
	
	public static String postThree(String encData,String url,int n){
		String data = null;
		if( n <= 3){
			n++;
			data = buyAppVerify(encData, url);
			if(StringUtils.isNotBlank(data)){
				return data;
			}else{
				return postThree(encData, url,n);
			}
		}
		return data;
	}
	/** 
     * 苹果服务器验证 
     * @param receipt 账单 
     * @url 要验证的地址 
     * @return null 或返回结果 
     * 沙盒   https://sandbox.itunes.apple.com/verifyReceipt 
     *  
     */  
    public static String buyAppVerify(String receipt,String url){     
       log.info("appstore data:"+receipt);
       log.info("appstore url:"+url);
       try{  
           SSLContext sc = SSLContext.getInstance("SSL");  
           sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());  
           URL console = new URL(url);  
           HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();  
           conn.setSSLSocketFactory(sc.getSocketFactory());  
           conn.setHostnameVerifier(new TrustAnyHostnameVerifier());  
           conn.setRequestMethod("POST");  
           conn.setRequestProperty("content-type", "text/json");  
           conn.setRequestProperty("Proxy-Connection", "Keep-Alive");  
           conn.setDoInput(true);  
           conn.setDoOutput(true);  
           BufferedOutputStream hurlBufOus=new BufferedOutputStream(conn.getOutputStream());  
             
           String str= String.format(Locale.CHINA,"{\"receipt-data\":\"" + receipt+"\"}");  
           hurlBufOus.write(str.getBytes());  
           hurlBufOus.flush();  
           InputStream is = conn.getInputStream();  
	       BufferedReader reader=new BufferedReader(new InputStreamReader(is));  
	       String line = null;  
	       StringBuffer sb = new StringBuffer();  
	       while((line = reader.readLine()) != null){  
	          sb.append(line);  
	       }
	       return sb.toString();  
       }catch(Exception ex){  
    	   log.error("appstore http throws exception!", ex);  
       }  
       return null;  
    } 
    
    public static void main(String[] args) {
    	String url = "https://sandbox.itunes.apple.com/verifyReceipt";
    	String receipt = "ewogICAgcG9kID0gNTA7CiAgICAicHVyY2hhc2UtaW5mbyIgPSAiZXdvSkltSjJjbk1pSUQwZ0lqRXpOQ0k3Q2draVlYQndMV2wwWlcwdGFXUWlJRDBnSWpFeE9USTJOelUxTmpRaU93b0pJbUpwWkNJZ1BTQWlZMjl0TG5odGNHRnZlVzkxTG1wM1oyeDVNU0k3Q2draWNIVnlZMmhoYzJVdFpHRjBaU0lnUFNBaU1qQXhOeTB3TlMwd055QXdNVG8xTWpvME1pQkZkR012UjAxVUlqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpZG1WeWMybHZiaTFsZUhSbGNtNWhiQzFwWkdWdWRHbG1hV1Z5SWlBOUlDSTRNakU1TlRFd01ETWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREUzTFRBMUxUQTJJREU0T2pVeU9qUXlJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluQjFjbU5vWVhObExXUmhkR1V0YlhNaUlEMGdJakUwT1RReE1qRTVOakl6TXpRaU93b0pJblZ1YVhGMVpTMTJaVzVrYjNJdGFXUmxiblJwWm1sbGNpSWdQU0FpTURjd1JqTXdRelF0TVRsQk5DMDBPRGszTFVKQ1JqRXRRVFZET1VZMU0wUTNORGMxSWpzS0NTSnBkR1Z0TFdsa0lpQTlJQ0kyTlRjeE5qSTRNakFpT3dvSkltOXlhV2RwYm1Gc0xYQjFjbU5vWVhObExXUmhkR1V0YlhNaUlEMGdJakUwT1RReE1qRTVOakl6TXpRaU93b0pJbTl5YVdkcGJtRnNMWFJ5WVc1ellXTjBhVzl1TFdsa0lpQTlJQ0kyTkRnd2VXSXhORGswTVRJeE9UWXlNekk1SWpzS0NTSjFibWx4ZFdVdGFXUmxiblJwWm1sbGNpSWdQU0FpTVRJMlpURmlOREV6WkRBM05USm1ORFE0WW1GaU1qWTROVFkzWmprME1UQm1ZV1ZqWmpVMlpDSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTSWdQU0FpTWpBeE55MHdOUzB3TnlBd01UbzFNam8wTWlCRmRHTXZSMDFVSWpzS0NTSndjbTlrZFdOMExXbGtJaUE5SUNJMk5EZ3dlV0lpT3dvSkluUnlZVzV6WVdOMGFXOXVMV2xrSWlBOUlDSTJORGd3ZVdJeE5EazBNVEl4T1RZeU16STVJanNLQ1NKdmNtbG5hVzVoYkMxd2RYSmphR0Z6WlMxa1lYUmxMWEJ6ZENJZ1BTQWlNakF4Tnkwd05TMHdOaUF4T0RvMU1qbzBNaUJCYldWeWFXTmhMMHh2YzE5QmJtZGxiR1Z6SWpzS2ZRPT0iOwogICAgc2lnbmF0dXJlID0gIkFwZHhKZHROd1BVMnJBNS9jbjNrSU8xT1RrMjVmZURLYTBhYWd5eVJ2ZVdsY0ZsZ2x2NlJGNnpua2lCUzN1bTlVYzdwVm9iK1BxWlIyVDh3eVZySE5wbG9mM0RYM0lxRE9sV3ErOTBhN1lsK3FyUjdBN2pXd3ZpdzcwOFBTKzY3UHlIUm5oTy9HN2JWcWdScEVyNkV1RnliaVUxRlhBaVhKYzZsczFZQXNzUXhBQUFEVnpDQ0ExTXdnZ0k3b0FNQ0FRSUNDR1VVa1UzWldBUzFNQTBHQ1NxR1NJYjNEUUVCQlFVQU1IOHhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1TWXdKQVlEVlFRTERCMUJjSEJzWlNCRFpYSjBhV1pwWTJGMGFXOXVJRUYxZEdodmNtbDBlVEV6TURFR0ExVUVBd3dxUVhCd2JHVWdhVlIxYm1WeklGTjBiM0psSUVObGNuUnBabWxqWVhScGIyNGdRWFYwYUc5eWFYUjVNQjRYRFRBNU1EWXhOVEl5TURVMU5sb1hEVEUwTURZeE5ESXlNRFUxTmxvd1pERWpNQ0VHQTFVRUF3d2FVSFZ5WTJoaGMyVlNaV05sYVhCMFEyVnlkR2xtYVdOaGRHVXhHekFaQmdOVkJBc01Fa0Z3Y0d4bElHbFVkVzVsY3lCVGRHOXlaVEVUTUJFR0ExVUVDZ3dLUVhCd2JHVWdTVzVqTGpFTE1Ba0dBMVVFQmhNQ1ZWTXdnWjh3RFFZSktvWklodmNOQVFFQkJRQURnWTBBTUlHSkFvR0JBTXJSakYyY3Q0SXJTZGlUQ2hhSTBnOHB3di9jbUhzOHAvUndWL3J0LzkxWEtWaE5sNFhJQmltS2pRUU5mZ0hzRHM2eWp1KytEcktKRTd1S3NwaE1kZEtZZkZFNXJHWHNBZEJFakJ3Ukl4ZXhUZXZ4M0hMRUZHQXQxbW9LeDUwOWRoeHRpSWREZ0p2MllhVnM0OUIwdUp2TmR5NlNNcU5OTEhzREx6RFM5b1pIQWdNQkFBR2pjakJ3TUF3R0ExVWRFd0VCL3dRQ01BQXdId1lEVlIwakJCZ3dGb0FVTmgzbzRwMkMwZ0VZdFRKckR0ZERDNUZZUXpvd0RnWURWUjBQQVFIL0JBUURBZ2VBTUIwR0ExVWREZ1FXQkJTcGc0UHlHVWpGUGhKWENCVE16YU4rbVY4azlUQVFCZ29xaGtpRzkyTmtCZ1VCQkFJRkFEQU5CZ2txaGtpRzl3MEJBUVVGQUFPQ0FRRUFFYVNiUGp0bU40Qy9JQjNRRXBLMzJSeGFjQ0RYZFZYQWVWUmVTNUZhWnhjK3Q4OHBRUDkzQmlBeHZkVy8zZVRTTUdZNUZiZUFZTDNldHFQNWdtOHdyRm9qWDBpa3lWUlN0USsvQVEwS0VqdHFCMDdrTHM5UVVlOGN6UjhVR2ZkTTFFdW1WL1VndkRkNE53Tll4TFFNZzRXVFFmZ2tRUVZ5OEdYWndWSGdiRS9VQzZZNzA1M3BHWEJrNTFOUE0zd294aGQzZ1NSTHZYaitsb0hzU3RjVEVxZTlwQkRwbUc1K3NrNHR3K0dLM0dNZUVONS8rZTFRVDlucC9LbDFuaithQnc3QzB4c3kwYkZuYUFkMWNTUzZ4ZG9yeS9DVXZNNmd0S3Ntbk9PZHFUZXNicDBiczhzbjZXcXMwQzlkZ2N4Ukh1T01aMnRtOG5wTFVtN2FyZ09TelE9PSI7CiAgICAic2lnbmluZy1zdGF0dXMiID0gMDsKfQ==";
    	System.out.println(buyAppVerify(receipt, url));
    	//CompletionReceipt
	}
}
