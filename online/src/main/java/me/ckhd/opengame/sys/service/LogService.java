/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/创酷互动">创酷互动</a> All rights reserved.
 */
package me.ckhd.opengame.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.ckhd.opengame.common.persistence.Page;
import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.sys.dao.LogDao;
import me.ckhd.opengame.sys.entity.Log;
@Service
@Transactional(readOnly = true)
public class LogService extends CrudService<LogDao, Log> {

	public Page<Log> findPage(Page<Log> page, Log log) {
		
		// 设置默认时间范围，默认当前月
		if (log.getBeginDate() == null){
			log.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (log.getEndDate() == null){
			log.setEndDate(DateUtils.addMonths(log.getBeginDate(), 1));
		}
		
		return super.findPage(page, log);
		
	}
	
}
