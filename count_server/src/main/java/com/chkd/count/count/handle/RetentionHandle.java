package com.chkd.count.count.handle;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.DateUtils;
import com.chkd.count.common.utils.LogUtils;

/**
 * 留存
 * @author ASUS
 *
 */
@Component("retention")
public class RetentionHandle extends BaseHandle{
	public RetentionHandle() {
		beforeInit();
	}
	
	/**
	 * imei idfa
	 */
	@Override
	public void handle(JSONObject json){
		StringBuffer sql = new StringBuffer();
		json.put("type", 4);
		try{
			LogUtils.log.info("retention count start time:"+System.currentTimeMillis());
			LogUtils.log.info("留存统计开始执行");
			String start = json.getString("start");
			/*if( !StringUtils.isNullOrEmpty(start)  ){
				start = start.replace("-", "");
			}*/
			int splitNum=5;//分割行数
			//1.获取活跃设备数
			StringBuffer actDevice = new StringBuffer();
			actDevice.append("SELECT DISTINCT UUID as deviceId,ckAppId FROM event_user_role ")
				.append(" WHERE createDate>='").append(start).append(" 00:00:00' ")
				.append(" AND createDate<='").append(start).append(" 23:59:59' AND TYPE=0");
			List<Map<String,Object>> actDeviceList = online.getOnlineListData(actDevice.toString());

			String lineSeparato = System.lineSeparator();
			
			StringBuffer insertSql = new StringBuffer();
			insertSql.append("REPLACE INTO app_device_retention (timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,deviceId,num)VALUES");
			int n=0;
			//2.获取app对应设备信息
			for(Map<String,Object> map:actDeviceList){
				StringBuffer deviceInfoSql = new StringBuffer(" select childCkAppId,ckChannelId,childChannelId,createTime from app_device_info a where a.ckAppId='");
				deviceInfoSql.append(map.get("ckAppId")).append("' and deviceId='").append(map.get("deviceId")).append("'");
				List<Map<String,Object>> deviceList = online.getOnlineListData(deviceInfoSql.toString());
				for( Map<String,Object> device : deviceList ){
					if( n<=splitNum ){
						Date endTime = DateUtils.parseDateByParsePatterns(start, "yyyy-MM-dd");
						Timestamp createTime = (Timestamp )device.get("createTime");
						String regTime = DateUtils.formatDate(createTime, "yyyy-MM-dd");
						Date startTime = DateUtils.parseDateByParsePatterns(regTime, "yyyy-MM-dd");
						long dayNum = DateUtils.getDateDifference(startTime, endTime);
						if( dayNum > 0 ){
							insertSql.append("('").append(start.replace("-", ""))
							.append("','").append(regTime.replace("-", ""))
							.append("','").append(map.get("ckAppId"))
							.append("','").append(device.get("childCkAppId"))
							.append("','").append(device.get("ckChannelId"))
							.append("','").append(device.get("childChannelId"))
							.append("','").append(map.get("deviceId"))
							.append("',").append(dayNum).append(")");
							if( n==splitNum ){
								insertSql.append(";").append(lineSeparato);
							}else{
								insertSql.append(",").append(lineSeparato);
							}
							n++;
						}
					}else{
						online.save(insertSql.toString());
						n=0;
						insertSql = new StringBuffer();
						insertSql.append("REPLACE INTO app_device_retention (timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,deviceId,num)VALUES");
					}
				}
			}
			LogUtils.log.info("count retention n="+n);
			if( n >0 && insertSql.length() > 0 ){
				insertSql.setLength(insertSql.length()-(lineSeparato.length()+1));
				insertSql.append(";");
				online.save(insertSql.toString());//不够1w补insert
			}
			StringBuffer countRetentionSql = new StringBuffer();
			countRetentionSql.append("REPLACE INTO count_retention_device(timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,retentionDay,num)")
				.append(" select timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,num,count(0) ")
				.append(" from app_device_retention ")
				.append(" where timeframe='").append(start.replace("-", "")).append("'")
				.append(" GROUP BY ckAppId,childAppId,channelId,childChannelId,num,timeframe,regTime ");
			online.save(countRetentionSql.toString());
			LogUtils.log.info("根据设备号统计的留存语句:"+countRetentionSql.toString());
			LogUtils.log.info("device retention end time:"+System.currentTimeMillis());
			json.put("ErrorCode", 0);
			json.put("ErrorMsg", "SUCCESS");
			json.put("insertSql", replaceSql(sql.toString()));
		}catch(Throwable e){
			LogUtils.log.error("根据设备号统计的留存语句失败", e);
			json.put("ErrorCode", -1);
			json.put("ErrorMsg", "留存统计失败");
			json.put("insertSql", sql.toString());
		}
	}
	
	private String replaceSql(String sql){
		sql = sql.replace("\\", "\\\\");
		sql = sql.replace("'", "\\'");
		sql = sql.replace("\"", "\\\"");
		return sql;
	}
}