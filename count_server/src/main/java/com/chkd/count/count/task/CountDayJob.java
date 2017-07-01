package com.chkd.count.count.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.DateUtils;
import com.chkd.count.common.utils.LogUtils;
import com.chkd.count.common.utils.QueueUtils;

/**
 * 每天统计一次
 * @author ASUS
 *
 */
public class CountDayJob {
	Logger logger = LoggerFactory.getLogger(CountJob.class);
	
	protected void execute(){
		LogUtils.log.info("定时任务启动");
		try {
			List<JSONObject> list = TaskList.getCountList("dayName");
			for( JSONObject json:list ){
				json.put("start", getCountTimeByDay());
				json.put("end", DateUtils.formatDateToStr("yyyy-MM-dd HH:mm:ss"));
				QueueUtils.offer(json.toJSONString());
			}
		} catch (Throwable e) {
			logger.error("count is error!", e);
		}
	}
	
	private String getCountTimeByDay(){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR,-1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}
}
