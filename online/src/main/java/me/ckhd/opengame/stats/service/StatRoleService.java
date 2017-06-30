package me.ckhd.opengame.stats.service;

import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.StatRoleDao;
import me.ckhd.opengame.stats.entity.StatRelated;

import org.springframework.stereotype.Service;

@Service
public class StatRoleService extends CrudService<StatRoleDao, StatRelated> {
	
	/**
	 * 新增统计
	 * @param statRelated
	 * @return
	 */
	public List<StatRelated> statsNew(StatRelated statRelated){
		return dao.statsNew(statRelated);
	}

}
