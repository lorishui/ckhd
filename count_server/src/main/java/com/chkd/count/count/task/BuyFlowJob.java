package com.chkd.count.count.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chkd.count.count.dao.OnlineCurdDataHandle;

@Component
@Lazy(false)
public class BuyFlowJob {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String DATE_FORMAT = "yyyyMMdd";

	private static final int[] CAL_CYCLE = { 0, 1, 2, 3, 7, 30, 60 };

	// 基本统计：点击数，设备点击数（按设备去重），激活数，注册数
	final String baseStatsSql = "REPLACE INTO buy_flow_base_stats(ckapp_id,child_ckapp_id,media,ad_item,stats_date,click_num,device_click_num,active_num,register_num) "
			+ "SELECT ckapp_id, child_ckapp_id, media, ad_item, DATE_FORMAT(monitor_time, '%Y%m%d') AS stats_date, COUNT(0) AS click_num,COUNT(distinct device_id) AS device_click_num, COUNT(CASE WHEN state in(1,2) THEN 1 ELSE NULL END) AS active_num, COUNT(CASE WHEN state =2 THEN 1 ELSE NULL END) AS register_num "
			+ "FROM buy_flow WHERE monitor_time >= ? "
			+ "GROUP BY ckapp_id, child_ckapp_id, media, ad_item, stats_date;";

	// 统计日期（stats_date）N日（days）后的留存数
	final String activityNumStatsSql = "REPLACE INTO buy_flow_retain_stats(ckapp_id, child_ckapp_id, media, ad_item, stats_date, days, retain_num) "
			+ "select a.ckAppId,a.childCkAppId,b.media,b.ad_item,? as stats_date,? as days,count(distinct a.uuid) as num "
			+ "from event_user_role a,app_device_info b "
			+ "where b.media!= '0' and b.ad_item!= '0' and a.ckAppId=b.ckAppId and a.uuid=b.deviceId and a.type=0 and "
			+ "b.createtime>? and b.createtime<? "
			+ "and a.createDate>? and a.createDate<? "
			+ "group by a.ckAppId,a.childCkAppId,b.media,b.ad_item;";

	// 留存收入：统计日期（stats_date）新增的设备N日（days）后的付费设备数及付费金额
	final String payStatsSql = "REPLACE INTO buy_flow_retain_income_stats(ckapp_id, child_ckapp_id, media, ad_item, stats_date, days, device_num, income) "
			+ "SELECT b.ckAppId as ckapp_id, b.childCkAppId as child_ckapp_id, b.media, b.ad_item, ? AS stats_date, ? AS days,count(distinct a.deviceid) AS device_num, SUM(a.prices) AS income "
			+ "FROM app_online_pay a, app_device_info b "
			+ "WHERE a.ckappid = b.ckappid AND a.deviceid = b.deviceId AND a.orderStatus = 3 AND a.create_date>? AND a.create_date<? AND b.createTime>? AND b.createTime<? "
			+ "GROUP BY b.ckAppId, b.childCkAppId, b.media, b.ad_item";

	// 总收入：统计日期（stats_date）付费设备数及付费金额
	final String totalPayStatsSql = "REPLACE INTO buy_flow_total_income_stats(ckapp_id,child_ckapp_id,media,ad_item,stats_date,device_num,income) "
			+ "SELECT a.ckAppId as ckapp_id, a.childCkAppId as child_ckapp_id, b.media, b.ad_item, DATE_FORMAT(a.create_date, '%Y%m%d') AS stats_date, COUNT(DISTINCT a.deviceId) AS device_num, SUM(a.prices) AS income "
			+ "FROM app_online_pay a, app_device_info b "
			+ "WHERE a.deviceId = b.deviceId and a.ckAppId =b.ckappId AND a.create_date>? AND a.orderStatus=3 "
			+ "GROUP BY a.ckAppId, a.childCkAppId, b.media, b.ad_item, stats_date;";

	@Autowired
	public OnlineCurdDataHandle online;

	@Scheduled(fixedDelay = 15 * 60 * 1000)
	public void baseStats() {
		long start = System.currentTimeMillis();

		Calendar now = Calendar.getInstance();
		// 减30分钟,凌晨00:30前覆盖执行前一天的数据
		now.add(Calendar.MINUTE, -30);
		String statsDate = DateFormatUtils.format(now.getTime(),
				DATE_FORMAT);
		try {
			online.update(baseStatsSql, statsDate);
			logger.info("count cost(ms):" + (System.currentTimeMillis() - start));
		} catch (Throwable t) {
			logger.error("error", t);
		}
	}

	/**
	 * 计算基于N日M天后活跃（留存使用）<br/>
	 * 计算基于N日M天后全部金额（LTV）<br/>
	 * 其中：M = 0,1,2,3,7,30,60
	 */
	@Scheduled(fixedDelay = 15 * 60 * 1000)
	public void retainStats() {

		// 新增后反向写值
		try {
			// 今天是2017-05-20，需要计算2017-05-20，2017-05-19,2017-05-18,2017-05-17
			// 计算方法
			// N日新增的M天后活跃数：select count(distinct imei) from table where date =
			// '2017-05-20' and is exists(select 1 from buy_flow and T-N)
			Set<String> set = new HashSet<String>();

			Calendar now = Calendar.getInstance();
			set.add(DateFormatUtils.format(now.getTime(), DATE_FORMAT));
			// 凌晨00:30之前覆盖统计昨天的
			now.add(Calendar.MINUTE, -30);
			set.add(DateFormatUtils.format(now.getTime(), DATE_FORMAT));

			for (String statsDate : set) {
				for (int cycle : CAL_CYCLE) {
					long start = System.currentTimeMillis();
					online.update(activityNumStatsSql,
							offsetDate(statsDate, -cycle), cycle,
							offsetDate(statsDate, -cycle),
							offsetDate(statsDate, -cycle + 1), statsDate,
							offsetDate(statsDate, 1));
					logger.info("cost(ms):"
							+ (System.currentTimeMillis() - start));
				}
			}

		} catch (Throwable t) {
			logger.info("", t);
		}
	}

	@Scheduled(fixedDelay = 15 * 60 * 1000)
	public void payStats() {

		try {
			Calendar now = Calendar.getInstance();
			// 减30分钟
			now.add(Calendar.MINUTE, -30);
			String statsDate = DateFormatUtils.format(now.getTime(),
					DATE_FORMAT);

			for (int days = 0; days <= 60; days++) {
				// 1
				long start = System.currentTimeMillis();
				online.update(payStatsSql, offsetDate(statsDate, -days), days,
						statsDate, offsetDate(statsDate, 1),
						offsetDate(statsDate, -days),
						offsetDate(statsDate, -days + 1));
				logger.info("cost(ms):" + (System.currentTimeMillis() - start));
			}
		} catch (Throwable t) {
			logger.info("error", t);
		}
	}
	
	/**
	 * 总金额
	 */
	@Scheduled(fixedDelay = 15 * 60 * 1000)
	public void totalPayStats() {

		Calendar now = Calendar.getInstance();
		// 减30分钟
		now.add(Calendar.MINUTE, -30);
		String statsDate = DateFormatUtils.format(now.getTime(),
				DATE_FORMAT);
		
		 online.update(totalPayStatsSql, statsDate);

	}
	
	public String offsetDate(String statsDate, int offsetDays)
			throws ParseException {
		if (offsetDays == 0) {
			return statsDate;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(statsDate));
		calendar.add(Calendar.DAY_OF_MONTH, offsetDays);

		return sdf.format(calendar.getTime());
	}
}
