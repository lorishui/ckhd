package me.ckhd.opengame.buyflow.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.buyflow.entity.BuyFlowRetainIncomeStats;
import me.ckhd.opengame.common.persistence.CrudDao;

@Repository
public interface BuyFlowRetainIncomeStatsDao extends CrudDao<BuyFlowRetainIncomeStats>{

	List<Map<String,Object>> queryRetainIncome(BuyFlowRetainIncomeStats vo);
	
	List<Map<String,Object>> queryRetainIncomeByDay(BuyFlowRetainIncomeStats vo);

	List<Map<String, Object>> queryMediaRetainIncome(BuyFlowRetainIncomeStats vo);
	
}
