package me.ckhd.opengame.online.handle.common.wft;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpUtils {
	static Logger log = LoggerFactory.getLogger(HttpUtils.class);
	public static String post(String url,String data,String charset){
        org.apache.http.client.HttpClient client = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entityParams = new StringEntity(data,"utf-8");
            httpPost.setEntity(entityParams);
            //httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = new SSLClient();
            HttpResponse response = client.execute(httpPost);
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        }catch(Exception e){
        	log.error("post error!", e);
        }
        return result;
	}
	
	public static String post(String url,String data,String contentType,String charset){
        org.apache.http.client.HttpClient client = null;
        String result = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entityParams = new StringEntity(data,"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", contentType+";charset="+charset);
            client = new SSLClient();
            HttpResponse response = client.execute(httpPost);
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        }catch(Exception e){
        	log.error("post error!", e);
        }
        return result;
	}
	
	/*public static void main(String[] args) {
		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		String data = "<xml><sign>CF84C0AB9CFCD3DA1FBA4CD73F82EC30</sign><mch_id>1452430102</mch_id><body>60元宝</body><total_fee>600</total_fee><spbill_create_ip>119.130.231.62</spbill_create_ip><notify_url>http://ol.haifurong.cn/netpay/callback/weixin/1.1.0/</notify_url><appid>wx7fee0dd0a1f6675e</appid><out_trade_no>ya1703270031a677</out_trade_no><nonce_str>alW3gtIaPw9Ignq9</nonce_str><trade_type>APP</trade_type></xml>";
		String respData = HttpUtils.post(url, data ,"utf-8");
		System.out.println(respData);
	}*/
}
