/*
 * 创酷互动® 2016
 */
package me.ckhd.opengame.stats.callable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.stats.entity.GameReport;
import me.ckhd.opengame.stats.entity.OrderQry;
import me.ckhd.opengame.stats.service.MmOrderStatsService;
import me.ckhd.opengame.sys.utils.DBDataCacheUtils;

/**
 * @author qibiao
 * @date 2016-06-14
 * mm订单金额，按渠道分组
 */
public class MmOrderAmtCallable implements Callable<Map<Long,Map<String,Integer>>>{

	protected static Logger logger = LoggerFactory
			.getLogger(MmOrderAmtCallable.class);
	
	private GameReport qryCnd;
	
	public MmOrderAmtCallable(GameReport qryCnd){
		this.qryCnd = qryCnd;
	}
	/**
	 * {1={mmAmt=,mmSuccAmt=,mmOrderNum=,mmOrderSuccNum=,mmUserNum=,mmUserSuccNum=}}
	 */
	@Override
	public Map<Long,Map<String,Integer>> call() {

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
			List<Map<String, Object>> mmStatsList = statsService
					.mmStatsByChannel(qry);
			Map<Long, Map<String, Integer>> result = new HashMap<Long, Map<String, Integer>>();

			for (Map<String, Object> mmStats : mmStatsList) {

				String mmChannelId = (String) mmStats.get("mm_channel_id");
				Map<String, String> map = DBDataCacheUtils
						.getMMChannelCKChannelMap();

				long channelId = -1;
				try {
					channelId = Long.parseLong(map.get(mmChannelId));
					if (channelId == -1) {
						channelId = Long.parseLong(mmChannelId);
					}
				} catch (Throwable t) {
					logger.error("cal mm channelId error:", t);
				}
				// 已经有这个渠道需要合并
				Map<String, Integer> rst = result.get(channelId);
				if (rst != null) {
					rst.put("mmAmt",
							rst.get("mmAmt")
									+ ((BigDecimal) mmStats.get("mm_amt")).intValue());
					rst.put("mmSuccAmt", rst.get("mmSuccAmt")
							+ ((BigDecimal) mmStats.get("mm_succ_amt")).intValue());
					rst.put("mmOrderNum", rst.get("mmOrderNum")
							+ ((Long) mmStats.get("mm_order_num")).intValue());
					rst.put("mmOrderSuccNum", rst.get("mmOrderSuccNum")
							+ ((Long) mmStats.get("mm_order_succ_num")).intValue());
					rst.put("mmUserNum", rst.get("mmUserNum")
							+ ((Long) mmStats.get("mm_user_num")).intValue());
					rst.put("mmUserSuccNum", rst.get("mmUserSuccNum")
							+ ((Long) mmStats.get("mm_user_succ_num")).intValue());
				} else {
					Map<String, Integer> data = new HashMap<String, Integer>();
					data.put("mmAmt", ((BigDecimal) mmStats.get("mm_amt")).intValue());
					data.put("mmSuccAmt",
							((BigDecimal) mmStats.get("mm_succ_amt")).intValue());
					data.put("mmOrderNum",
							((Long) mmStats.get("mm_order_num")).intValue());
					data.put("mmOrderSuccNum",
							((Long) mmStats.get("mm_order_succ_num")).intValue());
					data.put("mmUserNum",
							((Long) mmStats.get("mm_user_num")).intValue());
					data.put("mmUserSuccNum",
							((Long) mmStats.get("mm_user_succ_num")).intValue());
					result.put(channelId, data);
				}
			}
//			System.out.println(result);
			return result;
		} catch (Throwable t) {
			logger.error("", t);
			return new HashMap<Long, Map<String, Integer>>();
		}

	}

}
