package me.ckhd.opengame.buyflow.dao;

import java.util.List;
import java.util.Map;

import me.ckhd.opengame.buyflow.entity.BuyFlowTotalIncomeStats;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyFlowTotalIncomeStatsDao extends CrudDao<BuyFlowTotalIncomeStats>{

	List<Map<String,Object>> queryTotalIncome(BuyFlowTotalIncomeStats vo);

	List<Map<String, Object>> queryIncomeData(BuyFlowTotalIncomeStats vo);

	List<Map<String, Object>> queryTotalData(BuyFlowTotalIncomeStats vo);
	
}
