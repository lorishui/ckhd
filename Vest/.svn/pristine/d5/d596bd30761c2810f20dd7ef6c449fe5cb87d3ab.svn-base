package me.ckhd.opengame.buyflow.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import me.ckhd.opengame.buyflow.entity.BuyFlowRetainStats;
import me.ckhd.opengame.common.persistence.CrudDao;

@Repository
public interface BuyFlowRetainStatsDao extends CrudDao<BuyFlowRetainStats>{

	List<Map<String,Object>> queryRetain(BuyFlowRetainStats vo);

	List<Map<String, Object>> queryMediaRetain(BuyFlowRetainStats vo);

	List<Map<String, Object>> queryRetainByDay(BuyFlowRetainStats vo);
	
}
