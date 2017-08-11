package me.ckhd.opengame.buyflow.dao;

import java.util.HashMap;
import java.util.List;

import me.ckhd.opengame.buyflow.entity.BuyFlowActivity;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyFlowActivityDao extends CrudDao<BuyFlowActivity> {

	BuyFlowActivity isExist(BuyFlowActivity bfa);

	List<HashMap<String, Object>> getActivityList();

}
