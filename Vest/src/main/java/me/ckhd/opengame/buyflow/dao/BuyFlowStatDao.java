package me.ckhd.opengame.buyflow.dao;

import java.util.List;

import me.ckhd.opengame.buyflow.entity.BuyFlowStat;
import me.ckhd.opengame.common.persistence.CrudDao;

import org.springframework.stereotype.Repository;

@Repository
public interface BuyFlowStatDao extends CrudDao<BuyFlowStat> {

	List<BuyFlowStat> getData(BuyFlowStat buyFlowStat);

	List<BuyFlowStat> getDayData(BuyFlowStat buyFlowStat);

	List<BuyFlowStat> getMediaData(BuyFlowStat buyFlowStat);

	List<BuyFlowStat> getDayMediaData(BuyFlowStat buyFlowStat);

	List<BuyFlowStat> findBaseList(BuyFlowStat buyFlowStat);

	List<BuyFlowStat> findRetainList(BuyFlowStat buyFlowStat);

}
