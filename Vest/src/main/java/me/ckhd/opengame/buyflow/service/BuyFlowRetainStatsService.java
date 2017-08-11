/*
 * ckhd
 */
package me.ckhd.opengame.buyflow.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.buyflow.dao.BuyFlowRetainStatsDao;
import me.ckhd.opengame.buyflow.entity.BuyFlowRetainStats;
import me.ckhd.opengame.common.service.CrudService;

/**
 * ckhd
 */
@Service
public class BuyFlowRetainStatsService extends
		CrudService<BuyFlowRetainStatsDao, BuyFlowRetainStats> {

	public Map<String, Long> queryRetain(BuyFlowRetainStats vo) {

		Map<String, Long> queryResult = new HashMap<String, Long>();

		List<Map<String, Object>> retains = dao.queryRetain(vo);
		for (Map<String, Object> retain : retains) {
			queryResult.put((String) retain.get("key"),
					((BigDecimal) retain.get("retainNum")).longValue());
		}

		return queryResult;

	}
	
	public Map<String, Long> queryRetainByDay(BuyFlowRetainStats vo) {
		
		Map<String, Long> queryResult = new HashMap<String, Long>();
		
		List<Map<String, Object>> retains = dao.queryRetainByDay(vo);
		for (Map<String, Object> retain : retains) {
			queryResult.put((String) retain.get("key"),
					((BigDecimal) retain.get("retainNum")).longValue());
		}
		
		return queryResult;
		
	}

	public Map<String, Long> queryMediaRetain(BuyFlowRetainStats vo) {
		Map<String, Long> queryResult = new HashMap<String, Long>();

		List<Map<String, Object>> retains = dao.queryMediaRetain(vo);
		for (Map<String, Object> retain : retains) {
			queryResult.put((String) retain.get("key"),
				 ((BigDecimal) retain.get("retainNum")).longValue());
		}

		return queryResult;
	}

}
