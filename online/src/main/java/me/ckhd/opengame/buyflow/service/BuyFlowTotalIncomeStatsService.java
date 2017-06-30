package me.ckhd.opengame.buyflow.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.buyflow.dao.BuyFlowTotalIncomeStatsDao;
import me.ckhd.opengame.buyflow.entity.BuyFlowRetainIncomeStats;
import me.ckhd.opengame.buyflow.entity.BuyFlowTotalIncomeStats;
import me.ckhd.opengame.common.service.CrudService;

@Service
public class BuyFlowTotalIncomeStatsService extends CrudService<BuyFlowTotalIncomeStatsDao,BuyFlowTotalIncomeStats>{
	
	
	public Map<String, Long> queryIncomeData(BuyFlowTotalIncomeStats vo) {
		List<Map<String, Object>> retainIncomes = dao.queryIncomeData(vo);
		Map<String, Long> queryResult = new HashMap<String, Long>();
		for (Map<String, Object> ri : retainIncomes) {
			String key = (String) ri.get("key");
			
			long retainTotalIncome = ((BigDecimal) ri
					.get("retainTotalIncome")).longValue();
			long retainIncome = ((BigDecimal) ri
					.get("retainIncome")).longValue();
			long payDevice = ((BigDecimal) ri
					.get("payDevice")).longValue();
			long totalIncome = ((BigDecimal) ri
					.get("totalIncome")).longValue();
			long totalDevice = ((BigDecimal) ri
					.get("totalDevice")).longValue();

			queryResult.put(key+"_retainTotalIncome", retainTotalIncome);
			queryResult.put(key+"_retainIncome", retainIncome);
			queryResult.put(key+"_payDevice", payDevice);
			queryResult.put(key+"_totalIncome", totalIncome);
			queryResult.put(key+"_totalDevice", totalDevice);

		}
		return queryResult;
	}
}
