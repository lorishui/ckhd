package com.chkd.count.count.api;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.LogUtils;
import com.chkd.count.common.utils.SpringContextHolder;
import com.chkd.count.count.handle.BaseHandle;
import com.mysql.jdbc.StringUtils;

public class CountApi {
	
	public static void invoke(String data){
		LogUtils.log.info("处理模块开始执行");
		try{
			JSONObject json = null;
			if(!StringUtils.isNullOrEmpty(data)){
				json = JSONObject.parseObject(data);
				if(json.containsKey("countName")){
					LogUtils.log.info("处理模块执行的统计类型："+json.getString("countName"));
					BaseHandle base = SpringContextHolder.getBean(json.getString("countName"));
					if(base!=null){
					 	boolean isSuccess = base.invoke(json);
					 	LogUtils.log.info("count api "+json.getString("countName")+" "+isSuccess);
					}
				}
			}
		}catch(Throwable e){
			LogUtils.log.error("count error!",e);
		}
	}
	
}
