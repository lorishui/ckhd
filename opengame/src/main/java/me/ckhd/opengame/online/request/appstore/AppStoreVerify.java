package me.ckhd.opengame.online.request.appstore;

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
import me.ckhd.opengame.online.request.appstore.BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier;

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
			map.put("status", 2);//请求验证失败
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
           ex.printStackTrace();  
       }  
       return null;  
    } 
    
    public static void main(String[] args) {
    	String url = "https://buy.itunes.apple.com/verifyReceipt";
    	String str =     "ewoJInNpZ25hdHVyZSIgPSAiQTRXVUhjV2dlRmw1eVpWWk40ejZKVFNzQlh3UmZLeGtkZEZxa05yc2x5ZVVKMUg2ZmtKQUJ3ZlNQMjhtaVZMdzh1MmJoYld5a2NrYklQY2hJVUtQZEdVSlpOcnRnbXk5TEd3TnRpcXBML1JGNnVnWENIMS9jK24xSll2M3JndEdlUno3b0EzQjNRNW5mQ2pWOHFEcVV6Sk1nREJ6YnJVRHVnOE9uS2RpaFp0VDVlM1g3RGV1alZhY0tmcS9wVmdEUTNqTnhoMnRkazdtNzZnZjZlNzk2R3R2MDNvZWJHZkdiN3pOUDB5aWR6K1JTOFBoWlhITFBtVlN0WXVnc2kzOThabFk0K0xpMzFGT25LMkZ1MzVIWWdxNVgrMFVVSnEvbG9DakJRcW1oZmFvYndrME9UcWFmZG01RWIvbXBkYklUZWIyODBZbzdkendZKzBJaHBqWVM4a0FBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTNMVEEwTFRFMUlESXhPakl5T2pFMUlFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRiWE1pSUQwZ0lqRTBPVEl6TVRZMU16VTJNamNpT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0prWVRVM01EZ3lPRGswWldGak5qSTRNamhtT0dKbU56VTVOalU1WVdRd01HUmhPV1kxWm1OaUlqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTWpFd01EQXdNelE0T0RBek9EUXhJanNLQ1NKaWRuSnpJaUE5SUNJeE1qVWlPd29KSW1Gd2NDMXBkR1Z0TFdsa0lpQTlJQ0l4TVRreU5qYzFOVFkwSWpzS0NTSjBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNakV3TURBd016UTRPREF6T0RReElqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UUTVNak14TmpVek5UWXlOeUk3Q2draWRXNXBjWFZsTFhabGJtUnZjaTFwWkdWdWRHbG1hV1Z5SWlBOUlDSTVNVFV5TlRVNVJDMUdPRU5FTFRRMVJVTXRPVUpFUmkxR01UY3hNRFZDTnpCQk56TWlPd29KSW1sMFpXMHRhV1FpSUQwZ0lqRXhPVEkyTnpVNE56Z2lPd29KSW5abGNuTnBiMjR0WlhoMFpYSnVZV3d0YVdSbGJuUnBabWxsY2lJZ1BTQWlPREl4TlRreU1EZzVJanNLQ1NKd2NtOWtkV04wTFdsa0lpQTlJQ0kyTUhsaUlqc0tDU0p3ZFhKamFHRnpaUzFrWVhSbElpQTlJQ0l5TURFM0xUQTBMVEUySURBME9qSXlPakUxSUVWMFl5OUhUVlFpT3dvSkltOXlhV2RwYm1Gc0xYQjFjbU5vWVhObExXUmhkR1VpSUQwZ0lqSXdNVGN0TURRdE1UWWdNRFE2TWpJNk1UVWdSWFJqTDBkTlZDSTdDZ2tpWW1sa0lpQTlJQ0pqYjIwdWVHMXdZVzk1YjNVdWFuZG5iSGt4SWpzS0NTSndkWEpqYUdGelpTMWtZWFJsTFhCemRDSWdQU0FpTWpBeE55MHdOQzB4TlNBeU1Ub3lNam94TlNCQmJXVnlhV05oTDB4dmMxOUJibWRsYkdWeklqc0tmUT09IjsKCSJwb2QiID0gIjIxIjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=";
//    	String receipt = "ewoJInNpZ25hdHVyZSIgPSAiQTRNb3FkQ1d1T1JHajZVRUovSHFIdkl5dlhleFhWOGVLUElJK1JoWmxOQjdLd1hsbm9BV05mZGh1SStVaDNaM0dVeUltRFNHQTVqVWpRbS9MaUFHQ0tldHpmQVNkdzBGeTQ4QWhaSzMyOE4vcGFXVUdOYWt0dkZIaVhiVWJydUhQRzVyTU0yU0JFZU9sQU1sT1FWT0FpaEFHTDFDOFV2L2k0anRaWVFXVXRmYzU1ZWdVWllaRExwcndGcnRPOUN2TUM0aU9LK0t1TkRDK3BGbGVaZWxNZ3A5cmFQeVVTVWVXbURheHM1V0R2bjJ3eUtxWWxtRDlKeHByUjVGZjV1MjBJN0VmUXZEcFdYMGYxQWZyNEdtS09oRjJiRnRLd2d6c0VkdGVJMTMybkhRS1NwcjhjdklCRUNua0QzQWIwTm9QU1JCTjJLbXRWUmhvSURlTWZvUGJyVUFBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTNMVEEwTFRFM0lEQTNPakUzT2pJeUlFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRiWE1pSUQwZ0lqRTBPVEkwTXpnMk5ESXdNRGNpT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0kzTkRVM01ESXlPVFU0TUdObFkyWXdPV0UzTVRGak9ERmtZekl4TTJVeE5UYzVaak0yT1RSa0lqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTXpBd01EQXdNVGswTWpjMk1qTTFJanNLQ1NKaWRuSnpJaUE5SUNJeE1qVWlPd29KSW1Gd2NDMXBkR1Z0TFdsa0lpQTlJQ0l4TVRreU5qYzFOVFkwSWpzS0NTSjBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNekF3TURBd01UazBNamMyTWpNMUlqc0tDU0p4ZFdGdWRHbDBlU0lnUFNBaU1TSTdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTMXRjeUlnUFNBaU1UUTVNalF6T0RZME1qQXdOeUk3Q2draWRXNXBjWFZsTFhabGJtUnZjaTFwWkdWdWRHbG1hV1Z5SWlBOUlDSTJRakUxTnpCR01TMDVNelpCTFRSQk5FVXRRVU0yTWkwMk9FRTJRMEl5TTBVNU1ESWlPd29KSW1sMFpXMHRhV1FpSUQwZ0lqRXhPVEkyTnpVNE9UUWlPd29KSW5abGNuTnBiMjR0WlhoMFpYSnVZV3d0YVdSbGJuUnBabWxsY2lJZ1BTQWlPREl4TlRreU1EZzVJanNLQ1NKd2NtOWtkV04wTFdsa0lpQTlJQ0kyTkRnd2VXSWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVWlJRDBnSWpJd01UY3RNRFF0TVRjZ01UUTZNVGM2TWpJZ1JYUmpMMGROVkNJN0Nna2liM0pwWjJsdVlXd3RjSFZ5WTJoaGMyVXRaR0YwWlNJZ1BTQWlNakF4Tnkwd05DMHhOeUF4TkRveE56b3lNaUJGZEdNdlIwMVVJanNLQ1NKaWFXUWlJRDBnSW1OdmJTNTRiWEJoYjNsdmRTNXFkMmRzZVRFaU93b0pJbkIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTNMVEEwTFRFM0lEQTNPakUzT2pJeUlFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd3A5IjsKCSJwb2QiID0gIjMwIjsKCSJzaWduaW5nLXN0YXR1cyIgPSAiMCI7Cn0=";
    	System.out.println(buyAppVerify(str, url));
    	//CompletionReceipt
	}
}
