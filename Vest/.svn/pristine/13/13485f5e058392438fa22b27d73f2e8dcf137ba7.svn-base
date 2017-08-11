package me.ckhd.opengame.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MyJsonUtils {
	
	
	/**
	 * 传入json字符串,返回map数据
	 * @param jsonStr
	 * @return
	 */
	public static HashMap<String, Object> jsonStr2Map(String jsonStr){
		if(StringUtils.isBlank(jsonStr)){
			return null;
		}
		JSONObject json = JSONObject.parseObject(jsonStr);
		return reflect(json);
	}
	
	@SuppressWarnings("unchecked")
    public static List<Map<String, Object>> jsonArrayStr2List(String jsonStr){
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);
		Object o = reflect(jsonArray);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if(o instanceof ArrayList){
			ArrayList<?> array = (ArrayList<?>)o;
			for(Object obj:array){
				if(obj instanceof HashMap){
					list.add((HashMap<String, Object>)obj);
				}
			}
		}
		return list;
	}
	
	/**
	 * 将一个jsonObject转换成map对象
	 * @param json
	 * @return
	 */
	public static HashMap<String, Object> reflect(JSONObject json){ 
        HashMap<String, Object> map = new HashMap<String, Object>();
        Set<?> keys = json.keySet();
        for(Object key : keys){
            Object o = json.get(key);
            if(o instanceof JSONArray)
                map.put((String) key, reflect((JSONArray) o));
            else if(o instanceof JSONObject)
                map.put((String) key, reflect((JSONObject) o));
            else
                map.put((String) key, o);
        }
        return map;
	}
	
	 /**
     * 将JSONArray对象转换成List集合
     * @see JSONHelper#reflect(JSONObject)
     * @param json
     * @return
     */
    public static Object reflect(JSONArray json){
        List<Object> list = new ArrayList<Object>();
        for(Object o : json){
            if(o instanceof JSONArray)
                list.add(reflect((JSONArray) o));
            else if(o instanceof JSONObject)
                list.add(reflect((JSONObject) o));
            else
                list.add(o);
        }
        return list;
    }
    
    /**
	 * 将一个jsonObject转换成map对象
	 * @param json
	 * @return
	 */
	public static HashMap<String, ?> reflect2(JSONObject json){ 
	        HashMap<String, Object> map = new HashMap<String, Object>();
	        Set<?> keys = json.keySet();
	        for(Object key : keys){
	            Object o = json.get(key);
	            if(o instanceof JSONArray)
	                map.put((String) key, reflect((JSONArray) o));
	            else if(o instanceof JSONObject)
	                map.put((String) key, reflect((JSONObject) o));
	            else
	                map.put((String) key, o);
	        }
	        return map;
	}
}
