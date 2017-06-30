package me.ckhd.opengame.stats.service;

import java.util.List;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.StatRetentionDao;
import me.ckhd.opengame.stats.entity.StatRetention;

@Service
public class StatRetentionService extends CrudService<StatRetentionDao, StatRetention> {

	public List<StatRetention> statsData(StatRetention srd) {
		return dao.statsData(srd);
	}

	public List<StatRetention> statsLTVData(StatRetention statLTV) {
		return dao.statsLTVData(statLTV);
	}

}
