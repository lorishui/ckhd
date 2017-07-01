/*
 */
package me.ckhd.opengame.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.alibaba.fastjson.JSONObject;

public class HttpPostUtils {

	void testPost(String urlStr) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("POST");// POSTSS
//			con.addRequestProperty("Content-Type", "application/xml");
			con.setRequestProperty("Accept", "*/*;charset=UTF-8");
//			
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
//			con.setRequestProperty("Content-Type", "text/xml");
			con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			
			out = new OutputStreamWriter(con.getOutputStream());
			String xmlInfo = getXmlInfo();
			System.out.println("urlStr=" + urlStr);
			System.out.println("xmlInfo=" + xmlInfo);
			out.write(new String(xmlInfo.getBytes("UTF-8")));
			out.flush();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			for (line = in.readLine(); line != null; line = in.readLine()) {
				System.out.println(line);
			}
			/*byte[] buff = new byte[1024];  
	        //从字符串获取字节写入流  
	        InputStream is = con.getInputStream();  
	        int len = -1;  
	        while(-1 != (len = is.read(buff))) {  
	            //将字节数组转换为字符串  
	            String res = new String(buff, 0, len,"gb2312");   
	            System.out.println(res);
	            res = new String(buff, 0, len,"gbk");
	            System.out.println(res);
	            res = new String(buff, 0, len,"utf-8");
	            System.out.println(res);
	            res = new String(buff, 0, len,"iso-8859-1");
	            System.out.println(res);
	        }  */
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private String getXmlInfo() {
		
		String xml4mm =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
						"<SyncAppOrderReq xmlns=\"http://www.monternet.com/dsmp/schemas/\">"+
						"<TransactionID>CSSP16122856</TransactionID>"+
						"<MsgType>SyncAppOrderReq</MsgType>"+
						"<Version>1.0.0</Version>"+
						"<Send_Address>"+
						"<DeviceType>200</DeviceType>"+
						"<DeviceID>CSSP</DeviceID>"+
						"</Send_Address>"+
						"<Dest_Address>"+
						"<DeviceType>400</DeviceType>"+
						"<DeviceID>SPSYN</DeviceID>"+
						"</Dest_Address>"+
						"<OrderID>11130619144434998192</OrderID>"+
						"<CheckID>0</CheckID>"+
						"<ActionTime>20130619144435</ActionTime>"+
						"<ActionID>1</ActionID>"+
						"<MSISDN></MSISDN>"+
						"<FeeMSISDN>ECAD2EVFADF3AE2A</FeeMSISDN>"+
						"<AppID>300001489326</AppID>"+
						"<PayCode>30000148932601</PayCode>"+
						"<TradeID>L0IF7AF2J4L5IF1B</TradeID>"+
						"<Price>100</Price>"+
						"<TotalPrice>100</TotalPrice>"+
						"<SubsNumb>1</SubsNumb>"+
						"<SubsSeq>1</SubsSeq>"+
						"<ChannelID>2000000032</ChannelID>"+
						"<ExData>中文</ExData>"+
						"<OrderType>1</OrderType>"+
						"<MD5Sign>ABCDEFGHIKDFIEJFLAKDJFSIDF</MD5Sign>"+
						"</SyncAppOrderReq>";
		String xml4and = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		"<request> " +
		"<userId>xxx</userId>" + 
		"<contentId>000000000000</contentId>" + 
		"<consumeCode>000000000000</consumeCode>" + 
		"<cpid>701010</cpid>" + 
		"<hRet>0</hRet>" + 
		"<status>1800</status>" + 
		"<versionId>xxx</versionId>" + 
		"<cpparam>xxx</cpparam>" + 
		"<packageID></packageID>" + 
		"</request>";
		
		xml4and = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>"
				+ "<sid>62088992200003386</sid>"
				+ "<cid>6208899</cid>"
				+ "<price>5.0</price>"
				+ "<imsi>11160324120245722762</imsi>"
				+ "<feetype>0</feetype>"
				+ "<paycode>30000918389410</paycode>"
				+ "<hRet>1</hRet>"
				+ "<resulttime>2016-03-24 12:02:51</resulttime>"
				+ "<uniqueid>B243BE191C0431FC312B67AB18228CC2</uniqueid>"
				+ "<cpparam>2516032400082017</cpparam>"
				+ "<channelid>62088992200003386</channelid>"
				+ "</request>";
		
		JSONObject json = new JSONObject();
		json.put("ckAppId", "1025");
		json.put("payType", "124");
		json.put("version", "1.0.0");
		JSONObject json1 = new JSONObject();
		json1.put("accNo", "6216261000000000018");
		json.put("verifyInfo", json1);
		json.put("price", 100);
		json.put("userId", "111222");
		json.put("gameOnline", 1);
		json.put("ckChannelId", "11");
		json.put("productId", "1");
		System.out.println(json.toJSONString());
		
		JSONObject app = new JSONObject();
		String value = "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";
		app.put("receipt", value);
		app.put("orderId", "0516040600058276");
		app.put("total", "100");
		
		
		JSONObject pay = new JSONObject();
		pay.put("ckAppId", "1005");
		pay.put("ckChannelId", "130");
		pay.put("productId", "vip8");
		pay.put("payType", 130);
		pay.put("userId","212343");
		/*onlinePay.setCkAppId(map.get("ckAppId")==null?"":map.get("ckAppId").toString());
		onlinePay.setAppId(map.get("mmAppId")==null?"":map.get("mmAppId").toString());
		onlinePay.setVersion(map.get("version")==null?"":map.get("version").toString());
		onlinePay.setPayType(map.get("payType")==null?"":map.get("payType").toString());
		onlinePay.setExtension(map.get("extension")==null?"":map.get("extension").toString());
		onlinePay.setGameOnline(map.get("gameOnline")==null?0:Integer.valueOf(map.get("gameOnline").toString()));
		onlinePay.setPrices(map.get("price")==null?0:Integer.valueOf(map.get("price").toString()));
		onlinePay.setUserId(map.get("userId")==null?null:map.get("userId").toString());
		onlinePay.setProductId(map.get("productId")==null?"0":map.get("productId").toString());
		onlinePay.setAppPayContent(map.get("verifyInfo")==null?"":JSONObject.toJSONString(map.get("verifyInfo")));
		onlinePay.setSdkType(map.get("sdkType")==null?"":map.get("sdkType").toString());*/
//		String data = "{\"ckAppId\":\"1029\",\"verifyInfo\":{\"sid\":\"ssh1game46fdd5400f5d4a159790a11abc1df183179635\"},\"act\":\"1001\",\"ckChannelId\":\"16\",\"version\":\"10001\"}";
		String data = "{\"sign\":\"3360a54542a472c8ef97024c36ef18e0\",\"data\":{\"failedDesc\":\"\",\"amount\":\"0.01\",\"callbackInfo\":\"\",\"cpOrderId\":\"2916040700011812\",\"accountId\":\"61607dba3cfe8022d2d9013edddbfc68\",\"gameId\":\"724445\",\"payWay\":\"201\",\"orderStatus\":\"S\",\"orderId\":\"201604071600478400179\",\"creator\":\"JY\"},\"ver\":\"2.0\"}";
		String appData = "MIIaGwYJKoZIhvcNAQcCoIIaDDCCGggCAQExCzAJBgUrDgMCGgUAMIIJvAYJKoZIhvcNAQcBoIIJrQSCCakxggmlMAoCAQgCAQEEAhYAMAoC"+
				"ARQCAQEEAgwAMAsCAQECAQEEAwIBADALAgELAgEBBAMCAQAwCwIBDgIBAQQDAgFSMAsCAQ8CAQEEAwIBADALAgEQAgEBBAMCAQAwCwIBGQIBAQQDAgEDMAwC"+
				"AQoCAQEEBBYCNCswDQIBDQIBAQQFAgMBOOcwDQIBEwIBAQQFDAMxLjAwDgIBCQIBAQQGAgRQMjQ0MA8CAQMCAQEEBwwFMS4wLjEwGAIBAgIBAQQQDA5jb20u"+
				"Y2toZC5ja3BheTAYAgEEAgECBBDFOtLlYSeR1yE9nFVZElrKMBsCAQACAQEEEwwRUHJvZHVjdGlvblNhbmRib3gwHAIBBQIBAQQU/Y3/JYny4aYpq7L9oaI7"+
				"26/nj6IwHgIBDAIBAQQWFhQyMDE2LTA0LTE0VDEwOjIzOjI5WjAeAgESAgEBBBYWFDIwMTMtMDgtMDFUMDc6MDA6MDBaMC8CAQYCAQEEJ5qtujz6Tmq+9AQ4"+
				"quAO016aCgQkZV4Aovs8Qup5876bRqzeueeTPDBFAgEHAgEBBD2cow8ZhlK47QBo14PlO4GcU9+hv/GtZc/JFHacWRkJa49fjH26oqNyOL1yrhmktfkeexpJ"+
				"om1Qkl/l9cz4MIIBSAIBEQIBAQSCAT4xggE6MAsCAgasAgEBBAIWADALAgIGrQIBAQQCDAAwCwICBrACAQEEAhYAMAsCAgayAgEBBAIMADALAgIGswIBAQQC"+
				"DAAwCwICBrQCAQEEAgwAMAsCAga1AgEBBAIMADALAgIGtgIBAQQCDAAwDAICBqUCAQEEAwIBATAMAgIGqwIBAQQDAgEBMAwCAgauAgEBBAMCAQAwDAICBq8C"+
				"AQEEAwIBADAMAgIGsQIBAQQDAgEAMA4CAgamAgEBBAUMA2hoZDAbAgIGpwIBAQQSDBAxMDAwMDAwMjA1NzE3NjM0MBsCAgapAgEBBBIMEDEwMDAwMDAyMDU3"+
				"MTc2MzQwHwICBqgCAQEEFhYUMjAxNi0wNC0xNFQwOToxNzowNlowHwICBqoCAQEEFhYUMjAxNi0wNC0xNFQwOToxNzowNlowggFIAgERAgEBBIIBPjGCATow"+
				"CwICBqwCAQEEAhYAMAsCAgatAgEBBAIMADALAgIGsAIBAQQCFgAwCwICBrICAQEEAgwAMAsCAgazAgEBBAIMADALAgIGtAIBAQQCDAAwCwICBrUCAQEEAgwA"+
				"MAsCAga2AgEBBAIMADAMAgIGpQIBAQQDAgEBMAwCAgarAgEBBAMCAQEwDAICBq4CAQEEAwIBADAMAgIGrwIBAQQDAgEAMAwCAgaxAgEBBAMCAQAwDgICBqYC"+
				"AQEEBQwDaHhkMBsCAganAgEBBBIMEDEwMDAwMDAyMDUwMjgzMDQwGwICBqkCAQEEEgwQMTAwMDAwMDIwNTAyODMwNDAfAgIGqAIBAQQWFhQyMDE2LTA0LTEy"+
				"VDExOjI2OjE5WjAfAgIGqgIBAQQWFhQyMDE2LTA0LTEyVDExOjI2OjE5WjCCAUgCARECAQEEggE+MYIBOjALAgIGrAIBAQQCFgAwCwICBq0CAQEEAgwAMAsC"+
				"AgawAgEBBAIWADALAgIGsgIBAQQCDAAwCwICBrMCAQEEAgwAMAsCAga0AgEBBAIMADALAgIGtQIBAQQCDAAwCwICBrYCAQEEAgwAMAwCAgalAgEBBAMCAQEw"+
				"DAICBqsCAQEEAwIBATAMAgIGrgIBAQQDAgEAMAwCAgavAgEBBAMCAQAwDAICBrECAQEEAwIBADAOAgIGpgIBAQQFDANoeGQwGwICBqcCAQEEEgwQMTAwMDAw"+
				"MDIwNTMxNTQ1NjAbAgIGqQIBAQQSDBAxMDAwMDAwMjA1MzE1NDU2MB8CAgaoAgEBBBYWFDIwMTYtMDQtMTNUMTE6MjM6NDlaMB8CAgaqAgEBBBYWFDIwMTYt"+
				"MDQtMTNUMTE6MjM6NDlaMIIBSQIBEQIBAQSCAT8xggE7MAsCAgasAgEBBAIWADALAgIGrQIBAQQCDAAwCwICBrACAQEEAhYAMAsCAgayAgEBBAIMADALAgIG"+
				"swIBAQQCDAAwCwICBrQCAQEEAgwAMAsCAga1AgEBBAIMADALAgIGtgIBAQQCDAAwDAICBqUCAQEEAwIBATAMAgIGqwIBAQQDAgEAMAwCAgauAgEBBAMCAQAw"+
				"DAICBq8CAQEEAwIBADAMAgIGsQIBAQQDAgEAMA8CAgamAgEBBAYMBHZpcDgwGwICBqcCAQEEEgwQMTAwMDAwMDIwNTc1MTM1MjAbAgIGqQIBAQQSDBAxMDAw"+
				"MDAwMjA1NzUxMzUyMB8CAgaoAgEBBBYWFDIwMTYtMDQtMTRUMDk6NTY6MjVaMB8CAgaqAgEBBBYWFDIwMTYtMDQtMTRUMDk6NTY6MjVaMIIBSQIBEQIBAQSC"+
				"AT8xggE7MAsCAgasAgEBBAIWADALAgIGrQIBAQQCDAAwCwICBrACAQEEAhYAMAsCAgayAgEBBAIMADALAgIGswIBAQQCDAAwCwICBrQCAQEEAgwAMAsCAga1"+
				"AgEBBAIMADALAgIGtgIBAQQCDAAwDAICBqUCAQEEAwIBATAMAgIGqwIBAQQDAgEBMAwCAgauAgEBBAMCAQAwDAICBq8CAQEEAwIBADAMAgIGsQIBAQQDAgEA"+
				"MA8CAgamAgEBBAYMBDIwenowGwICBqcCAQEEEgwQMTAwMDAwMDIwNTgwMDQ0MzAbAgIGqQIBAQQSDBAxMDAwMDAwMjA1ODAwNDQzMB8CAgaoAgEBBBYWFDIw"+
				"MTYtMDQtMTRUMTA6MjM6MjlaMB8CAgaqAgEBBBYWFDIwMTYtMDQtMTRUMTA6MjM6MjlaMIIBSQIBEQIBAQSCAT8xggE7MAsCAgasAgEBBAIWADALAgIGrQIB"+
				"AQQCDAAwCwICBrACAQEEAhYAMAsCAgayAgEBBAIMADALAgIGswIBAQQCDAAwCwICBrQCAQEEAgwAMAsCAga1AgEBBAIMADALAgIGtgIBAQQCDAAwDAICBqUC"+
				"AQEEAwIBATAMAgIGqwIBAQQDAgEBMAwCAgauAgEBBAMCAQAwDAICBq8CAQEEAwIBADAMAgIGsQIBAQQDAgEAMA8CAgamAgEBBAYMBDUwenowGwICBqcCAQEE"+
				"EgwQMTAwMDAwMDIwNTc3NTI5NTAbAgIGqQIBAQQSDBAxMDAwMDAwMjA1Nzc1Mjk1MB8CAgaoAgEBBBYWFDIwMTYtMDQtMTRUMTA6MTA6NThaMB8CAgaqAgEB"+
				"BBYWFDIwMTYtMDQtMTRUMTA6MTA6NThaoIIOZTCCBXwwggRkoAMCAQICCA7rV4fnngmNMA0GCSqGSIb3DQEBBQUAMIGWMQswCQYDVQQGEwJVUzETMBEGA1UE"+
				"CgwKQXBwbGUgSW5jLjEsMCoGA1UECwwjQXBwbGUgV29ybGR3aWRlIERldmVsb3BlciBSZWxhdGlvbnMxRDBCBgNVBAMMO0FwcGxlIFdvcmxkd2lkZSBEZXZl"+
				"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";
		String appJson = "{\"total\":100,\"receipt\":\""+appData+"\",\"orderId\":\"0716041400007b74\"}";
		String dataMap = "ckappid=1029&channelid=13&uid=5cc3b89463403276&tradeType=01&respCode=200&tradeStatus=0000&appId=c3988130b9cb52ce6c3bb2856f4ba71e&payTime=20160421151138&cpOrderNumber=2916042100028138&cpId=20151113101410601305&signMethod=MD5&orderAmount=1&orderNumber=2016042115111150400014426052&respMsg=%E4%BA%A4%E6%98%93%E6%88%90%E5%8A%9F&signature=59f992708932d9ce99761352139d08e3";
		
		JSONObject jsonNew = new JSONObject();
		JSONObject user = new JSONObject();
		user.put("a", "ck53038441");
		user.put("b", "123");
		user.put("d", "2825289575@qq.com");
		user.put("f", 1);
		user.put("g", "123");
		JSONObject version = new JSONObject();
		version.put("a", "1.1.0");
		
		JSONObject verify = new JSONObject();
		verify.put("a", "456980");
		verify.put("b", 1);
		
		JSONObject session = new JSONObject();
		session.put("a", "4b43d9f945ba1302658bb340c5161233");
		session.put("b", 1462792343418L);
		
		jsonNew.put("user", user);
		jsonNew.put("version", version);
		jsonNew.put("verify", verify);
		jsonNew.put("session", session);
		/*{
  "version" : {
    "a" : "1.1.0"
  },
  "user" : {
    "a" : "ck53038441",
    "b" : "123",
    "c" : 0
  }
}*/
//		return jsonNew.toJSONString();
		/*JSONObject json111 = new JSONObject();
		json111.put("ckAppId", 1001);
		json111.put("versionName", "1.1.0");
		json111.put("rqType", "ad");*/
//		return json111.toJSONString();
		String dataNew = "user_info=test&uid=120235413&partner_id=117130980&notify_time=2016-05-11+15%3A17%3A06&product_id=1&buy_amount=0&sign_type=MD5&pay_time=1462948416000&order_id=16051121017335966&total_price=0.01&trade_status=3&product_unit=&sign=2ea31612983f186faf60019ed95c8c96&cp_order_id=2116051100006e12&pay_type=0&product_per_price=0.0&create_time=1462947228975&app_id=3078052&notify_id=1462951026517";
		String str = "uid=57892557&productCount=10&cpOrderId=2116042002892e34&appId=2882303761517420446&payFee=1000&productCode=01&productName=元宝&payTime=2016-04-20+16%3a16%3a13&orderStatus=TRADE_SUCCESS&signature=d80a5b8c30086903009c36aa75efab7e623f34be&orderId=21146114015373456506";
		str = "data=ciyaeE8pyArFLmg81wdeuIkBXLAFWY9K0bd28ccHIC6WcnmXTvbLuPcwZbBjyyHBIvvd7A8Lhkatt%2FQ0taREzebhQ0SqNw2hf9XRXiuvU1l%2BRdL9ppARSmB0i%2F6evWhqX59usSGf5BRzigmMUUyeTICCSMNR5a%2FjLxPgJexM14mh4igXfUKJ0tSzZzrOA49BK7jHaEhKmCHNDfP9L%2F2k68w7DCplB52FlxpiHReqjFI%3D";
		str = "{\"appleuser\":{\"ckAppId\":1001,\"version\":\"1.0.1\",\"deviceToken\":\"sfdsf\"}}";
		str = "{\"ckappid\" : \"1054\",\"receipt\" : \"ewoJInNpZ25hdHVyZSIgPSAiQTRRUUV6QytBRFB3UU5KejNCd0oyNkpFcnpiSTVBSEFVTjgzMzFSTmlrUWJiU0VqV1VCOGFIM0JRVWhkRWNXSkczajBBWVIrOXphbnR4TjNuUk5rdjZ6OC9sUTd6MFRNQ3d2Q2lKNDd0OUxPcnZjZTRiN01scTZCUTlBNFlObDRnNk9RMFZUb2Q2M0JtSDJVd1BkTjRoSGpCTEFENEtEeUxnQUlUQkxvOHFEYnI4RlIxbk13WlRUT0hrUWp2S3V0Y1V2WVlCMVZEV1RIZFNyTzhDVXk5TzRFZEtLZGRVZFRncXRqQmcyTVE1UC9JOU8rWlpQOTNVdHZXTTVjcjNzNjFNNGo1ZWRiQ2RTS2hGWW9YR21lVVlGMlZXQXBNNlZrN2xsMi9tSlZXMDFpbjNXajcxcXZRMjFqK0g1blkzN1hLeklnbW5sOXdnTkI5cm5wNTZKVVFyZ0FBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTJMVEEzTFRJd0lESXdPalEzT2pVMklFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5WdWFYRjFaUzFwWkdWdWRHbG1hV1Z5SWlBOUlDSXlOR1V6WWpoalpXUmhOR0l3TURRMU16TXpZakE0WVRaaFptUTBOamxtTWpjd09UWTFOek5tSWpzS0NTSnZjbWxuYVc1aGJDMTBjbUZ1YzJGamRHbHZiaTFwWkNJZ1BTQWlNVEF3TURBd01ESXlORGczTmpFM09DSTdDZ2tpWW5aeWN5SWdQU0FpTVM0d0lqc0tDU0owY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREl5TkRnM05qRTNPQ0k3Q2draWNYVmhiblJwZEhraUlEMGdJakVpT3dvSkltOXlhV2RwYm1Gc0xYQjFjbU5vWVhObExXUmhkR1V0YlhNaUlEMGdJakUwTmprd056STROell6TkRraU93b0pJblZ1YVhGMVpTMTJaVzVrYjNJdGFXUmxiblJwWm1sbGNpSWdQU0FpUkVRMlJUazVRVFV0T0RJNE5pMDBNalpGTFRoRVJqUXROakF6UlRFeE5qRXdNekU1SWpzS0NTSndjbTlrZFdOMExXbGtJaUE5SUNKamIyMHVZMnRvWkM1cWFXcHBZUzU2ZFdGdWMyaHBOakFpT3dvSkltbDBaVzB0YVdRaUlEMGdJakV4TXpNeE5EVXpNRFVpT3dvSkltSnBaQ0lnUFNBaVkyOXRMbU5yYUdRdWFtcDNJanNLQ1NKd2RYSmphR0Z6WlMxa1lYUmxMVzF6SWlBOUlDSXhORFk1TURjeU9EYzJNelE1SWpzS0NTSndkWEpqYUdGelpTMWtZWFJsSWlBOUlDSXlNREUyTFRBM0xUSXhJREF6T2pRM09qVTJJRVYwWXk5SFRWUWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREUyTFRBM0xUSXdJREl3T2pRM09qVTJJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkltOXlhV2RwYm1Gc0xYQjFjbU5vWVhObExXUmhkR1VpSUQwZ0lqSXdNVFl0TURjdE1qRWdNRE02TkRjNk5UWWdSWFJqTDBkTlZDSTdDbjA9IjsKCSJlbnZpcm9ubWVudCIgPSAiU2FuZGJveCI7CgkicG9kIiA9ICIxMDAiOwoJInNpZ25pbmctc3RhdHVzIiA9ICIwIjsKfQ==\","
				+ "\"orderId\" : \"5416072100044237\","
				+ "\"productId\" : \"com.ckhd.jijia.zuanshi60\","
						+ "\"total\" : \"钻石*60\""
								+ "}";
		str = "{"+
				  "\"orderId\" : \"201608230071fc97\","+
				  "\"receipt\" : \"ewoJInNpZ25hdHVyZSIgPSAiQXlVYzNHczBpVDI0V0VFTXM0RmtSOU1KMzd0U3IvblQrT0RvYjhZTjgxTm9iOFhzN1phcWNVNVhYa0NzcGhVbkJZSHFZUUNHVXBMbzZXVi9peGdueTRXWk51eG9lUkxWZURhV2xCU05XdEczblVTRTM3bWVTeTVpUWQxSkRPelo3bmF0WkxlZ1hYcGVSMktVSWxEZkZDeHg3WlQ4bXlLUldCaHFMd2xXVzdLaHJYbXFmb0dzdk11N2hkR2djTWprY2Z2a1JiRU8vcUFSRzlVRzVtMGlWSC9ITkRya1lvemZKekdyczlNM1VoWXRqTTdjbmgyM1E4NG5tY3JGYjFVeGJUNHV2cjc1Q3dqalRtdUpZaDZJUk9HZnhBQUp1QUNkeURuUVRqZE13S2xQMnhRZ2E2SG5DY3d3cU9qbVZwSUNFc2RqNG9EaDR3bmZ3d0d0QldMYjZxMEFBQVdBTUlJRmZEQ0NCR1NnQXdJQkFnSUlEdXRYaCtlZUNZMHdEUVlKS29aSWh2Y05BUUVGQlFBd2daWXhDekFKQmdOVkJBWVRBbFZUTVJNd0VRWURWUVFLREFwQmNIQnNaU0JKYm1NdU1Td3dLZ1lEVlFRTERDTkJjSEJzWlNCWGIzSnNaSGRwWkdVZ1JHVjJaV3h2Y0dWeUlGSmxiR0YwYVc5dWN6RkVNRUlHQTFVRUF3dzdRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTWdRMlZ5ZEdsbWFXTmhkR2x2YmlCQmRYUm9iM0pwZEhrd0hoY05NVFV4TVRFek1ESXhOVEE1V2hjTk1qTXdNakEzTWpFME9EUTNXakNCaVRFM01EVUdBMVVFQXd3dVRXRmpJRUZ3Y0NCVGRHOXlaU0JoYm1RZ2FWUjFibVZ6SUZOMGIzSmxJRkpsWTJWcGNIUWdVMmxuYm1sdVp6RXNNQ29HQTFVRUN3d2pRWEJ3YkdVZ1YyOXliR1IzYVdSbElFUmxkbVZzYjNCbGNpQlNaV3hoZEdsdmJuTXhFekFSQmdOVkJBb01Da0Z3Y0d4bElFbHVZeTR4Q3pBSkJnTlZCQVlUQWxWVE1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBcGMrQi9TV2lnVnZXaCswajJqTWNqdUlqd0tYRUpzczl4cC9zU2cxVmh2K2tBdGVYeWpsVWJYMS9zbFFZbmNRc1VuR09aSHVDem9tNlNkWUk1YlNJY2M4L1cwWXV4c1FkdUFPcFdLSUVQaUY0MWR1MzBJNFNqWU5NV3lwb041UEM4cjBleE5LaERFcFlVcXNTNCszZEg1Z1ZrRFV0d3N3U3lvMUlnZmRZZUZScjZJd3hOaDlLQmd4SFZQTTNrTGl5a29sOVg2U0ZTdUhBbk9DNnBMdUNsMlAwSzVQQi9UNXZ5c0gxUEttUFVockFKUXAyRHQ3K21mNy93bXYxVzE2c2MxRkpDRmFKekVPUXpJNkJBdENnbDdaY3NhRnBhWWVRRUdnbUpqbTRIUkJ6c0FwZHhYUFEzM1k3MkMzWmlCN2o3QWZQNG83UTAvb21WWUh2NGdOSkl3SURBUUFCbzRJQjF6Q0NBZE13UHdZSUt3WUJCUVVIQVFFRU16QXhNQzhHQ0NzR0FRVUZCekFCaGlOb2RIUndPaTh2YjJOemNDNWhjSEJzWlM1amIyMHZiMk56Y0RBekxYZDNaSEl3TkRBZEJnTlZIUTRFRmdRVWthU2MvTVIydDUrZ2l2Uk45WTgyWGUwckJJVXdEQVlEVlIwVEFRSC9CQUl3QURBZkJnTlZIU01FR0RBV2dCU0lKeGNKcWJZWVlJdnM2N3IyUjFuRlVsU2p0ekNDQVI0R0ExVWRJQVNDQVJVd2dnRVJNSUlCRFFZS0tvWklodmRqWkFVR0FUQ0IvakNCd3dZSUt3WUJCUVVIQWdJd2diWU1nYk5TWld4cFlXNWpaU0J2YmlCMGFHbHpJR05sY25ScFptbGpZWFJsSUdKNUlHRnVlU0J3WVhKMGVTQmhjM04xYldWeklHRmpZMlZ3ZEdGdVkyVWdiMllnZEdobElIUm9aVzRnWVhCd2JHbGpZV0pzWlNCemRHRnVaR0Z5WkNCMFpYSnRjeUJoYm1RZ1kyOXVaR2wwYVc5dWN5QnZaaUIxYzJVc0lHTmxjblJwWm1sallYUmxJSEJ2YkdsamVTQmhibVFnWTJWeWRHbG1hV05oZEdsdmJpQndjbUZqZEdsalpTQnpkR0YwWlcxbGJuUnpMakEyQmdnckJnRUZCUWNDQVJZcWFIUjBjRG92TDNkM2R5NWhjSEJzWlM1amIyMHZZMlZ5ZEdsbWFXTmhkR1ZoZFhSb2IzSnBkSGt2TUE0R0ExVWREd0VCL3dRRUF3SUhnREFRQmdvcWhraUc5Mk5rQmdzQkJBSUZBREFOQmdrcWhraUc5dzBCQVFVRkFBT0NBUUVBRGFZYjB5NDk0MXNyQjI1Q2xtelQ2SXhETUlKZjRGelJqYjY5RDcwYS9DV1MyNHlGdzRCWjMrUGkxeTRGRkt3TjI3YTQvdncxTG56THJSZHJqbjhmNUhlNXNXZVZ0Qk5lcGhtR2R2aGFJSlhuWTR3UGMvem83Y1lmcnBuNFpVaGNvT0FvT3NBUU55MjVvQVE1SDNPNXlBWDk4dDUvR2lvcWJpc0IvS0FnWE5ucmZTZW1NL2oxbU9DK1JOdXhUR2Y4YmdwUHllSUdxTktYODZlT2ExR2lXb1IxWmRFV0JHTGp3Vi8xQ0tuUGFObVNBTW5CakxQNGpRQmt1bGhnd0h5dmozWEthYmxiS3RZZGFHNllRdlZNcHpjWm04dzdISG9aUS9PamJiOUlZQVlNTnBJcjdONFl0UkhhTFNQUWp2eWdhWndYRzU2QWV6bEhSVEJoTDhjVHFBPT0iOwoJInB1cmNoYXNlLWluZm8iID0gImV3b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGNITjBJaUE5SUNJeU1ERTJMVEE0TFRJeUlEQTVPalF4T2pFM0lFRnRaWEpwWTJFdlRHOXpYMEZ1WjJWc1pYTWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVXRiWE1pSUQwZ0lqRTBOekU0T0RRd056YzJOeklpT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0psWkRSaU56azJOVGd5WXpVM05XRmpNMk14TVdFNFlUSmtPREZoTXpobVptSXdNemRrTkdFeklqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTkRJd01EQXdNakV4TURRNU1qWTVJanNLQ1NKaWRuSnpJaUE5SUNJeExqSXVOQ0k3Q2draVlYQndMV2wwWlcwdGFXUWlJRDBnSWpFeE1qTTVNak15TkRraU93b0pJblJ5WVc1ellXTjBhVzl1TFdsa0lpQTlJQ0kwTWpBd01EQXlNVEV3TkRreU5qa2lPd29KSW5GMVlXNTBhWFI1SWlBOUlDSXhJanNLQ1NKdmNtbG5hVzVoYkMxd2RYSmphR0Z6WlMxa1lYUmxMVzF6SWlBOUlDSXhORGN4T0RnME1EYzNOamN5SWpzS0NTSjFibWx4ZFdVdGRtVnVaRzl5TFdsa1pXNTBhV1pwWlhJaUlEMGdJakF5UWpGQ1JqWXpMVFpDUlRZdE5FRkRSaTFDUTBNeExUWkNORFUyUWpRME1VTTFPQ0k3Q2draWFYUmxiUzFwWkNJZ1BTQWlNVEV5TkRNMk9EZ3pPU0k3Q2draWRtVnljMmx2YmkxbGVIUmxjbTVoYkMxcFpHVnVkR2xtYVdWeUlpQTlJQ0k0TVRjM05qYzJOVFlpT3dvSkluQnliMlIxWTNRdGFXUWlJRDBnSW1OdmJTNWpheTVxWW1ka2R5NTZkV0Z1YzJocE5pSTdDZ2tpY0hWeVkyaGhjMlV0WkdGMFpTSWdQU0FpTWpBeE5pMHdPQzB5TWlBeE5qbzBNVG94TnlCRmRHTXZSMDFVSWpzS0NTSnZjbWxuYVc1aGJDMXdkWEpqYUdGelpTMWtZWFJsSWlBOUlDSXlNREUyTFRBNExUSXlJREUyT2pReE9qRTNJRVYwWXk5SFRWUWlPd29KSW1KcFpDSWdQU0FpWTI5dExtTnJhR1F1YW1KblpDSTdDZ2tpY0hWeVkyaGhjMlV0WkdGMFpTMXdjM1FpSUQwZ0lqSXdNVFl0TURndE1qSWdNRGs2TkRFNk1UY2dRVzFsY21sallTOU1iM05mUVc1blpXeGxjeUk3Q24wPSI7CgkicG9kIiA9ICI0MiI7Cgkic2lnbmluZy1zdGF0dXMiID0gIjAiOwp9\","
				  +"\"productId\" : \"com.ck.jbgdw.zuanshi6\","
				  +"\"ckappid\" : \"1020\","
				  +"\"total\" : \"600\""
				+"}";
//		str = "{\"ckAppId\":1030,\"ckChannelId\":200,\"rqType\":\"ad\",\"versionName\":\"1.1.2\"}";
		JSONObject data111 = new JSONObject();
		data111.put("mnc", "14276");
		data111.put("lac", "65535");
		data111.put("ci", "56945");
//		str = data111.toJSONString();
//		str = "{\"mcc\":\"460\",\"androidId\":\"1f3d46b1adeb18f1\",\"lng\":\"113.365299\",\"simNo\":\"89860015111481001021\",\"lac\":\"10299\",\"version\":\"10001\",\"userId\":\"unknow\",\"mnc\":\"00\",\"bid\":null,\"lat\":\"23.125074\",\"mmAppId\":\"300009127531\",\"sid\":null,\"nid\":null,\"imei\":\"865899023247850\",\"verifyInfo\":null,\"ckChannelId\":\"52\",\"productId\":\"1\",\"payNotifyUrl\":null,\"extension\":\"gameData\",\"price\":1,\"ckAppId\":\"1029\",\"gameOnline\":1,\"ci\":\"43363330\",\"payType\":\"124\",\"imsi\":\"460028571551021\",\"act\":\"1002\"}";
//		str = "{\"userId\" : \"unknow\",\"act\" : \"1002\",\"payType\" : \"121\",\"productId\" : \"1\",\"price\" : \"600\",\"gameOnline\" : \"0\",\"ckAppId\" : \"1013\", \"ckChannelId\" : \"60\",\"version\" : \"10001\",\"extension\" : \"gameData\",\"verifyInfo\":{\"ip\":\"192.168.1.112\"}}";
//		str = "{\"sign\":\"d9d45292b09786a64ce1081c4ea570fd\",\"uid\":\"unknow\",\"channelId\":\"200\",\"attach\":\"testExt\",\"gameId\":\"1033\",\"create_time\":1473416025000,\"prices\":\"600\",\"productName\":\"60元宝\",\"orderId\":\"3316090900977225\",\"productId\":\"1\"}";
//		str = "";
		str = "{\"application\":{\"a\":1030,\"b\":1}}";
//		str = "{\"gift\":{\"a\":1001,\"b\":1,\"c\":\"0BRH4BK6A90HO44Q\",\"d\":\"0BRH4BK6A90HO44Q\"}}";
//		str = "{\"act\":\"0\",\"ckChannelId\":\"151\",\"rqType\":\"androidAdPlugin\",\"bid\":null,\"carriers\":\"CMCC\",\"lng\":null,\"os\":\"android\",\"signMD5\":\"559C7DE1334AFF395AFB9ACD51F88AD6\",\"versionName\":\"1.1\",\"mnc\":\"01\",\"androidId\":\"7f905bb54e36cfb8\",\"sid\":null,\"ckAppId\":\"1031\",\"mmAppId\":\"300009127531\",\"imei\":\"868773028195082\",\"nid\":null,\"simNo\":\"89860111365101170114\",\"mcc\":\"460\",\"ci\":null,\"lat\":null,\"simNO\":\"89860113065101170114\",\"imsi\":\"460010956047433\",\"lac\":null}";
		str = "{\"user\":{\"a\":\"ck58120569\"},\"verify\":{\"a\":\"e3h6\",\"b\":3},\"version\":{\"a\":\"1.2.0\"}}";
		str = "{\"callBackUrl\":\"https://www.baidu.com\",\"ckAppId\":\"1001\",\"ckChannelId\":\"200\",\"extension\":\"restore\",\"gameOnline\":1,\"payType\":\"122\",\"price\":200,\"productId\":1,\"userId\":\"000000\",\"version\":\"10001\"}";
		str = "{\"resource\":{\"a\":\"134686\",\"d\":1,\"e\":\"300*300\"}}";
//		str = "{\"act\":\"0\",\"ckChannelId\":\"17\",\"orderId\":\"2816121000055069\",\"bid\":null,\"pfKey\":\"8a273311c95ed150f78c324c10d1afe7\",\"pf\":\"qq_m_qq-00000000-android-00000000-ysdk\",\"openKey\":\"F8C9F46E1254E5E3BC55333C7B795B7A\",\"payType\":\"141\",\"mnc\":\"\",\"androidId\":\"7316e420b2032214\",\"sid\":null,\"channelId\":17,\"ckAppId\":\"1028\",\"operate\":\"5\",\"imei\":\"358487070569410\",\"phone_model\":\"LG-K580\",\"tencentLoginType\":1,\"zoneId\":\"1\",\"nid\":null,\"simNo\":\"unknown\",\"mcc\":\"\",\"ci\":null,\"openId\":\"DED8F23E09979F58F02FD7F41A17E461\",\"imsi\":\"unknown\",\"lac\":null,\"environment\":\"Sandbox\"}";
//		str = "{\"event\":[{\"a\":\"134686\",\"c\":1,\"d\":1481524457284,\"e\":\"dsfadsfsadfa\",\"f\":2}]}";
		str = "Signature=ka8PDlV7S9sYqxEMRnmlBv%2FDoAE%3D&AccessKeyId=testid&Action=SingleSendSms&Format=XML&ParamString={\"name\":\"d\",\"name1\":\"d\"}&RecNum=13098765432&RegionId=cn-hangzhou&SignName=标签测试&SignatureMethod=HMAC-SHA1&SignatureNonce=9e030f6b-03a2-40f0-a6ba-157d44532fd0&SignatureVersion=1.0&TemplateCode=SMS_1650053&Timestamp=2016-10-20T05:37:52Z&Version=2016-09-27";
		str = "api_key=15CF694F62674AD58752B14839A9E2D1&close_time=20170103174347&create_time=20170103174312&deal_price=0.01&out_order_no=9017010300000a44&pay_channel=101&submit_time=20170103174312&user_id=null&sign=UHPC2lW0XG%2FYQbjelRBwwN25hQZqjBtcLuQYUZJc7WwzWLUtTeIRcNLdCGzFbcVZ9YLgZD0BbEhyHqG0piwzHLbtOCqn6wmSp6aSxfye2UlW0n6bX%2Btpc0ZKDkPo8mwGghsmc%2F%2BU4vgj5uoxc3wUyllB8afe2H1A9BuB9uB1N%2Fs%3D";
		str = "{\"user\":{\"c\":\"1\",\"e\":\"13516627732\"},\"version\":{\"a\":\"1.2.0\"}}";
		str = "transdata=%7B%22appid%22%3A%225000003442%22%2C%22appuserid%22%3A%2280942449%2615%22%2C%22cporderid%22%3A%22291702200002dd40%22%2C%22cpprivate%22%3A%22%22%2C%22currency%22%3A%22RMB%22%2C%22feetype%22%3A2%2C%22money%22%3A6.00%2C%22paytype%22%3A403%2C%22result%22%3A0%2C%22transid%22%3A%2232061702201657597570%22%2C%22transtime%22%3A%222017-02-20+16%3A58%3A30%22%2C%22transtype%22%3A0%2C%22waresid%22%3A1%7D&sign=0ecKI%2BFgBSiMKH9WgXSpGbH%2FVo59shpXk%2B8mixTy2CBtwu%2BD7QapjUv9N0krgpCTScwMkdRX%2FuGpm0mkRUxxVbG%2FWlA7ejzmhRzu6%2B%2Be101D4K0z%2B5IYOjJgEPaEKjKiZ6YtSGqB4thiCo4Ln0rA4P7VPBcureMiHY8jPV0jYeI%3D&signtype=RSA";
		str = "<xml><appid><![CDATA[wx69a915b95217ac67]]></appid>"
				+ "<bank_type><![CDATA[CFT]]></bank_type>"
				+ "<cash_fee><![CDATA[600]]></cash_fee>"
				+ "<fee_type><![CDATA[CNY]]></fee_type>"
				+ "<is_subscribe><![CDATA[N]]></is_subscribe>"
				+ "<mch_id><![CDATA[1440309802]]></mch_id>"
				+ "<nonce_str><![CDATA[dc6a6489640ca02b0d42dabeb8e46bb7]]></nonce_str>"
				+ "<openid><![CDATA[oTKoTxD6v1gU6qL2x9IZIzB40VqQ]]></openid>"
				+ "<out_trade_no><![CDATA[9817022103f30e21]]></out_trade_no>"
				+ "<result_code><![CDATA[SUCCESS]]></result_code>"
				+ "<return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<sign><![CDATA[A20EF41EB7143B8C22ED0F07AE658C30]]></sign>"
				+ "<time_end><![CDATA[20170221172611]]></time_end>"
				+ "<total_fee>600</total_fee>"
				+ "<trade_type><![CDATA[APP]]></trade_type>"
				+ "<transaction_id><![CDATA[4002642001201702210786896325]]></transaction_id>"
				+ "</xml>";
		str = "sign=wi2weK8SyO%2FRCvzmyVUrAMwoo50kQJdYsExoDt5g%2BHnVkqZQxgO%2F5aL%2Fh3YQDEnpQtYmgDc1MzzaQY8wFzGIBJqSRFdythS1mR78EJsUCTKWBjQnhpQ%2BXH4rFB3CNOsKFM4aYxECTtM%2F1LIawgFw%2BhFTR4RjUAP6a%2BayxIjFwYg%3D&transdata=%7B%22transtype%22%3A0%2C%22result%22%3A0%2C%22transtime%22%3A%222017-02-24+15%3A09%3A37%22%2C%22count%22%3A1%2C%22paytype%22%3A5%2C%22money%22%3A1%2C%22waresid%22%3A124006%2C%22appid%22%3A%221604251055915.app.ln%22%2C%22exorderno%22%3A%222917022400039851%22%2C%22feetype%22%3A0%2C%22transid%22%3A%222170224150937096054035757%22%2C%22cpprivate%22%3A%222917022400039851%22%7D";
		str = "discount=0.00&payment_type=1&subject=60%E5%85%83%E5%AE%9D&trade_no=2017030621001004040226732754&buyer_email=13192940580&gmt_create=2017-03-06+16%3A58%3A13&notify_type=trade_status_sync&quantity=1&out_trade_no=981703060000fb68&seller_id=2088421357373333&notify_time=2017-03-06+17%3A23%3A27&body=60%E5%85%83%E5%AE%9D&trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee=0.01&gmt_payment=2017-03-06+16%3A58%3A14&seller_email=3126575923%40qq.com&price=0.01&buyer_id=2088702679916045&notify_id=5173dc8096f03e9240d8322628d6018gb6&use_coupon=N&sign_type=RSA&sign=XTYZw25052Qn4h9KBsDMXTRSlxqMASiaIw3S4mef%2FzXQLaUZzwlooEIvzyF2N9IfyYkYF5qpG7aZWluRlR3etpgFQMqrUkmznigeR2FWsSnMO7ExYec1j2Ul%2F5mL4FwabzsAF0GfeJEsX6PiCBLEQ2evJoNOq9mEi0FJ9k%2BCsoc%3D";
		str = "{\"sign\":\"2b2267e765eba450e39b790a747a100c\",\"uid\":\"ck66150504\",\"channelId\":\"60\",\"attach\":\"{\\\"deviceID\\\":\\\"865899023257156\\\",\\\"areaType\\\":1,\\\"cVer\\\":122,\\\"orderID\\\":\\\"10298-b8bfb4a8-246b-bc16-140d-2f-1\\\",\\\"userID\\\":10298,\\\"gameID\\\":\\\"2000\\\",\\\"idfa\\\":\\\"865899023257156\\\"}\",\"gameId\":\"2000\",\"create_time\":1489058505000,\"prices\":\"600.0\",\"productName\":\"60元宝\",\"orderId\":\"ya17030900003216\",\"productId\":\"1\"}";
//		str = "{\"validation\":\"{\\\"packageName\\\":\\\"com.ckck.zm\\\",\\\"productId\\\":\\\"hs_czjb\\\",\\\"purchaseTime\\\":1490264733705,\\\"purchaseState\\\":0,\\\"developerPayload\\\":\\\"ya1703230002e486;http:\/\/154v61q180.imwork.net\/netpay\\/callback\/googleplay\/1.1.0\/\",\\\"purchaseToken\\\":\\\"dmoanfaloagaghkioiaclanf.AO-J1OyfE78tS7h8n3QZYUwOw5yKZxcbSGpL1Gl6Glkw3tpLHAsuIWEfPq1Rjv8nRqJMYlfzISAPbWrCVpuBhqDrMJvhz8q7HYvJAeJtL_Z-cVi6UMikM3E\\\"}\",\"sign\":\"AaEAz6Ln+HFRDpV6eJRkH3xe\/DDtu8RQ67SNZnNQooM3qu9sJwcuiWzKQ9GiP2Pxn9SmraRCbZFMbwbFrYQTsbGunJKHHu4hHCsBINnYl2v77QOz3e21v+cJIeabNFKsjGiwC34pjfGcShzYqspJlCXXLr6cdhtNbWptpR4zdZiNfOe1eeesGi4IkD+lCMxw7HZAdK5ZJ33Na+PQxm1c\/Nxx9OyH2goa32twP3o9eJC1914JNXp1PbXxjPT1G4+GMb\/VgxVDrlrm2A5ZcxHkfSZVykBHEYPOREaZF0vrto+bePkqVN5fmSgMVwWc4syUM\/TkdGvwIB5f6+5H7XNmcw==\",\"prices\":600}";
		return str;
	}

	public static void main(String[] args) {
//		String url1 = \"http://localhost:8080/opengame/ckhd/mmiap";
//		String url2 = \"http://120.25.154.85:8080/ck/online/channel/payCallBack/appstore";
//		String url2 = "http://open.mobee.im/ckhd/rdiap";
//		String url3 = "http://localhost:8080/ck/online/app/pay";
//		String url = "https://sandbox.itunes.apple.com/verifyReceipt";
//		String url = "http://localhost/ck/online/channel/payCallBack/appstore?ckappid=1005&channelid=130";
//		String url = "http://localhost:8080/ck/online/app/pay";
		String str = "sign=p%2BTIQqoU4gFDgXRpCygavc1QGBJQxcMPqaZY40kzAk1mgYeqDEgi93ljp9WyGx3GbzMyC0NRIKOvZ6wIKGgKt66m7TGjoyAUMkRS3WIButtOnryRyTPNnbG5LKTivM5prA%2Bb4ov7Aa2CU169OtMrZe1VzJ2m5bj2G3xXSEgniL0%3D&transdata=%7B%22transtype%22%3A0%2C%22result%22%3A0%2C%22transtime%22%3A%222016-05-19+21%3A09%3A35%22%2C%22count%22%3A1%2C%22paytype%22%3A5%2C%22money%22%3A1%2C%22waresid%22%3A73115%2C%22appid%22%3A%221604251055915.app.ln%22%2C%22exorderno%22%3A%222916051900064033%22%2C%22feetype%22%3A0%2C%22transid%22%3A%222160519210935056266179272%22%2C%22cpprivate%22%3A%222916051900064033%22%7D";
//		String url = "http://localhost/ck/online/channel/callBack/commonChannel/anzhi/1025/9/";
//		String url = "http://localhost/ck/app/getCfgparam";
//		String url = "http://localhost/ck/app/appleUser/add";
//		String url = "http://sdk.test4.g.uc.cn/cp/account.verifySession";
//		String url = "http://localhost/ck/online/app/init";
		String url = "http://open.mobee.im/ck/online/channel/payCallBack/appstore?ckappid=1020";
		url = "http://localhost/ck/app/getCfgparam";
//		url = "http://localhost/ck/online/app/pay";
//		url = "http://120.76.204.67/test/orderService";
		url = "http://120.76.226.148:8000/game/app/3/0/0";
//		url = "http://localhost/game/app/2/0/0";
//		url = "http://localhost/ck/online/init/getAddr";
		url = "http://120.25.154.85:8080/ck/online/channe/callBack/commonChannel/tencent1/1028/17/";
		url = "http://localhost/ck/user/app/14/0/0";
		url = "http://localhost/ck/online/app/pay";
		url = "http://localhost:8080/ck/a/2/0/0";
		//url = "http://localhost/ck/online/app/other/tencent";
//		url = "http://localhost:8080/ck/a/e1";
		url = "http://115.28.224.220:8003/api/prepay?appId=1480667098072516&appSecret=uWwtxl2SWIdptLggpBuywhIgxUb4FVa2&orderName=%E6%B5%8B%E8%AF%95&orderNo=011612140001ec70&orderAmt=1&orderDetail=%E6%B5%8B%E8%AF%95";
		url = "http://154v61q180.imwork.net/netpay/callback/gionee/1.1.0/";
		url = "http://120.25.201.106:8000/ck/user/app/4/0/0";
		url = "http://154v61q180.imwork.net/netpay/callback/coolpad/1.1.0/";
		url = "http://127.0.0.1/ck/online/channel/payCallBack/weixin";
//		url = "http://154v61q180.imwork.net/netpay/callback/aplipay/1.1.0/";
		url = "http://192.168.1.57/netpay/callback/alipay/1.1.0/";
		str = "discount=0.00&payment_type=1&subject=60%E5%85%83%E5%AE%9D&trade_no=2017030621001004040226732754&buyer_email=13192940580&gmt_create=2017-03-06+16%3A58%3A13&notify_type=trade_status_sync&quantity=1&out_trade_no=981703060000fb68&seller_id=2088421357373333&notify_time=2017-03-06+17%3A23%3A27&body=60%E5%85%83%E5%AE%9D&trade_status=TRADE_SUCCESS&is_total_fee_adjust=N&total_fee=0.01&gmt_payment=2017-03-06+16%3A58%3A14&seller_email=3126575923%40qq.com&price=0.01&buyer_id=2088702679916045&notify_id=5173dc8096f03e9240d8322628d6018gb6&use_coupon=N&sign_type=RSA&sign=XTYZw25052Qn4h9KBsDMXTRSlxqMASiaIw3S4mef%2FzXQLaUZzwlooEIvzyF2N9IfyYkYF5qpG7aZWluRlR3etpgFQMqrUkmznigeR2FWsSnMO7ExYec1j2Ul%2F5mL4FwabzsAF0GfeJEsX6PiCBLEQ2evJoNOq9mEi0FJ9k%2BCsoc%3D";
		url = "http://120.76.117.196:8888/Order/OrderAgent.aspx";
		url = "http://localhost/netpay/callback/googleplay/1.1.0/";
//		new HttpPostUtils().get(url+"?"+str, 20000, 20000);
		new HttpPostUtils().testPost(url);

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
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
	
	/*public static void main(String[] args) {
//		String url = "http://localhost/ck/user/app/1/0/0";
//		String url = "http://120.25.154.85:8080/ck/user/app/6/0/0";
		String url = "http://120.25.154.85:8080/ck/online/channel/callBack/meizu/1021/52";
		new HttpPostUtils().testPost(url);
	}*/
}
