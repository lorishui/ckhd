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
 * @date 2016-06-14 新增用户数统计，按渠道分组
 */
public class NewUserNumCallable implements Callable<Map<Long,Map<String,Integer>>> {

	protected static Logger logger = LoggerFactory
			.getLogger(NewUserNumCallable.class);

	private GameReport qryCnd;

	public NewUserNumCallable(GameReport qryCnd) {
		this.qryCnd = qryCnd;
	}

	/**
	 * 返回结果集ex:{1={newUserNum=10},2={newUserNum=6}}
	 */
	@Override
	public Map<Long,Map<String,Integer>> call() {

		try {
			EventService eventService = SpringContextHolder
					.getBean(EventService.class);

			AppEventStat cnd = new AppEventStat();
			cnd.setCkAppId(qryCnd.getCkAppId());
			cnd.setStartDate(qryCnd.getDate());

			// 查询新增
			List<Map<String, Object>> newUsers = eventService.getNewAccoutChannelId(cnd);
			Map<Long,Map<String,Integer>> result = new HashMap<Long,Map<String,Integer>>();
			
			for(Map<String, Object> activeUser: newUsers){
				Map<String,Integer> data = new HashMap<String,Integer>();
				data.put("newUserNum", ((Long)activeUser.get("num")).intValue());
				data.put("newUserNum2", ((Long)activeUser.get("allnum")).intValue());
				
				try{
					result.put(Long.parseLong((String)activeUser.get("channelid")), data);
				}catch(Throwable t){
					// NOP
				}
			}
			
			return result;
		} catch (Throwable t) {
			logger.error("",t);
			return new HashMap<Long,Map<String,Integer>>();
		}

	}

}
