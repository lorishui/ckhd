/*
 * 
 */
package me.ckhd.opengame.app.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.PurchaseSwitch;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.StringUtils;

/**
 * 付费开关
 * 
 * @author qibiao
 */
public class PurchaseSwitchService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	
	
	/**
	 * @param vo
	 * @return
	 */
	public boolean judge(Cfgparam vo) {

		// 根据策略编号路由到策略规则
		List<PurchaseSwitch> rules = new ArrayList<PurchaseSwitch>();

		// 当前日期
		String currDate = DateUtils.getDate();
		String currTime = DateUtils.getTime();

		if (rules == null || rules.isEmpty()) {
			// 没有配置规则，则全开
			return true;
		}
		for (PurchaseSwitch rule : rules) {
			// 匹配日期，weekend，default依次在最后
			if (rule.getDates() == null) {
				// 配置数据异常，需要检查代码
				logger.error("[code_error]", "配置数据不符合规范，请检查配置！");
				return false;
			}
			// 保证顺序，weekend、default依次在最后
			if (rule.isUsable()
					&& (rule.getDates().indexOf(currDate) >= 0
							|| ("weekend".equals(rule.getDates()) && DateUtils
									.isWeekend()) || "default".equals(rule
							.getDates()))) {
				if (StringUtils.isBlank(rule.getTimes())) {
					// 没有配置时间段=全天开启
					return true;
				} else {
					// 匹配时间段，格式如：09:00-13:00;15:05-22:30
					String[] times = rule.getTimes().split(";");
					// 09:00-13:00
					for (String timeArea : times) {
						String[] timeAreas = timeArea.split("-");
						if (currTime.compareTo(timeAreas[0]) >= 0
								&& currTime.compareTo(timeAreas[1]) <= 0) {
							// 当前时间在配置时间段
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
