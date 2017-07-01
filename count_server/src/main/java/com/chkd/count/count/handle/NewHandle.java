package com.chkd.count.count.handle;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.LogUtils;
import com.mysql.jdbc.StringUtils;

@Component("new")
public class NewHandle extends BaseHandle{
	
	public NewHandle() {
		beforeInit();
	}
	
	/**
	 * imei idfa
	 */
	@Override
	public void handle(JSONObject json){
		StringBuffer sql = new StringBuffer();
		json.put("type", 2);
		try{
			LogUtils.log.info("act count start time:"+System.currentTimeMillis());
			LogUtils.log.info("新增统计开始执行");
			String start = json.getString("start");
			if( !StringUtils.isNullOrEmpty(start) && start.length()>13 ){
				start = start.substring(0, 13);
			}
			String lineSeparator = System.getProperty("line.separator", "\n"); 
			sql.append("REPLACE INTO count_new_device(timeframe,ckAppId,childCkAppId,ckChannelId,childChannelId,newNum) ").append(lineSeparator)
			.append("select date_format(a.createTime, '%Y%m%d%H') as timeframe,a.ckAppId,a.childCkAppId,a.ckChannelId,a.childChannelId,count(0) ").append(lineSeparator)
			.append("FROM  app_device_info  a ").append(lineSeparator)
			.append("WHERE a. createTime >='").append(start).append(":00:00' AND a.createTime <='").append(start).append(":59:59' ").append(lineSeparator)
			.append("GROUP BY a.ckAppId,a.childChannelId,a.ckChannelId,a.childCkAppId,timeframe");
			LogUtils.log.info("根据设备号统计的新增语句:"+sql.toString());
			online.save(sql.toString());
			LogUtils.log.info("act count end time:"+System.currentTimeMillis());
			json.put("ErrorCode", 0);
			json.put("ErrorMsg", "SUCCESS");
			json.put("insertSql", replaceSql(sql.toString()));
		}catch(Throwable e){
			json.put("ErrorCode", -1);
			json.put("ErrorMsg", "新增统计失败");
			json.put("insertSql", sql.toString());
			LogUtils.log.error("根据设备号统计的新增语句失败", e);
		}
	}
	
	private String replaceSql(String sql){
		sql = sql.replace("\\", "\\\\");
		sql = sql.replace("'", "\\'");
		sql = sql.replace("\"", "\\\"");
		return sql;
	}

}