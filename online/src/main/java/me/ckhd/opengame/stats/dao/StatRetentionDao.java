package me.ckhd.opengame.stats.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.common.persistence.CrudDao;
import me.ckhd.opengame.stats.entity.StatRetention;

@Repository
public interface StatRetentionDao extends CrudDao<StatRetention> {

	List<StatRetention> statsData(StatRetention srd);

	List<StatRetention> statsLTVData(StatRetention statLTV);

}
