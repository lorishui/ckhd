package com.chkd.count.count.handle;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.DateUtils;
import com.chkd.count.common.utils.LogUtils;
import com.mysql.jdbc.StringUtils;

/**
 *次留统计
 * @author ASUS
 *
 */
@Component("firstRetention")
public class FirstRetentionHandle extends BaseHandle{
	
	public FirstRetentionHandle() {
		beforeInit();
	}
	
	/**
	 * imei idfa
	 */
	@Override
	public void handle(JSONObject json){
		StringBuffer sql = new StringBuffer();
		json.put("type", 6);
		try{
			LogUtils.log.info("retention count start time:"+System.currentTimeMillis());
			LogUtils.log.info("次留统计开始执行");
			String start = json.getString("start");
			if( !StringUtils.isNullOrEmpty(start)  ){
				start = start.substring(0, 10);
			}
			Date lastTime = DateUtils.addOrDelDate(DateUtils.parseDateByParsePatterns(json.getString("start"), "yyyy-MM-dd HH:mm:ss"), -1);
			String lastStr = DateUtils.formatDate(lastTime, "yyyy-MM-dd");
			int splitNum=10000;//分割行数
			
			//1.获取昨天的新增信息
			String deviceSql = "select ckAppId,childCkAppId,ckChannelId,childChannelId,deviceId from app_device_info a where a.createTime>='"+lastStr+" 00:00:00' and a.createTime<='"+lastStr+" 23:59:59'";
			List<Map<String,Object>> deviceList = online.getOnlineListData(deviceSql);
			String lineSeparato = System.lineSeparator();
			
			StringBuffer insertSql = new StringBuffer();
			insertSql.append("REPLACE INTO app_device_retention (timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,deviceId,num)VALUES");
			int n=0;
			//2.获取app对应设备信息
			for(Map<String,Object> device:deviceList){
				StringBuffer countSql = new StringBuffer("");
				countSql.append("SELECT COUNT(0) as num FROM event_user_role WHERE ckAppId =").append(device.get("ckAppId"))
						.append(" AND uuid ='").append(device.get("deviceId")).append("' AND createDate >= '")
						.append(start).append(" 00:00:00'").append(" AND createDate <='").append(start).append(" 23:59:59' AND type = 0");
				List<Map<String,Object>> numList = online.getOnlineListData(countSql.toString());
				if( numList != null && (Long)numList.get(0).get("num") > 0 ){
					if( n<=splitNum ){
						insertSql.append("('").append(start.replace("-", ""))
						.append("','").append(lastStr.replace("-", ""))
						.append("','").append(device.get("ckAppId"))
						.append("','").append(device.get("childCkAppId"))
						.append("','").append(device.get("ckChannelId"))
						.append("','").append(device.get("childChannelId"))
						.append("','").append(device.get("deviceId"))
						.append("',").append(1).append(")");
						if( n==splitNum ){
							insertSql.append(";").append(lineSeparato);
						}else{
							insertSql.append(",").append(lineSeparato);
						}
						n++;
					}else{
						online.save(insertSql.toString());
						n=0;
						insertSql = new StringBuffer();
						insertSql.append("REPLACE INTO app_device_retention (timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,deviceId,num)VALUES");
					}
				}
			}
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
			LogUtils.log.info("根据设备号统计的次留语句:"+countRetentionSql.toString());
			LogUtils.log.info("money retention end time:"+System.currentTimeMillis());
			json.put("ErrorCode", 0);
			json.put("ErrorMsg", "SUCCESS");
			json.put("insertSql", replaceSql(sql.toString()));
		}catch(Throwable e){
			json.put("ErrorCode", -1);
			json.put("ErrorMsg", "次留统计失败");
			json.put("insertSql", sql.toString());
			LogUtils.log.error("根据设备号统计的次留语句失败", e);
		}
	}
	
	private String replaceSql(String sql){
		sql = sql.replace("\\", "\\\\");
		sql = sql.replace("'", "\\'");
		sql = sql.replace("\"", "\\\"");
		return sql;
	}
	
}
