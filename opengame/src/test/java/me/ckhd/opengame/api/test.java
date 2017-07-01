package me.ckhd.opengame.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import me.ckhd.opengame.common.utils.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName test
 * @Description TODO(这里用一句话描述这个类的作用)
 * @author ASUS
 * @Date 2017年6月27日 下午5:46:16
 * @version 1.0.0
 */
public class test {
	/*public static void main(String[] args) {
		String param = "﻿1|1|1|1|1";
		String str = "1|1|1|1|1";
		System.out.println(param.length());
		System.out.println(str.length());
		String[] arr = str.split("\\|");
		System.out.println(arr.length);
		System.out.println(arr[0].length());
		System.out.println(Integer.parseInt(arr[0]));
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("code", 0);
		System.out.println(JSONObject.toJSON(map));
		String code = "5d9106b0-c202-11e6-b985-cfd4b50f6dbf";
		System.out.println(code);
		String patternStr = "^\\d\\.\\d\\.\\d$";
		boolean is = Pattern.matches(patternStr, patternStr);
		System.out.println();
		try{
			int a = 1/0;
			System.out.println(a);
		}catch(Throwable e){
			System.out.println(e.getMessage());
		}
		char ch = 'o';
		System.out.println(getIndex(ch));
	}
	
	public static String getIndex(char ch){
		StringBuffer sb = new StringBuffer();
		if(ch >=48 && ch <= 57 ){
			sb.append(ch);
		}else{
			int n=ch%10;
			sb.append(n);
		}
		return sb.toString();
	}*/
	
	public static void main(String[] args) {
		/*String str = "transdata={\"appid\":\"5000003442\",\"appuserid\":\"80942449&15\",\"cporderid\":\"291702200002dd40\",\"cpprivate\":\"\",\"currency\":\"RMB\","
				+ "\"feetype\":2,\"money\":6.00,\"paytype\":403,\"result\":0,\"transid\":\"32061702201657597570\",\"transtime\":\"2017-02-20 16:58:30\","
				+ "\"transtype\":0,\"waresid\":1}&sign=0ecKI+FgBSiMKH9WgXSpGbH/Vo59shpXk+8mixTy2CBtwu+D7QapjUv9N0krgpCTScwMkdRX/uGpm0mkRUxxVbG/WlA7ejzmhRzu6++e101D4K0z+5IYOjJgEPaEKjKiZ6YtSGqB4thiCo4Ln0rA4P7VPBcureMiHY8jPV0jYeI=&signtype=RSA";*/
	    
	    String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getCanonicalHostName();
            System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
            System.out.println(new String(InetAddress.getLocalHost().getAddress()));
            System.out.println(InetAddress.getLocalHost().getLocalHost());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei").length);
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[0].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[1].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[2].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[3].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[4].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[5].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[6].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("liupei")[7].getHostAddress());
            System.out.println(InetAddress.getLocalHost().getAllByName("ASUS").length);
            System.out.println(InetAddress.getLocalHost().getAllByName("ASUS")[0].getHostAddress());
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    System.out.println(localHost);
		
	}
	
	public static JSONObject splitStr(String data){
		JSONObject json = new JSONObject();
		if( StringUtils.isNotBlank(data) ){
			json.put("transdata", data.substring(data.indexOf("transdata")+2, data.indexOf("}")+1));
			json.put("sign", data.substring(data.indexOf("sign")+2, data.substring(data.indexOf("sign")).indexOf("}")+1));
			json.put("signtype", "RSA");
		}
		return null;
	}
}
