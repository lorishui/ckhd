package com.chkd.count.count.handle;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.LogUtils;
import com.mysql.jdbc.StringUtils;

@Component("money")
public class MoneyHandle extends BaseHandle{
	
	public MoneyHandle() {
		beforeInit();
	}
	
	/**
	 * imei idfa
	 */
	@Override
	public void handle(JSONObject json){
		StringBuffer sql = new StringBuffer();
		json.put("type", 3);
		try{
			LogUtils.log.info("money count start time:"+System.currentTimeMillis());
			LogUtils.log.info("金额统计开始执行");
			String start = json.getString("start");
			if( !StringUtils.isNullOrEmpty(start) && start.length()>13 ){
				start = start.substring(0, 13);
			}
			String lineSeparator = System.getProperty("line.separator", "\n"); 
			sql.append("REPLACE INTO `count_money_device`(timeframe,ckAppId,childCkAppId,ckChannelId,childChannelId,money,successMoney,payPeopleNum,payTimes,paySuccessPeopleNum,paySuccessTimes) ").append(lineSeparator)
			.append("SELECT DATE_FORMAT(a.`create_date`,'%Y%m%d%H') AS timeframe, ").append(lineSeparator)
			.append("a.`ckAppId`,IFNULL(a.`childCkAppId`,1),a.`channelId`,IFNULL(a.`childChannelId`,1), ").append(lineSeparator)
			.append("SUM(a.`prices`),SUM(IF(a.`orderStatus`=3,a.`actualAmount`,0)),").append(lineSeparator)
			.append("COUNT(DISTINCT a.`deviceId`),COUNT(0), ").append(lineSeparator)
			.append("COUNT(DISTINCT CASE WHEN a.`orderStatus` = '3' THEN a.`deviceId` ELSE NULL END ), ").append(lineSeparator)
			.append("COUNT(CASE WHEN a.`orderStatus` = '3' THEN 0 ELSE NULL END) ").append(lineSeparator)
			.append("FROM app_online_pay a ").append(lineSeparator)
			.append("WHERE a.create_date >='").append(start).append(":00:00' AND a.create_date <='").append(start).append(":59:59' ").append(lineSeparator)
			.append("GROUP BY timeframe,a.`ckAppId`,a.`childCkAppId`,a.`channelId`,a.`childChannelId`");
			LogUtils.log.info("根据设备号统计的金额语句:"+sql.toString());
			online.save(sql.toString());
			LogUtils.log.info("money count end time:"+System.currentTimeMillis());
			//统计每天
			if( !StringUtils.isNullOrEmpty(start) && start.length()>13 ){
				start = start.substring(0, 10);
			}
			sql = new StringBuffer();
			if( !StringUtils.isNullOrEmpty(start) && start.length()>10 ){
				start = start.substring(0, 10);
			}
			sql.append("REPLACE INTO `count_money_device`(timeframe,ckAppId,childCkAppId,ckChannelId,childChannelId,money,successMoney,payPeopleNum,payTimes,paySuccessPeopleNum,paySuccessTimes) ").append(lineSeparator)
			.append("SELECT DATE_FORMAT(a.`create_date`,'%Y%m%d') AS timeframe, ").append(lineSeparator)
			.append("a.`ckAppId`,IFNULL(a.`childCkAppId`,1),a.`channelId`,IFNULL(a.`childChannelId`,1), ").append(lineSeparator)
			.append("SUM(a.`prices`),SUM(IF(a.`orderStatus`=3,a.`actualAmount`,0)),").append(lineSeparator)
			.append("COUNT(DISTINCT a.`deviceId`),COUNT(0), ").append(lineSeparator)
			.append("COUNT(DISTINCT CASE WHEN a.`orderStatus` = '3' THEN a.`deviceId` ELSE NULL END ), ").append(lineSeparator)
			.append("COUNT(CASE WHEN a.`orderStatus` = '3' THEN 0 ELSE NULL END) ").append(lineSeparator)
			.append("FROM app_online_pay a ").append(lineSeparator)
			.append("WHERE a.create_date >='").append(start).append(" 00:00:00' AND a.create_date <='").append(start).append(" 23:59:59' ").append(lineSeparator)
			.append("GROUP BY timeframe,a.`ckAppId`,a.`childCkAppId`,a.`channelId`,a.`childChannelId`");
			online.save(sql.toString());
			LogUtils.log.info("根据设备号统计的每天的金额语句:"+sql.toString());
			json.put("ErrorCode", 0);
			json.put("ErrorMsg", "SUCCESS");
			json.put("insertSql", replaceSql(sql.toString()));
		}catch(Throwable e){
			json.put("ErrorCode", -1);
			json.put("ErrorMsg", "金额统计失败");
			json.put("insertSql", sql.toString());
			LogUtils.log.error("根据设备号统计的金额语句失败", e);
		}
	}
	
	private String replaceSql(String sql){
		sql = sql.replace("\\", "\\\\");
		sql = sql.replace("'", "\\'");
		sql = sql.replace("\"", "\\\"");
		return sql;
	}
	
}