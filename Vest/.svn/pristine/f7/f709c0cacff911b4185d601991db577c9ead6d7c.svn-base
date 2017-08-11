package me.ckhd.opengame.buyflow.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.buyflow.dao.BuyFlowActivityDao;
import me.ckhd.opengame.buyflow.entity.BuyFlowActivity;
import me.ckhd.opengame.common.service.CrudService;

@Service
public class BuyFlowActivityService extends CrudService<BuyFlowActivityDao,BuyFlowActivity> {

	public BuyFlowActivity isExist(BuyFlowActivity bfa) {
		return dao.isExist(bfa);
	}

	public Set<String> getActivicyList() {
		List<HashMap<String, Object>> list = dao.getActivityList();
		HashSet<String> result = new HashSet<String>();
		for (HashMap<String, Object> map : list) {
			result.add(map.get("name").toString().split("_")[0]);
		}
		return result;
	}

}
