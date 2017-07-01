/*
 * 创酷互动® 2016
 */
package me.ckhd.opengame.stats.callable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.drds.service.EventService;
import me.ckhd.opengame.evnet.entity.AppEventStat;
import me.ckhd.opengame.stats.entity.GameReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author qibiao
 * @date 2016-06-14
 * 活跃用户数统计，按渠道分组
 */
public class ActiveUserNumCallable implements Callable<Map<Long,Map<String,Integer>>>{

	protected static Logger logger = LoggerFactory
			.getLogger(ActiveUserNumCallable.class);
	
	private GameReport qryCnd;
	
	public ActiveUserNumCallable(GameReport qryCnd){
		this.qryCnd = qryCnd;
	}
	
	/**
	 * 返回结果集ex:{1={activeUserNum=10},2={activeUserNum=16}}
	 */
	@Override
	public Map<Long,Map<String,Integer>> call() {

		try {
			EventService eventService = SpringContextHolder
					.getBean(EventService.class);

			AppEventStat cnd = new AppEventStat();
			cnd.setCkAppId(qryCnd.getCkAppId());
			cnd.setStartDate(qryCnd.getDate());

			// 查询活跃
			List<Map<String, Object>> activeUsers = eventService.getActiveUserNumChannelId(cnd);
			Map<Long,Map<String,Integer>> result = new HashMap<Long,Map<String,Integer>>();
			
			for(Map<String, Object> activeUser: activeUsers){
				Map<String,Integer> data = new HashMap<String,Integer>();
				data.put("activeUserNum", ((Long)activeUser.get("num")).intValue());
				// 总用户
				data.put("activeUserNum2", ((Long)activeUser.get("allnum")).intValue());
				String channelid = (String)activeUser.get("channelid");
				if(channelid == null){
					channelid = "-1";
				}
				result.put(Long.parseLong(channelid), data);
			}
//			System.out.println(result);
			return result;
		} catch (Throwable t) {
			logger.error("",t);
			return new HashMap<Long,Map<String,Integer>>();
		}
	
	}

}
