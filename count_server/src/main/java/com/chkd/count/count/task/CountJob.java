package com.chkd.count.count.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import me.ckhd.opengame.stats.task.AndCountMoneyTask;
//import me.ckhd.opengame.stats.task.MMCountMoneyTask;





import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.DateUtils;
import com.chkd.count.common.utils.LogUtils;
import com.chkd.count.common.utils.QueueUtils;

/**
 * 3-5 分钟统计一次
 * @author ASUS
 *
 */
public class CountJob {
	
	Logger logger = LoggerFactory.getLogger(CountJob.class);
	
	protected void execute(){
		LogUtils.log.info("定时任务启动");
		try {
			List<JSONObject> list = TaskList.getCountList("name");
			if( list!= null && list.size()>0 ){
				for( JSONObject json:list ){
					json.put("start", getCountTime());
					json.put("end", DateUtils.formatDateToStr("yyyy-MM-dd HH:mm:ss"));
					QueueUtils.offer(json.toJSONString());
				}
			}
		} catch (Throwable e) {
			logger.error("count is error!", e);
		}
	}
	
	private String getCountTime(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, -5);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(cal.getTime());
	}
}
