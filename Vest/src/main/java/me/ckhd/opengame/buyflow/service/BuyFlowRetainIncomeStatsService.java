package me.ckhd.opengame.buyflow.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ckhd.opengame.buyflow.dao.BuyFlowRetainIncomeStatsDao;
import me.ckhd.opengame.buyflow.entity.BuyFlowRetainIncomeStats;
import me.ckhd.opengame.common.service.CrudService;

import org.springframework.stereotype.Service;

@Service
public class BuyFlowRetainIncomeStatsService extends
		CrudService<BuyFlowRetainIncomeStatsDao, BuyFlowRetainIncomeStats> {

	public Map<String, Long> queryRetainIncome(BuyFlowRetainIncomeStats vo) {
		List<Map<String, Object>> retainIncomes = dao.queryRetainIncome(vo);

		Map<String, Long> queryResult = new HashMap<String, Long>();
		for (Map<String, Object> retainIncome : retainIncomes) {

			String key = (String) retainIncome.get("key");
			long retainIncome0 = ((BigDecimal) retainIncome
					.get("retainIncome0")).longValue();
			long retainIncome1 = ((BigDecimal) retainIncome
					.get("retainIncome1")).longValue();
			long retainIncome7 = ((BigDecimal) retainIncome
					.get("retainIncome7")).longValue();
			long retainIncome30 = ((BigDecimal) retainIncome
					.get("retainIncome30")).longValue();
			long retainIncome60 = ((BigDecimal) retainIncome
					.get("retainIncome60")).longValue();

			queryResult.put(key + "_0", retainIncome0);
			queryResult.put(key + "_1", retainIncome1);
			queryResult.put(key + "_7", retainIncome7);
			queryResult.put(key + "_30", retainIncome30);
			queryResult.put(key + "_60", retainIncome60);

		}
		return queryResult;
	}
	
	public Map<String, Long> queryRetainIncomeByDay(BuyFlowRetainIncomeStats vo) {
		List<Map<String, Object>> retainIncomes = dao.queryRetainIncomeByDay(vo);
		
		Map<String, Long> queryResult = new HashMap<String, Long>();
		for (Map<String, Object> retainIncome : retainIncomes) {
			
			String key = (String) retainIncome.get("key");
			long retainIncome0 = ((BigDecimal) retainIncome
					.get("retainIncome0")).longValue();
			long retainIncome1 = ((BigDecimal) retainIncome
					.get("retainIncome1")).longValue();
			long retainIncome7 = ((BigDecimal) retainIncome
					.get("retainIncome7")).longValue();
			long retainIncome30 = ((BigDecimal) retainIncome
					.get("retainIncome30")).longValue();
			long retainIncome60 = ((BigDecimal) retainIncome
					.get("retainIncome60")).longValue();
			
			queryResult.put(key + "_0", retainIncome0);
			queryResult.put(key + "_1", retainIncome1);
			queryResult.put(key + "_7", retainIncome7);
			queryResult.put(key + "_30", retainIncome30);
			queryResult.put(key + "_60", retainIncome60);
			
		}
		return queryResult;
	}

	public Map<String, Long> queryMediaRetainIncome(BuyFlowRetainIncomeStats vo) {
		List<Map<String, Object>> retainIncomes = dao.queryMediaRetainIncome(vo);

		Map<String, Long> queryResult = new HashMap<String, Long>();
		for (Map<String, Object> retainIncome : retainIncomes) {

			String key = (String) retainIncome.get("key");
			long retainIncome0 = ((BigDecimal) retainIncome
					.get("retainIncome0")).longValue();
			long retainIncome1 = ((BigDecimal) retainIncome
					.get("retainIncome1")).longValue();
			long retainIncome7 = ((BigDecimal) retainIncome
					.get("retainIncome7")).longValue();
			long retainIncome30 = ((BigDecimal) retainIncome
					.get("retainIncome30")).longValue();
			long retainIncome60 = ((BigDecimal) retainIncome
					.get("retainIncome60")).longValue();

			queryResult.put(key + "_0", retainIncome0);
			queryResult.put(key + "_1", retainIncome1);
			queryResult.put(key + "_7", retainIncome7);
			queryResult.put(key + "_30", retainIncome30);
			queryResult.put(key + "_60", retainIncome60);

		}
		return queryResult;
	}

}
