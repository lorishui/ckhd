package me.ckhd.opengame.stats.task;

import java.util.Calendar;
import java.util.Date;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.stats.service.OfflineMoneyCountSerivce;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndCountMoneyTask {
	
	public static Logger log = LoggerFactory.getLogger(AndCountMoneyTask.class);
	
	public static OfflineMoneyCountSerivce offlineMoneyCountSerivce = null;
	
	public static void countMMMoney(){
		String time = getDate();
		StringBuffer sql = new StringBuffer();
		sql.append("REPLACE INTO offline_middle_count_and(timeframe,ckAppId,ckChannelId,carriersChannelId,province,totalMoney,")
		.append("successMoney,payPeopleNum,payPeopleSuccessNum,payNum,paySuccessNum) ") 
		.append("SELECT ")
		.append("DATE_FORMAT(a.create_date,'%Y%m%d%H') AS timeframe,")
		.append("IF(a.`ckapp_id` IS NULL,'-1',a.`ckapp_id`),")
		.append("IF(b.`channel_id` IS NULL,'-1',b.`channel_id`),")
		.append("IF(a.`channelId` IS NULL,'-1',a.`channelId`),")
		.append("IF(a.`provinceId` IS NULL,'-1',a.`provinceId`),")
		.append("SUM(a.price),")
		.append("SUM(CASE WHEN a.hRet = '0' THEN a.price ELSE 0 END ),")
		.append("COUNT(DISTINCT a.userId) AS user_num,")
		.append("COUNT(DISTINCT CASE WHEN a.hRet = '0' THEN a.userId ELSE NULL END)  AS succ_user_num,")
		.append("COUNT(0),COUNT(CASE WHEN a.hRet = '0' THEN 0 ELSE NULL END) ")
		.append("FROM `and_app_order` a ")
		.append("LEFT JOIN `channel_carriers` b ON (a.`channelId`=b.`carriers_channelid`) ")
		.append("WHERE a.create_date >='").append(time).append(":00:00").append("' ")
		.append("GROUP BY a.`ckapp_id`,b.`channel_id`,a.`provinceId`,a.`channelId`,timeframe");
		if( offlineMoneyCountSerivce == null ){
			offlineMoneyCountSerivce = SpringContextHolder.getBean("OfflineMoneyCountSerivce");
		}
		StringBuffer sqlMsg = new StringBuffer("INSERT INTO t_count_message(id,countId,ErrorCOde,ErrorMsg,insertsql,createDate) VALUES (");
		sqlMsg.append("'").append(getId()).append("'").append(",2,");
		try{
			int n = offlineMoneyCountSerivce.saveDatas(sql.toString());
			if( n > 0 ){
				sqlMsg.append("0,'SUCCESS','").append(sql.toString().replace("'", "\\'")).append("','").append(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).append("');");
			}else{
				sqlMsg.append("2001,'FAILURE','").append(sql.toString().replace("'", "\\'")).append("','").append(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).append("');");
			}
		}catch(Throwable e){
			log.info("AndCountMoneyTask ERROR!", e);
			sqlMsg.append("'2002','','").append(sql.toString().replace("'", "\\'")).append("','")
			.append(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).append("');");
		}
		offlineMoneyCountSerivce.saveDatas(sqlMsg.toString());
	}
	
	public static String getId(){
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
		sb.append(date.getTime());
		sb.append(RandomUtils.nextInt(10)).append(1);
		return sb.toString();
	}
	
	public static String getDate(){
		Calendar cal = Calendar.getInstance();
		int n = cal.get(Calendar.MINUTE);
		if( n<=5 ){
			cal.add(Calendar.HOUR_OF_DAY, -1);
		}
		return DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH");
	}
}
