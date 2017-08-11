package me.ckhd.opengame.stats.dao;

import java.util.List;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.StatRelated;

import org.springframework.stereotype.Repository;

@Repository
public interface StatRoleDao extends CrudDao<StatRelated>{
	/**
	 * 新增统计
	 * @param statRelated
	 * @return
	 */
	public List<StatRelated> statsNew(StatRelated statRelated);
	
}
