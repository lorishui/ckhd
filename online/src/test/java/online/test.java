package online;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class test {
	/*public static void main(String[] args)  {
		int a=10;
		int b=10;
		method(a,b);
		System.out.println("a="+a);
		System.out.println("b="+b);
	}*/

	@Test
	public void method(){
		/*System.out.println("a=100,b=100");
		System.exit(0);*/
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(11));
	}
	
	/*public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(1000.897));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", 1);
		map.put("b", 1);
		JSONObject json = (JSONObject) JSONObject.toJSON(map);
		System.out.println(json.toJSONString());
	}*/
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		/*String str = "{\"sign\":\"cbf557b0496326be788d074ce9f4f462\",\"amount\":\"1\",\"product_id\":\"1\",\"sign_type\":\"md5\",\"app_uid\":\"2887397238\",\"gateway_flag\":\"success\",\"user_id\":\"2887397238\",\"app_key\":\"08158bf9f09b919790a63f10c381be52\",\"sign_return\":\"0e09df6930324c325688d212c8af3eca\",\"order_id\":\"1705066783326166891\",\"app_order_id\":\"ya1705060005ba17\"}";
		JSONObject json = JSONObject.parseObject(str);
		System.out.println(getSignContent(json));*/
		/*String str = "60åå®";
		System.out.println(new String(str.getBytes("iso8859-1"),"utf-8"));*/
		String str = "abc";
		System.out.println(str.indexOf("a"));
	}
	
	private static String getSignContent(JSONObject json){
		Object[] key = json.keySet().toArray();
		Arrays.sort(key);
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<key.length ; i++){
			if(!key[i].equals("sign") && !key[i].equals("sign_return")) sb.append(json.get(key[i])).append("#");
		}
		return sb.toString();
	}
}
