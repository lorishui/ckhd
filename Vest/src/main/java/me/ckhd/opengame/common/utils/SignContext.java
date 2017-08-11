package me.ckhd.opengame.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class SignContext {
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getSignContext(Map<String,?> map,String appKey){
		if( map!=null && map.size()>0 && StringUtils.isNotBlank(appKey)){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign")) sb.append(obj[i]).append("=").append(map.get(obj[i]));
			}
			sb.append(appKey);
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * xy获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @param contcat 连接符
	 * @return
	 */
	public static String getSignContext(Map<String,?> map,String appKey,String contcat){
		if( map!=null && map.size()>0 && StringUtils.isNotBlank(appKey)){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer(appKey);
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") && !obj[i].equals("sig") && !obj[i].equals("dataType")) sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @param contcat 连接符
	 * @return
	 */
	public static String getSignContextUrlEncode(Map<String,?> map,String appKey,String contcat){
		if( map!=null && map.size()>0 && StringUtils.isNotBlank(appKey)){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") || !obj[i].equals("sig") || !obj[i].equals("cee_extend") ) {
					sb.append(obj[i]+"="+map.get(obj[i])+contcat);
				}
			}
			sb.setLength(sb.length()-1);
			String content = "";
			try {
				content = appKey + URLEncoder.encode(sb.toString(), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			sb.append(appKey);
			return content.replace("+", "%20").replace("*", "%2A");
		}
		return "";
	}
	
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getSignContext(Map<String,?> map){
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign")) sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getMeizuSignContext(Map<String,Object> map){
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") || !obj[i].equals("sign_type")){
					if( map.get(obj[i]) != null && map.get(obj[i]).toString().length() > 0){
						sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
					}
				}
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getMeizuPaySignContext(Map<String,Object> map){
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") && !obj[i].equals("sign_type")){
					sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
				}
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
	
	/**
	 * 获取key自然排序的
	 * @param map 加密的参数集
	 * @param appKey 加密密钥
	 * @return
	 */
	public static String getSignContextNotNull(Map<String,Object> map){
		if( map!=null && map.size()>0 ){
			Set<String> keySet = map.keySet();
			Object[] obj = keySet.toArray();
			Arrays.sort(obj);
			StringBuffer sb = new StringBuffer();
			for(int i=0; i<obj.length ; i++){
				if(!obj[i].equals("sign") && map.get(obj[i]) != null && map.get(obj[i]).toString().length() > 0){
					sb.append(obj[i]).append("=").append(map.get(obj[i])+"&");
				}
			}
			sb.setLength(sb.length()-1);
			return sb.toString();
		}
		return "";
	}
}
