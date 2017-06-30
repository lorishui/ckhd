package online;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import me.ckhd.opengame.online.version.OnlineVersion120;


public class test2 {
	
	public static void main(String[] args) {
		/*String str = "eyJCYW5rRGF0ZVRpbWUiOiIyMDE3LTA1LTIzIDE4OjA2OjUxIiwiRXh0SW5mbyI6ImNrc2RrIiwiTWVyY2hhbmRpc2VOYW1lIjoiMTk4MOWFg%2BWunSIsIk9yZGVyTW9uZXkiOiIxOTguMDAiLCJPcmRlclN0YXR1cyI6MSwiU3RhcnREYXRlVGltZSI6IjIwMTctMDUtMjMgMTg6MDY6NDQiLCJTdGF0dXNNc2ciOiLmiJDlip8iLCJVSUQiOiIxMDgzNzExOTg5IiwiVm91Y2hlck1vbmV5IjowfQ%3D%3D";
		try {
			str = URLDecoder.decode(str,"utf-8");
			String data = Base64.decode(URLDecoder.decode(str,"utf-8"));
			System.out.println(data);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*Annotation[] annotation = test2.class.getAnnotations();
		Map<String,String> map = new HashMap<String, String>();
		Set<Entry<String,String>> set = map.entrySet();*/
	    System.out.println(1);
	}
	
}
