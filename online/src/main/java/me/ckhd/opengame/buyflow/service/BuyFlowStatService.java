package me.ckhd.opengame.buyflow.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import me.ckhd.opengame.buyflow.dao.BuyFlowStatDao;
import me.ckhd.opengame.buyflow.entity.BuyFlowStat;
import me.ckhd.opengame.common.service.CrudService;

@Service
public class BuyFlowStatService extends CrudService<BuyFlowStatDao, BuyFlowStat> {

	public List<BuyFlowStat> getData(BuyFlowStat buyFlowStat) {
		return dao.getData(buyFlowStat);
	}

	public List<BuyFlowStat> getDayData(BuyFlowStat buyFlowStat) {
		return dao.getDayData(buyFlowStat);
	}

	public List<BuyFlowStat> getMediaData(BuyFlowStat buyFlowStat) {
		return dao.getMediaData(buyFlowStat);
	}

	public List<BuyFlowStat> getDayMediaData(BuyFlowStat buyFlowStat) {
		return dao.getDayMediaData(buyFlowStat);
	}

}
