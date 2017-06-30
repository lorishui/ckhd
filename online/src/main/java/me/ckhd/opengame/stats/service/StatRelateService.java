package me.ckhd.opengame.stats.service;

import java.util.List;

import me.ckhd.opengame.common.service.CrudService;
import me.ckhd.opengame.stats.dao.StatRelatedDao;
import me.ckhd.opengame.stats.entity.StatRelated;

import org.springframework.stereotype.Service;

@Service
public class StatRelateService extends CrudService<StatRelatedDao, StatRelated> {
	
	/**
	 * 新增统计
	 * @param statRelated
	 * @return
	 */
	public List<StatRelated> statsNew(StatRelated statRelated){
		return dao.statsNew(statRelated);
	}

	/**
	 * 活跃统计
	 * @param statRelated
	 * @return
	 */
	public List<StatRelated> statsAct(StatRelated statRelated){
		return dao.statsAct(statRelated);
	}

	public List<StatRelated> getTotalNum(StatRelated statRelated) {
		return dao.getTotalNum(statRelated);
	}

	public List<StatRelated> getTotalActNum(StatRelated statRelated) {
		return dao.getTotalActNum(statRelated);
	}
}
