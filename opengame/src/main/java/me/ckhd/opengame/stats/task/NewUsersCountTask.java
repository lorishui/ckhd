package me.ckhd.opengame.stats.task;

import java.util.Calendar;
import java.util.Date;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.drds.service.EventService;
import me.ckhd.opengame.stats.service.OfflineMoneyCountSerivce;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewUsersCountTask {
	public static Logger log = LoggerFactory.getLogger(NewUsersCountTask.class);
	public static OfflineMoneyCountSerivce offlineMoneyCountSerivce = null;
	public static EventService eventService = null;
	
	public static void countNewUsers(){
		String time = getDate();
		StringBuffer sql = new StringBuffer();
		String headSql = "REPLACE INTO count_new_users(timeframe,ckAppId,ckChannelId,newNum) values ";
		sql.append("select date_format(occur_time,'%Y%m%d%H') as timeframe,ckappid,channelid,count(0) as newNum ")
		.append("FROM `app_account` a ")
		.append("WHERE a.occur_time >='").append(time).append(":00:00").append("' AND a.occur_time <='").append(time).append(":59:59").append("' ")
		.append("GROUP BY ckappid,channelid,timeframe");
		if( offlineMoneyCountSerivce == null ){
			offlineMoneyCountSerivce = SpringContextHolder.getBean("OfflineMoneyCountSerivce");
		}
		if( eventService == null ){
			eventService = SpringContextHolder.getBean("EventService");
		}
		StringBuffer sqlMsg = new StringBuffer("INSERT INTO t_count_message(id,countId,ErrorCOde,ErrorMsg,insertsql,createDate) VALUES (");
		sqlMsg.append("'").append(getId()).append("'").append(",3,");
		try{
			String countSql = eventService.generateSql(headSql, sql.toString());
			if(StringUtils.isNotBlank(countSql)){
				int n = offlineMoneyCountSerivce.saveDatas(countSql);
				if( n > 0 ){
					sqlMsg.append("0,'SUCCESS','").append(sql.toString().replace("'", "\\'")).append("','").append(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).append("');");
				}else{
					sqlMsg.append("2001,'FAILURE','").append(sql.toString().replace("'", "\\'")).append("','").append(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).append("');");
				}
			}else{
				sqlMsg.append("2001,'FAILURE','").append(sql.toString().replace("'", "\\'")).append("','").append(DateUtils.getDate("yyyy-MM-dd HH:mm:ss")).append("');");
			}
		}catch(Throwable e){
			log.info("NewUsersCountTask ERROR!", e);
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
		if( n<5 ){
			cal.add(Calendar.HOUR_OF_DAY, -1);
		}
		return DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd HH");
	}
	
}