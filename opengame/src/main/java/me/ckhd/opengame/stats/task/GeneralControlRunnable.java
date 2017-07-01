package me.ckhd.opengame.stats.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.stats.callable.ActiveUserNumCallable;
import me.ckhd.opengame.stats.callable.AndOrderAmtCallable;
import me.ckhd.opengame.stats.callable.MmOrderAmtCallable;
import me.ckhd.opengame.stats.callable.NewUserNumCallable;
import me.ckhd.opengame.stats.entity.GameReport;
import me.ckhd.opengame.stats.service.GameReportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qibiao 归并计算结果
 */
public class GeneralControlRunnable implements Runnable {

	protected static Logger logger = LoggerFactory
			.getLogger(GameReportTask.class);

	// 任务线程池
	private static final ExecutorService service = Executors
			.newFixedThreadPool(8);

	public GameReport qryCnd;

	public GeneralControlRunnable(GameReport qryCnd) {
		this.qryCnd = qryCnd;
	}

	@Override
	public void run() {
		GameReportService gameReportService = SpringContextHolder
				.getBean(GameReportService.class);
		try {
			gameReportService.save(qryCnd);

			Future<Map<Long, Map<String, Integer>>> newUserNumFuture = service
					.submit(new NewUserNumCallable(qryCnd));
			Future<Map<Long, Map<String, Integer>>> activeUserNumFuture = service
					.submit(new ActiveUserNumCallable(qryCnd));

			Future<Map<Long, Map<String, Integer>>> mmStatsFuture = service
					.submit(new MmOrderAmtCallable(qryCnd));
			Future<Map<Long, Map<String, Integer>>> andStatsFuture = service
					.submit(new AndOrderAmtCallable(qryCnd));

			Map<Long, Map<String, Integer>> newUserNums = newUserNumFuture.get(
					5 * 60, TimeUnit.SECONDS);
			Map<Long, Map<String, Integer>> activeUserNums = activeUserNumFuture
					.get(5 * 60, TimeUnit.SECONDS);
			Map<Long, Map<String, Integer>> mmStatsNums = mmStatsFuture.get(5 * 60,
					TimeUnit.SECONDS);
			Map<Long, Map<String, Integer>> andStatsNums = andStatsFuture.get(
					5 * 60, TimeUnit.SECONDS);

			// System.out.println(newUserNums);
			// System.out.println(activeUserNums);

			Set<Long> set = new TreeSet<Long>();
			set.addAll(newUserNums.keySet());
			set.addAll(activeUserNums.keySet());
			set.addAll(mmStatsNums.keySet());
			set.addAll(andStatsNums.keySet());

			List<Map<String, Object>> rst = new ArrayList<Map<String, Object>>();

			int allNewUserNum = 0;
			// 含盗版的所有数目
			int allNewUserNum2 = 0;
			int allActiveUserNum = 0;
			// 含盗版的所有数目
			int allActiveUserNum2 = 0;
			int allMmAmt = 0;
			int allMmSuccAmt = 0;
			int allMmOrderNum = 0;
			int allMmOrderSuccNum = 0;
			int allMmUserNum = 0;
			int allMmUserSuccNum = 0;
			int allAndAmt = 0;
			int allAndSuccAmt = 0;
			int allAndOrderNum = 0;
			int allAndOrderSuccNum = 0;
			int allAndUserNum = 0;
			int allAndUserSuccNum = 0;

			boolean existXiaomi1 = false;
			Map<String, Integer> xiaomi115newUserNums = newUserNums.get(115L);
			Map<String, Integer> xiaomi115activeUserNums = activeUserNums.get(115L);
			Map<String, Integer> xiaomi115mmStatsNums = mmStatsNums.get(115L);
			Map<String, Integer> xiaomi115andStatsNums = andStatsNums.get(115L);
			
			int xiaomi115NewUserNum = 0;
			//
			int xiaomi115NewUserNum2 = 0;
			int xiaomi115ActiveUserNum = 0;
			//
			int xiaomi115ActiveUserNum2 = 0;
			int xiaomi115MmAmt = 0;
			int xiaomi115MmSuccAmt = 0;
			int xiaomi115MmOrderNum = 0;
			int xiaomi115MmOrderSuccNum = 0;
			int xiaomi115MmUserNum = 0;
			int xiaomi115MmUserSuccNum = 0;
			int xiaomi115AndAmt = 0;
			int xiaomi115AndSuccAmt = 0;
			int xiaomi115AndOrderNum = 0;
			int xiaomi115AndOrderSuccNum = 0;
			int xiaomi115AndUserNum = 0;
			int xiaomi115AndUserSuccNum = 0;
			if(xiaomi115newUserNums != null){
				xiaomi115NewUserNum = xiaomi115newUserNums.get("newUserNum");
				xiaomi115NewUserNum2 = xiaomi115newUserNums.get("newUserNum2");
			}
			if(xiaomi115activeUserNums != null){
				xiaomi115ActiveUserNum = xiaomi115activeUserNums.get("activeUserNum");
				xiaomi115ActiveUserNum2 = xiaomi115activeUserNums.get("activeUserNum2");
			}
			if(xiaomi115mmStatsNums != null){
				xiaomi115MmAmt = xiaomi115mmStatsNums.get("mmAmt");
				xiaomi115MmSuccAmt = xiaomi115mmStatsNums.get("mmSuccAmt");
				xiaomi115MmOrderNum = xiaomi115mmStatsNums.get("mmOrderNum");
				xiaomi115MmOrderSuccNum = xiaomi115mmStatsNums.get("mmOrderSuccNum");
				xiaomi115MmUserNum = xiaomi115mmStatsNums.get("mmUserNum");
				xiaomi115MmUserSuccNum = xiaomi115mmStatsNums.get("mmUserSuccNum");
			}
			if(xiaomi115andStatsNums != null){
				xiaomi115AndAmt = xiaomi115andStatsNums.get("andAmt");
				xiaomi115AndSuccAmt = xiaomi115andStatsNums.get("andSuccAmt");
				xiaomi115AndOrderNum = xiaomi115andStatsNums.get("andOrderNum");
				xiaomi115AndOrderSuccNum = xiaomi115andStatsNums.get("andOrderSuccNum");
				xiaomi115AndUserNum = xiaomi115andStatsNums.get("andUserNum");
				xiaomi115AndUserSuccNum = xiaomi115andStatsNums.get("andUserSuccNum");
			}
			for (long channelId : set) {
				
				// 苹果渠道新增活跃不计入
				if (channelId == 200L) {
					continue;
				}
				// xiaomi 1
				if(channelId == 1L){
					existXiaomi1 = true;
				}
				if(existXiaomi1 && channelId == 115L){
					continue;
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("channelId", channelId);
				Map<String, Integer> newUserNum = newUserNums.get(channelId);
				if (newUserNum != null) {
					map.putAll(newUserNum);
					if( channelId != 200L ){ // 苹果的不计入
						allNewUserNum += newUserNum.get("newUserNum");
						allNewUserNum2 += newUserNum.get("newUserNum2");
					}
				} else {
					map.put("newUserNum", 0);
					map.put("newUserNum2", 0);
				}
				
				if(channelId == 1L){
					map.put("newUserNum", xiaomi115NewUserNum + (Integer)map.get("newUserNum"));
					map.put("newUserNum2", xiaomi115NewUserNum2 + (Integer)map.get("newUserNum2"));
					allNewUserNum += xiaomi115NewUserNum;
					allNewUserNum2 += xiaomi115NewUserNum2;
				}
				
				Map<String, Integer> activeUserNum = activeUserNums
						.get(channelId);

				if (activeUserNum != null) {
					map.putAll(activeUserNum);
					if( channelId != 200L ){ // 苹果的不计入
						allActiveUserNum += activeUserNum.get("activeUserNum");
						allActiveUserNum2 += activeUserNum.get("activeUserNum2");
					}
				} else {
					map.put("activeUserNum", 0);
					map.put("activeUserNum2", 0);
				}
				
				if(channelId == 1L){
					map.put("activeUserNum", xiaomi115ActiveUserNum + (Integer)map.get("activeUserNum"));
					map.put("activeUserNum2", xiaomi115ActiveUserNum2 + (Integer)map.get("activeUserNum2"));
					allActiveUserNum += xiaomi115ActiveUserNum;
					allActiveUserNum2 += xiaomi115ActiveUserNum2;
				}
				
				Map<String, Integer> mmStatsNum = mmStatsNums.get(channelId);
				if (mmStatsNum != null) {
					map.putAll(mmStatsNum);
					allMmAmt += mmStatsNum.get("mmAmt");
					allMmSuccAmt += mmStatsNum.get("mmSuccAmt");
					allMmOrderNum += mmStatsNum.get("mmOrderNum");
					allMmOrderSuccNum += mmStatsNum.get("mmOrderSuccNum");
					allMmUserNum += mmStatsNum.get("mmUserNum");
					allMmUserSuccNum += mmStatsNum.get("mmUserSuccNum");
				} else {
					map.put("mmAmt", 0);
					map.put("mmSuccAmt", 0);
					map.put("mmOrderNum", 0);
					map.put("mmOrderSuccNum", 0);
					map.put("mmUserNum", 0);
					map.put("mmUserSuccNum", 0);
				}
				if(channelId == 1L){
					map.put("mmAmt", xiaomi115MmAmt + (Integer)map.get("mmAmt"));
					map.put("mmSuccAmt", xiaomi115MmSuccAmt + (Integer)map.get("mmSuccAmt"));
					map.put("mmOrderNum", xiaomi115MmOrderNum + (Integer)map.get("mmOrderNum"));
					map.put("mmOrderSuccNum", xiaomi115MmOrderSuccNum + (Integer)map.get("mmOrderSuccNum"));
					map.put("mmUserNum", xiaomi115MmUserNum + (Integer)map.get("mmUserNum"));
					map.put("mmUserSuccNum", xiaomi115MmUserSuccNum + (Integer)map.get("mmUserSuccNum"));
					allMmAmt += xiaomi115MmAmt;
					allMmSuccAmt += xiaomi115MmSuccAmt;
					allMmOrderNum += xiaomi115MmOrderNum;
					allMmOrderSuccNum += xiaomi115MmOrderSuccNum;
					allMmUserNum += xiaomi115MmUserNum;
					allMmUserSuccNum += xiaomi115MmUserSuccNum;
				}
				Map<String, Integer> andStatsNum = andStatsNums.get(channelId);
				if (andStatsNum != null) {
					map.putAll(andStatsNum);
					allAndAmt += andStatsNum.get("andAmt");
					allAndSuccAmt += andStatsNum.get("andSuccAmt");
					allAndOrderNum += andStatsNum.get("andOrderNum");
					allAndOrderSuccNum += andStatsNum.get("andOrderSuccNum");
					allAndUserNum += andStatsNum.get("andUserNum");
					allAndUserSuccNum += andStatsNum.get("andUserSuccNum");
				} else {
					map.put("andAmt", 0);
					map.put("andSuccAmt", 0);
					map.put("andOrderNum", 0);
					map.put("andOrderSuccNum", 0);
					map.put("andUserNum", 0);
					map.put("andUserSuccNum", 0);
				}
				if(channelId == 1L){
					map.put("andAmt", xiaomi115AndAmt + (Integer)map.get("andAmt"));
					map.put("andSuccAmt", xiaomi115AndSuccAmt + (Integer)map.get("andSuccAmt"));
					map.put("andOrderNum", xiaomi115AndOrderNum + (Integer)map.get("andOrderNum"));
					map.put("andOrderSuccNum", xiaomi115AndOrderSuccNum + (Integer)map.get("andOrderSuccNum"));
					map.put("andUserNum", xiaomi115AndUserNum + (Integer)map.get("andUserNum"));
					map.put("andUserSuccNum", xiaomi115AndUserSuccNum + (Integer)map.get("andUserSuccNum"));
					allAndAmt += xiaomi115AndAmt;
					allAndSuccAmt += xiaomi115AndSuccAmt;
					allAndOrderNum += xiaomi115AndOrderNum;
					allAndOrderSuccNum += xiaomi115AndOrderSuccNum;
					allAndUserNum += xiaomi115AndUserNum;
					allAndUserSuccNum += xiaomi115AndUserSuccNum;
				}
				rst.add(map);
			}
			Map<String, Object> totalMap = new HashMap<String, Object>();
			totalMap.put("channelId", "合计");
			totalMap.put("newUserNum", allNewUserNum);
			totalMap.put("newUserNum2", allNewUserNum2);
			totalMap.put("activeUserNum", allActiveUserNum);
			totalMap.put("activeUserNum2", allActiveUserNum2);
			
			totalMap.put("mmAmt", allMmAmt);
			totalMap.put("mmSuccAmt", allMmSuccAmt);
			totalMap.put("mmOrderNum", allMmOrderNum);
			totalMap.put("mmOrderSuccNum", allMmOrderSuccNum);
			totalMap.put("mmUserNum", allMmUserNum);
			totalMap.put("mmUserSuccNum", allMmUserSuccNum);

			totalMap.put("andAmt", allAndAmt);
			totalMap.put("andSuccAmt", allAndSuccAmt);
			totalMap.put("andOrderNum", allAndOrderNum);
			totalMap.put("andOrderSuccNum", allAndOrderSuccNum);
			totalMap.put("andUserNum", allAndUserNum);
			totalMap.put("andUserSuccNum", allAndUserSuccNum);

			rst.add(0, totalMap);
			qryCnd.setDataList(rst);
			gameReportService.updateSucc(qryCnd);
		} catch (Throwable t) {
			logger.error("", t);
			gameReportService.updateFail(qryCnd);
		}

	}

	public static void stop() {
		service.shutdown();
	}

}