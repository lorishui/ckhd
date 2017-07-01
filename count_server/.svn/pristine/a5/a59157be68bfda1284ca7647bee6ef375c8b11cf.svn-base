package com.chkd.count.count.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.LogUtils;

public class TaskList {
	//private static List<JSONObject> list = new ArrayList<JSONObject>();
	private static JSONObject count = new JSONObject();
	
	@SuppressWarnings("unchecked")
	public static List<JSONObject> getCountList(String name){
		init();
		return (List<JSONObject>)count.get(name);
	}
	
	public static void init(){
		if(count.size()==0){
			Properties pro = new Properties();
			try {
				LogUtils.log.info("读取配置文件");
				pro.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("countlist.properties"));
				Set<Object> set = pro.keySet();
				for(Object obj : set){
					List<JSONObject> list = new ArrayList<JSONObject>();
					String name = pro.getProperty((String)obj);
					LogUtils.log.info("配置文件内容:"+name);
					String[] countArr = name.split(",");
					for(String key:countArr){
						JSONObject json = new JSONObject();
						json.put("countName", key);
						list.add(json);
					}
					count.put((String)obj, list);
				}
			} catch (Throwable e) {
				LogUtils.log.error("导入任务模块的配置文件出现问题!",e);
			}
		}
	}
}
