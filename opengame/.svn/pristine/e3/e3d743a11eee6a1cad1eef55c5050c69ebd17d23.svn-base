/*
 * 创酷互动® 2016
 */
package me.ckhd.opengame.stats.callable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.stats.entity.GameReport;
import me.ckhd.opengame.stats.entity.OrderQry;
import me.ckhd.opengame.stats.service.MmOrderStatsService;
import me.ckhd.opengame.sys.utils.DBDataCacheUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qibiao
 * @date 2016-06-14 and订单金额，按渠道分组
 */
public class AndOrderAmtCallable implements
		Callable<Map<Long, Map<String, Integer>>> {

	protected static Logger logger = LoggerFactory
			.getLogger(AndOrderAmtCallable.class);

	private GameReport qryCnd;

	public AndOrderAmtCallable(GameReport qryCnd) {
		this.qryCnd = qryCnd;
	}
	/**
	 * {1={andAmt=,andSuccAmt=,andOrderNum=,andOrderSuccNum=,andUserNum=,andUserNum=,andUserSuccNum=}}
	 */
	@Override
	public Map<Long, Map<String, Integer>> call() {

		try {
			MmOrderStatsService statsService = SpringContextHolder
					.getBean(MmOrderStatsService.class);

			OrderQry qry = new OrderQry();
			qry.setCkAppId(qryCnd.getCkAppId());
			qry.setStartDate(qryCnd.getDate().replace("-", ""));
			qry.setEndDate(DateUtils.formatDate(
					DateUtils.nextDate(DateUtils.parseDate(qryCnd.getDate())),
					"yyyyMMdd"));

			// 查询活跃
			List<Map<String, Object>> andStatsList = statsService
					.andStatsByChannel(qry);
			Map<Long, Map<String, Integer>> result = new HashMap<Long, Map<String, Integer>>();

			for (Map<String, Object> andStats : andStatsList) {

				String andChannelId = (String) andStats.get("and_channel_id");
				Map<String, String> map = DBDataCacheUtils
						.getAndChannelCKChannelMap();

				long channelId = -1;
				try {
					channelId = Long.parseLong(map.get(andChannelId));
					if (channelId == -1) {
						channelId = Long.parseLong(andChannelId);
					}
				} catch (Throwable t) {
					logger.error("cal and channelId error:", t);
				}
				// 已经有这个渠道需要合并
				Map<String, Integer> rst = result.get(channelId);
				if (rst != null) {
					rst.put("andAmt",
							rst.get("andAmt")
									+ ((BigDecimal) andStats.get("and_amt")).intValue());
					rst.put("andSuccAmt", rst.get("andSuccAmt")
							+ ((BigDecimal) andStats.get("and_succ_amt")).intValue());
					rst.put("andOrderNum", rst.get("andOrderNum")
							+ ((Long) andStats.get("and_order_num")).intValue());
					rst.put("andOrderSuccNum", rst.get("andOrderSuccNum")
							+ ((Long) andStats.get("and_order_succ_num")).intValue());
					rst.put("andUserNum", rst.get("andUserNum")
							+ ((Long) andStats.get("and_user_num")).intValue());
					rst.put("andUserSuccNum", rst.get("andUserSuccNum")
							+ ((Long) andStats.get("and_user_succ_num")).intValue());
				} else {
					Map<String, Integer> data = new HashMap<String, Integer>();
					data.put("andAmt", ((BigDecimal) andStats.get("and_amt")).intValue());
					data.put("andSuccAmt",
							((BigDecimal) andStats.get("and_succ_amt")).intValue());
					data.put("andOrderNum",
							((Long) andStats.get("and_order_num")).intValue());
					data.put("andOrderSuccNum",
							((Long) andStats.get("and_order_succ_num")).intValue());
					data.put("andUserNum",
							((Long) andStats.get("and_user_num")).intValue());
					data.put("andUserSuccNum",
							((Long) andStats.get("and_user_succ_num")).intValue());
					result.put(channelId, data);
				}
			}
		//	System.out.println(result);
			return result;
		} catch (Throwable t) {
			logger.error("", t);
			return new HashMap<Long, Map<String, Integer>>();
		}

	}

}
