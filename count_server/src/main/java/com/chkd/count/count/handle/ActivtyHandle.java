package com.chkd.count.count.handle;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.LogUtils;
import com.mysql.jdbc.StringUtils;

@Component("act")
public class ActivtyHandle extends BaseHandle{
	
	public ActivtyHandle() {
		beforeInit();
	}
	
	/**
	 * imei idfa
	 */
	@Override
	public void handle(JSONObject json){
		StringBuffer sql = new StringBuffer();
		json.put("type", 1);
		try{
			LogUtils.log.info("act count start time:"+System.currentTimeMillis());
			LogUtils.log.info("活跃统计开始执行");
			String start = json.getString("start");
			if( !StringUtils.isNullOrEmpty(start) && start.length()>13 ){
				start = start.substring(0, 13);
			}
			String lineSeparator = System.getProperty("line.separator", "\n"); 
			sql.append("REPLACE INTO count_act_device(timeframe,ckAppId,childCkAppId,ckChannelId,childChannelId,actNum) ").append(lineSeparator)
			.append("SELECT DATE_FORMAT(a.createDate, '%Y%m%d%H') AS timeframe,a.ckAppId,a.childCkAppId,a.ckChannelId,a.childChannelId,COUNT(DISTINCT a.`uuid`) actNum ").append(lineSeparator)
			.append("FROM `event_user_role` a ").append(lineSeparator)
			.append("WHERE a.createDate >='").append(start).append(":00:00' AND a.createDate <='").append(start).append(":59:59' ").append(lineSeparator)
			.append("GROUP BY ckAppId,childCkAppId,ckChannelId,childChannelId,timeframe");
			LogUtils.log.info("根据设备号统计的每小时活跃语句:"+sql.toString());
			online.save(sql.toString());
			//天统计
			sql.setLength(0);
			if( !StringUtils.isNullOrEmpty(start) && start.length()>10 ){
				start = start.substring(0, 10);
			}
			sql.append("REPLACE INTO count_act_device(timeframe,ckAppId,childCkAppId,ckChannelId,childChannelId,actNum) ").append(lineSeparator)
			.append("SELECT DATE_FORMAT(a.createDate, '%Y%m%d') AS timeframe,a.ckAppId,a.childCkAppId,a.ckChannelId,a.childChannelId,COUNT(DISTINCT a.`uuid`) actNum ").append(lineSeparator)
			.append("FROM `event_user_role` a ").append(lineSeparator)
			.append("WHERE a.createDate >='").append(start).append(" 00:00:00' AND a.createDate <='").append(start).append(" 23:59:59' ").append(lineSeparator)
			.append("GROUP BY ckAppId,childCkAppId,ckChannelId,childChannelId,timeframe");
			LogUtils.log.info("根据设备号统计的每天活跃语句:"+sql.toString());
			online.save(sql.toString());
			LogUtils.log.info("act count end time:"+System.currentTimeMillis());
			json.put("ErrorCode", 0);
			json.put("ErrorMsg", "SUCCESS");
			json.put("insertSql", replaceSql(sql.toString()));
		}catch(Throwable e){
			json.put("ErrorCode", -1);
			json.put("ErrorMsg", "活跃统计失败");
			json.put("insertSql", sql.toString());
			LogUtils.log.error("根据设备号统计的活跃语句失败", e);
		}
	}
	
	private String replaceSql(String sql){
		sql = sql.replace("\\", "\\\\");
		sql = sql.replace("'", "\\'");
		sql = sql.replace("\"", "\\\"");
		return sql;
	}

}
