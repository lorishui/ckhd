package me.ckhd.opengame.app.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.ckhd.opengame.common.utils.CacheUtils;

public class IccidUtils {
	public static Map<String, Map<String,String>> CACHE_ICCID_MAP = null;
	
	/**
	 * 根据运营商获取省份/市的代码
	 * @param simNo
	 * @param carries
	 * @return
	 */
	public static String getProvinceCodeBySimNO(String simNo,String carries){
		String provinceCode = "99";
		if(simNo.length()>=10 && "CMCC".equals(carries)){
			provinceCode=simNo.substring(8,10);
		}else if(simNo.length()>=11 && "CUCC".equals(carries)){
			provinceCode=simNo.substring(9,11);
		}else if(simNo.length()>=13 && "CTCC".equals(carries)) {
			provinceCode=simNo.substring(10,13);
		}
		return provinceCode;
	}
	
	/**
	 * 根据联通/电信的provinceCode转换成移动的provinceCode
	 * @param otherProvinceCode
	 * @param carries
	 * @return
	 */
	public static String getCmccProvinceCode(String provinceCode, String carries) {
		if ("CMCC".equals(carries) || "99".equals(provinceCode)) {
			return provinceCode;
		}
		if (CACHE_ICCID_MAP == null) {
			CACHE_ICCID_MAP = getSaveCacheMap();
		}
		Map<String, String> carriesMap = CACHE_ICCID_MAP.get(carries);
		// 判断缓存中是否有此运营商的数据,如果无则判断为移动的province,所以直接返回
		if (carriesMap == null) {
			return "99";
		}
		// 根据传入的省份Code,获取到相对应的移动省份CODE
		provinceCode = carriesMap.get(provinceCode) == null ? null : String
				.valueOf(carriesMap.get(provinceCode));
		if (provinceCode == null) {
			provinceCode = "99";
		}
		return provinceCode;
	}
	
	
	private static Map<String, Map<String,String>> getSaveCacheMap(){
		Map<String, Map<String,String>> maps = new HashMap<String, Map<String,String>>();
		try {
			String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();    //添加
			BufferedReader br= new BufferedReader(new InputStreamReader(new FileInputStream(path+"/provinces.txt"),"UTF-8"));
			String s = null;
			Map<String,String> cmcc =new HashMap<String, String>();
			Map<String, String> cucc =new HashMap<String, String>();
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
				if(!"".equals(s.trim())){
					String st[]=s.split("&&");
					cmcc.put(st[0], st[2]);
					cucc.put(st[1], st[2]);
				}
			}
			maps.put("CTCC", cmcc);
			maps.put("CUCC", cucc);
			br.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		return maps;
	}
	
}
