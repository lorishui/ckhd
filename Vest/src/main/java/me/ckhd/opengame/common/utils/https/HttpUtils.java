package me.ckhd.opengame.common.utils.https;

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
	
	public static void main(String[] args) {
		String url = "https://sms.aliyuncs.com/";
		String data = "Signature=ka8PDlV7S9sYqxEMRnmlBv%2FDoAE%3D&AccessKeyId=testid&Action=SingleSendSms&Format=XML&ParamString={\"name\":\"d\",\"name1\":\"d\"}&RecNum=13098765432&RegionId=cn-hangzhou&SignName=标签测试&SignatureMethod=HMAC-SHA1&SignatureNonce=9e030f6b-03a2-40f0-a6ba-157d44532fd0&SignatureVersion=1.0&TemplateCode=SMS_1650053&Timestamp=2016-10-20T05:37:52Z&Version=2016-09-27";
		String respData = HttpUtils.post(url, data,"application/x-www-form-urlencoded" ,"utf-8");
		System.out.println(respData);
	}
}
